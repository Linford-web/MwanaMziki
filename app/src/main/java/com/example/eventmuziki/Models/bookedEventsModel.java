package com.example.eventmuziki.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class bookedEventsModel implements Parcelable {

    String eventName, eventDetails, date, time, location, eventId, creatorID, bidId, category, biddersId, organizerName, biddersName, about, socials, bookedId;

    public bookedEventsModel(String eventName, String eventDetails, String date, String time, String location, String eventId,
                             String creatorID, String bidId, String category, String biddersId, String organizerName,
                             String biddersName, String about, String socials, String bookedId) {
        this.eventName = eventName;
        this.eventDetails = eventDetails;
        this.date = date;
        this.time = time;
        this.location = location;
        this.eventId = eventId;
        this.creatorID = creatorID;
        this.bidId = bidId;
        this.category = category;
        this.biddersId = biddersId;
        this.organizerName = organizerName;
        this.biddersName = biddersName;
        this.about = about;
        this.socials = socials;
        this.bookedId = bookedId;
    }

    public String getBookedId() {
        return bookedId;
    }

    public void setBookedId(String bookedId) {
        this.bookedId = bookedId;
    }

    public bookedEventsModel() {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getSocials() {
        return socials;
    }

    public void setSocials(String socials) {
        this.socials = socials;
    }

    protected bookedEventsModel(Parcel in) {
        eventName = in.readString();
        eventDetails = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        eventId = in.readString();
        creatorID = in.readString();
        bidId = in.readString();
        category = in.readString();
        biddersId = in.readString();
        organizerName = in.readString();
        biddersName = in.readString();
        about = in.readString();
        socials = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(eventDetails);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(eventId);
        dest.writeString(creatorID);
        dest.writeString(bidId);
        dest.writeString(category);
        dest.writeString(biddersId);
        dest.writeString(organizerName);
        dest.writeString(biddersName);
        dest.writeString(about);
        dest.writeString(socials);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<bookedEventsModel> CREATOR = new Creator<bookedEventsModel>() {
        @Override
        public bookedEventsModel createFromParcel(Parcel in) {
            return new bookedEventsModel(in);
        }

        @Override
        public bookedEventsModel[] newArray(int size) {
            return new bookedEventsModel[size];

        }
    };
}
