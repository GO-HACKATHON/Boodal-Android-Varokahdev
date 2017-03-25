package com.example.ariel.boodal.Object;

import java.util.Date;

/**
 * Created by Ariel on 3/25/2017.
 */

public class UserObject {
    private int id,status,phone;
    private String name,email,api_token;
    private Date created_at,update_at;
    private double lat,lng;

    public UserObject(int id, int status, int phone, String name, String email, String api_token, Date created_at, Date update_at, double lat, double lng) {
        this.id = id;
        this.status = status;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.api_token = api_token;
        this.created_at = created_at;
        this.update_at = update_at;
        this.lat = lat;
        this.lng = lng;
    }

    public UserObject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
