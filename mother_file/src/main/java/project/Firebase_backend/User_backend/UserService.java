package project.Firebase_backend.User_backend;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class UserService {

    private static final DatabaseReference usersRef =
            FirebaseDatabase.getInstance().getReference("users");

    // ================= CHECK IF USER PROFILE EXISTS =================
    public static void userExistsAsync(String email,
                                       UserExistsCallback callback) {

        usersRef.orderByChild("email")
                .equalTo(email.toLowerCase())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        callback.onComplete(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onComplete(false);
                    }
                });
    }

    // ================= REGISTER USER PROFILE =================
    // NOTE: Password is NOT stored anymore
    public static void registerUserAsync(User user,
                                    RegisterCallback callback) {

        if (user == null || user.getId() == null) {
            callback.onComplete(false);
            return;
        }

        try {
            // Save main user profile
            usersRef.child(user.getId())
                    .setValueAsync(user)
                    .get();

            if (user.getStatus() == null) {
                usersRef.child(user.getId())
                        .child("status")
                        .setValueAsync("ACTIVE")
                        .get();
            }

            // If student, create default studentData
            if ("STUDENT".equals(user.getRole())) {

                DatabaseReference studentRef =
                        usersRef.child(user.getId())
                                .child("studentData");

                studentRef.child("borrowedCount")
                        .setValueAsync(0).get();

                studentRef.child("offenseCount")
                        .setValueAsync(0).get();

                studentRef.child("penaltyAmount")
                        .setValueAsync(0).get();

                studentRef.child("restricted")
                        .setValueAsync(false).get();

                studentRef.child("restrictionUntil")
                        .setValueAsync(0).get();

                usersRef.child(user.getId())
                    .child("status")
                    .setValueAsync("ACTIVE")
                    .get();

            }

            callback.onComplete(true);

        } catch (Exception e) {
            e.printStackTrace();
            callback.onComplete(false);
        }
    }


    // ================= FETCH USER BY EMAIL =================
    // Used AFTER Firebase Auth login success
    public static void fetchUserByEmail(String email,
                                        FetchUserCallback callback) {

        Query query = usersRef
                .orderByChild("email")
                .equalTo(email.toLowerCase());

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onComplete(null);
                    return;
                }

                for (DataSnapshot child : snapshot.getChildren()) {

                    User user = child.getValue(User.class);
                    callback.onComplete(user);
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onComplete(null);
            }
        });
    }

    //===============login by fulkl name 

    public static void fetchUserByFullName(String fullName,
                                       FetchUserCallback callback) {

        String formattedName = capitalizeWords(fullName);

        usersRef.orderByChild("fullName")
                .equalTo(formattedName)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            callback.onComplete(null);
                            return;
                        }

                        for (DataSnapshot child : snapshot.getChildren()) {

                            User user = child.getValue(User.class);
                                if (user != null) {
                                    callback.onComplete(user);
                                } else {
                                    callback.onComplete(null);
                                }
                                return;

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onComplete(null);
                    }
                });
    }


    private static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                .append(word.substring(1))
                .append(" ");
        }

        return result.toString().trim();
    }

    // ================= FETCH USER BY LAST NAME =================
    public static void fetchUserByLastName(String lastName,
                                        FetchUserCallback callback) {

        if (lastName == null || lastName.trim().isEmpty()) {
            callback.onComplete(null);
            return;
        }

        usersRef.orderByChild("lastName")
                .equalTo(capitalizeWords(lastName))
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            callback.onComplete(null);
                            return;
                        }

                        for (DataSnapshot child : snapshot.getChildren()) {

                            User user = child.getValue(User.class);
                            if (user != null) {
                                callback.onComplete(user);
                            } else {
                                callback.onComplete(null);
                            }
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onComplete(null);
                    }
                });
    }

    
  
        // ================= UPDATE STUDENT PROFILE IMAGE WITH CLEANUP =================
        public static void updateStudentProfileImageWithCleanup(
                String userId,
                java.io.File newFile,
                RegisterCallback callback
        ) {

            usersRef.child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        callback.onComplete(false);
                        return;
                    }

                    User user = snapshot.getValue(User.class);
                    String oldImageUrl = user != null
                            ? user.getProfileImageUrl()
                            : null;

                    // 1️⃣ Upload new image
                    String newImageUrl =
                            project.Firebase_backend.Storage_backend.ImageService
                                    .uploadStudentImage(newFile, userId);

                    if (newImageUrl == null) {
                        callback.onComplete(false);
                        return;
                    }

                    try {
                        // 2️⃣ Update database
                        usersRef.child(userId)
                                .child("profileImageUrl")
                                .setValueAsync(newImageUrl)
                                .get();

                        // 3️⃣ Delete old image
                        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                            project.Firebase_backend.Storage_backend.ImageService
                                    .deleteImageByUrl(oldImageUrl);
                        }

                        callback.onComplete(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onComplete(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onComplete(false);
                }
            });
        }

        // ================= DELETE STUDENT PROFILE IMAGE =================
        public static void deleteStudentProfileImage(
                String userId,
                RegisterCallback callback
        ) {

            usersRef.child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        callback.onComplete(false);
                        return;
                    }

                    User user = snapshot.getValue(User.class);

                    if (user == null ||
                        user.getProfileImageUrl() == null ||
                        user.getProfileImageUrl().isEmpty()) {

                        callback.onComplete(false);
                        return;
                    }

                    try {
                        // 1️⃣ Delete from storage
                        project.Firebase_backend.Storage_backend.ImageService
                                .deleteImageByUrl(user.getProfileImageUrl());

                        // 2️⃣ Remove URL from database
                        usersRef.child(userId)
                                .child("profileImageUrl")
                                .removeValueAsync()
                                .get();

                        callback.onComplete(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onComplete(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onComplete(false);
                }
            });
        }

        // ================= DELETE USER COMPLETELY (AUTH + DATABASE + STORAGE) =================
        public static void deleteUserCompletely(String studentId,
                                        RegisterCallback callback) {

            DatabaseReference userRef =
                    usersRef.child(studentId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        callback.onComplete(false);
                        return;
                    }

                    User user = snapshot.getValue(User.class);

                    try {

                        // 🔥 1️⃣ Delete from Firebase Authentication using UID
                        if (user.getUid() != null && !user.getUid().isEmpty()) {

                            com.google.firebase.auth.FirebaseAuth
                                    .getInstance()
                                    .deleteUser(user.getUid());
                        }

                        // 🔥 2️⃣ Delete profile image if exists
                        if (user.getProfileImageUrl() != null &&
                                !user.getProfileImageUrl().isEmpty()) {

                            project.Firebase_backend.Storage_backend.ImageService
                                    .deleteImageByUrl(user.getProfileImageUrl());
                        }

                        // 🔥 3️⃣ Delete from Realtime Database
                        usersRef.child(studentId).removeValueAsync();

                        callback.onComplete(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onComplete(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onComplete(false);
                }
            });
        }







}
