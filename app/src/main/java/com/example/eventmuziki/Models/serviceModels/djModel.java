package com.example.eventmuziki.Models.serviceModels;

import java.util.ArrayList;

public class djModel {
    String company_name, socials, price_dj, company_services,category, status, creatorId;
    ArrayList<String> images;

    public djModel() {
    }

    public djModel(String company_name, String socials, String price_dj, String company_services, String category, String status, String creatorId, ArrayList<String> images) {
        this.company_name = company_name;
        this.socials = socials;
        this.price_dj = price_dj;
        this.company_services = company_services;
        this.category = category;
        this.status = status;
        this.creatorId = creatorId;
        this.images = images;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getSocials() {
        return socials;
    }

    public void setSocials(String socials) {
        this.socials = socials;
    }

    public String getPrice_dj() {
        return price_dj;
    }

    public void setPrice_dj(String price_dj) {
        this.price_dj = price_dj;
    }

    public String getCompany_services() {
        return company_services;
    }

    public void setCompany_services(String company_services) {
        this.company_services = company_services;
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
