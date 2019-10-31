package com.mobitrack.mobi.model;

public class MonthlyRBody {

    private String company_name;
    private String address;
    private String model;
    private String driver;
    private String data;
    private double milage;
    private double fuel_in_congestion;


    public MonthlyRBody() {
    }

    public MonthlyRBody(String company_name, String address, String model, String driver, String data) {
        this.company_name = company_name;
        this.address = address;
        this.model = model;
        this.driver = driver;
        this.data = data;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getMilage() {
        return milage;
    }

    public void setMilage(double milage) {
        this.milage = milage;
    }

    public double getFuel_in_congestion() {
        return fuel_in_congestion;
    }

    public void setFuel_in_congestion(double fuel_in_congestion) {
        this.fuel_in_congestion = fuel_in_congestion;
    }
}
