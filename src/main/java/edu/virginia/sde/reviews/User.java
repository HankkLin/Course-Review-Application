package edu.virginia.sde.reviews;

public class User {
    public String getUserName() {
        return userName;
    }

    private final String userName, password;
    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

}
