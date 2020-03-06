package net.joffinwatts.companyz.data.model;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Serializable {

    private String userId;
    private String name;
    private String email;

    public boolean isCreated;
    public boolean isNew;

    public LoggedInUser(String uid, String name, String email) {
        this.userId = uid;
        this.name = name;
        this.email = email;
    }

    public boolean isNew() {
        return isNew;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
