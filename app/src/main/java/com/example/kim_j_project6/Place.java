package com.example.kim_j_project6;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Place implements Parcelable {
    private String id;
    private String name;
    private String description;
    private String lat;
    private String lng;
    private ArrayList<Rating> rating;
    private HashMap<String, String> visited;
    private ArrayList<String> favorited;
    private String userId;

    // constructors
    public Place(String id, String name, String description, String lat, String lng,
                 ArrayList<Rating> rating, HashMap<String, String> visited, ArrayList<String> favorited, String userId) {
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
        rating = new ArrayList<>();
        in.readList(rating, Rating.class.getClassLoader());
        visited = new HashMap<>();
        in.readMap(visited, HashMap.class.getClassLoader());
        favorited = new ArrayList<>();
        in.readList(favorited, String.class.getClassLoader());
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

    public ArrayList<Rating> getRating() {
        return rating;
    }

    public void setRating(ArrayList<Rating> rating) {
        this.rating = rating;
    }

    public HashMap<String, String> getVisited() {
        if (visited == null) {
            visited = new HashMap<>();
        }
        return visited;
    }

    public void setVisited(HashMap<String, String> visited) {
        this.visited = visited;
    }

    public ArrayList<String> getFavorited() {
        if (favorited == null) {
            favorited = new ArrayList<>();
        }
        return favorited;
    }

    public void setFavorited(ArrayList<String> favorited) {
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
        dest.writeList(rating);
        dest.writeMap(visited);
        dest.writeList(favorited);
        dest.writeString(userId);
    }
}
