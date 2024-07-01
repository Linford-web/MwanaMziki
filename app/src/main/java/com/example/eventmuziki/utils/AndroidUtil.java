package com.example.eventmuziki.utils;

import android.content.Intent;

import com.example.eventmuziki.Models.chatUserModel;

public class AndroidUtil {

    public static chatUserModel getUserModelAsIntent(Intent intent){
        chatUserModel userModel = new chatUserModel();
        userModel.setUserID(intent.getStringExtra("userId"));
        userModel.setName(intent.getStringExtra("userName"));
        userModel.setEmail(intent.getStringExtra("userEmail"));
        userModel.setProfilePicture(intent.getStringExtra("userImage"));
        userModel.setPhone(intent.getStringExtra("userPhone"));
        return userModel;
    }
}
