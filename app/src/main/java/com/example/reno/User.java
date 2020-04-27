package com.example.reno;

public class User {
    public String name, email, phone, password, Level, Department, message, currentbal,usertoken;

    public User(){

    }

    public User(String name, String email, String phone, String password, String Level, String Department, String message, String currentbal,String usertoken) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.Level = Level;
        this.Department = Department;
        this.message = message;
        this.currentbal = currentbal;
        this.usertoken= usertoken;
    }
}
