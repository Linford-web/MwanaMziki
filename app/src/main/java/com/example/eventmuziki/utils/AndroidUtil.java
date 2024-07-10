package com.example.eventmuziki.utils;

import android.content.Intent;

import com.example.eventmuziki.Models.UserModel;

public class AndroidUtil {

    public static UserModel getUserModelAsIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUserID(intent.getStringExtra("userId"));
        userModel.setName(intent.getStringExtra("userName"));
        userModel.setEmail(intent.getStringExtra("userEmail"));
        userModel.setProfilePicture(intent.getStringExtra("userImage"));
        userModel.setPhone(intent.getStringExtra("userPhone"));
        return userModel;
    }
}
