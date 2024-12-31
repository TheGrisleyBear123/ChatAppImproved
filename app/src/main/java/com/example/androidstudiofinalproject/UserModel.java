package com.example.androidstudiofinalproject;

public class UserModel {
    private String userid, userEmail, userpassword;

    public UserModel(String userid,  String userEmail, String userpassword) {
        this.userid = userid;
        this.userEmail = userEmail;
        this.userpassword = userpassword;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
}
