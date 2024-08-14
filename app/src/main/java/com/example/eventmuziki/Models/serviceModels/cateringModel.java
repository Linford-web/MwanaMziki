package com.example.eventmuziki.Models.serviceModels;

import java.util.ArrayList;

public class cateringModel {
    String catering_company_name, social_media_catering, no_of_people, price_catering, catering_type, category, status, creatorId;
    ArrayList<String> images;

    public cateringModel() {
    }

    public cateringModel(String catering_company_name, String social_media_catering, String no_of_people, String price_catering, String catering_type, String category, String status, String creatorId, ArrayList<String> images) {
        this.catering_company_name = catering_company_name;
        this.social_media_catering = social_media_catering;
        this.no_of_people = no_of_people;
        this.price_catering = price_catering;
        this.catering_type = catering_type;
        this.category = category;
        this.status = status;
        this.creatorId = creatorId;
        this.images = images;
    }

    public String getCatering_company_name() {
        return catering_company_name;
    }

    public void setCatering_company_name(String catering_company_name) {
        this.catering_company_name = catering_company_name;
    }

    public String getSocial_media_catering() {
        return social_media_catering;
    }

    public void setSocial_media_catering(String social_media_catering) {
        this.social_media_catering = social_media_catering;
    }

    public String getNo_of_people() {
        return no_of_people;
    }

    public void setNo_of_people(String no_of_people) {
        this.no_of_people = no_of_people;
    }

    public String getPrice_catering() {
        return price_catering;
    }

    public void setPrice_catering(String price_catering) {
        this.price_catering = price_catering;
    }

    public String getCatering_type() {
        return catering_type;
    }

    public void setCatering_type(String catering_type) {
        this.catering_type = catering_type;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
