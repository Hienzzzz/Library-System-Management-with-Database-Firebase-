package Login_screen;


public  class User{
    private String firstName;
    private String surname;
    private String username;
    private String emial;
    private String role;
    private String password;

    public User(String firstName, String surname, String email, String role, String password){
        this.firstName = firstName;
        this.surname = surname;
        this.username = firstName + " " + surname;
        this.emial = email;
        this.role = role;
        this.password = password; 
    }

 
    public String getUsername(){
        return  username;
    }

    public String getEmail(){
        return emial;
    }

    public String getPassword(){
        return  password;
    }

    public String getRole(){
        return role;
    }

}