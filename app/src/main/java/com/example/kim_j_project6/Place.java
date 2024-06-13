package com.example.kim_j_project6;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Place implements Parcelable {
    private String id;
    private String name;
    private String description;
    private String lat;
    private String lng;
    private int rating;
    private boolean visited;
    private boolean favorited;
    private String userId;

    // constructors
    public Place(String id, String name, String description, String lat, String lng,
                 int rating, boolean visited, boolean favorited, String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.visited = visited;
        this.favorited = favorited;
        this.userId = userId;
    }

    public Place() {
    }

    protected Place(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        lat = in.readString();
        lng = in.readString();
        rating = in.readInt();
        visited = in.readByte() != 0;
        favorited = in.readByte() != 0;
        userId = in.readString();
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeInt(rating);
        dest.writeByte((byte) (visited ? 1 : 0));
        dest.writeByte((byte) (favorited ? 1 : 0));
        dest.writeString(userId);
    }
}
