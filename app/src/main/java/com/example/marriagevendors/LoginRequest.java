package com.example.marriagevendors;

public class LoginRequest {
    String email;
    String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
