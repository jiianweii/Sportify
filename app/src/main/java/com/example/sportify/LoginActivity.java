package com.example.sportify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextView pageTitle;
    Button btnLogin, btnRegister;
    TextInputLayout emailText, passText;
    DatabaseHelper DB;
    RegisterFragment register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Request for permission to allow notification
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 46);
                requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegi);

        pageTitle = findViewById(R.id.loginPage);
        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);

        emailText = findViewById(R.id.emailLayout);
        passText = findViewById(R.id.passwordLayout);
        // Initialise the database with the current context
        DB = new DatabaseHelper(this);

        register = new RegisterFragment();
        // When the user login, it will get the username(email) and password field text
        // IF the fields are empty, it will show a toast message to fill up the fields
        // IF the username and password are correct, it will bring the user to the dashboard
        // IF either the username and password is wrong, it will display a toast message indicating that
        btnLogin.setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.equals("")||pass.equals(""))
                Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else {
                Boolean checkUserPass = DB.checkUsernamePassword(user, pass);
                if (checkUserPass) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("Email", user);
                        i.putExtra("Password", pass);
                        startActivity(i);

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
            hideKeyboard(view);
        });
        // This button is used to change the fragment into register fragment
        btnRegister.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.userContainer, register, "Register Page")
                    .commit();

            pageTitle.setVisibility(View.GONE);
            username.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            emailText.setVisibility(View.GONE);
            passText.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
            hideKeyboard(view);
        });

    }

    /**
     * This method is used to hide the keyboard when the fragment changed to registration
     * @param view View
     */
    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {

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