package com.example.sportify;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {

    EditText email, password, rePassword, age, name;
    Spinner genderSpinner;
    String genderText;
    DatabaseHelper DB;

    /**
     * The method is used during initialisation to inflate the layout for the fragment
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button register = view.findViewById(R.id.btnRegister);
        Button login = view.findViewById(R.id.btnLogin);

        email = view.findViewById(R.id.regiEmail);
        password = view.findViewById(R.id.regiPassword);
        rePassword = view.findViewById(R.id.regiRePassword);
        age = view.findViewById(R.id.regiAge);
        name = view.findViewById(R.id.regiName);

        genderSpinner = view.findViewById(R.id.regiGenderText);

        DB = new DatabaseHelper(getActivity());

        // Set-up the spinner for the gender
        setGenderSpinner();
        // If the user clicks on register, it will check for all the fields
        // If any field is empty, it will display a toast message
        // If the email is invalid, it will display a toast message
        // If the password and re-type password is not equal to each other, it will display a toast message
        // If the email has a match in the database, it will display a toast message
        // If the registration is completed, it will move to login page where the user can login
        register.setOnClickListener(v -> {
            String mail = email.getText().toString();
            String pass = password.getText().toString();
            String repass = rePassword.getText().toString();
            String ageText = age.getText().toString();
            String names = name.getText().toString();

            if (mail.equals("")||pass.equals("")||repass.equals("")||names.equals("")||ageText.equals("")||genderText.equals(""))
                Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else {
                if (checkEmailField(mail)) {
                    if (pass.equals(repass)) {
                        Boolean checkUser = DB.checkUsername(mail);
                        if (!checkUser) {
                            Boolean insert = DB.insertUserData(names, mail, pass, ageText, genderText);
                            if (insert) {
                                Toast.makeText(getActivity(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Incorrect email format", Toast.LENGTH_LONG).show();
                }
            }
        });

        login.setOnClickListener(v-> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }

    /**
     * This method is to check if the email is valid
     * @param email String
     * @return boolean
     */
    private boolean checkEmailField(String email)
    {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    /**
     * This method is used to set up the spinner inside an Adapter to be displayed when the fragment
     * is created
     */
    private void setGenderSpinner() {
        List<String> items = new ArrayList<>();
        items.add("Select Gender");
        items.add("Male");
        items.add("Female");
        items.add("Other");

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