package com.example.eventmuziki.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.List;

public class chatRoomModel implements Parcelable {

    String chatRoomId;
    List<String> userIds;
    Timestamp lastMessageTime;
    String lastMessageSenderId;
    String lastMessage;

    public chatRoomModel() {
    }

    public chatRoomModel(String chatRoomId, List<String> userIds, Timestamp lastMessageTime, String lastMessageSenderId, String lastMessage) {
        this.chatRoomId = chatRoomId;
        this.userIds = userIds;
        this.lastMessageTime = lastMessageTime;
        this.lastMessageSenderId = lastMessageSenderId;
        this.lastMessage = lastMessage;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    protected chatRoomModel(Parcel in) {
        chatRoomId = in.readString();
        userIds = in.createStringArrayList();
        lastMessageTime = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        lastMessageSenderId = in.readString();
        lastMessage = in.readString();
    }

    public static final Creator<chatRoomModel> CREATOR = new Creator<chatRoomModel>() {
        @Override
        public chatRoomModel createFromParcel(Parcel in) {
            return new chatRoomModel(in);
        }

        @Override
        public chatRoomModel[] newArray(int size) {
            return new chatRoomModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(chatRoomId);
        dest.writeStringList(userIds);
        dest.writeValue(lastMessageTime);
        dest.writeString(lastMessageSenderId);
        dest.writeString(lastMessage);
    }
}
