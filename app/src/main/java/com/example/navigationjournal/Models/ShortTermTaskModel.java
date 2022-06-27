package com.example.navigationjournal.Models;
import android.content.ContentValues;

import com.example.navigationjournal.database.DatabaseHelper;

public class ShortTermTaskModel {
    private int STT_ID;
    private String STT_TASK_NAME;
    private String STT_TASK_LOCATION ;
    private String STT_TASK_DESCRIPTION;
    private String STT_TASK_DATE;
    private String STT_TASK_TIME;
    private String STT_TASK_IMG;

    public ShortTermTaskModel() {

    }
    public ShortTermTaskModel(int STT_ID, String STT_TASK_NAME, String STT_TASK_LOCATION, String STT_TASK_DESCRIPTION, String STT_TASK_TIME, String STT_TASK_IMG) {
        this.STT_ID = STT_ID;
        this.STT_TASK_NAME = STT_TASK_NAME;
        this.STT_TASK_LOCATION = STT_TASK_LOCATION;
        this.STT_TASK_DESCRIPTION = STT_TASK_DESCRIPTION;
        this.STT_TASK_TIME = STT_TASK_TIME;
        this.STT_TASK_IMG = STT_TASK_IMG;
    }

    //Setters and Getters
    public int getSTT_ID() {
        return this.STT_ID;
    }

    public void setSTT_ID(int STT_ID) {
        this.STT_ID = STT_ID;
    }

    public String getSTT_TASK_NAME() {
        return this.STT_TASK_NAME;
    }

    public void setSTT_TASK_NAME(String STT_TASK_NAME) {
        this.STT_TASK_NAME = STT_TASK_NAME;
    }

    public String getSTT_TASK_LOCATION() {
        return this.STT_TASK_LOCATION;
    }

    public void setSTT_TASK_LOCATION(String STT_TASK_LOCATION) {
        this.STT_TASK_LOCATION = STT_TASK_LOCATION;
    }

    public String getSTT_TASK_DESCRIPTION() {
        return this.STT_TASK_DESCRIPTION;
    }

    public void setSTT_TASK_DESCRIPTION(String STT_TASK_DESCRIPTION) {
        this.STT_TASK_DESCRIPTION = STT_TASK_DESCRIPTION;
    }

    public String getSTT_TASK_DATE() {
        return this.STT_TASK_DATE;
    }

    public void setSTT_TASK_DATE(String STT_TASK_DATE) {
        this.STT_TASK_DATE = STT_TASK_DATE;
    }

    public String getSTT_TASK_TIME() {
        return this.STT_TASK_TIME;
    }

    public void setSTT_TASK_TIME(String STT_TASK_TIME) {
        this.STT_TASK_TIME = STT_TASK_TIME;
    }

    public String getSTT_TASK_IMG() {
        return this.STT_TASK_IMG;
    }

    public void setSTT_TASK_IMG(String STT_TASK_IMG) {
        this.STT_TASK_IMG = STT_TASK_IMG;
    }

    public ContentValues getContentValues() {
        final ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.STT_TASK_NAME, this.STT_TASK_NAME);
        contentValue.put(DatabaseHelper.STT_TASK_LOCATION, this.STT_TASK_LOCATION);
        contentValue.put(DatabaseHelper.STT_TASK_DESCRIPTION, this.STT_TASK_DESCRIPTION);
        contentValue.put(DatabaseHelper.STT_TASK_DATE, this.STT_TASK_DATE);
        contentValue.put(DatabaseHelper.STT_TASK_TIME, this.STT_TASK_TIME);
        contentValue.put(DatabaseHelper.STT_TASK_IMG, this.STT_TASK_IMG);
        return contentValue;
    }
    @Override
    public String toString() {
        return "SHORT_TERM_TASK{" +
                "_id =" + this.STT_ID +
                ", task_name='" + this.STT_TASK_NAME + '\'' +
                ", task_location='" + this.STT_TASK_LOCATION + '\'' +
                ", description='" + this.STT_TASK_DESCRIPTION + '\'' +
                ", task_date='" + this.STT_TASK_DATE + '\'' +
                ", task_time=" + this.STT_TASK_TIME +
                ", task_img=" + this.STT_TASK_IMG+
                '}';
    }
}
