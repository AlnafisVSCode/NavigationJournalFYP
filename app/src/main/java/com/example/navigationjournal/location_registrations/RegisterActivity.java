package com.example.navigationjournal.location_registrations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.navigationjournal.Models.LocationModel;
import com.example.navigationjournal.R;
import com.example.navigationjournal.RegisterLocation;
import com.example.navigationjournal.database.DBManager;

import java.io.File;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private Context context;
    private EditText edtName, edtCity, edtStree, edtDate, edtReview, edtRating;
    private boolean isEditMode = false;
    private LinearLayout linearRating;
    private int locationIndex;
    private LocationModel locationModel;
    private ImageView imgStar1, imgStar2, imgStar3, imgStar4, imgStar5, imgStar6, imgStar7, imgStar8, imgStar9, imgStar10;
    private ImageView ImgPlaceImage;
    private int rating;
    private CheckBox checkBox;

    //Picture
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    public File photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        //this will get permission from user
        getPermissions();


        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey("edit_mode")) {
                    isEditMode = true;
                    if (getIntent().getExtras().containsKey("LocationModel_index")) {
                        locationIndex = getIntent().getExtras().getInt("LocationModel_index");
                    }
                }
            }
        }

        initUI();

        updateData();
        setClicks();

        findViewById(R.id.btnSave).setOnClickListener(view -> {
            if (validFields()) {
                /* Code to insert location in database*/
                try {
                    locationModel = new LocationModel();
                    final DBManager dbManager = new DBManager(context);
                    if (isEditMode) {
                        if(photoFile!=null){  locationModel.setLocation_img(photoFile.getAbsolutePath());
                        locationModel.setLocationName(edtName.getText().toString());
                        locationModel.setLocation_rating(rating);
                        locationModel.setLocation_city(edtCity.getText().toString());
                        locationModel.setLocation_street(edtStree.getText().toString());
                        locationModel.setAdded_date(edtDate.getText().toString());
                        locationModel.setLocation_review(edtReview.getText().toString());

                        dbManager.update(locationModel.getId(),
                                locationModel);
                        }else {
                            Toast.makeText(context, "Please Select Image Also!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(edtCity.getText().toString().isEmpty()){
                            Toast.makeText(context, "Please Enter City Name!", Toast.LENGTH_SHORT).show();
                            return;
                        }if(edtName.getText().toString().isEmpty()){
                            Toast.makeText(context, "Please Enter Location Name!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(edtStree.getText().toString().isEmpty()){
                            Toast.makeText(context, "Please Enter Street Name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(edtDate.getText().toString().isEmpty()){
                            Toast.makeText(context, "Please Select Date Correctly!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(photoFile!=null){
                            locationModel.setLocation_img(photoFile.getAbsolutePath());
                        }else {
                            locationModel.setLocation_img("N/A");
                            Toast.makeText(context, "Please add an Image!", Toast.LENGTH_SHORT).show();
                        }
                        locationModel.setLocationName(edtName.getText().toString());
                        locationModel.setLocation_rating(Integer.parseInt(edtRating.getText().toString()));
                        locationModel.setLocation_city(edtCity.getText().toString());
                        locationModel.setLocation_street(edtStree.getText().toString());
                        locationModel.setAdded_date(edtDate.getText().toString());
                        locationModel.setLocation_review(edtReview.getText().toString());
                        dbManager.insert(locationModel);
                    }
                    Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Error On Insert",e.toString());
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(RegisterActivity.this, "Permission was granted!", Toast.LENGTH_SHORT).show();

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(RegisterActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /*Click methods of UI elements*/
    private void setClicks() {
        imgStar1.setOnClickListener(view -> setReview(1));
        imgStar2.setOnClickListener(view -> setReview(2));
        imgStar3.setOnClickListener(view -> setReview(3));
        imgStar4.setOnClickListener(view -> setReview(4));
        imgStar5.setOnClickListener(view -> setReview(5));
        imgStar6.setOnClickListener(view -> setReview(6));
        imgStar7.setOnClickListener(view -> setReview(7));
        imgStar8.setOnClickListener(view -> setReview(8));
        imgStar9.setOnClickListener(view -> setReview(9));
        imgStar10.setOnClickListener(view -> setReview(10));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                locationModel.setFavourite(b ? 1 : 0);
            }
        });

    }
    //Update the data once edited
    private void updateData() {
        if (isEditMode) {
            locationModel = new DBManager(this).getLocation(locationIndex);

            edtName.setText(locationModel.getLocationName());
            edtCity.setText(String.valueOf(locationModel.getLocation_city()));
            edtStree.setText(locationModel.getLocation_street());
            edtReview.setText(locationModel.getLocation_review());
            edtDate.setText(locationModel.getAdded_date());
            setReview(locationModel.getLocation_rating());
            checkBox.setChecked(locationModel.isFavourite());
        }
    }
    private void getPermissions() {
        //to get permission from user
        ActivityCompat.requestPermissions(RegisterActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,},
                1);
    }
    //rating of location
    private void setReview(int index) {
        rating = index;
        Log.d(TAG, "setReview: " + rating);
        for (int i = 0; i < linearRating.getChildCount(); i++) {
            View view1 = linearRating.getChildAt(i);
            if (view1 instanceof ImageView) {
                if (i > (index - 1))
                    ((ImageView) view1).setImageResource(R.drawable.ic_baseline_star_24);
                else
                    ((ImageView) view1).setImageResource(R.drawable.ic_baseline_star_y_24);
            }
        }
    }

    /* Convert UI element to variable*/
    private void initUI() {
        edtName = findViewById(R.id.edtName);
        edtCity = findViewById(R.id.edtCity);
        edtStree = findViewById(R.id.edtStreet);
        edtDate = findViewById(R.id.edtDate);
        edtRating = findViewById(R.id.edtRating);
        edtReview = findViewById(R.id.edtReview);
        linearRating = findViewById(R.id.linearRating);

        ImgPlaceImage = findViewById(R.id.ImgPlaceImage);
        imgStar1 = findViewById(R.id.imgStar1);
        imgStar2 = findViewById(R.id.imgStar2);
        imgStar3 = findViewById(R.id.imgStar3);
        imgStar4 = findViewById(R.id.imgStar4);
        imgStar5 = findViewById(R.id.imgStar5);
        imgStar6 = findViewById(R.id.imgStar6);
        imgStar7 = findViewById(R.id.imgStar7);
        imgStar8 = findViewById(R.id.imgStar8);
        imgStar9 = findViewById(R.id.imgStar9);
        imgStar10 = findViewById(R.id.imgStar10);

        checkBox = findViewById(R.id.checkBox);

        if (isEditMode) {
            edtRating.setVisibility(View.GONE);
            linearRating.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
        } else {
            linearRating.setVisibility(View.GONE);
            edtRating.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
        }
        ImgPlaceImage .setOnClickListener(view -> {
            // Here, we are making a folder named picture Folder to store
            // picture taken by the camera using this application.
            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
            File newdir = new File(dir);
            newdir.mkdirs();
            count++;
            String file = dir+count+".jpg";
            photoFile = new File(file);
            try {
                photoFile.createNewFile();
            }
            catch (IOException e)
            {
                Log.d("Error :",e.toString());
            }

            Uri outputFileUri = Uri.fromFile(photoFile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        });
    }

    /*Method to validate fields form UI*/
    private boolean validFields() {
        String name = edtName.getText().toString();
        String street = edtStree.getText().toString();
        String city = edtCity.getText().toString();
        String review = edtReview.getText().toString();
        String rating = edtRating.getText().toString();
        String date = edtDate.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (street.isEmpty()) {
            Toast.makeText(this, "street must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
       if (city.isEmpty()) {
            Toast.makeText(this, "city Name must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "Date must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isEditMode) {
            if (rating.isEmpty()) {
                Toast.makeText(this, "Rating must not be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            if ((Integer.parseInt(rating) > 10 || Integer.parseInt(rating) < 1)) {
                Toast.makeText(this, "Rating must be between 1 - 10", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (review.isEmpty()) {
            Toast.makeText(this, "Review must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //To check image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ImgPlaceImage.setImageBitmap(myBitmap);
        } else {
            Log.d("CameraDemo", "Pic not saved correctly");

        }
    }
}