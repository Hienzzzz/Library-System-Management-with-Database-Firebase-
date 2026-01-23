package Login_screen;

import java.util.ArrayList;

public class Data{
    private static ArrayList<User> users = new ArrayList<>();

    public static boolean registerUser(String first, String last, String email, String id, String password ){
        for(User user: users){
            if(user.getEmail().equalsIgnoreCase(email) || user.getUsername().equalsIgnoreCase(first + " " + last)){
                return false;
            }
        }
        users.add(new User(first, last, email, id, password));
        return true;
    }

    public static User login(String input, String password){
        for(User user: users){
            if(user.getUsername().equalsIgnoreCase(input) ||
            user.getEmail().equalsIgnoreCase(input)){
                if(user.getPassword().equals(password)){
                    return user;
                }else{
                    return null;
                }
            }
        }
        return null;
    }

    public static boolean userExists(String input){
        for(User user : users){
            if( user.getUsername().equalsIgnoreCase(input)||
                user.getEmail().equalsIgnoreCase(input)
            ){
                return true;
            }
        }
        return false;
    }
}