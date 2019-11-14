package com.example.finalproject;

class ChargingStation {
    private String title;
    private String latitude;
    private String longitude;
    private String phoneNo;
    private String address;
    private boolean fav;

    public ChargingStation(){
        this.title = "Unknown";
        this.latitude = "Unknown";
        this.longitude = "Unknown";
        this.phoneNo = "Unknown";
        this.address = "Unknown";
        this.fav = false;
    }

    public ChargingStation(String title, String latitude, String longitude, String phoneNo, String address){
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNo = phoneNo;
        this.address = address;
        this.fav = false;
    }

    public ChargingStation(ChargingStation station){
        this(station.title,station.latitude,station.longitude,station.phoneNo,station.address);
        this.setFav(station.isFav());

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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
