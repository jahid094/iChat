package com.example.ichat;

public class UserProfile {

    public String userEmail;

    public UserProfile(){
    }

    public UserProfile(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
