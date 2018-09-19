package com.example.admin.litepaltoexcel.bean;

/**
 * Created by admin on 2018/6/23.
 */

public class Product extends BaseBean  {

    private String boatName;
    private String dateTime;
    private String productName;
    private String productBatch;
    private String area;
    private String Lat;
    private String Lon;
    private String note;

    public Product() {}

    public Product(String boatName, String dateTime, String productName, String productBatch, String area, String lat, String lon, String note) {
        this.boatName = boatName;
        this.dateTime = dateTime;
        this.productName = productName;
        this.productBatch = productBatch;
        this.area = area;
        Lat = lat;
        Lon = lon;
        this.note = note;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBatch() {
        return productBatch;
    }

    public void setProductBatch(String productBatch) {
        this.productBatch = productBatch;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Product{" +
                "boatName='" + boatName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", productName='" + productName + '\'' +
                ", productBatch='" + productBatch + '\'' +
                ", area='" + area + '\'' +
                ", Lat='" + Lat + '\'' +
                ", Lon='" + Lon + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
