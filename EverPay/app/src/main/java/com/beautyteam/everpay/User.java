package com.beautyteam.everpay;

/**
 * Created by asus on 09.03.2015.
 */
public class User {
    private final int id;
    private final String name;
    private final String last_name;
    private final String photo;

    public User(int id, String name, String last_name, String photo) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getPhoto() {return photo;}

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }
}
