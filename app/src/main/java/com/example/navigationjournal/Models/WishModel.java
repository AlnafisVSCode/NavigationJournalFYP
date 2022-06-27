package com.example.navigationjournal.Models;
import android.content.ContentValues;
import com.example.navigationjournal.database.DatabaseHelper;

public class WishModel {
    private int Wish_ID;
    private String Wish_NAME;
    private String Wish_LOCATION ;
    private String Wish_DESCRIPTION;
    private String Wish_DATE;
    private String Wish_IMG;
    private int isDeleted;

    //Setters and getters

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getWish_ID() {
        return Wish_ID;
    }

    public void setWish_ID(int wish_ID) {
        Wish_ID = wish_ID;
    }

    public String getWish_NAME() {
        return Wish_NAME;
    }

    public void setWish_NAME(String wish_NAME) {
        Wish_NAME = wish_NAME;
    }

    public String getWish_LOCATION() {
        return Wish_LOCATION;
    }

    public void setWish_LOCATION(String wish_LOCATION) {
        Wish_LOCATION = wish_LOCATION;
    }

    public String getWish_DESCRIPTION() {
        return Wish_DESCRIPTION;
    }

    public void setWish_DESCRIPTION(String wish_DESCRIPTION) {
        Wish_DESCRIPTION = wish_DESCRIPTION;
    }

    public String getWish_DATE() {
        return Wish_DATE;
    }

    public void setWish_DATE(String wish_DATE) {
        Wish_DATE = wish_DATE;
    }

    public String getWish_IMG() {
        return Wish_IMG;
    }

    public void setWish_IMG(String wish_IMG) {
        Wish_IMG = wish_IMG;
    }

    public ContentValues getContentValues() {
        final ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.WISH_NAME, this.Wish_NAME);
        contentValue.put(DatabaseHelper.WISH_LOCATION, this.Wish_LOCATION);
        contentValue.put(DatabaseHelper.WISH_DESCRIPTION, this.Wish_DESCRIPTION);
        contentValue.put(DatabaseHelper.WISH_DATE, this.Wish_DATE);
       contentValue.put(DatabaseHelper.WISH_IsDELETE, this.Wish_IMG);
       contentValue.put(DatabaseHelper.WISH_IMG, this.isDeleted);
        return contentValue;
    }
    @Override
    public String toString() {
        return "WISH_LIST{" +
                "_id =" + this.Wish_ID +
                ", wish_name='" + this.Wish_NAME + '\'' +
                ", wish_location='" + this.Wish_LOCATION + '\'' +
                ", wish_description='" + this.Wish_DESCRIPTION + '\'' +
                ", wish_date='" + this.Wish_DATE + '\'' +
                ", wish_isDeleted='" + this.isDeleted + '\'' +
                ", wish_img=" + this.Wish_IMG+
                '}';
    }
}
