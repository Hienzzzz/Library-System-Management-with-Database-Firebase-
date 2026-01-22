package Login_screen;

import java.util.ArrayList;

public class Data{

    private static ArrayList<User> users = new ArrayList<>();

    public static boolean regsiterUser (String firstName, String surname, String email, String role,  String password){
       
        String username = firstName + surname;

        for (User user: users){
            if(user.getUsername().equalsIgnoreCase(username) || 
            user.getEmail().equalsIgnoreCase(email)){
                return false;
            }
        }

        users.add(new User(firstName, surname, email, role, password));
        return true;
    }

    public static User login(String loginInput, String password){
        for(User user : users){
            if((user.getUsername().equalsIgnoreCase(loginInput) ||
            user.getEmail().equalsIgnoreCase(loginInput)) &&
            user.getPassword().equals(password)){

                return user;
            }
        }
        return null;
    }

    public static boolean userExists(String loginInput){
        for (User user : users){
            if(user.getUsername().equalsIgnoreCase(loginInput) ||
            user.getEmail().equalsIgnoreCase(loginInput)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean passwordCorrect(String loginIput, String password){
        for (User user: users){
            if((user.getUsername().equalsIgnoreCase(loginIput) ||
            user.getUsername().equalsIgnoreCase(loginIput)) &&
            user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
    
}
