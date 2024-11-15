package com.example.eventmuziki.Models.walletModels;

public class walletModel {

    public static class walletMenu{
        String serviceName;
        int serviceIcon;

        public walletMenu() {
        }

        public walletMenu(String serviceName, int serviceIcon) {
            this.serviceName = serviceName;
            this.serviceIcon = serviceIcon;
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
    }


}
