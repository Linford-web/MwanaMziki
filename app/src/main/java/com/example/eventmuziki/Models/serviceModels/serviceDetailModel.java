package com.example.eventmuziki.Models.serviceModels;

public class serviceDetailModel {

    public static class hireMusician {

        public String genre, duration, equipment, priceRange, service;

        public hireMusician() {
        }

        public hireMusician(String genre, String duration, String equipment, String priceRange, String service) {
            this.genre = genre;
            this.duration = duration;
            this.equipment = equipment;
            this.priceRange = priceRange;
            this.service = service;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getEquipment() {
            return equipment;
        }

        public void setEquipment(String equipment) {
            this.equipment = equipment;
        }

        public String getPriceRange() {
            return priceRange;
        }

        public void setPriceRange(String priceRange) {
            this.priceRange = priceRange;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class carHire{
        public String model, type, color, seats, time, carModel, service;

        public carHire() {
        }

        public carHire(String model, String type, String color, String seats, String time, String carModel,String service) {
            this.model = model;
            this.type = type;
            this.color = color;
            this.seats = seats;
            this.time = time;
            this.carModel = carModel;
            this.service = service;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSeats() {
            return seats;
        }

        public void setSeats(String seats) {
            this.seats = seats;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hirePhotographer{
        public String photoPackage, duration, equipment, delivery, service;

        public hirePhotographer() {
        }
        public hirePhotographer(String photoPackage, String duration, String equipment, String delivery, String service) {
            this.photoPackage = photoPackage;
            this.duration = duration;
            this.equipment = equipment;
            this.delivery = delivery;
            this.service = service;
        }

        public String getPhotoPackage() {
            return photoPackage;
        }

        public void setPhotoPackage(String photoPackage) {
            this.photoPackage = photoPackage;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getEquipment() {
            return equipment;
        }

        public void setEquipment(String equipment) {
            this.equipment = equipment;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireCatering{
        public String caterings, cuisine, serviceCatering, cateringGuests, service;

        public hireCatering() {
        }

        public hireCatering(String caterings, String cuisine, String serviceCatering, String cateringGuests, String service) {
            this.caterings = caterings;
            this.cuisine = cuisine;
            this.serviceCatering = serviceCatering;
            this.cateringGuests = cateringGuests;
            this.service = service;
        }

        public String getCaterings() {
            return caterings;
        }

        public void setCaterings(String caterings) {
            this.caterings = caterings;
        }

        public String getCuisine() {
            return cuisine;
        }

        public void setCuisine(String cuisine) {
            this.cuisine = cuisine;
        }

        public String getServiceCatering() {
            return serviceCatering;
        }

        public void setServiceCatering(String serviceCatering) {
            this.serviceCatering = serviceCatering;
        }

        public String getCateringGuests() {
            return cateringGuests;
        }

        public void setCateringGuests(String cateringGuests) {
            this.cateringGuests = cateringGuests;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireCostumes{
        public String costumeAge, costumeSize, costumeQuantity, costumeDelivery, costumeDuration, service;

        public hireCostumes() {
        }

        public hireCostumes(String costumeAge, String costumeSize, String costumeQuantity, String costumeDelivery, String costumeDuration, String service) {
            this.costumeAge = costumeAge;
            this.costumeSize = costumeSize;
            this.costumeQuantity = costumeQuantity;
            this.costumeDelivery = costumeDelivery;
            this.costumeDuration = costumeDuration;
            this.service = service;
        }

        public String getCostumeAge() {
            return costumeAge;
        }

        public void setCostumeAge(String costumeAge) {
            this.costumeAge = costumeAge;
        }

        public String getCostumeSize() {
            return costumeSize;
        }

        public void setCostumeSize(String costumeSize) {
            this.costumeSize = costumeSize;
        }

        public String getCostumeQuantity() {
            return costumeQuantity;
        }

        public void setCostumeQuantity(String costumeQuantity) {
            this.costumeQuantity = costumeQuantity;
        }

        public String getCostumeDelivery() {
            return costumeDelivery;
        }

        public void setCostumeDelivery(String costumeDelivery) {
            this.costumeDelivery = costumeDelivery;
        }

        public String getCostumeDuration() {
            return costumeDuration;
        }

        public void setCostumeDuration(String costumeDuration) {
            this.costumeDuration = costumeDuration;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hirePA{
        public String soundPackage, equipmentType, quantity,delivery, service;

        public hirePA() {
        }

        public hirePA(String soundPackage, String equipmentType, String quantity, String delivery, String service) {
            this.soundPackage = soundPackage;
            this.equipmentType = equipmentType;
            this.quantity = quantity;
            this.delivery = delivery;
            this.service = service;
        }

        public String getSoundPackage() {
            return soundPackage;
        }

        public void setSoundPackage(String soundPackage) {
            this.soundPackage = soundPackage;
        }

        public String getEquipmentType() {
            return equipmentType;
        }

        public void setEquipmentType(String equipmentType) {
            this.equipmentType = equipmentType;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireDeco{
        public String decoPackage ,color1, color2 ,decoDuration, decoTheme, service;

        public hireDeco() {
        }

        public hireDeco(String decoPackage, String color1, String color2, String decoDuration, String decoTheme, String service) {
            this.decoPackage = decoPackage;
            this.color1 = color1;
            this.color2 = color2;
            this.decoDuration = decoDuration;
            this.decoTheme = decoTheme;
            this.service = service;
        }

        public String getDecoPackage() {
            return decoPackage;
        }

        public void setDecoPackage(String decoPackage) {
            this.decoPackage = decoPackage;
        }

        public String getColor1() {
            return color1;
        }

        public void setColor1(String color1) {
            this.color1 = color1;
        }

        public String getColor2() {
            return color2;
        }

        public void setColor2(String color2) {
            this.color2 = color2;
        }

        public String getDecoDuration() {
            return decoDuration;
        }

        public void setDecoDuration(String decoDuration) {
            this.decoDuration = decoDuration;
        }

        public String getDecoTheme() {
            return decoTheme;
        }

        public void setDecoTheme(String decoTheme) {
            this.decoTheme = decoTheme;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireContent{
        public String creatorPackage, duration, creatorTheme, creativity, eventCoverages, service;

        public hireContent() {
        }

        public hireContent(String creatorPackage, String duration, String creatorTheme, String creativity, String eventCoverages, String service) {
            this.creatorPackage = creatorPackage;
            this.duration = duration;
            this.creatorTheme = creatorTheme;
            this.creativity = creativity;
            this.eventCoverages = eventCoverages;
            this.service = service;
        }

        public String getCreatorPackage() {
            return creatorPackage;
        }

        public void setCreatorPackage(String creatorPackage) {
            this.creatorPackage = creatorPackage;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getCreatorTheme() {
            return creatorTheme;
        }

        public void setCreatorTheme(String creatorTheme) {
            this.creatorTheme = creatorTheme;
        }

        public String getCreativity() {
            return creativity;
        }

        public void setCreativity(String creativity) {
            this.creativity = creativity;
        }

        public String getEventCoverages() {
            return eventCoverages;
        }

        public void setEventCoverages(String eventCoverages) {
            this.eventCoverages = eventCoverages;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireSponsors {
        public String  sponsorType, industry ,sponsorshipAmount ,currentSponsors ,service;

        public hireSponsors() {
        }

        public hireSponsors(String sponsorType, String industry, String sponsorshipAmount, String currentSponsors, String service) {
            this.sponsorType = sponsorType;
            this.industry = industry;
            this.sponsorshipAmount = sponsorshipAmount;
            this.currentSponsors = currentSponsors;
            this.service = service;
        }

        public String getSponsorType() {
            return sponsorType;
        }

        public void setSponsorType(String sponsorType) {
            this.sponsorType = sponsorType;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getSponsorshipAmount() {
            return sponsorshipAmount;
        }

        public void setSponsorshipAmount(String sponsorshipAmount) {
            this.sponsorshipAmount = sponsorshipAmount;
        }

        public String getCurrentSponsors() {
            return currentSponsors;
        }

        public void setCurrentSponsors(String currentSponsors) {
            this.currentSponsors = currentSponsors;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

}
