package project.Firebase_backend.User_backend;

/* =========================================================
 * ========================= IMPORTS =======================
 * ========================================================= */

// ================= FIREBASE DATABASE =====================
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/* =========================================================
 * ======================= USER SERVICE ====================
 * =========================================================
 * Handles:
 * - User existence checking
 * - User registration
 * - User fetching (email, fullname, lastname)
 * - Profile image management
 * - Full user deletion (Auth + DB + Storage)
 * ========================================================= */

public class UserService {

    /* =====================================================
     * ================= STATIC REFERENCES ==================
     * ===================================================== */

    private static final DatabaseReference usersRef =
            FirebaseDatabase.getInstance().getReference("users");


    /* =====================================================
     * ================= EXISTENCE CHECK ====================
     * ===================================================== */

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


    /* =====================================================
     * ================= REGISTRATION =======================
     * ===================================================== */

    public static void registerUserAsync(User user,
                                         RegisterCallback callback) {

        if (user == null || user.getId() == null) {
            callback.onComplete(false);
            return;
        }

        try {

            // ===== Save Main Profile =====
            usersRef.child(user.getId())
                    .setValueAsync(user)
                    .get();

            // ===== Ensure Status Exists =====
            if (user.getStatus() == null) {
                usersRef.child(user.getId())
                        .child("status")
                        .setValueAsync("ACTIVE")
                        .get();
            }

            // ===== Default Student Data =====
            if ("STUDENT".equals(user.getRole())) {

                DatabaseReference studentRef =
                        usersRef.child(user.getId())
                                .child("studentData");

                studentRef.child("borrowedCount").setValueAsync(0).get();
                studentRef.child("offenseCount").setValueAsync(0).get();
                studentRef.child("totalPenaltyAmount").setValueAsync(0).get();
                studentRef.child("restrictionLevel").setValueAsync(0).get();
                studentRef.child("restrictionUntil").setValueAsync(0).get();
                studentRef.child("permanentlyBlocked").setValueAsync(false).get();

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


    /* =====================================================
     * ================= FETCH SECTION ======================
     * ===================================================== */

    // ===== Fetch by Email =====
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


    // ===== Fetch by Full Name =====
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
                            callback.onComplete(user != null ? user : null);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onComplete(null);
                    }
                });
    }


    // ===== Fetch by Last Name =====
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
                            callback.onComplete(user != null ? user : null);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onComplete(null);
                    }
                });
    }


    /* =====================================================
     * ================= PROFILE IMAGE ======================
     * ===================================================== */

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

                        String oldImageUrl =
                                user != null ? user.getProfileImageUrl() : null;

                        // 1️⃣ Upload new image
                        String newImageUrl =
                                project.Firebase_backend.Storage_backend.ImageService
                                        .uploadStudentImage(newFile, userId);

                        if (newImageUrl == null) {
                            callback.onComplete(false);
                            return;
                        }

                        try {

                            // 2️⃣ Update DB
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

                            // 2️⃣ Remove URL from DB
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


    /* =====================================================
     * ================= FULL USER DELETION =================
     * ===================================================== */

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

                    // 1️⃣ Delete from Firebase Authentication
                    if (user.getUid() != null &&
                            !user.getUid().isEmpty()) {

                        com.google.firebase.auth.FirebaseAuth
                                .getInstance()
                                .deleteUser(user.getUid());
                    }

                    // 2️⃣ Delete profile image
                    if (user.getProfileImageUrl() != null &&
                            !user.getProfileImageUrl().isEmpty()) {

                        project.Firebase_backend.Storage_backend.ImageService
                                .deleteImageByUrl(user.getProfileImageUrl());
                    }

                    // 3️⃣ Delete from Realtime Database
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


    /* =====================================================
     * ================= HELPER METHODS =====================
     * ===================================================== */

    private static String capitalizeWords(String input) {

        if (input == null || input.isEmpty()) return input;

        String[] words =
                input.trim().toLowerCase().split("\\s+");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return result.toString().trim();
    }

}
