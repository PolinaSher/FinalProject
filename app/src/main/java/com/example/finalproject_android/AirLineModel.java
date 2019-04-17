package com.example.finalproject_android;

public class AirLineModel {


    private String departureCode;
    private String status;
    private double horizontalSpeed;
    private double altitude;
    private String location;
    private int id;



    public AirLineModel(){

    }


    public AirLineModel(String departureCode, String status, double horizontalSpeed, double altitude, String location, int id) {
        this.departureCode = departureCode;
        this.status = status;
        this.horizontalSpeed = horizontalSpeed;
        this.altitude = altitude;
        this.location = location;
        this.id = id;
    }

    public String getDepartureCode() {
        return departureCode;
    }

    public void setDepartureCode(String departureCode) {
        this.departureCode = departureCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public void setHorizontalSpeed(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
