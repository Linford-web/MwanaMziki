package com.example.eventmuziki.Models.serviceModels;

public class serviceDetailModel {

    public static class carHire{
        public String model, type, color, seats, time, service;

        public carHire() {
        }

        public carHire(String model, String type, String color, String seats, String time, String service) {
            this.model = model;
            this.type = type;
            this.color = color;
            this.seats = seats;
            this.time = time;
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
        public String event_location, event_date, sound_duration, soundServices, service;

        public hirePA() {
        }

        public hirePA(String event_location, String event_date, String sound_duration, String soundServices, String service) {
            this.event_location = event_location;
            this.event_date = event_date;
            this.sound_duration = sound_duration;
            this.soundServices = soundServices;
            this.service = service;
        }

        public String getEvent_location() {
            return event_location;
        }

        public void setEvent_location(String event_location) {
            this.event_location = event_location;
        }

        public String getEvent_date() {
            return event_date;
        }

        public void setEvent_date(String event_date) {
            this.event_date = event_date;
        }

        public String getSound_duration() {
            return sound_duration;
        }

        public void setSound_duration(String sound_duration) {
            this.sound_duration = sound_duration;
        }

        public String getSoundServices() {
            return soundServices;
        }

        public void setSoundServices(String soundServices) {
            this.soundServices = soundServices;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireDeco{
        public String deco_location, deco_date, deco_duration, deco_price, service;

        public hireDeco() {
        }

        public hireDeco(String deco_location, String deco_date, String deco_duration, String deco_price, String service) {
            this.deco_location = deco_location;
            this.deco_date = deco_date;
            this.deco_duration = deco_duration;
            this.deco_price = deco_price;
            this.service = service;
        }

        public String getDeco_location() {
            return deco_location;
        }

        public void setDeco_location(String deco_location) {
            this.deco_location = deco_location;
        }

        public String getDeco_date() {
            return deco_date;
        }

        public void setDeco_date(String deco_date) {
            this.deco_date = deco_date;
        }

        public String getDeco_duration() {
            return deco_duration;
        }

        public void setDeco_duration(String deco_duration) {
            this.deco_duration = deco_duration;
        }

        public String getDeco_price() {
            return deco_price;
        }

        public void setDeco_price(String deco_price) {
            this.deco_price = deco_price;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireContent{
        public String creatorName, creatorSocials, content_duration, content_price, service;

        public hireContent() {
        }

        public hireContent(String creatorName, String creatorSocials, String content_duration, String content_price, String service) {
            this.creatorName = creatorName;
            this.creatorSocials = creatorSocials;
            this.content_duration = content_duration;
            this.content_price = content_price;
            this.service = service;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public String getCreatorSocials() {
            return creatorSocials;
        }

        public void setCreatorSocials(String creatorSocials) {
            this.creatorSocials = creatorSocials;
        }

        public String getContent_duration() {
            return content_duration;
        }

        public void setContent_duration(String content_duration) {
            this.content_duration = content_duration;
        }

        public String getContent_price() {
            return content_price;
        }

        public void setContent_price(String content_price) {
            this.content_price = content_price;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireSponsors {
        public String sponsorEventCategory, currentSponsor, sponsorsLocation, sponsorsPrice, service;

        public hireSponsors() {
        }

        public hireSponsors(String sponsorEventCategory, String currentSponsor, String sponsorsLocation, String sponsorsPrice, String service) {
            this.sponsorEventCategory = sponsorEventCategory;
            this.currentSponsor = currentSponsor;
            this.sponsorsLocation = sponsorsLocation;
            this.sponsorsPrice = sponsorsPrice;
            this.service = service;
        }

        public String getSponsorEventCategory() {
            return sponsorEventCategory;
        }

        public void setSponsorEventCategory(String sponsorEventCategory) {
            this.sponsorEventCategory = sponsorEventCategory;
        }

        public String getCurrentSponsor() {
            return currentSponsor;
        }

        public void setCurrentSponsor(String currentSponsor) {
            this.currentSponsor = currentSponsor;
        }

        public String getSponsorsLocation() {
            return sponsorsLocation;
        }

        public void setSponsorsLocation(String sponsorsLocation) {
            this.sponsorsLocation = sponsorsLocation;
        }

        public String getSponsorsPrice() {
            return sponsorsPrice;
        }

        public void setSponsorsPrice(String sponsorsPrice) {
            this.sponsorsPrice = sponsorsPrice;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

}
