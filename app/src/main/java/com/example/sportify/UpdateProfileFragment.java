package com.example.sportify;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfileFragment extends Fragment {

    EditText password, age, name;
    TextView email;
    Spinner genderSpinner;
    String emailText, passwordText, ageText, genderText, nameText;
    DatabaseHelper DB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        Button update = view.findViewById(R.id.btnUpdate);
        Button back = view.findViewById(R.id.btnBack);

        email = view.findViewById(R.id.editEmail);
        password = view.findViewById(R.id.editPassword);
        age = view.findViewById(R.id.editAge);
        name = view.findViewById(R.id.editName);

        DB = new DatabaseHelper(getActivity());
        Bundle bundle = this.getArguments();

        getDataFromDB(bundle);
        setGenderSpinner(view, bundle);

        update.setOnClickListener(v -> {
            DB.updateUserData(name.getText().toString(),email.getText().toString(), password.getText().toString(), age.getText().toString(), genderText);
            Toast.makeText(getActivity(), "User Profile Updated", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new UserProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new UserProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    /**
     * This method is used to get data from the database through the bundle object
     * @param bundle Bundle
     */
    private void getDataFromDB(Bundle bundle) {
        Account user = DB.getUser(bundle.getString("Email"));
        passwordText = user.getPassword();
        genderText = user.getGender();
        emailText = user.getEmail();
        ageText = String.valueOf(user.getAge());
        nameText = user.getName();


        email.setText(emailText);
        password.setText(passwordText);
        age.setText(String.valueOf(ageText));
        name.setText(nameText);

    }

    /**
     * This method is used to add the genders in the spinner
     * @param view View
     * @param bundle Bundle
     */
    private void setGenderSpinner(View view, Bundle bundle) {
        genderSpinner = view.findViewById(R.id.editGender);
        List<String> items = new ArrayList<>();

        String male = "Male";
        String female = "Female";
        String other = "Other";

        if (bundle != null) {
            genderText = bundle.getString("Gender");
            items.add(genderText);

            if (genderText.equals(male)) {
                items.add(female);
                items.add(other);
            } else if (genderText.equals(female)) {
                items.add(male);
                items.add(other);
            } else if (genderText.equals(other)) {
                items.add(male);
                items.add(female);
            } else {
                items.add(male);
                items.add(female);
                items.add(other);
            }
        }

        genderSpinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, items));

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                genderText = items.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}