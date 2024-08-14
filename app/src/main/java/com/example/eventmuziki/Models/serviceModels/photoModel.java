package com.example.eventmuziki.Models.serviceModels;

import java.util.ArrayList;

public class photoModel {
    String artist_name, socials, price_photography, category, status, creatorId;
    ArrayList<String> images;
    public photoModel() {
    }

    public photoModel(String artist_name, String socials, String price_photography, String category, String status, String creatorId, ArrayList<String> images) {
        this.artist_name = artist_name;
        this.socials = socials;
        this.price_photography = price_photography;
        this.category = category;
        this.status = status;
        this.creatorId = creatorId;
        this.images = images;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getSocials() {
        return socials;
    }

    public void setSocials(String socials) {
        this.socials = socials;
    }

    public String getPrice_photography() {
        return price_photography;
    }

    public void setPrice_photography(String price_photography) {
        this.price_photography = price_photography;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
