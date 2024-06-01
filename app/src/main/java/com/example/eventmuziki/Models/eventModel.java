package com.example.eventmuziki.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class eventModel implements Parcelable {
    String eventName, eventDetails, date, time, location, amount, category, creatorID;

    public eventModel() {
        // Default constructor required for Firestore
    }

    public eventModel(String eventName, String eventDetails, String date, String time, String location, String amount, String category, String creatorID) {
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.date = date;
        this.time = time;
        this.location = location;
        this.amount = amount;
        this.category = category;
        this.creatorID = creatorID;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    protected eventModel(Parcel in) {
        eventName = in.readString();
        eventDetails = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        amount = in.readString();
        category = in.readString();
        creatorID = in.readString();
    }

    public static final Creator<eventModel> CREATOR = new Creator<eventModel>() {
        @Override
        public eventModel createFromParcel(Parcel in) {
            return new eventModel(in);
        }

        @Override
        public eventModel[] newArray(int size) {
            return new eventModel[size];
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
        dest.writeString(amount);
        dest.writeString(category);
        dest.writeString(creatorID);
    }
}
