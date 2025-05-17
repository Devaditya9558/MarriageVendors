package com.example.marriagevendors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView nameTextView, emailTextView, phoneTextView, addressTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        nameTextView = findViewById(R.id.profile_name);
        emailTextView = findViewById(R.id.profile_email);
        logoutButton = findViewById(R.id.logout_button);

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // No user logged in, redirect to UserLogin
            startActivity(new Intent(this, UserLogin.class));
            finish();
            return;
        }

        // Display basic user info from Firebase Auth
        emailTextView.setText(currentUser.getEmail());
        if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
            nameTextView.setText(currentUser.getDisplayName());
        } else {
            nameTextView.setText("N/A");
        }

        // Fetch additional user details from Firestore
        fetchUserDetails(currentUser.getUid());

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            // Redirect to UserLogin
            startActivity(new Intent(this, UserLogin.class));
            finish();
        });
    }

    private void fetchUserDetails(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Update name if available in Firestore
                            String name = document.getString("name");
                            if (name != null && !name.isEmpty()) {
                                nameTextView.setText(name);
                            }

                            // Update phone number if available
                            String phone = document.getString("phone");
                            if (phone != null && !phone.isEmpty()) {
                                phoneTextView.setText(phone);
                            }

                            // Update address if available
                            String address = document.getString("address");
                            if (address != null && !address.isEmpty()) {
                                addressTextView.setText(address);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.w(TAG, "Error fetching user details: ", task.getException());
                    }
                });
    }
}