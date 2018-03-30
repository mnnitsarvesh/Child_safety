package com.example.root.child;

import java.util.ArrayList;

/**
 * Created by root on 24/2/18.
 */

public class Adduser {
    String name,email,password,gender,userType,imageUrl;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getUserType() {
        return userType;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public Adduser(String name, String email, String password, String gender, String userType, String imageUrl) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.gender= gender;
        this.userType = userType;
        this.imageUrl = imageUrl;


    }

    public Adduser() {
    }

}
