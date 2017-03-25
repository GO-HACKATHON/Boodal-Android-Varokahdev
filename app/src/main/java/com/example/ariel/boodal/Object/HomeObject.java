package com.example.ariel.boodal.Object;

/**
 * Created by Ariel on 3/25/2017.
 */

public class HomeObject {
    private int img;
    private String service;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public HomeObject(String service,int img) {

        this.img = img;
        this.service = service;
    }

    public HomeObject() {
    }
}
