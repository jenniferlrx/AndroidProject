package com.example.finalproject;

class ChargingStation {
    private String title;
    private String latitude;
    private String longtitude;
    private String phoneNo;
    private String address;
    private boolean fav;

    public ChargingStation(){
        this.title = "Unknown";
        this.latitude = "Unknown";
        this.longtitude = "Unknown";
        this.phoneNo = "Unknown";
        this.address = "Unknown";
        this.fav = false;
    }

    public ChargingStation(String title, String latitude, String longtitude, String phoneNo, String address){
        this.title = title;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.phoneNo = phoneNo;
        this.address = address;
        this.fav = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

}
