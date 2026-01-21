package Login_screen;

import java.util.ArrayList;

public class Data {

    private ArrayList<User> users;

    public Data(){
        users = new ArrayList<>();
    }

    public void addUser(String name, String surname, String email, int stduentID, String password ){
        users.add(new User(name, surname,email, stduentID,password));
    }

    public void displayUser(){
        for(User user : users){
            System.out.println(user.getFullName() + " - " + user.getEmail());
        }
    }
    
}
