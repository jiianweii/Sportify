package com.example.sportify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

public class GetStarted extends AppCompatActivity {
    private ArrayList<Integer> Cards = new ArrayList<>();
    private ViewPager viewPager;
    private DatabaseHelper DB;
    Button i1, i2, i3, login;
    private GSViewPagerAdapter gsViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // Initialise the database with the current context
        DB = new DatabaseHelper(this);

        i1 = findViewById(R.id.icon1);
        i2 = findViewById(R.id.icon2);
        i3 = findViewById(R.id.icon3);
        login = findViewById(R.id.toLogin);

        Cards.add(R.drawable.adapter_image_1);
        Cards.add(R.drawable.adapter_image_2);
        Cards.add(R.drawable.adapter_image_3);

        viewPager = findViewById(R.id.viewPager2);
        gsViewPagerAdapter = new GSViewPagerAdapter(this, Cards);

        viewPager.setAdapter(gsViewPagerAdapter);
        // For first button
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        // Second button
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        // Third button
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });
        // For ViewPager Change
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                checkPosition(position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                checkPosition(position);
            }
        });
        // If the user clicks on login, it will start the LoginActivity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GetStarted.this, LoginActivity.class);
                startActivity(i);
            }
        });

        // Request for location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetStarted.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    /**
     * This method is used to check the position of the slider, it will changed the button colors below
     * whenever the position has changed to indicate where the position of the slider is at
     * @param pos int
     */
    public void checkPosition(int pos)
    {
        if (pos == 0)
        {
            i1.setBackgroundColor(Color.LTGRAY);
            i2.setBackgroundColor(Color.WHITE);
            i3.setBackgroundColor(Color.WHITE);
        }
        else if (pos == 1)
        {
            i1.setBackgroundColor(Color.WHITE);
            i2.setBackgroundColor(Color.LTGRAY);
            i3.setBackgroundColor(Color.WHITE);
        }
        else {
            i1.setBackgroundColor(Color.WHITE);
            i2.setBackgroundColor(Color.WHITE);
            i3.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     * This method is used to request permission from the user
     */
    public ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {


        }
    });
}