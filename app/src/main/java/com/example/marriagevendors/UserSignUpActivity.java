package com.example.marriagevendors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserSignUpActivity extends AppCompatActivity {

    private EditText usernameInput, emailInput, passwordInput, confirmPasswordInput, eventLocationInput, phoneNumberInput, eventDateInput;
    private CheckBox termsCheckbox;
    private Button signUpButton, googleLoginButton;
    private TextView loginLink;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private static final String TAG = "UserSignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: UserSignUpActivity started");
        setContentView(R.layout.activity_user_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize ActivityResultLauncher for Google Sign-In
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            Log.w(TAG, "Google sign in failed", e);
                            Toast.makeText(UserSignUpActivity.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Google sign in cancelled");
                        Toast.makeText(UserSignUpActivity.this, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

        // Initialize UI elements
        usernameInput = findViewById(R.id.username_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        eventLocationInput = findViewById(R.id.event_location_input);
        phoneNumberInput = findViewById(R.id.phone_number_input);
        eventDateInput = findViewById(R.id.event_date_input);
        termsCheckbox = findViewById(R.id.terms_checkbox);
        signUpButton = findViewById(R.id.sign_up_button);
        googleLoginButton = findViewById(R.id.google_login_button);
        loginLink = findViewById(R.id.login_link);

        // Sign Up button click listener
        signUpButton.setOnClickListener(v -> {
            Log.d(TAG, "Sign Up button clicked");
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();
            String eventLocation = eventLocationInput.getText().toString().trim();
            String phoneNumber = phoneNumberInput.getText().toString().trim();
            String eventDate = eventDateInput.getText().toString().trim();

            // Enhanced validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                    eventLocation.isEmpty() || phoneNumber.isEmpty() || eventDate.isEmpty()) {
                Toast.makeText(UserSignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Validation failed: Empty fields");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(UserSignUpActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Validation failed: Invalid email");
            } else if (password.length() < 6) {
                Toast.makeText(UserSignUpActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Validation failed: Password too short");
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(UserSignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Validation failed: Passwords do not match");
            } else if (!phoneNumber.matches("\\d{10}")) {
                Toast.makeText(UserSignUpActivity.this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Validation failed: Invalid phone number");
            } else if (!eventDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                Toast.makeText(UserSignUpActivity.this, "Please enter a valid date in dd-mm-yyyy format", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Validation failed: Invalid date format");
            } else if (!termsCheckbox.isChecked()) {
                Toast.makeText(UserSignUpActivity.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Validation failed: Terms not accepted");
            } else {
                // Create user with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign up success
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(UserSignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                // Navigate to LandingActivity
                                Intent intent = new Intent(UserSignUpActivity.this, LandingActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Sign up failed
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(UserSignUpActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Google login button click listener
        googleLoginButton.setOnClickListener(v -> {
            Log.d(TAG, "Google login button clicked");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // Login link click listener
        loginLink.setOnClickListener(v -> {
            Log.d(TAG, "Login link clicked, navigating to UserLogin");
            Intent intent = new Intent(UserSignUpActivity.this, UserLogin.class);
            startActivity(intent);
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(UserSignUpActivity.this, "Google Sign-In successful", Toast.LENGTH_SHORT).show();
                        // Navigate to LandingActivity
                        Intent intent = new Intent(UserSignUpActivity.this, LandingActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Sign in failed
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(UserSignUpActivity.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}