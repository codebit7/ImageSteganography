package com.example.imagesteganographyapp;


import static com.google.android.material.internal.ContextUtils.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private  Context context;
    private  List<ImageModel> images;


    public ImageAdapter(Context context, List<ImageModel> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_row, parent, false);
        return new ImageViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(images.get(position).getWebformatURL())
                .into(holder.image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences =context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("onlineImage", images.get(position).getWebformatURL());  // Store data with key "onlineImage"
                editor.apply();
                context.startActivity(new Intent(context,MainActivity.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView);

        }
    }
}

