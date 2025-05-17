package com.example.marriagevendors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.marriagevendors.viewmodel.UserLoginViewModel;

public class UserLogin extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton, googleLoginButton;
    private TextView forgetPassword, signUpLink;
    private UserLoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Initialize UI elements
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        googleLoginButton = findViewById(R.id.google_login_button);
        forgetPassword = findViewById(R.id.forget_password);
        signUpLink = findViewById(R.id.sign_up);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(UserLoginViewModel.class);

        // Observe login result
        viewModel.getLoginResult().observe(this, loginResponse -> {
            if (loginResponse.isSuccess()) {
                String role = loginResponse.getRole();
                redirectBasedOnRole(role);
            } else {
                Toast.makeText(UserLogin.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(UserLogin.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, password);
        });

        // Sign up link click listener
        signUpLink.setOnClickListener(v -> {
            startActivity(new Intent(UserLogin.this, UserSignUpActivity.class));
        });

        // Google login (placeholder for now)
        googleLoginButton.setOnClickListener(v -> {
            Toast.makeText(UserLogin.this, "Google login not implemented yet", Toast.LENGTH_SHORT).show();
        });

        // Forget password (placeholder for now)
        forgetPassword.setOnClickListener(v -> {
            Toast.makeText(UserLogin.this, "Forget password not implemented yet", Toast.LENGTH_SHORT).show();
        });
    }

    private void redirectBasedOnRole(String role) {
        Intent intent;
        switch (role) {
            case "user":
                intent = new Intent(UserLogin.this, LandingActivity.class);
                break;
            case "vendor":
                intent = new Intent(UserLogin.this, VendorActivity.class);
                break;
            case "admin":
                intent = new Intent(UserLogin.this, AdminActivity.class);
                break;
            default:
                Toast.makeText(UserLogin.this, "Invalid role", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }
}