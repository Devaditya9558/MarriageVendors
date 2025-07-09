package com.example.marriagevendors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.marriagevendors.Model.LoginRequest;
import com.example.marriagevendors.Model.LoginResponse;
import com.example.marriagevendors.RetrofitClient.RetrofitClient;
import com.example.marriagevendors.Services.ApiServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogin extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Button loginButton, googleLoginButton;
    private TextView forgetPassword, signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        googleLoginButton = findViewById(R.id.google_login_button);
        forgetPassword = findViewById(R.id.forget_password);
        signUpLink = findViewById(R.id.sign_up);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(UserLogin.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            ApiServices apiService = RetrofitClient.getRetrofitInstance().create(ApiServices.class);
            Call<LoginResponse> call = apiService.loginUser(request);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d("XYZZ","Testing");
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("XYZZ","if");
                        Toast.makeText(UserLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // TODO: Navigate to next activity
                    } else {
                        Log.d("XYZZ ","else");
                        Toast.makeText(UserLogin.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("XYZZ","FAILURE");
                    Toast.makeText(UserLogin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        signUpLink.setOnClickListener(v -> {
            startActivity(new Intent(UserLogin.this, UserSignUpActivity.class));
        });

        googleLoginButton.setOnClickListener(v -> {
            Toast.makeText(UserLogin.this, "Google login not implemented yet", Toast.LENGTH_SHORT).show();
        });

        forgetPassword.setOnClickListener(v -> {
            Toast.makeText(UserLogin.this, "Forget password not implemented yet", Toast.LENGTH_SHORT).show();
        });
    }
}
