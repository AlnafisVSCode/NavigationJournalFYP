package com.example.navigationjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.navigationjournal.location_registrations.DisplayActivity;
import com.example.navigationjournal.location_registrations.RegisterActivity;
import com.example.navigationjournal.shortTermTasks.AddShortTermTask;
import com.example.navigationjournal.shortTermTasks.HistoryTasks;
import com.example.navigationjournal.shortTermTasks.TaskDisplay;

import java.io.File;

public class ShortTermTaskActivity extends AppCompatActivity {
    Button AddNew , DisplayTask, DisplayHistoryTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_term_task);
        //this will get permission from user
        getPermissions();

        AddNew = findViewById(R.id.AddNewTask);
        DisplayTask = findViewById(R.id.DisplayTask);
        DisplayHistoryTask = findViewById(R.id.DisplayHistoryTask);
        AddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShortTermTaskActivity.this, AddShortTermTask.class).putExtra("addTask", true));
            }
        });
        DisplayTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShortTermTaskActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShortTermTaskActivity.this, TaskDisplay.class));

            }
        });

        DisplayHistoryTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShortTermTaskActivity.this, HistoryTasks.class));

            }
        });
    }

    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ShortTermTaskActivity.this, "Permission was granted!", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(ShortTermTaskActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
            // permissions this app might request
        }
    }

    private void getPermissions() {
        //to get permission from user
        ActivityCompat.requestPermissions(ShortTermTaskActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,},
                1);
    }
}