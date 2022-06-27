package com.example.navigationjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.navigationjournal.Models.LocationModel;
import com.example.navigationjournal.database.DBManager;
import com.example.navigationjournal.location_registrations.DisplayActivity;
import com.example.navigationjournal.location_registrations.RegisterActivity;

import java.util.List;

public class RegisterLocation extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);
        findViewById(R.id.btnRegister).setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        findViewById(R.id.btnDisplay).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class));
        });

        findViewById(R.id.btnFavourite).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class).putExtra("fav", true));
        });

        findViewById(R.id.btnEdit).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class).putExtra("edit_mode", true));
        });
        findViewById(R.id.btnSearch).setOnClickListener(view -> {
            startActivity(new Intent(this, DisplayActivity.class).putExtra("search_mode", true));
        });

        final List<LocationModel> w = new DBManager(this).getListOfLocations("w");
        for (LocationModel h : w) {
            Log.d(TAG, "onCreate: " + h);
        }
    }
}