package com.example.eventmuziki.Models;

import android.widget.ImageView;

public class serviceNameModel {

    String serviceName;
    int serviceIcon;
    private Class<?> activityClass;

    public serviceNameModel(String serviceName, int serviceIcon, Class<?> activityClass) {
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
        this.activityClass = activityClass;
    }

    public serviceNameModel() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(int serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<?> activityClass) {
        this.activityClass = activityClass;
    }


}
