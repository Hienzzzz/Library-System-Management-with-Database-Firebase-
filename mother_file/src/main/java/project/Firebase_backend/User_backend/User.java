package project.Firebase_backend.User_backend;


public  class User{
    private String firstName;
    private String surname;
    private String fullname;
    private String email;
    private String id;
    private String password;

    public User(){}

    public User(String firstName, String surname, String email, String id, String password){
        this.firstName = firstName;
        this.surname = surname;
        this.fullname = firstName + " " + surname;
        this.email = email;
        this.id = id;
        this.password = password; 
    }

 
    public String getFirstname(){
        return  firstName;
    }

    public String getSurname(){
        return  surname;
    }
    public String getFullname(){
        return  fullname;
    }

    public String getEmail(){
        return email;
    }
    public String getId(){
        return  id;
    }

    public String getPassword(){
        return  password;
    }

    public String getRole() {
        if (id == null) return "UNKNOWN";
        if (id.matches("^AD\\d{7}$")) return "ADMIN";
        if (id.matches("^LB\\d{7}$")) return "LIBRARIAN";
        if (id.matches("^\\d{4}-\\d{7}$")) return "STUDENT";
        return "UNKNOWN";
    }


}