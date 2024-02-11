package com.example.my_splashscreen_app;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class WelcomeScreenActivity extends AppCompatActivity {

    private static final long UPDATE_CHECK_DURATION = 5000; // 5 seconds

    private Dialog updateDialog; // Declare the dialog as a class variable

    // Method to handle Check for Updates button click
    public void onCheckForUpdatesButtonClick(View view) {
        // Initialize and display the custom dialog for update check
        showUpdateDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        int color = ContextCompat.getColor(this, R.color.teal);
        getWindow().setStatusBarColor(color);
    }




    // Method to handle Exit button click
    public void onExitButtonClick(View view) {
        // Finish the activity to exit the app
        finish();
    }

    private void showUpdateDialog() {
        // Initialize the custom dialog
        updateDialog = new Dialog(this);
        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateDialog.setContentView(R.layout.update_dialog);

        // Find views in the dialog layout
        ProgressBar spinner = updateDialog.findViewById(R.id.spinner);
        TextView updateDialogTitle = updateDialog.findViewById(R.id.dialogTitle);
        TextView updateDialogResult = updateDialog.findViewById(R.id.resultsMsg);
        Button dismissUpdatesDialogBtn = updateDialog.findViewById(R.id.okButton);

        // Show spinner initially
        spinner.setVisibility(ProgressBar.VISIBLE);
        updateDialogResult.setVisibility(TextView.GONE);
        dismissUpdatesDialogBtn.setVisibility(View.GONE); // Hide the dismiss button initially

        // Simulate checking for updates
        new CountDownTimer(UPDATE_CHECK_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Not needed for update check
            }

            @Override
            public void onFinish() {
                // Fake result: no updates available
                spinner.setVisibility(ProgressBar.GONE);
                updateDialogResult.setVisibility(TextView.VISIBLE);
                updateDialogTitle.setText("Updates status checked");
                updateDialogResult.setText("No updates available.");
                dismissUpdatesDialogBtn.setVisibility(View.VISIBLE); // Show the dismiss button
            }

        }.start();

        // Set click listener to dismiss the dialog when the button is clicked
        dismissUpdatesDialogBtn.setOnClickListener(v -> {
            updateDialog.dismiss();
        });

        // Show the dialog
        updateDialog.show();
    }
}
