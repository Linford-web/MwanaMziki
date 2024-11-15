package com.example.eventmuziki.Models;

public class AdvertisementDetails {

    public static class advertModel{
        String title, contacts, date, time, details, duration, image, advertId, creatorId;

        public advertModel() {
        }
        public advertModel(String title, String contacts, String date, String time, String details, String duration, String image, String advertId, String creatorId) {
            this.title = title;
            this.contacts = contacts;
            this.date = date;
            this.time = time;
            this.details = details;
            this.duration = duration;
            this.image = image;
            this.advertId = advertId;
            this.creatorId = creatorId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAdvertId() {
            return advertId;
        }

        public void setAdvertId(String advertId) {
            this.advertId = advertId;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }

    public static class posterModel{

        String cost, reach, description, place, advertId, posterId;
        public posterModel() {
        }

        public posterModel(String cost, String reach, String description, String place, String advertId, String posterId) {
            this.cost = cost;
            this.reach = reach;
            this.description = description;
            this.place = place;
            this.advertId = advertId;
            this.posterId = posterId;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getReach() {
            return reach;
        }

        public void setReach(String reach) {
            this.reach = reach;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getAdvertId() {
            return advertId;
        }

        public void setAdvertId(String advertId) {
            this.advertId = advertId;
        }

        public String getPosterId() {
            return posterId;
        }

        public void setPosterId(String posterId) {
            this.posterId = posterId;
        }
    }

    public static class reviewModel{
        String reviewMessage, userId, timestamp;

        public reviewModel() {
        }

        public reviewModel(String reviewMessage, String userId, String timestamp) {
            this.reviewMessage = reviewMessage;
            this.userId = userId;
            this.timestamp = timestamp;
        }

        public String getReviewMessage() {
            return reviewMessage;
        }

        public void setReviewMessage(String reviewMessage) {
            this.reviewMessage = reviewMessage;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

}
