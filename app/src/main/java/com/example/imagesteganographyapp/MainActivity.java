package com.example.imagesteganographyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);


        SharedPreferences p =getSharedPreferences("MyPrefs",MODE_PRIVATE);
        String uri =  p.getString("onlineImage",null);

        if(uri != null)
        {
           viewPager.setCurrentItem(0);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {


            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.nav_search) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.nav_generate) {
                viewPager.setCurrentItem(2);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(3);
                return true;
            }
            return false;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_search);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_generate);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(auth != null && auth.getCurrentUser() !=null)
        {

        }
        else{
            auth.signOut();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}