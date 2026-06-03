package com.project.backend.dto;

/**
 * Created by Sergio.
 */
public class LoginDTO {

    private String identifier;
    private String password;

    // Default constructor (required for deserialization)
    public LoginDTO() {
    }

    // Getters

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }

    // Setters

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
