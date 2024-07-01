package com.example.eventmuziki.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class chatUserModel {

    String  profilePicture, userId, name, email, phone;

    public chatUserModel(String profilePicture, String userId, String name, String email, String phone) {
        this.profilePicture = profilePicture;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public chatUserModel() {
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserID() {
        return userId;
    }

    public void setUserID(String userID) {
        this.userId = userID;
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

    protected chatUserModel(Parcel in) {
        profilePicture = in.readString();
        userId = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
    }

}

