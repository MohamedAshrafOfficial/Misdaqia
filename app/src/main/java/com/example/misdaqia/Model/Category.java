package com.example.misdaqia.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {
//    int id;
//    String created_at;
//    String updated_at;
//    String type;
//    int year;
//    String model;
//    String vendor;
//    String color;
//    @SerializedName("new")
//    int neww;
//    int ownerID;
//    int price;

    public int id;
    public String created_at ;
    public String updated_at ;
    public String type ;
    public int year ;
    public String model ;
    public String vendor;
    public String color ;
    @SerializedName("new")
    public int neww ;
    public int ownerID;
    public int price;
    public Category() {
    }

    public Category(int id, String created_at, String updated_at, String type, int year, String model, String vendor, String color, int neww, int ownerID, int price) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.type = type;
        this.year = year;
        this.model = model;
        this.vendor = vendor;
        this.color = color;
        this.neww = neww;
        this.ownerID = ownerID;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNeww() {
        return neww;
    }

    public void setNeww(int neww) {
        this.neww = neww;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
