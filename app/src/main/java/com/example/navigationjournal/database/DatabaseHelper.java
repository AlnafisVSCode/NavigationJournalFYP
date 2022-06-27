package com.example.navigationjournal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "LOCATION";
    public static final String SST_TABLE_NAME = "SHORT_TERM_TASK";
    public static final String SST_HISTORY_TABLE_NAME = "HISTORY_SHORT_TERM_TASK";
    public static final String WISHLIST_HISTORY_TABLE_NAME = "HISTORY_WISHLIST";
    public static final String WISHLIST_TABLE_NAME = "WISH_LIST";

    // Table columns
    public static final String _ID = "_id";
    public static final String LOCATION_NAME = "name";
    public static final String LOCATION_Date = "date";
    public static final String LOCATION_City = "city";
    public static final String LOCATION_Street = "street";
    public static final String LOCATION_Rating = "rating";
    public static final String LOCATION_Review = "review";
    public static final String LOCATION_IMG = "image";
    public static final String LOCATION_IsFavourite = "favourite";

    //Short Term Task Columns
    public static final String STT_ID = "_id";
    public static final String STT_TASK_NAME = "task_name";
    public static final String STT_TASK_LOCATION = "task_location";
    public static final String STT_TASK_DESCRIPTION = "description";
    public static final String STT_TASK_DATE = "task_date";
    public static final String STT_TASK_TIME = "task_time";
    public static final String STT_TASK_IMG = "task_img";

    //WishList Columns
    public static final String WISH_ID = "_id";
    public static final String WISH_NAME = "wish_name";
    public static final String WISH_LOCATION = "wish_location";
    public static final String WISH_DESCRIPTION = "wish_description";
    public static final String WISH_DATE = "wish_date";
    public static final String WISH_IMG = "wish_img";
    public static final String WISH_IsDELETE = "wish_isDeleted";


    // Database Information
    static final String DB_NAME = "DB_LOCATIONS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LOCATION_NAME + " TEXT NOT NULL, " +
            LOCATION_Date + " TEXT NOT NULL, " +
            LOCATION_City + " TEXT NOT NULL, " +
            LOCATION_Street + " TEXT NOT NULL, " +
            LOCATION_Review + " TEXT NOT NULL, " +
            LOCATION_IMG + " TEXT NOT NULL, " +
            LOCATION_Rating+ " INTEGER NOT NULL, " +
            LOCATION_IsFavourite + " INTEGER NOT NULL);";
    private static final String CREATE_TABLE_SST = "create table " + SST_TABLE_NAME + "(" +
            STT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            STT_TASK_NAME + " TEXT NOT NULL, " +
            STT_TASK_LOCATION + " TEXT NOT NULL, " +
            STT_TASK_DESCRIPTION + " TEXT NOT NULL, " +
            STT_TASK_DATE + " TEXT NOT NULL, " +
            STT_TASK_TIME + " TEXT NOT NULL, " +
            STT_TASK_IMG + " TEXT NOT NULL);";

 private static final String CREATE_TABLE_WISHLIST = "create table " + WISHLIST_TABLE_NAME + "(" +
            WISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
         WISH_NAME + " TEXT NOT NULL, " +
         WISH_LOCATION + " TEXT NOT NULL, " +
         WISH_DESCRIPTION + " TEXT NOT NULL, " +
         WISH_DATE + " TEXT NOT NULL, " +
         WISH_IsDELETE + " INTEGER , " +
         WISH_IMG + " TEXT );";

 private static final String CREATE_WISHLIST_HISTORY = "create table " + WISHLIST_HISTORY_TABLE_NAME + "(" +
            WISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
         WISH_NAME + " TEXT NOT NULL, " +
         WISH_LOCATION + " TEXT NOT NULL, " +
         WISH_DESCRIPTION + " TEXT NOT NULL, " +
         WISH_DATE + " TEXT NOT NULL, " +
         WISH_IsDELETE + " INTEGER , " +
         WISH_IMG + " TEXT );";

    private static final String CREATE_TABLE_SST_HISTORY = "create table " + SST_HISTORY_TABLE_NAME + "(" +
            STT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            STT_TASK_NAME + " TEXT NOT NULL, " +
            STT_TASK_LOCATION + " TEXT NOT NULL, " +
            STT_TASK_DESCRIPTION + " TEXT NOT NULL, " +
            STT_TASK_DATE + " TEXT NOT NULL, " +
            STT_TASK_TIME + " TEXT NOT NULL, " +
            STT_TASK_IMG + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Database Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_SST);
        db.execSQL(CREATE_TABLE_WISHLIST);
        db.execSQL(CREATE_WISHLIST_HISTORY);
        db.execSQL(CREATE_TABLE_SST_HISTORY);
    }
    //handling new db changes(could be new columns addition,table addition)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WISHLIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WISHLIST_HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SST_HISTORY_TABLE_NAME);
        onCreate(db);
    }
}