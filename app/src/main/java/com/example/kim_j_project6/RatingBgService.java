package com.example.kim_j_project6;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatingBgService extends Service {
    private static final long interval = 10 * 60 * 1000;
    private Handler handler;
    private Runnable ratingCheckRunnable;
    private DatabaseReference placesDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        placesDatabase = FirebaseDatabase.getInstance().getReference("places");
        handler = new Handler();
        ratingCheckRunnable = () -> {
            // Fetch data from Firebase database
            placesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                        Place place = placeSnapshot.getValue(Place.class);
                        if (place != null) {
                            ArrayList<Rating> ratings = place.getRating();
                            if (ratings != null && !ratings.isEmpty()) {
                                for (Rating rating : ratings) {
                                    if (isNewRating(rating.getTime())) {
                                        sendNotification(place.getName(), rating.getRatedByUser(), rating.getTime(), rating.getRating());
                                    } else {
                                        Log.i("HERE SERVICE", "no new ratings");
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("HERE SERVICE", "Error fetching places", databaseError.toException());
                }
            });

            handler.postDelayed(ratingCheckRunnable, interval);
        };
    }

    private boolean isNewRating(long ratingTime) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - ratingTime;
        return timeDifference <= interval;
    }

    private void sendNotification(String placeReviewed, String userName, long timeOfRating, double rating) {
        Intent intent = new Intent("com.example.kim_j_project6.new_rating");
        intent.putExtra("place_reviewed", placeReviewed);
        intent.putExtra("user", userName);
        intent.putExtra("time", timeOfRating);
        intent.putExtra("rating", rating);
        sendBroadcast(intent);
        Log.i("HERE SERVICE", "intent sent");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(ratingCheckRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(ratingCheckRunnable);
    }

}
