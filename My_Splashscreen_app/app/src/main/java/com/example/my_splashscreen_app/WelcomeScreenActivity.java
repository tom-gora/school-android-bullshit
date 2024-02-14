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

    private static final long CHECK_DURATION = 5000; // 5 seconds delay for mock check

    private Dialog updateDialog; // init update dialog
    private Dialog networkDialog; // init update dialog

    // Updates check handler
    public void onCheckForUpdatesButtonClick(View view) {
        showUpdateDialog();
    }

    // Network check handler
    public void onCheckForNetworkButtonClick(View view) {
        showNetworkDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        int color = ContextCompat.getColor(this, R.color.teal);
        getWindow().setStatusBarColor(color);
    }


    // Exit button handler
    public void onExitButtonClick(View view) {
        finish();
    }

    private void showUpdateDialog() {
        // Init dialog
        updateDialog = new Dialog(this);
        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateDialog.setContentView(R.layout.update_dialog);

        // Init needed view components
        ProgressBar spinner = updateDialog.findViewById(R.id.spinner);
        TextView updateDialogTitle = updateDialog.findViewById(R.id.dialogTitle);
        TextView updateDialogResult = updateDialog.findViewById(R.id.resultsMsg);
        Button dismissUpdatesDialogBtn = updateDialog.findViewById(R.id.okButton);

        // Show spinner
        spinner.setVisibility(ProgressBar.VISIBLE);
        updateDialogResult.setVisibility(TextView.GONE);
        dismissUpdatesDialogBtn.setVisibility(View.GONE); // Hide the dismiss button initially

        // Simulate checks
        new CountDownTimer(CHECK_DURATION, 1000) {
            @Override
            public void onTick(long timeTillFinish) {
                // Not needed for do anything here because its mock
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

        // Set listener to dismiss dialog
        dismissUpdatesDialogBtn.setOnClickListener(v -> {
            updateDialog.dismiss();
        });

        // Show the dialog
        updateDialog.show();

    }

    private void showNetworkDialog() {
        // Init dialog
        networkDialog = new Dialog(this);
        networkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        networkDialog.setContentView(R.layout.network_dialog);

        // Init needed view components
        ProgressBar spinner = networkDialog.findViewById(R.id.spinner);
        TextView updateDialogTitle = networkDialog.findViewById(R.id.dialogTitle);
        TextView updateDialogResult = networkDialog.findViewById(R.id.resultsMsg);
        Button dismissUpdatesDialogBtn = networkDialog.findViewById(R.id.okButton);

        // Show spinner
        spinner.setVisibility(ProgressBar.VISIBLE);
        updateDialogResult.setVisibility(TextView.GONE);
        dismissUpdatesDialogBtn.setVisibility(View.GONE); // Hide the dismiss button initially

        // Simulate checks
        new CountDownTimer(CHECK_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Not needed for do anything here because its mock
            }

            @Override
            public void onFinish() {
                // Fake result: connect
                spinner.setVisibility(ProgressBar.GONE);
                updateDialogResult.setVisibility(TextView.VISIBLE);
                updateDialogTitle.setText("Status confirmed");
                updateDialogResult.setText("You are connected");
                dismissUpdatesDialogBtn.setVisibility(View.VISIBLE); // Show the dismiss button
            }

        }.start();

        // Set listener to dismiss dialog
        dismissUpdatesDialogBtn.setOnClickListener(v -> {
            networkDialog.dismiss();
        });

        // Show the dialog
        networkDialog.show();

    }
}