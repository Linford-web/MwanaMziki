package com.example.eventmuziki.Models.serviceModels;

import java.util.ArrayList;

public class carModel {

    String car_model, car_details, car_type, status, time, transmission, seats, category, creatorId;
    ArrayList<String> images;

    public carModel() {
    }

    public carModel(String car_model, String car_details, String car_type, String status, String time, String transmission, String seats, String category, String creatorId, ArrayList<String> images) {
        this.car_model = car_model;
        this.car_details = car_details;
        this.car_type = car_type;
        this.status = status;
        this.time = time;
        this.transmission = transmission;
        this.seats = seats;
        this.category = category;
        this.creatorId = creatorId;
        this.images = images;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_details() {
        return car_details;
    }

    public void setCar_details(String car_details) {
        this.car_details = car_details;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
