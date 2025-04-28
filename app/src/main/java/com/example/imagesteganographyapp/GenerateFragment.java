package com.example.imagesteganographyapp;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GenerateFragment extends Fragment {

    public GenerateFragment() {

    }


    EditText edtPrompt;
    Button btnGenerate;
    ImageView imgResult1;
    ProgressBar bgBar;
    Retrofit retrofit;

   private  static  final String apiKey =  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Ijk4ZjIzMGJmMWM3ZGRhYjg1MDgyNWQwNzFkYTkzYWNiIiwiY3JlYXRlZF9hdCI6IjIwMjUtMDEtMThUMTI6NTY6MjkuMDE0NzAzIn0.lvpwStMHGseC036nzMg3vdGoFGYOrRJbOjCVUb3dXTw";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view =  inflater.inflate(R.layout.fragment_generate, container, false);

         edtPrompt = view.findViewById(R.id.edtPrompt);
         btnGenerate = view.findViewById(R.id.btnGenerate);
         imgResult1 = view.findViewById(R.id.imgResult1);


         bgBar = view.findViewById(R.id.loadingInd);



         retrofit= new Retrofit.Builder()
                .baseUrl("https://api.monsterapi.ai/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        edtPrompt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyboard(getContext(),edtPrompt);
                    btnGenerate.performClick();
                    return true;
                }
                return false;
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String prompt = edtPrompt.getText().toString();
                 if (!prompt.isEmpty()) {

                     generateImageFromPrompt(prompt);
                     edtPrompt.setText("");
                 } else {
                     Toast.makeText(getContext(), "Please enter a prompt", Toast.LENGTH_SHORT).show();
                 }
             }
         });



         imgResult1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getContext(),MainActivity.class));
             }
         });

        return view;
    }




    public void generateImageFromPrompt(String prompt) {
        RequestBody promptBody = RequestBody.create(MediaType.parse("text/plain"), prompt);
        RequestBody aspectRatioBody = RequestBody.create(MediaType.parse("text/plain"), "portrait");
        RequestBody guidanceScaleBody = RequestBody.create(MediaType.parse("text/plain"), "20.5");

        MonsterApiService apiService = retrofit.create(MonsterApiService.class);


        bgBar.setVisibility(View.VISIBLE);


        Call<ProcessIdResponse> call = apiService.generateImage(
                "Bearer " + apiKey,
                promptBody,
                aspectRatioBody,
                guidanceScaleBody
        );

        call.enqueue(new Callback<ProcessIdResponse>() {
            @Override
            public void onResponse(Call<ProcessIdResponse> call, Response<ProcessIdResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String processId = response.body().getProcessId();

                    pollForResult(processId);
                } else {
                    Toast.makeText(getContext(), "Message: Request not successful", Toast.LENGTH_SHORT).show();
                    bgBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProcessIdResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                bgBar.setVisibility(View.GONE);
            }
        });
    }



    private void pollForResult(String processId) {
        MonsterApiService apiService = retrofit.create(MonsterApiService.class);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Call<ImageResultResponse> call = apiService.fetchResult("Bearer " + apiKey, processId);
            call.enqueue(new Callback<ImageResultResponse>() {
                @Override
                public void onResponse(Call<ImageResultResponse> call, Response<ImageResultResponse> response) {


                    if (response.isSuccessful() && response.body() != null) {
                        ImageResultResponse resultResponse = response.body();
                        if ("COMPLETED".equalsIgnoreCase(resultResponse.getStatus())) {
                            List<String> imageUrls = resultResponse.getResult().getOutput();
                            if (imageUrls != null && !imageUrls.isEmpty()) {
                                String imageUrl = imageUrls.get(0);
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("onlineImage", imageUrl);
                                editor.apply();
                                Glide.with(getContext()).load(imageUrl).into(imgResult1);
                            }
                            bgBar.setVisibility(View.GONE);
                        } else if ("IN_PROGRESS".equalsIgnoreCase(resultResponse.getStatus()) ||
                                "IN_QUEUE".equalsIgnoreCase(resultResponse.getStatus())) {


                            pollForResult(processId);
                        } else {
                            Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                            bgBar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(getContext(), "Request not successful", Toast.LENGTH_SHORT).show();
                        bgBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ImageResultResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    bgBar.setVisibility(View.GONE);
                }
            });
        }, 5000);
    }

}