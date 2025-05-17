package com.example.marriagevendors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 4000; // 4 seconds (animation + display)
    private static final int LETTER_DELAY = 100; // Delay between letters
    private TextView splashText;
    private String fullText = "Marriage Vendors";
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashText = findViewById(R.id.splash_text);
        animateText();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }

    private void animateText() {
        if (currentIndex < fullText.length()) {
            splashText.setText(fullText.substring(0, currentIndex + 1));
            currentIndex++;
            new Handler(Looper.getMainLooper()).postDelayed(this::animateText, LETTER_DELAY);
        }
    }
}