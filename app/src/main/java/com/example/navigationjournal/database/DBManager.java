package com.example.navigationjournal.database;

import static android.provider.MediaStore.Video.VideoColumns.DESCRIPTION;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_City;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Date;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_IMG;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_IsFavourite;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Rating;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Review;
import static com.example.navigationjournal.database.DatabaseHelper.LOCATION_Street;
import static com.example.navigationjournal.database.DatabaseHelper.SST_HISTORY_TABLE_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.SST_TABLE_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.STT_ID;
import static com.example.navigationjournal.database.DatabaseHelper.STT_TASK_DATE;
import static com.example.navigationjournal.database.DatabaseHelper.STT_TASK_DESCRIPTION;
import static com.example.navigationjournal.database.DatabaseHelper.STT_TASK_IMG;
import static com.example.navigationjournal.database.DatabaseHelper.STT_TASK_LOCATION;
import static com.example.navigationjournal.database.DatabaseHelper.STT_TASK_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.STT_TASK_TIME;
import static com.example.navigationjournal.database.DatabaseHelper.TABLE_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.WISHLIST_HISTORY_TABLE_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.WISHLIST_TABLE_NAME;
import static com.example.navigationjournal.database.DatabaseHelper.WISH_DATE;
import static com.example.navigationjournal.database.DatabaseHelper.WISH_DESCRIPTION;
import static com.example.navigationjournal.database.DatabaseHelper.WISH_ID;
import static com.example.navigationjournal.database.DatabaseHelper.WISH_IMG;
import static com.example.navigationjournal.database.DatabaseHelper.WISH_LOCATION;
import static com.example.navigationjournal.database.DatabaseHelper.WISH_NAME;
import static com.example.navigationjournal.database.DatabaseHelper._ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.navigationjournal.Models.LocationModel;
import com.example.navigationjournal.Models.ShortTermTaskModel;
import com.example.navigationjournal.Models.WishModel;
import com.example.navigationjournal.shortTermTasks.AddShortTermTask;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String TAG = "DBManager";
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * TO store data - inserting them to individual DB tables
     */
    public void insert(LocationModel locationModel) {
        open();
        database.insert(TABLE_NAME, null, locationModel.getContentValues());
    }
    public void insertSTT(ShortTermTaskModel taskModel) {
        open();
        long d = database.insert(SST_TABLE_NAME, null, taskModel.getContentValues());
        Log.d(TAG,""+d);


    } public long insertWL(WishModel taskModel) {
        open();
       return database.insert(WISHLIST_TABLE_NAME, null, taskModel.getContentValues()) ;
    }
     public void insertSTT_HISTORY(ShortTermTaskModel taskModel) {
        open();
        database.insert(SST_HISTORY_TABLE_NAME, null, taskModel.getContentValues());
    }
    public void insertSTT_HISTORY(WishModel taskModel) {
        open();
        database.insert(WISHLIST_HISTORY_TABLE_NAME, null, taskModel.getContentValues());
    }
    //---deletes a particular title---
    public boolean deleteSTTask(int ID)
    {
        return database.delete(SST_TABLE_NAME, STT_ID + "=" + ID, null) > 0;
    }

    public boolean deleteSTWishList(int itemID) {
        return database.delete(WISHLIST_TABLE_NAME, WISH_ID + "=" + itemID, null) > 0;
    }
    //To move the location saved into another page- favourite location page
    //Todo Delete this or rename to related activity
    public List<LocationModel> getFavouriteLocations() {
        open();
        Cursor cursor = database.rawQuery(String.format("select * from %s where %s = %s", TABLE_NAME, LOCATION_IsFavourite, 1), null);
        List<LocationModel> locationList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationModel locationModel = new LocationModel();
            locationModel.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            locationModel.setLocationName(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
            locationModel.setLocation_city(cursor.getString(cursor.getColumnIndex(LOCATION_City)));
            locationModel.setLocation_street(cursor.getString(cursor.getColumnIndex(LOCATION_Street)));
            locationModel.setLocation_rating(cursor.getInt(cursor.getColumnIndex(LOCATION_Rating)));
            locationModel.setLocation_review(cursor.getString(cursor.getColumnIndex(LOCATION_Review)));
            locationModel.setAdded_date(cursor.getString(cursor.getColumnIndex(LOCATION_Date)));
            locationModel.setLocation_img(cursor.getString(cursor.getColumnIndex(LOCATION_IMG)));
            locationModel.setFavourite(cursor.getInt(cursor.getColumnIndex(LOCATION_IsFavourite)));
            locationList.add(locationModel);

            cursor.moveToNext();
        }
        return locationList;
    }
    //Get the table containing the locations stored - based on ID
    public LocationModel getLocation(int index) {
        open();
        Cursor cursor = database.rawQuery(String.format("select * from %s where %s = %s", TABLE_NAME, _ID, index), null);
        List<LocationModel> locationList = new ArrayList<>();
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            LocationModel locationModel = new LocationModel();
            locationModel.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            locationModel.setLocationName(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
            locationModel.setLocation_city(cursor.getString(cursor.getColumnIndex(LOCATION_City)));
            locationModel.setLocation_street(cursor.getString(cursor.getColumnIndex(LOCATION_Street)));
            locationModel.setLocation_rating(cursor.getInt(cursor.getColumnIndex(LOCATION_Rating)));
            locationModel.setLocation_review(cursor.getString(cursor.getColumnIndex(LOCATION_Review)));
            locationModel.setAdded_date(cursor.getString(cursor.getColumnIndex(LOCATION_Date)));
            locationModel.setLocation_img(cursor.getString(cursor.getColumnIndex(LOCATION_IMG)));
            locationModel.setFavourite(cursor.getInt(cursor.getColumnIndex(LOCATION_IsFavourite)));
            locationList.add(locationModel);
            cursor.moveToNext();
            break;
        }
        return locationList.get(0);
    }
    //Get all the locations stored from DB
    public List<LocationModel> getAllLocation() {
        open();
        Cursor cursor = database.rawQuery(String.format("select * from %s", TABLE_NAME), null);
        List<LocationModel> locationList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            LocationModel locationModel = new LocationModel();
//            locationList.add(new LocationModel());
            locationModel.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            locationModel.setLocationName(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
            locationModel.setLocation_city(cursor.getString(cursor.getColumnIndex(LOCATION_City)));
            locationModel.setLocation_street(cursor.getString(cursor.getColumnIndex(LOCATION_Street)));
            locationModel.setLocation_rating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOCATION_Rating))));
            locationModel.setLocation_review(cursor.getString(cursor.getColumnIndex(LOCATION_Review)));
            locationModel.setAdded_date(cursor.getString(cursor.getColumnIndex(LOCATION_Date)));
            locationModel.setLocation_img(cursor.getString(cursor.getColumnIndex(LOCATION_IMG)));
            locationModel.setFavourite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOCATION_IsFavourite))));
            locationList.add(locationModel);
            cursor.moveToNext();
        }
        return locationList;
    }


    //this function to get all short tasks from db
    @SuppressLint("Recycle")
    public List<ShortTermTaskModel> getAllTask() {
        open();
        String[] columns = {STT_ID,STT_TASK_NAME,STT_TASK_LOCATION,STT_TASK_TIME,STT_TASK_DATE,STT_TASK_DESCRIPTION,STT_TASK_IMG};
        Cursor cursor =database.query(SST_TABLE_NAME,columns,  null, null,      null,null,null);
        List<ShortTermTaskModel> taskModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            ShortTermTaskModel locationModel = new ShortTermTaskModel();
            locationModel.setSTT_ID(cursor.getInt(cursor.getColumnIndex(STT_ID)));
            locationModel.setSTT_TASK_NAME(cursor.getString(cursor.getColumnIndex(STT_TASK_NAME)));
            locationModel.setSTT_TASK_LOCATION(cursor.getString(cursor.getColumnIndex(STT_TASK_LOCATION)));
            locationModel.setSTT_TASK_TIME(cursor.getString(cursor.getColumnIndex(STT_TASK_TIME)));
            locationModel.setSTT_TASK_DATE(cursor.getString(cursor.getColumnIndex(STT_TASK_DATE)));
            locationModel.setSTT_TASK_DESCRIPTION(cursor.getString(cursor.getColumnIndex(STT_TASK_DESCRIPTION)));
            locationModel.setSTT_TASK_IMG(cursor.getString(cursor.getColumnIndex(STT_TASK_IMG)));
            taskModels.add(locationModel);
            cursor.moveToNext();
            Log.d(TAG,locationModel.toString());
        }
        return taskModels;
    } @SuppressLint("Recycle")
    public List<WishModel> getAllList() {
        open();
        String[] columns = {WISH_ID,WISH_NAME,WISH_LOCATION,WISH_DATE,WISH_DESCRIPTION,WISH_IMG};
        Cursor cursor =database.query(WISHLIST_TABLE_NAME,columns,  null, null,      null,null,null);
        List<WishModel> taskModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            WishModel locationModel = new WishModel();
            locationModel.setWish_ID(cursor.getInt(cursor.getColumnIndex(WISH_ID)));
            locationModel.setWish_NAME(cursor.getString(cursor.getColumnIndex(WISH_NAME)));
            locationModel.setWish_LOCATION(cursor.getString(cursor.getColumnIndex(WISH_LOCATION)));
            locationModel.setWish_DATE(cursor.getString(cursor.getColumnIndex(WISH_DATE)));
            locationModel.setWish_DESCRIPTION(cursor.getString(cursor.getColumnIndex(WISH_DESCRIPTION)));
            locationModel.setWish_IMG(cursor.getString(cursor.getColumnIndex(WISH_IMG)));
            taskModels.add(locationModel);
            cursor.moveToNext();
        }
        return taskModels;
    }
 //this function to get all task from db
    @SuppressLint("Recycle")
    public List<ShortTermTaskModel> getAllTaskHistory() {
        open();
        String[] columns = {STT_ID,STT_TASK_NAME,STT_TASK_LOCATION,STT_TASK_TIME,STT_TASK_DATE,STT_TASK_DESCRIPTION,STT_TASK_IMG};
        Cursor cursor =database.query(SST_HISTORY_TABLE_NAME,columns,  null, null,      null,null,null);
        List<ShortTermTaskModel> taskModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            ShortTermTaskModel locationModel = new ShortTermTaskModel();
            locationModel.setSTT_ID(cursor.getInt(cursor.getColumnIndex(STT_ID)));
            locationModel.setSTT_TASK_NAME(cursor.getString(cursor.getColumnIndex(STT_TASK_NAME)));
            locationModel.setSTT_TASK_LOCATION(cursor.getString(cursor.getColumnIndex(STT_TASK_LOCATION)));
            locationModel.setSTT_TASK_TIME(cursor.getString(cursor.getColumnIndex(STT_TASK_TIME)));
            locationModel.setSTT_TASK_DATE(cursor.getString(cursor.getColumnIndex(STT_TASK_DATE)));
            locationModel.setSTT_TASK_DESCRIPTION(cursor.getString(cursor.getColumnIndex(STT_TASK_DESCRIPTION)));
            locationModel.setSTT_TASK_IMG(cursor.getString(cursor.getColumnIndex(STT_TASK_IMG)));
            taskModels.add(locationModel);
            cursor.moveToNext();
        }
        return taskModels;
    }
    //Wish list history - get wishes into history
    @SuppressLint("Recycle")
    public List<WishModel> getAllWishHistory() {
        open();
        String[] columns = {WISH_ID,WISH_NAME,WISH_LOCATION,WISH_DATE,WISH_DESCRIPTION,WISH_IMG};
        Cursor cursor =database.query(WISHLIST_HISTORY_TABLE_NAME,columns,  null, null,      null,null,null);
        List<WishModel> taskModels = new ArrayList<>();
        while (cursor.moveToNext()) {
            WishModel locationModel = new WishModel();
            locationModel.setWish_ID(cursor.getInt(cursor.getColumnIndex(WISH_ID)));
            locationModel.setWish_LOCATION(cursor.getString(cursor.getColumnIndex(WISH_LOCATION)));
            locationModel.setWish_NAME(cursor.getString(cursor.getColumnIndex(WISH_NAME)));
            locationModel.setWish_DATE(cursor.getString(cursor.getColumnIndex(WISH_DATE)));
            locationModel.setWish_DESCRIPTION(cursor.getString(cursor.getColumnIndex(WISH_DESCRIPTION)));
            locationModel.setWish_IMG(cursor.getString(cursor.getColumnIndex(WISH_IMG)));
            taskModels.add(locationModel);
            cursor.moveToNext();
        }
        return taskModels;
    }

    /**
     * To update values in database
     */
    public int update(long _id, LocationModel location) {
        Log.d(TAG, "update() called with: _id = [" + _id + "], location = [" + location.toString() + "]");
        open();
        return database.update(DatabaseHelper.TABLE_NAME, location.getContentValues(), DatabaseHelper._ID + " = " + _id, null);
    }
    public int updateWishList(long _id, WishModel wishModel) {
        open();
        return database.update(WISHLIST_TABLE_NAME, wishModel.getContentValues(), WISH_ID + " = " + _id, null);
    }

    public List<LocationModel> getListOfLocations(String searchText) {
        open();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s LIKE '%%%s%%' or %s LIKE '%%%s%%' or %s LIKE '%%%s%%';",
                TABLE_NAME, LOCATION_NAME, searchText, LOCATION_City, searchText, LOCATION_Street, searchText), null);
        List<LocationModel> locationList = new ArrayList<>();
        Log.d(TAG, "getListOfLocations: "+cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            LocationModel locationModel = new LocationModel();
//            locationList.add(new LocationModel());
            locationModel.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            locationModel.setLocationName(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
            locationModel.setLocation_city(cursor.getString(cursor.getColumnIndex(LOCATION_City)));
            locationModel.setLocation_street(cursor.getString(cursor.getColumnIndex(LOCATION_Street)));
            locationModel.setLocation_rating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOCATION_Rating))));
            locationModel.setLocation_review(cursor.getString(cursor.getColumnIndex(LOCATION_Review)));
            locationModel.setAdded_date(cursor.getString(cursor.getColumnIndex(LOCATION_Date)));
            locationModel.setLocation_img(cursor.getString(cursor.getColumnIndex(LOCATION_IMG)));
            locationModel.setFavourite(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOCATION_IsFavourite))));
            locationList.add(locationModel);

            cursor.moveToNext();
            break;
        }
        return locationList;
    }
    public List<WishModel> getSearchDataForWishList(String searchText) {
        open();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s LIKE '%%%s%%' or %s LIKE '%%%s%%';",
                WISHLIST_TABLE_NAME, WISH_LOCATION, searchText, WISH_NAME, searchText), null);
        List<WishModel> wishModelList = new ArrayList<>();
        Log.d(TAG, "getListOfLocations: "+cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            WishModel wishModel = new WishModel();
//            locationList.add(new LocationModel());
            wishModel.setWish_ID(cursor.getInt(cursor.getColumnIndex(WISH_ID)));
            wishModel.setWish_NAME(cursor.getString(cursor.getColumnIndex(WISH_NAME)));
            wishModel.setWish_LOCATION(cursor.getString(cursor.getColumnIndex(WISH_LOCATION)));
            wishModel.setWish_DESCRIPTION(cursor.getString(cursor.getColumnIndex(WISH_DESCRIPTION)));
            wishModel.setWish_DATE(cursor.getString(cursor.getColumnIndex(WISH_DATE)));
            wishModel.setWish_IMG(cursor.getString(cursor.getColumnIndex(WISH_IMG)));
            wishModelList.add(wishModel);

            cursor.moveToNext();
            break;
        }
        return wishModelList;
    }
}