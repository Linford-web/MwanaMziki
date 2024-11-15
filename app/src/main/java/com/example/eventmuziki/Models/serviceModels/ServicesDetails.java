package com.example.eventmuziki.Models.serviceModels;

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

    public static class Musicians{
        String genre, instrument, instrumentDetails, status, payRate, musicDetails, policy, image, productId, creatorId, productType;

        public Musicians() {
        }

        public Musicians(String genre, String instrument, String instrumentDetails, String status, String payRate, String musicDetails, String policy, String image, String productId, String creatorId, String productType) {
            this.genre = genre;
            this.instrument = instrument;
            this.instrumentDetails = instrumentDetails;
            this.status = status;
            this.payRate = payRate;
            this.musicDetails = musicDetails;
            this.policy = policy;
            this.image = image;
            this.productId = productId;
            this.creatorId = creatorId;
            this.productType = productType;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getInstrument() {
            return instrument;
        }

        public void setInstrument(String instrument) {
            this.instrument = instrument;
        }

        public String getInstrumentDetails() {
            return instrumentDetails;
        }

        public void setInstrumentDetails(String instrumentDetails) {
            this.instrumentDetails = instrumentDetails;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayRate() {
            return payRate;
        }

        public void setPyRate(String pyRate) {
            this.payRate = pyRate;
        }

        public String getMusicDetails() {
            return musicDetails;
        }

        public void setMusicDetails(String musicDetails) {
            this.musicDetails = musicDetails;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }
    }

    public static class carModel {

        String car_model, car_details, car_type, car_color, status, transmission, seats, driverProvided, creatorId, price_per_hour, price_per_extra_hour, image, productId, productType;

        public carModel() {
        }

        public carModel(String car_model, String car_details, String car_type, String car_color, String status, String transmission, String seats, String driverProvided, String creatorId, String price_per_hour, String price_per_extra_hour, String image, String productId, String productType) {
            this.car_model = car_model;
            this.car_details = car_details;
            this.car_type = car_type;
            this.car_color = car_color;
            this.status = status;
            this.transmission = transmission;
            this.seats = seats;
            this.driverProvided = driverProvided;
            this.creatorId = creatorId;
            this.price_per_hour = price_per_hour;
            this.price_per_extra_hour = price_per_extra_hour;
            this.image = image;
            this.productId = productId;
            this.productType = productType;

        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public static class cateringModel {
        String name,Packaged,cuisine, service ,number, packagePrice, detail, booking, setup, staff, noOffStaff, coordinator, theme, transportation ,cancelPolicy, status ,creatorId, image, productId, productType;

        public cateringModel() {
        }

        public cateringModel(String name, String Packaged, String cuisine, String service, String number, String packagePrice, String detail, String booking, String setup, String staff,String noOffStaff,String coordinator, String theme, String transportation, String cancelPolicy, String status, String creatorId, String image, String productId, String productType) {
            this.name = name;
            this.Packaged = Packaged;
            this.cuisine = cuisine;
            this.service = service;
            this.number = number;
            this.packagePrice = packagePrice;
            this.detail = detail;
            this.booking = booking;
            this.setup = setup;
            this.staff = staff;
            this.noOffStaff = noOffStaff;
            this.coordinator = coordinator;
            this.theme = theme;
            this.transportation = transportation;
            this.cancelPolicy = cancelPolicy;
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.productType = productType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackaged() {
            return Packaged;
        }

        public void setPackaged(String packaged) {
            Packaged = packaged;
        }

        public String getCuisine() {
            return cuisine;
        }

        public void setCuisine(String cuisine) {
            this.cuisine = cuisine;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(String packagePrice) {
            this.packagePrice = packagePrice;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getBooking() {
            return booking;
        }

        public String getNoOffStaff() {
            return noOffStaff;
        }

        public void setNoOffStaff(String noOffStaff) {
            this.noOffStaff = noOffStaff;
        }

        public void setBooking(String booking) {
            this.booking = booking;
        }

        public String getSetup() {
            return setup;
        }

        public void setSetup(String setup) {
            this.setup = setup;
        }

        public String getStaff() {
            return staff;
        }

        public void setStaff(String staff) {
            this.staff = staff;
        }

        public String getCoordinator() {
            return coordinator;
        }

        public void setCoordinator(String coordinator) {
            this.coordinator = coordinator;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getTransportation() {
            return transportation;
        }

        public void setTransportation(String transportation) {
            this.transportation = transportation;
        }

        public String getCancelPolicy() {
            return cancelPolicy;
        }

        public void setCancelPolicy(String cancelPolicy) {
            this.cancelPolicy = cancelPolicy;
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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }

    public static class costumeModel {

        String costumeName, gender, ageGroup, details, size, productAmount, material, customization, cleaning, duration, buyPrice, hirePrice, lateFee, delivery, policy, status, creatorId, image, productId, productType;

        public costumeModel() {
        }

        public costumeModel(String costumeName, String gender, String ageGroup, String details, String size, String productAmount, String material, String customization, String cleaning, String duration, String buyPrice, String hirePrice, String lateFee, String delivery, String policy, String status, String creatorId, String image, String productId, String productType) {
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
            this.productId = productId;
            this.productType = productType;
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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
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

        String type, name, details, area, quantity, price, extraPrice, setup, delivery, wireless, packaged, status, creatorId, image, productId, productType;

        public soundModel() {
        }

        public soundModel(String type, String name, String details, String area, String quantity, String price, String extraPrice, String setup, String delivery, String wireless, String packaged, String status, String creatorId, String image, String productId, String productType) {
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
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.productType = productType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

    public static class photoModel {

        String packageName, NoPhotographers,noPhotos,format, deliveryTime, delivery, portfolioLink, photoPackagePrice, pricePerHour, photoExtraPrice, photoAdvancedBooking, specialEquipment, status, creatorId, image, travel, preShoot, productId, productType;

        public photoModel() {
        }

        public photoModel( String packageName, String noPhotographers, String noPhotos, String format, String deliveryTime, String delivery, String portfolioLink, String photoPackagePrice, String pricePerHour, String photoExtraPrice, String photoAdvancedBooking, String specialEquipment, String status, String creatorId, String image, String travel, String preShoot, String productId, String productType) {
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
            this.productId = productId;
            this.productType = productType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
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

    public static class influencerModel{

        String handle ,platform, subscribers, age, gender, location , Package, content,posts,schedule ,theme ,freedom ,collaboration ,cancellation, coverage, status, creatorId, image, productId, productType;

        public influencerModel() {
        }

        public influencerModel(String handle, String platform, String subscribers, String age, String gender, String location, String aPackage, String content, String posts, String schedule, String theme, String freedom, String collaboration, String cancellation, String coverage, String status, String creatorId, String image, String productId, String productType) {
            this.handle = handle;
            this.platform = platform;
            this.subscribers = subscribers;
            this.age = age;
            this.gender = gender;
            this.location = location;
            this.Package = aPackage;
            this.content = content;
            this.posts = posts;
            this.schedule = schedule;
            this.theme = theme;
            this.freedom = freedom;
            this.collaboration = collaboration;
            this.cancellation = cancellation;
            this.coverage = coverage;
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.productType = productType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getSubscribers() {
            return subscribers;
        }

        public void setSubscribers(String subscribers) {
            this.subscribers = subscribers;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPackage() {
            return Package;
        }

        public void setPackage(String aPackage) {
            Package = aPackage;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPosts() {
            return posts;
        }

        public void setPosts(String posts) {
            this.posts = posts;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getFreedom() {
            return freedom;
        }

        public void setFreedom(String freedom) {
            this.freedom = freedom;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCollaboration() {
            return collaboration;
        }

        public void setCollaboration(String collaboration) {
            this.collaboration = collaboration;
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

        public String getCancellation() {
            return cancellation;
        }

        public void setCancellation(String cancellation) {
            this.cancellation = cancellation;
        }

        public String getCoverage() {
            return coverage;
        }

        public void setCoverage(String coverage) {
            this.coverage = coverage;
        }
    }

    public static class sponsorModel{
        String name, type, event , age , industry, interests, promotion, amount, guide, preBooking , audience ,cancellationPolicy, status, creatorId, image, productId, productType;

        public sponsorModel() {
        }

        public sponsorModel(String name, String type, String event, String age, String industry,
                            String interests, String promotion, String amount, String guide, String preBooking,
                            String audience, String cancellationPolicy, String status, String creatorId, String image, String productId, String productType) {
            this.name = name;
            this.type = type;
            this.event = event;
            this.age = age;
            this.industry = industry;
            this.interests = interests;
            this.promotion = promotion;
            this.amount = amount;
            this.guide = guide;
            this.preBooking = preBooking;
            this.audience = audience;
            this.cancellationPolicy = cancellationPolicy;
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.productType = productType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getInterests() {
            return interests;
        }

        public void setInterests(String interests) {
            this.interests = interests;
        }

        public String getPromotion() {
            return promotion;
        }

        public void setPromotion(String promotion) {
            this.promotion = promotion;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGuide() {
            return guide;
        }

        public void setGuide(String guide) {
            this.guide = guide;
        }

        public String getPreBooking() {
            return preBooking;
        }

        public void setPreBooking(String preBooking) {
            this.preBooking = preBooking;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public String getCancellationPolicy() {
            return cancellationPolicy;
        }

        public void setCancellationPolicy(String cancellationPolicy) {
            this.cancellationPolicy = cancellationPolicy;
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

    public static class decorationModel {

        String name , Package , theme , event, details , customization , emergency, setUp ,time , cancellation, amount, status, creatorId, image, productId, productType;

        public decorationModel() {
        }

        public decorationModel(String name, String aPackage, String theme, String event, String details, String customization,
                               String emergency, String setUp, String time, String cancellation, String status, String amount,
                               String creatorId, String image, String productId, String productType) {
            this.name = name;
            Package = aPackage;
            this.theme = theme;
            this.event = event;
            this.details = details;
            this.customization = customization;
            this.emergency = emergency;
            this.setUp = setUp;
            this.time = time;
            this.cancellation = cancellation;
            this.status = status;
            this.amount = amount;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.productType = productType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackage() {
            return Package;
        }

        public void setPackage(String aPackage) {
            Package = aPackage;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getCustomization() {
            return customization;
        }

        public void setCustomization(String customization) {
            this.customization = customization;
        }

        public String getEmergency() {
            return emergency;
        }

        public void setEmergency(String emergency) {
            this.emergency = emergency;
        }

        public String getSetUp() {
            return setUp;
        }

        public void setSetUp(String setUp) {
            this.setUp = setUp;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCancellation() {
            return cancellation;
        }

        public void setCancellation(String cancellation) {
            this.cancellation = cancellation;
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

    public static class makeUpModel{
        String name, specialization, makeUpPackage , price , portfolio, travel, preBooking ,Policy, status, creatorId, image, productId, productType;

        public makeUpModel() {
        }

        public makeUpModel(String name, String specialization, String makeUpPackage, String price, String portfolio, String travel, String preBooking, String policy, String status, String creatorId, String image, String productId, String productType) {
            this.name = name;
            this.specialization = specialization;
            this.makeUpPackage = makeUpPackage;
            this.price = price;
            this.portfolio = portfolio;
            this.travel = travel;
            this.preBooking = preBooking;
            this.Policy = policy;
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.productType = productType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getMakeUpPackage() {
            return makeUpPackage;
        }

        public void setMakeUpPackage(String makeUpPackage) {
            this.makeUpPackage = makeUpPackage;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPortfolio() {
            return portfolio;
        }

        public void setPortfolio(String portfolio) {
            this.portfolio = portfolio;
        }

        public String getTravel() {
            return travel;
        }

        public void setTravel(String travel) {
            this.travel = travel;
        }

        public String getPreBooking() {
            return preBooking;
        }

        public void setPreBooking(String preBooking) {
            this.preBooking = preBooking;
        }

        public String getPolicy() {
            return Policy;
        }

        public void setPolicy(String policy) {
            Policy = policy;
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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }
    }

    public static class mcModel{
        String specialization, eventRole, duration, audience, price, equipment, bookingTime, policy, status, creatorId, image, productId, productType;

        public mcModel() {
        }

        public mcModel(String specialization, String eventRole, String duration, String audience, String price, String equipment,
                       String bookingTime, String policy, String status, String creatorId, String image, String productId, String productType) {
            this.specialization = specialization;
            this.eventRole = eventRole;
            this.duration = duration;
            this.audience = audience;
            this.price = price;
            this.equipment = equipment;
            this.bookingTime = bookingTime;
            this.policy = policy;
            this.status = status;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.productType = productType;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getEventRole() {
            return eventRole;
        }

        public void setEventRole(String eventRole) {
            this.eventRole = eventRole;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getEquipment() {
            return equipment;
        }

        public void setEquipment(String equipment) {
            this.equipment = equipment;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }
    }

    public static class cartModel{
        String name, packageName, price, creatorId, productId, image, bookerId, bookedServiceId, pType, cartId;

        public cartModel() {
        }

        public cartModel(String name, String packageName, String price, String creatorId,
                         String productId, String image, String bookerId, String bookedServiceId, String pType, String cartId) {
            this.name = name;
            this.packageName = packageName;
            this.price = price;
            this.creatorId = creatorId;
            this.image = image;
            this.productId = productId;
            this.bookerId = bookerId;
            this.bookedServiceId = bookedServiceId;
            this.pType = pType;
            this.cartId = cartId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBookedServiceId() {
            return bookedServiceId;
        }

        public void setBookedServiceId(String bookedServiceId) {
            this.bookedServiceId = bookedServiceId;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getBookerId() {
            return bookerId;
        }

        public void setBookerId(String bookerId) {
            this.bookerId = bookerId;
        }

        public String getpType() {
            return pType;
        }

        public void setpType(String pType) {
            this.pType = pType;
        }
    }

    public static class bookedBiddersModel {

        String biddersName, biddersEmail, biddersPhone ,  profile, organizerName, creatorId, bidAmount, biddersId, eventId, docId, bookedEventId;

        public bookedBiddersModel() {
        }

        public bookedBiddersModel(String biddersName, String biddersEmail, String biddersPhone, String organizerName, String creatorId, String profile, String bidAmount, String biddersId, String eventId, String docId, String bookedEventId) {
            this.biddersName = biddersName;
            this.biddersEmail = biddersEmail;
            this.biddersPhone = biddersPhone;
            this.organizerName = organizerName;
            this.creatorId = creatorId;
            this.profile = profile;
            this.bidAmount = bidAmount;
            this.biddersId = biddersId;
            this.eventId = eventId;
            this.docId = docId;
            this.bookedEventId = bookedEventId;
        }

        public String getOrganizerName() {
            return organizerName;
        }

        public void setOrganizerName(String organizerName) {
            this.organizerName = organizerName;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getBidAmount() {
            return bidAmount;
        }

        public void setBidAmount(String bidAmount) {
            this.bidAmount = bidAmount;
        }

        public String getBiddersName() {
            return biddersName;
        }

        public void setBiddersName(String biddersName) {
            this.biddersName = biddersName;
        }

        public String getBiddersEmail() {
            return biddersEmail;
        }

        public void setBiddersEmail(String biddersEmail) {
            this.biddersEmail = biddersEmail;
        }

        public String getBiddersPhone() {
            return biddersPhone;
        }

        public void setBiddersPhone(String biddersPhone) {
            this.biddersPhone = biddersPhone;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getBiddersId() {
            return biddersId;
        }

        public void setBiddersId(String biddersId) {
            this.biddersId = biddersId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getBookedEventId() {
            return bookedEventId;
        }

        public void setBookedEventId(String bookedEventId) {
            this.bookedEventId = bookedEventId;
        }
    }

}
