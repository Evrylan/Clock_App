package com.example.clockproject;

//All imports
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Handler;
import android.os.Looper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView timeTextView;
    private TextView ntpTimeTextView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final String NTP_SERVER = "pool.ntp.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the UI element
        timeTextView = findViewById(R.id.timeTextView);
        ntpTimeTextView = findViewById(R.id.ntpTimeTextView);

        // update the system time using a handler
        handler.postDelayed(updateSystemTimeRunnable, 0);


        updateNtpTime();
    }

    // Runnable to update the system time and schedule itself to run every second
    private final Runnable updateSystemTimeRunnable = new Runnable() {
        @Override
        public void run() {
            //Update the NTP time
            updateNtpTime();

            // Format the current time and display it timeTextView
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            timeTextView.setText(currentTime);

            // Update the time again after 1 second
            handler.postDelayed(this, 1000);
        }
    };

    // Method to update the NTP time if the device is connected to wifi/3g/inte i flygplanslägge
    private void updateNtpTime() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            // If the device is connected to a network, create a new thread to fetch NTP time
            Thread ntpThread = new Thread(() -> {
                try {
                    // Get the NTP time from the specified server
                    long ntpTime = NtpTimeSynchronization.getNtpTime(NTP_SERVER);
                    // Format the NTP time and update the UI on the main thread
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    String ntpTimeString = sdf.format(new Date(ntpTime));

                    runOnUiThread(() -> {
                        ntpTimeTextView.setText(ntpTimeString);
                        ntpTimeTextView.setVisibility(View.VISIBLE);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle network or NTP time synchronization errors
                    runOnUiThread(() -> ntpTimeTextView.setVisibility(View.GONE));
                }
            });
            // Start the NTP time retrieval
            ntpThread.start();
        } else {
            // If not connected, hide the NTP time TextView
            ntpTimeTextView.setVisibility(View.GONE);
        }
    }
}
