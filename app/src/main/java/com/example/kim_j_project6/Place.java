package com.example.kim_j_project6;

public class Place {
    private String name;
    private String lat;
    private String lng;
    private int rating;
    private boolean visited;
    private boolean favorited;

    // constructor
    public Place(String name, String lat, String lng, int rating, boolean visited, boolean favorited) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.visited = visited;
        this.favorited = favorited;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}