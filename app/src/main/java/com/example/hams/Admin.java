package com.example.hams;

public class Admin {

    // Attributes
    private String username;
    private String password;

    // Constructor with parameters
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor with no parameters
    public Admin() {
        this.username = "";
        this.password = "";
    }

    // Getters and Setters for each attribute

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString method to represent the User as a String (optional)
    @Override
    public String toString() {
        return "Admin: " +
                "username='" + username + '\'' +
                ", password='" + password + '\'';
    }
}
