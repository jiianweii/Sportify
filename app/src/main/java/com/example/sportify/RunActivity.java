package com.example.sportify;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.sportify.databinding.ActivityRunBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class RunActivity extends FragmentActivity implements OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView backToMain;
    Button startRunning;
    private GoogleMap mMap;
    private ActivityRunBinding binding;
    Fragment fragment;

    /**
     * This method is used during the initialisation of the activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRunBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // If the user clicks on back button, it will display a dialog to confirm if they want to leave
        backToMain = findViewById(R.id.backButton);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitMessage();
            }
        });

        startRunning = findViewById(R.id.startRunning);
        // If the user clicks on start running, it will change the fragment to Fragment_Run
        startRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new Fragment_Run();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                startRunning.setVisibility(View.INVISIBLE); // Hide the button

            }
        });
        // Request for location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RunActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Request for location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RunActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LocationManager LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    LocationListener LL = new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.clear();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                            googleMap.addMarker(new MarkerOptions().position(latLng));
                        }
                    };

                    if (ActivityCompat.checkSelfPermission(RunActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RunActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RunActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                    LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1.0f, LL);
                }
            };


        });

    }

    /**
     * This method is used to show the dialog message to confirm if the user wants to leave the activity
     */
    public void showExitMessage()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Are you sure you want to exit?").setMessage("The current activity will not be saved if you exit")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Exit Anyway", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(RunActivity.this, MainActivity.class);
                intent.putExtra("Email", getIntent().getStringExtra("Email"));
                intent.putExtra("Name", getIntent().getStringExtra("Name"));
                startActivity(intent);
            }
        }).show();
        alertDialog.create();
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