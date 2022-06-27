package com.example.navigationjournal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.navigationjournal.Models.ShortTermTaskModel;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDatabase.db";
    public static final String SST_TABLE_NAME = "short_term_task";
    private static final String TAG = "DBManager";

    private HashMap hp;

    //Short Term Task Columns
    public static final String STT_ID = "_id";
    public static final String STT_TASK_NAME = "task_name";
    public static final String STT_TASK_LOCATION = "task_location";
    public static final String STT_TASK_DESCRIPTION = "description";
    public static final String STT_TASK_DATE = "task_date";
    public static final String STT_TASK_TIME = "task_time";
    public static final String STT_TASK_IMG = "task_img";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table " + SST_TABLE_NAME +
                        "("+STT_ID+" integer primary key, " +STT_TASK_NAME +" text," +STT_TASK_LOCATION +" text," +STT_TASK_DESCRIPTION +" text," +STT_TASK_TIME +"  text," +STT_TASK_IMG +"  text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+SST_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact (ShortTermTaskModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STT_TASK_NAME, model.getSTT_TASK_NAME());
        contentValues.put(STT_TASK_LOCATION, model.getSTT_TASK_LOCATION());
        contentValues.put(STT_TASK_DESCRIPTION, model.getSTT_TASK_DESCRIPTION());
        contentValues.put(STT_TASK_DATE, model.getSTT_TASK_DATE());
        contentValues.put(STT_TASK_TIME, model.getSTT_TASK_TIME());
        contentValues.put(STT_TASK_IMG, model.getSTT_TASK_IMG());
        db.insert(SST_TABLE_NAME, null, contentValues);
        return true;
    }

    public ShortTermTaskModel getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+SST_TABLE_NAME+" where _id="+id+"", null );
        res.moveToFirst();

        ShortTermTaskModel add = new ShortTermTaskModel();
        add.setSTT_ID(res.getInt(res.getColumnIndex(STT_ID)));
        add.setSTT_TASK_NAME(res.getString(res.getColumnIndex(STT_TASK_NAME)));
        add.setSTT_TASK_LOCATION(res.getString(res.getColumnIndex(STT_TASK_LOCATION)));
        add.setSTT_TASK_DESCRIPTION(res.getString(res.getColumnIndex(STT_TASK_DESCRIPTION)));
        add.setSTT_TASK_DATE(res.getString(res.getColumnIndex(STT_TASK_DATE)));
        add.setSTT_TASK_TIME(res.getString(res.getColumnIndex(STT_TASK_TIME)));
        add.setSTT_TASK_IMG(res.getString(res.getColumnIndex(STT_TASK_IMG)));

        if (!res.isClosed())  {
            res.close();
        }
        return add;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SST_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact  (ShortTermTaskModel model) {
        Log.d(TAG,model.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STT_TASK_NAME, model.getSTT_TASK_NAME());
        contentValues.put(STT_TASK_LOCATION, model.getSTT_TASK_LOCATION());
        contentValues.put(STT_TASK_DESCRIPTION, model.getSTT_TASK_DESCRIPTION());
        contentValues.put(STT_TASK_DATE, model.getSTT_TASK_DATE());
        contentValues.put(STT_TASK_TIME, model.getSTT_TASK_TIME());
        contentValues.put(STT_TASK_IMG, model.getSTT_TASK_IMG());
        int d=    db.update(SST_TABLE_NAME, contentValues, "_id = ? ", new String[] { Integer.toString(model.getSTT_ID()) } );
        Log.d(TAG,""+d);

        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ShortTermTaskModel> getAllCotacts() {
        ArrayList<ShortTermTaskModel> array_list = new ArrayList<ShortTermTaskModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+SST_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ShortTermTaskModel add = new ShortTermTaskModel();
            add.setSTT_ID(res.getInt(res.getColumnIndex(STT_ID)));
            add.setSTT_TASK_NAME(res.getString(res.getColumnIndex(STT_TASK_NAME)));
            add.setSTT_TASK_LOCATION(res.getString(res.getColumnIndex(STT_TASK_LOCATION)));
            add.setSTT_TASK_DESCRIPTION(res.getString(res.getColumnIndex(STT_TASK_DESCRIPTION)));
            add.setSTT_TASK_DATE(res.getString(res.getColumnIndex(STT_TASK_DATE)));
            add.setSTT_TASK_TIME(res.getString(res.getColumnIndex(STT_TASK_TIME)));
            add.setSTT_TASK_IMG(res.getString(res.getColumnIndex(STT_TASK_IMG)));
            array_list.add(add);
            res.moveToNext();
        }
        return array_list;
    }
}