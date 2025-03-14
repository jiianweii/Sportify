package com.example.sportify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class Fragment_Run extends Fragment {
    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView time;
    TextView Distance;
    TextView avg;
    boolean MOVEMENT_FLAG = true;
    Activity activity = new Activity();
    private int timer = 0;

    private ArrayList<LatLng> currentLatLng = new ArrayList<>();
    private PolylineOptions currentPolyline;

    private DatabaseHelper databaseHelper;

    /**
     * This method is created when the fragment first initialised and run
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        // Initialise the database with the current context
        databaseHelper = new DatabaseHelper(getContext());

        // This is to get the user's current location for displaying when the map is created and ready
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    getCurrentLocation();
                }
            }
        });
        // This thread is used to calculate the timer based on the number of seconds passed
        // The UI thread will be updating the timer as well as the average pace made by the user
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // If the user pause/stop the activity, the thread should not continue executing
                    if (MOVEMENT_FLAG) {
                        try {
                            Thread.sleep(1000); // Sleep every 1 second for the timer to count
                            timer += 1;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int hours = timer / 3600;
                                int minutes = (timer / 60) % 60;
                                int seconds = timer % 60;

                                String timeFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

                                time = (TextView) getActivity().findViewById(R.id.time);
                                time.setText(timeFormat);
                                activity.setTime(timeFormat);
                                // Update the notification bar with the current timing
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    updateNotificationStatus(timeFormat);
                                }
                                // Display the average pace if the user had moved
                                if (activity.getKilometer() > 0.00) {
                                    avg = (TextView) getActivity().findViewById(R.id.avgDist);
                                    double pace = timer / activity.getKilometer();
                                    int paceMinute = (int) (Math.round(pace) / 60);
                                    int paceSeconds = (int) (Math.round(pace) % 60);

                                    avg.setText(String.format(Locale.getDefault(), "%02d:%02d", paceMinute, paceSeconds));
                                }
                            }
                        });
                    }
                }

            }
        });

        thread.start();

        return view;
    }

    /**
     * This method is used to request permission from the user
     */
    public ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                getCurrentLocation();
            }
        }
    });

    /**
     * This method is used after permissions has been granted by the device
     * It allows the user to pause, unpause, and stop the activity
     */
    public void getCurrentLocation() {
        Button toggleBtn = (Button) getActivity().findViewById(R.id.pauseButton);
        Button endBtn = (Button) getActivity().findViewById(R.id.stopButton);
        // This button is used for pausing and unpausing
        // If the user clicks on pause, the timer will stop running and text of the button will be changed
        // Until the user clicks on start
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MOVEMENT_FLAG = !MOVEMENT_FLAG;
                if (MOVEMENT_FLAG) {
                    toggleBtn.setText("Pause");
                } else {
                    storesData();
                    toggleBtn.setText("Start");
                }
            }
        });
        // This button is used to end the activity and store the activity into the database
        // before returning to the dashboard page
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MOVEMENT_FLAG = false;
                String formattedDate = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                activity.setDate(formattedDate);
                storesData();



                long result = databaseHelper.insertActivity(activity.getDate(), activity.getTime(), Double.toString(activity.getKilometer()), getActivity().getIntent().getStringExtra("Email"));

                if (result > 0)
                {
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra("Email", getActivity().getIntent().getStringExtra("Email"));
                    i.putExtra("Name", getActivity().getIntent().getStringExtra("Name"));
                    startActivity(i);
                }
            }
        });
        // This is to request permission from the device for location
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        // This will run last, in order to capture the user's movement and location
        // which will be used for displaying the number of distance covered based on the sensors
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            LocationManager LM = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                            LocationListener LL = new LocationListener() {
                                @Override
                                public void onLocationChanged(@NonNull Location location) {
                                    // MOVEMENT_FLAG is the boolean variable used to track if the user clicks on pause/stop button
                                    if (MOVEMENT_FLAG) {
                                        currentPolyline = new PolylineOptions().geodesic(true).color(Color.RED).width(20);
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        currentLatLng.add(latLng);
                                        int size = currentLatLng.size();

                                        for (int i = 0; i < size; i++)
                                            currentPolyline.add(currentLatLng.get(i)); // Stores all latlng as polyline
                                        // If there are more than one latitude or longnitude, which can be used to calculate
                                        // the distance between both coordinates which will be used as the distance
                                        if (size > 1) {
                                            Location l1 = new Location("lastLocation");
                                            l1.setLatitude(currentLatLng.get(size - 2).latitude);
                                            l1.setLongitude(currentLatLng.get(size - 2).longitude);

                                            Location l2 = new Location("currentLocation");
                                            l2.setLatitude(currentLatLng.get(size - 1).latitude);
                                            l2.setLongitude(currentLatLng.get(size - 1).longitude);

                                            double distance = (l1.distanceTo(l2) / 1000) + activity.getKilometer();
                                            activity.setKilometer((double) Math.round(distance * 100) / 100);

                                        }
                                        Distance = (TextView) getActivity().findViewById(R.id.distance);
                                        Distance.setText(String.format("%.2f", activity.getKilometer()));
                                        // To set the camera zoomed into the user's current location
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                        // To add polylines into the google map for displaying where the user had ran
                                        // It will not record during the period of pausing and will continue after the user resume
                                        if (activity.getPoints().size() > 0) {
                                            for (int i = 0; i < activity.getPoints().size(); i++) {
                                                googleMap.addPolyline(activity.getPoints().get(i));
                                            }
                                        }

                                        googleMap.addPolyline(currentPolyline);
                                    }
                                }
                            };
                            // To get the user's location in a short period of time
                            LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1.0f, LL);
                        }
                    });
                }
            }
        });

    }

    /**
     * This method is used to collect all the past polylines when the user clicks on pause/stop
     * which will be stored into the activity class
     */
    private void storesData() {
        activity.addPoints(currentPolyline); // Add past polylines
        currentLatLng.clear(); // Clear current list of latlng captured
    }

    /**
     * This is to display the notification of the current activity
     * the notification will show the current timer on the activity in another thread
     * which will run as background task if the user leaves the application
     * @param currentTiming String
     */
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void updateNotificationStatus(String currentTiming) {
        NotificationChannel channel = new NotificationChannel("1", "StatusUpdate", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(null, null);
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification.Builder builder = new Notification.Builder(getContext(), "1");
        builder.setSmallIcon(R.drawable.icon_activity)
                .setContentTitle("Current Activity")
                .setContentText(currentTiming)
                .setSound(null, null);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 46);
            requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
        managerCompat.notify(1, builder.build());
    }

}