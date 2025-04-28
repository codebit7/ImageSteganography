package com.example.imagesteganographyapp;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {



    private static final String API_KEY = "47996016-380aea1fd742957773c399883";
    RecyclerView imageRecycler;
    private ImageAdapter imageAdapter;
    private SearchView searchImage;
    private ProgressBar loadingIndicator;
    private List<ImageModel> images = new ArrayList<>();


    public SearchFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        imageRecycler = view.findViewById(R.id.imagesRecycler);
        searchImage = view.findViewById(R.id.searchImage);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);




        EditText searchEditText = searchImage.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        ImageView searchIcon = searchImage.findViewById(androidx.appcompat.R.id.search_mag_icon);
        if (searchIcon != null) {
            searchIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_IN);
        }


        ImageView closeIcon = searchImage.findViewById(androidx.appcompat.R.id.search_close_btn);
        if (closeIcon != null) {
            closeIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.white), PorterDuff.Mode.SRC_IN);
        }

        Window window = getActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));



        if(new InternetDialog(getContext()).getInternetStatus()){
            Toast.makeText(getContext(), "INTERNET VALIDATION PASSED", Toast.LENGTH_SHORT).show();
        }





        searchImage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    fetchImages(query);
                    searchImage.setQuery("",false);
                    imageAdapter.notifyDataSetChanged();
                    searchImage.clearFocus();
                    Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });




        fetchImages("trendings");

        imageRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        imageAdapter = new ImageAdapter(getContext(), images);
        imageRecycler.setAdapter(imageAdapter);



        return  view;
    }





    public void fetchImages(String query){

        loadingIndicator.setVisibility(View.VISIBLE);


        new Thread(()->{

            PixabayApi pixabayApi = RetrofitClient.getClient().create(PixabayApi.class);

            Call<RequestResponse> call = pixabayApi.searchImages(API_KEY, query, "photo");
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    loadingIndicator.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        images.clear();

                        images.addAll(response.body().getHits());
                        imageAdapter.notifyDataSetChanged();
                        Log.d("Show_Images", "Images size: " + images.size());

                        Toast.makeText(getContext(), "Images fetched successfully!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Images not found ", Toast.LENGTH_SHORT).show();
                        Log.e("MainActivity", "Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    loadingIndicator.setVisibility(View.GONE);
                    Log.e("MainActivity", "Failure: " + t.getMessage());
                    Toast.makeText(getContext(), "Failed to fetch images. Try again.", Toast.LENGTH_SHORT).show();
                }
            });

        }).start();
}

}