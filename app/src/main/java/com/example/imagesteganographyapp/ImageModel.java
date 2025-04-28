package com.example.imagesteganographyapp;


public class ImageModel {

    private String id;
    private String webformatURL;
    private String type;

    public ImageModel(String id, String webformatURL, String type) {
        this.id = id;
        this.webformatURL = webformatURL;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public String getType() {
        return type;
    }
}

