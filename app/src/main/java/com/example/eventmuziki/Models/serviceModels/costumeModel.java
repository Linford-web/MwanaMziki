package com.example.eventmuziki.Models.serviceModels;

import java.util.ArrayList;
import java.util.List;

public class costumeModel {
    String productName,socials,productPrice,category,status, productDetails,size, productAmount,serviceId,creatorId;
    ArrayList<String> images;

    public costumeModel() {
    }

    public costumeModel(String productName, String socials, String productPrice, String category, String status, String productDetails, String size, String productAmount, String serviceId, String creatorId, ArrayList<String> images) {
        this.productName = productName;
        this.socials = socials;
        this.productPrice = productPrice;
        this.category = category;
        this.status = status;
        this.productDetails = productDetails;
        this.size = size;
        this.productAmount = productAmount;
        this.serviceId = serviceId;
        this.creatorId = creatorId;
        this.images = images;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSocials() {
        return socials;
    }

    public void setSocials(String socials) {
        this.socials = socials;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
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

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
