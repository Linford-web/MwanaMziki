package com.example.eventmuziki.Models;

public class advertisementModel {

    String advertId, eventId, eventName, eventLocation, eventDate, eventTime, details,organizerName, advertPlans, userId, posterUrl;

    public advertisementModel() {
    }

    public advertisementModel(String advertId, String eventId, String eventName, String eventLocation, String eventDate, String eventTime, String details,String organizerName, String advertPlans, String userId, String posterUrl) {
        this.advertId = advertId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.details = details;
        this.organizerName = organizerName;
        this.advertPlans = advertPlans;
        this.userId = userId;
        this.posterUrl = posterUrl;

    }

    public String getAdvertId() {
        return advertId;
    }

    public void setAdvertId(String advertId) {
        this.advertId = advertId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getAdvertPlans() {
        return advertPlans;
    }

    public void setAdvertPlans(String advertPlans) {
        this.advertPlans = advertPlans;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
