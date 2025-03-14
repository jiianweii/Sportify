package com.example.sportify;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UserProfileFragment extends Fragment {

    TextView email, age, gender;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarToggle;
    NavigationView navigationView;
    UserProfileFragment userProfile;
    UpdateProfileFragment updateProfile;
    DatabaseHelper DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        Button update = view.findViewById(R.id.btnUpdate);

        email = view.findViewById(R.id.emailText);
        age = view.findViewById(R.id.ageText);
        gender = view.findViewById(R.id.genderText);

        DB = new DatabaseHelper(getActivity());

        Intent i = getActivity().getIntent();
        String username = i.getStringExtra("Email");

        Account user = DB.getUser(username);
        Bundle bundle = new Bundle();
        bundle.putString("Email", user.getEmail());
        bundle.putString("Password", user.getPassword());
        bundle.putString("Gender", user.getGender());
        bundle.putString("Name", user.getName());
        bundle.putString("Age", String.valueOf(user.getAge()));

        getUserData(user);
        setupToolBar(view);
        setSideNav(view);

        updateProfile = new UpdateProfileFragment();
        updateProfile.setArguments(bundle);

        update.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, updateProfile)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    /**
     * The method is used to get the user information set it in the fragment view
     * @param user Account
     */
    private void getUserData(Account user) {
        email.setText(user.getEmail());
        age.setText(String.format("%d", user.getAge()));
        gender.setText(user.getGender());
    }

    /**
     * This method is used to setup the toolbar in the fragment
     * @param view View
     */
    private void setupToolBar(View view) {
        userProfile = new UserProfileFragment();
        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, userProfile)
                        .commit());
    }

    /**
     * This method is used to set up the side navigation in the fragment
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
     * This method is used for the item selected in the side navigation
     * and change fragment or activity depending on which item they selected
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