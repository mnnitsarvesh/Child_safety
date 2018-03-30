package com.example.root.child;

/**
 * Created by root on 1/3/18.
 */

public class SingleUserclass {
    private String name,Email,image;

    public SingleUserclass()
    {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {
        return Email;
    }

    public String getImage() {
        return image;
    }

    public SingleUserclass(String name, String email, String image) {

        this.name = name;
        Email = email;
        this.image = image;
    }
}
