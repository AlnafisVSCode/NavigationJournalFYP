package com.example.navigationjournal.wishList;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationjournal.Models.ShortTermTaskModel;
import com.example.navigationjournal.Models.WishModel;
import com.example.navigationjournal.R;
import com.example.navigationjournal.database.DBManager;
import com.example.navigationjournal.shortTermTasks.AddShortTermTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddWishList extends AppCompatActivity {
    private static final String TAG = "AddWishListActivity";
    final Calendar myCalendar= Calendar.getInstance();
    TextView edt_date;
    EditText edt_taskName,edt_taskLocation,edt_description;
    ImageView ImgPlaceImage;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    public File photoFile;
    private Button btnSave;
    private WishModel wishModel;
    //view finder
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish_list);
        edt_date = findViewById(R.id.edtTaskDate);
        edt_taskName = findViewById(R.id.edtName);
        edt_taskLocation = findViewById(R.id.edtTaskLocation);
        ImgPlaceImage = findViewById(R.id.ImgPlaceImage);
        edt_description = findViewById(R.id.edtdescription);
        btnSave =  findViewById(R.id.btnSave);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        //this to set date for task and set it to textview
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddWishList.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ImgPlaceImage .setOnClickListener(view -> {
            // Here, we are making a folder named picFolder to store
            // pics taken by the camera using this application.
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

            Uri outputFileUri = Uri.fromFile(photoFile);        //photo location

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validFields()) {
                    try {
                        wishModel = new WishModel();
                        final DBManager dbManager = new DBManager(AddWishList.this);
                        //Check if there is photo
                        wishModel.setWish_IMG("N/A");
                        if(photoFile!=null){
                            wishModel.setWish_IMG(photoFile.getAbsolutePath());
                        }

                        String name = edt_taskName.getText().toString();
                        String locationName = edt_taskLocation.getText().toString();
                        String description = edt_description.getText().toString();
                        String date = edt_date.getText().toString();
                        wishModel.setWish_NAME(name);
                        wishModel.setWish_DATE(date);
                        wishModel.setWish_DESCRIPTION(description);
                        wishModel.setWish_LOCATION(locationName);
                        wishModel.setIsDeleted(0);
                        long rows = dbManager.insertWL(wishModel);
//                        }
                        Toast.makeText(AddWishList.this," Wish Saved!!!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG,e.toString());
                        Toast.makeText(AddWishList.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                }            }
        });
    }
    //To validate the fields
    private boolean validFields() {
        String name = edt_taskName.getText().toString();
        String locationName = edt_taskLocation.getText().toString();
        String description = edt_description.getText().toString();
        String date = edt_date.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (getCurrentDateTime().contains(edt_date.getText().toString()) ){
            Toast.makeText(this, "You  must select next date only", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (locationName.isEmpty()) {
            Toast.makeText(this, "location name must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.isEmpty()) {
            Toast.makeText(this, "description must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (date.isEmpty()) {
            Toast.makeText(this, "date must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
    //Time and date
    private void updateLabel(){
        String myFormat="MMM dd HH:mm:ss yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edt_date.setText(dateFormat.format(myCalendar.getTime()));
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentDateTime(){
        DateFormat df = new SimpleDateFormat("MMM dd HH:mm:ss yyyy");
        return df.format(Calendar.getInstance().getTime());
    }
    //camera photo
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