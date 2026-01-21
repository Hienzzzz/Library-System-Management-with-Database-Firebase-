package Login_screen;

public class User{
    private String name;
    private String surname;
    private String email;
    private int student_ID;
    private String pasword;


    public User(String name, String surname, String email, int studentID, String password){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.student_ID = studentID;
        this.pasword = password;
    }
    public String getFullName(){
        return name + " " + surname;
    }

    public String getEmail(){
        return email;
    }

}