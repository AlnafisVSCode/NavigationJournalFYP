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

import com.example.navigationjournal.shortTermTasks.AddShortTermTask;
import com.example.navigationjournal.shortTermTasks.HistoryTasks;
import com.example.navigationjournal.shortTermTasks.TaskDisplay;
import com.example.navigationjournal.wishList.AddWishList;
import com.example.navigationjournal.wishList.DeletedWishList;
import com.example.navigationjournal.wishList.DisplayWishList;

public class WishListActivity extends AppCompatActivity {
    Button AddNew , DisplayTask, DisplayHistoryTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        //this will get permission from user
        getPermissions();

        AddNew = findViewById(R.id.AddNewTask);
        DisplayTask = findViewById(R.id.DisplayTask);
        DisplayHistoryTask = findViewById(R.id.DisplayHistoryTask);
        AddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishListActivity.this, AddWishList.class).putExtra("addTask", true));
            }
        });
        DisplayTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishListActivity.this, DisplayWishList.class));

            }
        });

        DisplayHistoryTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishListActivity.this, DeletedWishList.class));

            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WishListActivity.this, "Permission was granted!", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(WishListActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void getPermissions() {
        //to get permission from user
        ActivityCompat.requestPermissions(WishListActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,},
                1);
    }
}