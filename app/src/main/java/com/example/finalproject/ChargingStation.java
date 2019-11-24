package com.example.finalproject;

class ChargingStation {
    /**
     * station title
     */
    private String title;
    /**
     * station latitude
     */
    private String latitude;
    /**
     * station longitude
     */
    private String longitude;
    /**
     * station phone number
     */
    private String phoneNo;
    /**
     * station address
     */
    private String address;
    /**
     * station saved in favs
     */
    private boolean fav;

    /**
     * default constructor
     */
    public ChargingStation(){
        this.title = "Unknown";
        this.latitude = "Unknown";
        this.longitude = "Unknown";
        this.phoneNo = "Unknown";
        this.address = "Unknown";
        this.fav = false;
    }

    /**
     * constructor with parameters, default the station is not saved in favs
     * @param title - station title
     * @param latitude - station latitude
     * @param longitude - station longitude
     * @param phoneNo - station phone number
     * @param address - station address
     */
    public ChargingStation(String title, String latitude, String longitude, String phoneNo, String address){
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNo = phoneNo;
        this.address = address;
        this.fav = false;
    }

    /**
     * constructor with an station object
     * @param station - station instance
     */
    public ChargingStation(ChargingStation station){
        this(station.title,station.latitude,station.longitude,station.phoneNo,station.address);
        this.setFav(station.isFav());

    }

    /**
     * get station title
     * @return - station title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set station title
     * @param title - station title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get station latitude
     * @return - station latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * get station longitude
     * @return - station longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * get station phone number
     * @return - station phone number
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * get station address
     * @return - station address
     */
    public String getAddress() {
        return address;
    }

    /**
     * check if the station is saved
     * @return - <code>true</code> if the station is saved, <code>false</code> if not
     */
    public boolean isFav() {
        return fav;
    }

    /**
     * set whether the station is saved
     * @param fav - <code>true</code> if the station is saved, <code>false</code> if not
     */
    public void setFav(boolean fav) {
        this.fav = fav;
    }

}
