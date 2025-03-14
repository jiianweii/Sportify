package com.example.sportify;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Fragment_Dashboard extends Fragment {

    private TextView user;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarToggle;
    NavigationView navigationView;
    UserProfileFragment userProfile;
    ArrayList<Activity> activityDetail;
    RecyclerView recyclerView;
    TextView item;
    DatabaseHelper DB;

    /**
     * This method is to create view when it is initialise in the activity/fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        DB = new DatabaseHelper(getContext());
        activityDetail = new ArrayList<>();
        user = view.findViewById(R.id.userProfile);
        recyclerView = view.findViewById(R.id.recyclerView);
        item = view.findViewById(R.id.itemText);

        activityDetail = DB.getActivityData(getActivity().getIntent().getStringExtra("Email"));

        getUserDetail();
        setupToolBar(view);
        setSideNav(view);
        setAdapter();

        return view;
    }

    /**
     * This method is used to set the adapter of the RecyclerView for the dashboard
     * to display activities created by the user after completing each run
     */
    private void setAdapter() {
        ActivityAdapter adapter = new ActivityAdapter(activityDetail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        String text = "No Activity Found";
        if (adapter.getItemCount() == 0) {
            item.setText(text);
        } else {
            item.setVisibility(View.GONE);
        }
    }

    /**
     * This method is used to get the name of the user to display in the dashboard
     * The intent message will be sent from the login to across other activities for
     * reference purposes
     */
    private void getUserDetail() {
        Intent i = getActivity().getIntent();
        Account u = DB.getUser(i.getStringExtra("Email"));
        user.setText(u.getName());
    }

    /**
     * This method is used to set up the toolbar in the dashboard
     * @param view View
     */
    private void setupToolBar(View view) {
        userProfile = new UserProfileFragment();
        toolbar = view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.userContainer, userProfile)
                        .commit());
    }

    /**
     * This method is used to set up the side navigation for the dashboard and user profile
     * @param view View
     */
    private void setSideNav(View view) {
        drawerLayout = view.findViewById(R.id.drawerLayout);
        drawerLayout.addDrawerListener(actionBarToggle);

        navigationView = view.findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        actionBarToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        actionBarToggle.syncState();
    }

    /**
     * This method is used when the user clicks on any items on the side navigation bar
     * to go to other part of the application
     * @param item MenuItem
     * @return boolean
     */
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        // For adding, replacing and remove fragment
        FragmentTransaction ft = fm.beginTransaction();
        Intent intent = getActivity().getIntent();
        if (id == R.id.nav_home) {
            ft.replace(R.id.frameLayout, new Fragment_Dashboard()).commit();
        } else if (id == R.id.nav_profile) {
            ft.replace(R.id.frameLayout, new UserProfileFragment()).commit();
        } else if (id == R.id.nav_activity) {
            Intent i = new Intent(getActivity(), RunActivity.class);
            i.putExtra("Email", intent.getStringExtra("Email"));
            i.putExtra("Name", intent.getStringExtra("Name"));
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}