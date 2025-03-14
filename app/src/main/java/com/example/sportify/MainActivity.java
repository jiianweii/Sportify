package com.example.sportify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    // Fragment to replace frame layout
    Fragment selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising the database
        databaseHelper = new DatabaseHelper(this);

        // Get intent message from login
        Intent intent = getIntent();
        selected = new Fragment_Dashboard(); // Set as default page
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selected).commit();
        // Set up the bottom navigation
        BottomNavigationView BNV = findViewById(R.id.bottomNavigationView);
        BNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /**
             * This method is used to check if any item of the navigation is selected
             * it will either change fragment or move to RunActivity
             * @param item MenuItem
             * @return boolean
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                // For adding, replacing and remove fragment
                FragmentTransaction ft = fm.beginTransaction();

                // If the user clicks on dashboard item
                if (item.getItemId() == R.id.dashboard)
                {
                    selected = new Fragment_Dashboard();
                }
                // If the user clicks on run item
                else if (item.getItemId() == R.id.map) {
                    // selected = new Fragment_Run();
                    Intent i = new Intent(MainActivity.this, RunActivity.class);
                    i.putExtra("Email",intent.getStringExtra("Email"));
                    i.putExtra("Name", intent.getStringExtra("Name"));
                    startActivity(i);
                }
                // If the user clicks on user profile item
                else if (item.getItemId() == R.id.user)
                {
                    selected = new UserProfileFragment();
                }

                // Replace the fragment in the framelayout
                ft.replace(R.id.frameLayout, selected).commit();

                return true;
            }
        });
    }



}