package com.bacancy.eprodigy.Models;




public class UserLocation {

    private String addressTitle;
    private String addressDesc;
    private String addressLatitude;
    private String addressLongitude;
    private String addressImageUrl;


    public UserLocation(String addressTitle, String addressDesc, String addressLatitude, String addressLongitude) {
        this.addressTitle = addressTitle;
        this.addressDesc = addressDesc;
        this.addressLatitude = addressLatitude;
        this.addressLongitude = addressLongitude;

    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressDesc() {
        return addressDesc;
    }

    public void setAddressDesc(String addressDesc) {
        this.addressDesc = addressDesc;
    }

    public String getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(String addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public String getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(String addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public String getAddressImageUrl() {
        return addressImageUrl;
    }

    public void setAddressImageUrl(String addressImageUrl) {
        this.addressImageUrl = addressImageUrl;
    }
}
