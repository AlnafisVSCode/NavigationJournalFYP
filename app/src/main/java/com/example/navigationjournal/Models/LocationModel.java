package com.example.navigationjournal.Models;

import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Date;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_IMG;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Rating;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Review;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_City;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_IsFavourite;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Street;

import android.content.ContentValues;

public class LocationModel {
    private int id;
    private String locationName;
    private String added_date;
    private String location_city;
    private String location_street;
    private Integer location_rating;
    private String location_review;
    private String location_img;
    private Integer favourite = 0;

    //Setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocation_city() {
        return location_city;
    }

    public void setLocation_city(String location_city) {
        this.location_city = location_city;
    }

    public String getLocation_street() {
        return location_street;
    }

    public void setLocation_street(String location_street) {
        this.location_street = location_street;
    }

    public Integer getLocation_rating() {
        return location_rating;
    }

    public void setLocation_rating(Integer location_rating) {
        this.location_rating = location_rating;
    }

    public String getLocation_review() {
        return location_review;
    }

    public void setLocation_review(String location_review) {
        this.location_review = location_review;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getLocation_img() {
        return location_img;
    }

    public void setLocation_img(String location_img) {
        this.location_img = location_img;
    }

    public Integer getFavourite() {
        return favourite;
    }
    public boolean isFavourite() {
        return favourite == 1;
    }
    public void setFavourite(Integer favourite) {
        this.favourite = favourite;
    }
    public ContentValues getContentValues() {
        final ContentValues contentValue = new ContentValues();
        contentValue.put(LOCATION_NAME, locationName);
        contentValue.put(LOCATION_Date, added_date);
        contentValue.put(LOCATION_City, location_city);
        contentValue.put(LOCATION_Street, location_street);
        contentValue.put(LOCATION_Rating, location_rating);
        contentValue.put(LOCATION_Review, location_review);
        contentValue.put(LOCATION_IMG, location_img);
        contentValue.put(LOCATION_IsFavourite, favourite);
        return contentValue;
    }


    @Override
    public String toString() {
        return "LOCATION{" +
                "id=" + id +
                ", name='" + locationName + '\'' +
                ", date='" + added_date + '\'' +
                ", city='" + location_city + '\'' +
                ", street='" + location_street + '\'' +
                ", rating=" + location_rating +
                ", review=" + location_review +
                ", image=" + location_img +
                ", favourite=" + favourite +
                '}';
    }
}
