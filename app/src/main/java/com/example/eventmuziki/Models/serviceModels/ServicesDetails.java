package com.example.eventmuziki.Models.serviceModels;

import java.util.ArrayList;

public class ServicesDetails {

    public static class serviceDetail{
        String creatorId, category, serviceId;

        public serviceDetail() {
        }

        public serviceDetail(String creatorId, String category, String serviceId) {
            this.creatorId = creatorId;
            this.category = category;
            this.serviceId = serviceId;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }
    }

    public static class carModel {

        String car_model, car_details, car_type, car_color, status, time, transmission, seats, driverProvided, creatorId, price_per_hour, price_per_extra_hour, carImage;

        public carModel() {
        }

        public carModel(String car_model, String car_details, String car_type, String car_color, String status, String time, String transmission, String seats, String driverProvided, String creatorId, String price_per_hour, String price_per_extra_hour, String carImage) {
            this.car_model = car_model;
            this.car_details = car_details;
            this.car_type = car_type;
            this.car_color = car_color;
            this.status = status;
            this.time = time;
            this.transmission = transmission;
            this.seats = seats;
            this.driverProvided = driverProvided;
            this.creatorId = creatorId;
            this.price_per_hour = price_per_hour;
            this.price_per_extra_hour = price_per_extra_hour;
            this.carImage = carImage;

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

        public String getCar_color() {
            return car_color;
        }

        public void setCar_color(String car_color) {
            this.car_color = car_color;
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

        public String getDriverProvided() {
            return driverProvided;
        }

        public void setDriverProvided(String driverProvided) {
            this.driverProvided = driverProvided;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getPrice_per_hour() {
            return price_per_hour;
        }

        public void setPrice_per_hour(String price_per_hour) {
            this.price_per_hour = price_per_hour;
        }

        public String getPrice_per_extra_hour() {
            return price_per_extra_hour;
        }

        public void setPrice_per_extra_hour(String price_per_extra_hour) {
            this.price_per_extra_hour = price_per_extra_hour;
        }

        public String getCarImage() {
            return carImage;
        }

        public void setCarImage(String carImage) {
            this.carImage = carImage;
        }
    }

    public static class cateringModel {
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

    public static class costumeModel {

        String costumeName, gender, ageGroup, details, size, productAmount, material, customization, cleaning, duration, buyPrice, hirePrice, lateFee, delivery, policy, status, creatorId, image;

        public costumeModel() {
        }

        public costumeModel(String costumeName, String gender, String ageGroup, String details, String size, String productAmount, String material, String customization, String cleaning, String duration, String buyPrice, String hirePrice, String lateFee, String delivery, String policy, String status, String creatorId, String image) {
            this.costumeName = costumeName;
            this.gender = gender;
            this.ageGroup = ageGroup;
            this.details = details;
            this.size = size;
            this.productAmount = productAmount;
            this.material = material;
            this.customization = customization;
            this.cleaning = cleaning;
            this.duration = duration;
            this.buyPrice = buyPrice;
            this.hirePrice = hirePrice;
            this.lateFee = lateFee;
            this.delivery = delivery;
            this.policy = policy;
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
        }

        public String getCostumeName() {
            return costumeName;
        }

        public void setCostumeName(String costumeName) {
            this.costumeName = costumeName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAgeGroup() {
            return ageGroup;
        }

        public void setAgeGroup(String ageGroup) {
            this.ageGroup = ageGroup;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
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

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getCustomization() {
            return customization;
        }

        public void setCustomization(String customization) {
            this.customization = customization;
        }

        public String getCleaning() {
            return cleaning;
        }

        public void setCleaning(String cleaning) {
            this.cleaning = cleaning;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getBuyPrice() {
            return buyPrice;
        }

        public void setBuyPrice(String buyPrice) {
            this.buyPrice = buyPrice;
        }

        public String getHirePrice() {
            return hirePrice;
        }

        public void setHirePrice(String hirePrice) {
            this.hirePrice = hirePrice;
        }

        public String getLateFee() {
            return lateFee;
        }

        public void setLateFee(String lateFee) {
            this.lateFee = lateFee;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class soundModel {

        String type, name, details, area, quantity, price, extraPrice, setup, delivery, wireless, packaged, userId, image;

        public soundModel() {
        }

        public soundModel(String type, String name, String details, String area, String quantity, String price, String extraPrice, String setup, String delivery, String wireless, String packaged, String userId, String image) {
            this.type = type;
            this.name = name;
            this.details = details;
            this.area = area;
            this.quantity = quantity;
            this.price = price;
            this.extraPrice = extraPrice;
            this.setup = setup;
            this.delivery = delivery;
            this.wireless = wireless;
            this.packaged = packaged;
            this.userId = userId;
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getExtraPrice() {
            return extraPrice;
        }

        public void setExtraPrice(String extraPrice) {
            this.extraPrice = extraPrice;
        }

        public String getSetup() {
            return setup;
        }

        public void setSetup(String setup) {
            this.setup = setup;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getWireless() {
            return wireless;
        }

        public void setWireless(String wireless) {
            this.wireless = wireless;
        }

        public String getPackaged() {
            return packaged;
        }

        public void setPackaged(String packaged) {
            this.packaged = packaged;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class photoModel {

        String packageName, NoPhotographers,noPhotos,format, deliveryTime, delivery, portfolioLink, photoPackagePrice, pricePerHour, photoExtraPrice, photoAdvancedBooking, specialEquipment, status, creatorId, image, travel, preShoot;

        public photoModel() {
        }

        public photoModel( String packageName, String noPhotographers, String noPhotos, String format, String deliveryTime, String delivery, String portfolioLink, String photoPackagePrice, String pricePerHour, String photoExtraPrice, String photoAdvancedBooking, String specialEquipment, String status, String creatorId, String image, String travel, String preShoot) {
            this.packageName = packageName;
            this.NoPhotographers = noPhotographers;
            this.noPhotos = noPhotos;
            this.format = format;
            this.deliveryTime = deliveryTime;
            this.delivery = delivery;
            this.portfolioLink = portfolioLink;
            this.photoPackagePrice = photoPackagePrice;
            this.pricePerHour = pricePerHour;
            this.photoExtraPrice = photoExtraPrice;
            this.photoAdvancedBooking = photoAdvancedBooking;
            this.specialEquipment = specialEquipment;
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
            this.travel = travel;
            this.preShoot = preShoot;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getNoPhotographers() {
            return NoPhotographers;
        }

        public void setNoPhotographers(String noPhotographers) {
            NoPhotographers = noPhotographers;
        }

        public String getNoPhotos() {
            return noPhotos;
        }

        public void setNoPhotos(String noPhotos) {
            this.noPhotos = noPhotos;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getPortfolioLink() {
            return portfolioLink;
        }

        public void setPortfolioLink(String portfolioLink) {
            this.portfolioLink = portfolioLink;
        }

        public String getPhotoPackagePrice() {
            return photoPackagePrice;
        }

        public void setPhotoPackagePrice(String photoPackagePrice) {
            this.photoPackagePrice = photoPackagePrice;
        }

        public String getPricePerHour() {
            return pricePerHour;
        }

        public void setPricePerHour(String pricePerHour) {
            this.pricePerHour = pricePerHour;
        }

        public String getPhotoExtraPrice() {
            return photoExtraPrice;
        }

        public void setPhotoExtraPrice(String photoExtraPrice) {
            this.photoExtraPrice = photoExtraPrice;
        }

        public String getPhotoAdvancedBooking() {
            return photoAdvancedBooking;
        }

        public void setPhotoAdvancedBooking(String photoAdvancedBooking) {
            this.photoAdvancedBooking = photoAdvancedBooking;
        }

        public String getSpecialEquipment() {
            return specialEquipment;
        }

        public void setSpecialEquipment(String specialEquipment) {
            this.specialEquipment = specialEquipment;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTravel() {
            return travel;
        }

        public void setTravel(String travel) {
            this.travel = travel;
        }

        public String getPreShoot() {
            return preShoot;
        }

        public void setPreShoot(String preShoot) {
            this.preShoot = preShoot;
        }
    }

}
