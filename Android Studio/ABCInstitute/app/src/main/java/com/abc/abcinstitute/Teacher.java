package com.abc.abcinstitute;


import java.sql.Blob;

public class Teacher {

    int id;
    String name;
    String username;
    String password;
    String profile;

    public void setInfo(int id, String name, String username, String password) {

        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public void setInfo(String name, String username, String password) {

        this.name = name;
        this.username = username;
        this.password = password;
    }

    public void setLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setProfile(String profile) { this.profile = profile; }
    public String getProfile() {
        return this.profile;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
