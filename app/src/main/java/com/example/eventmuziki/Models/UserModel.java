package com.example.eventmuziki.Models;

import android.os.Parcel;

public class UserModel {

    String  profilePicture, userID, name, email, phone, userType, category;

    public UserModel(String profilePicture, String userID, String name, String email, String phone , String userType, String category) {
        this.profilePicture = profilePicture;
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.category = category;

    }

    public UserModel() {
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

