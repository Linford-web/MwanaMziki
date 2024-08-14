package com.example.eventmuziki.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class biddersEventModel implements Parcelable {

    String eventName, eventDetails, date, time, location, bidAmount, eventId, creatorID, bidId, userCategory, biddersId, organizerName, biddersName;

    public biddersEventModel() {
        // Default constructor required for Firestore
    }

    public biddersEventModel(String eventName, String eventDetails, String date, String time, String location, String bidAmount, String eventId, String creatorID, String bidId, String userCategory, String biddersId, String organizerName, String biddersName) {
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.date = date;
        this.time = time;
        this.location = location;
        this.bidAmount = bidAmount;
        this.eventId = eventId;
        this.creatorID = creatorID;
        this.bidId = bidId;
        this.userCategory = userCategory;
        this.biddersId = biddersId;
        this.organizerName = organizerName;
        this.biddersName = biddersName;

    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getBiddersId() {
        return biddersId;
    }

    public void setBiddersId(String biddersId) {
        this.biddersId = biddersId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getBiddersName() {
        return biddersName;
    }

    public void setBiddersName(String biddersName) {
        this.biddersName = biddersName;
    }

    protected biddersEventModel(Parcel in) {
        eventName = in.readString();
        eventDetails = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        bidAmount = in.readString();
        eventId = in.readString();
        creatorID = in.readString();
        bidId = in.readString();
        userCategory = in.readString();
        biddersId = in.readString();
        organizerName = in.readString();
        biddersName = in.readString();
    }

    public static final Creator<biddersEventModel> CREATOR = new Creator<biddersEventModel>() {
        @Override
        public biddersEventModel createFromParcel(Parcel in) {
            return new biddersEventModel(in);
        }

        @Override
        public biddersEventModel[] newArray(int size) {
            return new biddersEventModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(eventDetails);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(bidAmount);
        dest.writeString(eventId);
        dest.writeString(creatorID);
        dest.writeString(bidId);
        dest.writeString(userCategory);
        dest.writeString(biddersId);
        dest.writeString(organizerName);
        dest.writeString(biddersName);
    }
}
