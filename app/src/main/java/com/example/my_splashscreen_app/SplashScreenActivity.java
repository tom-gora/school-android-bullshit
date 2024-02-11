

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
    private static final int SPLASH_DURATION = 6000; // 6 seconds

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
                // Do something when the countdown finishes, if needed
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer to avoid memory leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
