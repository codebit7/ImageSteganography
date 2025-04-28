package com.example.imagesteganographyapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {
    @GET("api/")
    Call<RequestResponse> searchImages(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("image_type") String imageType
    );
}