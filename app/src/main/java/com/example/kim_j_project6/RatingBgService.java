package com.example.kim_j_project6;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RatingBgService extends Service {
    private Date date;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        Log.i("HERE SERVICE", "date: " + formattedDate);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
