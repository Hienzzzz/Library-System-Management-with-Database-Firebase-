package Login_screen;


public  class User{
    private String firstName;
    private String surname;
    private String username;
    private String emial;
    private String ID;
    private String password;

    public User(String firstName, String surname, String email, String ID, String password){
        this.firstName = firstName;
        this.surname = surname;
        this.username = firstName + " " + surname;
        this.emial = email;
        this.ID = ID;
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
        if (ID.matches("^AD\\d{7}$")) return "ADMIN";
        if(ID.matches("^LB\\d{7}$")) return "LIBRARIAN";
        if (ID.matches("^\\d{4} - \\d{7}$")) return "STUDENT";
        return "UNKNOWN";

    }

}