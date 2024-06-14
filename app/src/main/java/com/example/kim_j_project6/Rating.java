package com.example.kim_j_project6;

public class Rating {
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

    public long getDate() {
        return time;
    }

    public void setDate(long time) {
        this.time = time;
    }
}
