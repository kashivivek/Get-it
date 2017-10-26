package com.getit.getit.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by kashivivek on 10/25/2017.
 */

@IgnoreExtraProperties
public class User {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String name;
    public String email;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String place;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String place) {
        this.name = name;
        this.email = email;
        this.place = place;
    }

}