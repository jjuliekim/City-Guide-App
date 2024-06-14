package com.example.kim_j_project6;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Rating implements Parcelable {
    private String ratedByUser;
    private double rating;
    private long time;

    public Rating() {

    }

    public Rating(String ratedByUser, double rating, long time) {
        this.ratedByUser = ratedByUser;
        this.rating = rating;
        this.time = time;
    }

    // getters and setters

    public String getRatedByUser() {
        return ratedByUser;
    }

    public void setRatedByUser(String ratedByUser) {
        this.ratedByUser = ratedByUser;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ratedByUser);
        dest.writeDouble(rating);
        dest.writeLong(time);
    }

    protected Rating(Parcel in) {
        ratedByUser = in.readString();
        rating = in.readDouble();
        time = in.readLong();
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

}
