package com.example.kim_j_project6;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RatingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "com.example.kim_j_project6.new_rating".equals(intent.getAction())) {
            Log.i("HERE RECEIVED", "received");
            String placeReviewed = intent.getStringExtra("place_reviewed");
            String userName = intent.getStringExtra("user_name");
            double ratingReceived = intent.getDoubleExtra("rating_received", 0.0);

            // convert time to readable string
            long timeOfRating = intent.getLongExtra("time_of_rating", 0);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault());
            String timeString = sdf.format(new Date(timeOfRating));

            // check and request permission if needed
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }

            // send notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel_id")
                    .setContentTitle("Rating Received")
                    .setContentText(placeReviewed + " received a rating of " + ratingReceived + " from " + userName + " at " + timeString)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}
