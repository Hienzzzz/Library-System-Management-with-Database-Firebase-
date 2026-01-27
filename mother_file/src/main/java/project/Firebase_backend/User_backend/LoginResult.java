package project.Firebase_backend.User_backend;

public class LoginResult{
    public enum Status{
        SUCCESS,
        USER_NOT_FOUND,
        WRONG_PASSWORD
    }

    private Status status;
    private User user;

    public LoginResult(Status status, User user){
        this.status = status;
        this.user = user;

    }

    public Status getStatus(){
        return status;
    }

    public User getUser(){
        return user;
    }
}