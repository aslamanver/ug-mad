package com.abc.abcinstitute;


public class Student {

    int id;
    String name;
    String mobile;

    String java;
    String php;
    String cpp;
    String python;
    String golang;

    String username;
    String password;


    public void setInfo(int id, String name, String mobile, String username, String password) {

        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.username = username;
        this.password = password;
    }

    public void setInfo(String name, String mobile, String username, String password) {

        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.username = username;
        this.password = password;
    }


    public void setMarks(String java, String php, String cpp, String python, String golang) {

        this.java = java;
        this.php = php;
        this.cpp = cpp;
        this.python = python;
        this.golang = golang;
    }

    public void setLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public String getName() { return this.name; }

    public String getMobile() {
        return this.mobile;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getJava() {
        return this.java;
    }

    public String getPhp() {
        return this.php;
    }

    public String getCpp() {
        return this.cpp;
    }

    public String getPython() {
        return this.python;
    }

    public String getGolang() { return this.golang; }

}
