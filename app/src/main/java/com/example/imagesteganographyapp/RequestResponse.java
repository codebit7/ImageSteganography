package com.example.imagesteganographyapp;

import java.util.List;

public class RequestResponse {

    private int total;
    private int totalHits;
    private List<ImageModel> hits;

    public int getTotalHits() {
        return totalHits;
    }

    public int getTotal() {
        return total;
    }

    public List<ImageModel> getHits() {
        return hits;
    }
}
