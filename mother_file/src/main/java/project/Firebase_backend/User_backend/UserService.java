package project.Firebase_backend.User_backend;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserService {

    private static DatabaseReference usersRef =
            FirebaseDatabase.getInstance().getReference("users");



    // ================= LOGIN =================

    public static void loginAsync(
            String input,
            String password,
            LoginCallback callback
    ) {

        Query queryEmail = usersRef
                .orderByChild("email")
                .equalTo(input.toLowerCase());

        queryEmail.addListenerForSingleValueEvent(
                new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot child : snapshot.getChildren()) {

                        User user = child.getValue(User.class);

                        if (user.getStatus().equals("BLOCKED")) {
                            callback.onComplete(
                                    new LoginResult(
                                            LoginResult.Status.ACCOUNT_BLOCKED,
                                            null));
                            return;
                        }
                        String hashedInput = PasswordUtil.hashPassword(password);

                        if (user.getPassword().equals(hashedInput)) {
                            callback.onComplete(
                                    new LoginResult(
                                            LoginResult.Status.SUCCESS,
                                            user));
                        } else {
                            callback.onComplete(
                                    new LoginResult(
                                            LoginResult.Status.WRONG_PASSWORD,
                                            null));
                        }

                        return;
                    }
                }

                // If not found by email → try full name
                Query queryName = usersRef
                        .orderByChild("fullName")
                        .equalTo(capitalizeWords(input));

                queryName.addListenerForSingleValueEvent(
                        new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snap) {

                        if (!snap.exists()) {
                            callback.onComplete(
                                    new LoginResult(
                                            LoginResult.Status.USER_NOT_FOUND,
                                            null));
                            return;
                        }

                        for (DataSnapshot child : snap.getChildren()) {

                            User user = child.getValue(User.class);

                            String hashedInput = PasswordUtil.hashPassword(password);

                            if (user.getPassword().equals(hashedInput)) {
                                callback.onComplete(
                                        new LoginResult(
                                                LoginResult.Status.SUCCESS,
                                                user));
                            } else {
                                callback.onComplete(
                                        new LoginResult(
                                                LoginResult.Status.WRONG_PASSWORD,
                                                null));
                            }

                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {}
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
        //=====================register 
        public static void userExistsAsync(String email,
            UserExistsCallback callback) {

        DatabaseReference usersRef =
                FirebaseDatabase.getInstance().getReference("users");

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

    public static void registerUserAsync(User user,
            RegisterCallback callback) {

        DatabaseReference usersRef =
                FirebaseDatabase.getInstance().getReference("users");

        try {

            usersRef.child(user.getId()).setValueAsync(user).get();

            if (user.getRole().equals("STUDENT")) {

                DatabaseReference studentRef =
                        usersRef.child(user.getId())
                                .child("studentData");

                studentRef.child("borrowedCount").setValueAsync(0);
                studentRef.child("offenseCount").setValueAsync(0);
                studentRef.child("penaltyAmount").setValueAsync(0);
                studentRef.child("restricted").setValueAsync(false);
                studentRef.child("restrictionUntil").setValueAsync(0);
            }

            callback.onComplete(true);

        } catch (Exception e) {
            callback.onComplete(false);
        }

    }

    

}
