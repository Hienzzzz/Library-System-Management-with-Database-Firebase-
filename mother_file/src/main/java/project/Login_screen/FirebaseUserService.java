package project.Login_screen;



import java.util.concurrent.CountDownLatch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;





public class FirebaseUserService{

    private static final String[] ROLE_PATH = {
        "students",
        "librarians",
        "admins"
    };

    private static String getRolePath(String id) {
        if (id == null) return null;

        if (id.matches("^AD\\d{7}$")) return "admins";
        if (id.matches("^LB\\d{7}$")) return "librarians";
        if (id.matches("^\\d{4}-\\d{7}$")) return "students";

        return null;
    }


    //register
    public static boolean registerUser(User user){

        String rolePath = getRolePath(user.getId());
        if(rolePath == null) return  false;

        DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(rolePath)
            .child(user.getId());

            try {
                ref.setValueAsync(user).get();
                System.out.println("Fullname: " + user.getFullname());
                System.out.println("Email: " + user.getEmail());
                System.out.println("ID no.: " + user.getId());
                System.out.println("Register as: " + rolePath);
                return  true;
            } catch (Exception e) {
                System.out.println("Failed to register");
                e. printStackTrace();
                return false;
            }
    }

    //login
    public static LoginResult login(String input, String password){

        boolean userFound = false;

        for(String role : ROLE_PATH){
           LoginResult result = tryLoginInRole(role, input, password);

           if(result.getStatus() == LoginResult.Status.WRONG_PASSWORD){
            return result;
           }
         if(result.getStatus() == LoginResult.Status.SUCCESS){
            return result;
           }
        if(result.getStatus() == LoginResult.Status.USER_NOT_FOUND){
            userFound = userFound || false;
           }
        }
        return new LoginResult(LoginResult.Status.USER_NOT_FOUND, null);
    }
    
   private static LoginResult tryLoginInRole(String role, String input, String password) {

    CountDownLatch latch = new CountDownLatch(1);
    final LoginResult[] result =
            { new LoginResult(LoginResult.Status.USER_NOT_FOUND, null) };

    DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(role);

    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {

            for (DataSnapshot child : snapshot.getChildren()) {
                User user = child.getValue(User.class);
                if (user == null) continue;

                if (user.getEmail().equalsIgnoreCase(input) ||
                    user.getFullname().equalsIgnoreCase(input)) {

                    if (user.getPassword().equals(password)) {
                        result[0] = new LoginResult(
                                LoginResult.Status.SUCCESS, user);
                    } else {
                        result[0] = new LoginResult(
                                LoginResult.Status.WRONG_PASSWORD, null);
                    }
                    break;
                }
            }
            latch.countDown();
        }

        @Override
        public void onCancelled(DatabaseError error) {
            latch.countDown();
        }
    });

    try { latch.await(); } catch (InterruptedException e) {}

    return result[0];
}


    // check if user exists (username or email)
    public static boolean userExists(String input){
        for(String role: ROLE_PATH){
            if (existsInRole(role, input)) return true;
        }
        return false;
    }

    private static boolean existsInRole(String role, String input){
        
        CountDownLatch latch = new CountDownLatch(1);

        final boolean[] exists = {false};

        DatabaseReference ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(role);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot account){
                for(DataSnapshot child : account.getChildren()){
                    User user = child.getValue(User.class);
                    if(user == null) continue;

                    if(user.getFullname().equalsIgnoreCase(input) ||
                        user.getEmail().equals(input)){
                        exists[0] = true;
                        break;
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error){
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exists[0];
    }
}