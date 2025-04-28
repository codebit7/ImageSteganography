package com.example.imagesteganographyapp;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface MonsterApiService {

    @Multipart
    @POST("v1/generate/txt2img")
    Call<ProcessIdResponse> generateImage(
            @Header("Authorization") String bearerToken,
            @Part("prompt") RequestBody prompt,
            @Part("aspect_ratio") RequestBody aspectRatio,
            @Part("guidance_scale") RequestBody guidanceScale
    );

    @GET("v1/status/{process_id}")
    Call<ImageResultResponse> fetchResult(
            @Header("Authorization") String bearerToken,
            @Path("process_id") String processId
    );
}
