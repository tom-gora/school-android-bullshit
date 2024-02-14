

package com.example.my_splashscreen_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    // Duration of the splash screen in milliseconds
    //  set to 4 seconds to account for a startup delay
    // result: 3 - 2 - 1 is clearly displayed on full launch
    // this is done purely for clearer user experience
    // I am aware of this being a second longer than expected
    private static final int SPLASH_DURATION = 4000;

    private TextView countdownTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        int color = ContextCompat.getColor(this, R.color.teal);
        getWindow().setStatusBarColor(color);

        countdownTextView = findViewById(R.id.countdownTextView);

        // Start countdown
        startCountdown();

        // Delayed transition to the welcome screen after SPLASH_DURATION
        View decorView = getWindow().getDecorView();
        decorView.postDelayed(() -> {
            // Start the welcome screen activity
            Intent intent = new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class);
            startActivity(intent);
            finish(); // Finish the splash screen activity
        }, SPLASH_DURATION);
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(SPLASH_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                countdownTextView.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                // Not needed for do anything here because its mock
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unmounting the component as react ppl would say
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
