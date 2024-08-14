package com.example.eventmuziki.Models.serviceModels;

public class serviceDetailModel {

    public static class carHire{
        public String pickUpLocation, dropOffLocation, pickUpDate, dropOffDate, carType, service;

        public carHire() {
        }

        public carHire(String pickUpLocation, String dropOffLocation, String pickUpDate, String dropOffDate, String carType, String service) {
            this.pickUpLocation = pickUpLocation;
            this.dropOffLocation = dropOffLocation;
            this.pickUpDate = pickUpDate;
            this.dropOffDate = dropOffDate;
            this.carType = carType;
            this.service = service;
        }

        public String getPickUpLocation() {
            return pickUpLocation;
        }

        public void setPickUpLocation(String pickUpLocation) {
            this.pickUpLocation = pickUpLocation;
        }

        public String getDropOffLocation() {
            return dropOffLocation;
        }

        public void setDropOffLocation(String dropOffLocation) {
            this.dropOffLocation = dropOffLocation;
        }

        public String getPickUpDate() {
            return pickUpDate;
        }

        public void setPickUpDate(String pickUpDate) {
            this.pickUpDate = pickUpDate;
        }

        public String getDropOffDate() {
            return dropOffDate;
        }

        public void setDropOffDate(String dropOffDate) {
            this.dropOffDate = dropOffDate;
        }

        public String getCarType() {
            return carType;
        }

        public void setCarType(String carType) {
            this.carType = carType;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hirePhotographer{
        public String price, photography_location, photography_date, duration, service;

        public hirePhotographer() {
        }

        public hirePhotographer(String price, String photography_location, String photography_date, String duration, String service) {
            this.price = price;
            this.photography_location = photography_location;
            this.photography_date = photography_date;
            this.duration = duration;
            this.service = service;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPhotography_location() {
            return photography_location;
        }

        public void setPhotography_location(String photography_location) {
            this.photography_location = photography_location;
        }

        public String getPhotography_date() {
            return photography_date;
        }

        public void setPhotography_date(String photography_date) {
            this.photography_date = photography_date;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireCatering{
        public String catering_location, catering_date, number_of_guests, cuisine_type, catering_tpye, service;

        public hireCatering() {
        }

        public hireCatering(String catering_location, String catering_date, String number_of_guests, String cuisine_type, String catering_tpye, String service) {
            this.catering_location = catering_location;
            this.catering_date = catering_date;
            this.number_of_guests = number_of_guests;
            this.cuisine_type = cuisine_type;
            this.catering_tpye = catering_tpye;
            this.service = service;
        }

        public String getCatering_location() {
            return catering_location;
        }

        public void setCatering_location(String catering_location) {
            this.catering_location = catering_location;
        }

        public String getCatering_date() {
            return catering_date;
        }

        public void setCatering_date(String catering_date) {
            this.catering_date = catering_date;
        }

        public String getNumber_of_guests() {
            return number_of_guests;
        }

        public void setNumber_of_guests(String number_of_guests) {
            this.number_of_guests = number_of_guests;
        }

        public String getCatering_tpye() {
            return catering_tpye;
        }

        public void setCatering_tpye(String catering_tpye) {
            this.catering_tpye = catering_tpye;
        }

        public String getCuisine_type() {
            return cuisine_type;
        }

        public void setCuisine_type(String cuisine_type) {
            this.cuisine_type = cuisine_type;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }
    }

    public static class hireCostumes{
        public String costumeType, costumeSize, costumeQuantity, service;

        public hireCostumes() {
        }

        public hireCostumes(String costumeType, String costumeSize, String costumeQuantity, String service) {
            this.costumeType = costumeType;
            this.costumeSize = costumeSize;
            this.costumeQuantity = costumeQuantity;
            this.service = service;
        }

        public String getCostumeType() {
            return costumeType;
        }

        public void setCostumeType(String costumeType) {
            this.costumeType = costumeType;
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
