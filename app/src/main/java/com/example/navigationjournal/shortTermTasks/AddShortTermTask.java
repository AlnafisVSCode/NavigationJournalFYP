package com.example.navigationjournal.shortTermTasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.navigationjournal.Models.LocationModel;
import com.example.navigationjournal.Models.ShortTermTaskModel;
import com.example.navigationjournal.R;
import com.example.navigationjournal.database.DBManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddShortTermTask extends AppCompatActivity {
    private static final String TAG = "AddShortTermTaskActivity";
    final Calendar myCalendar= Calendar.getInstance();
    TextView edt_date,edt_Time;
    EditText edt_taskName,edt_taskLocation,edt_description;
    ImageView ImgPlaceImage;
    //Picture
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    public File photoFile;
    private Button  btnSave;
    private ShortTermTaskModel shortTermTaskModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_short_term_task);
        edt_date = findViewById(R.id.edtTaskDate);
        edt_Time = findViewById(R.id.edt_Time);
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
                new DatePickerDialog(AddShortTermTask.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validFields()) {
                    try {
                        shortTermTaskModel = new ShortTermTaskModel();
                        final DBManager dbManager = new DBManager(AddShortTermTask.this);
//                        if (isEditMode) {
//                            if(photoFile!=null){  shortTermTaskModel.setLocation_img(photoFile.getAbsolutePath());
//                                locationModel.setLocationName(edtName.getText().toString());
//                                locationModel.setLocation_rating(edtRating.getNumStars());
//                                locationModel.setLocation_city(edtCity.getText().toString());
//                                locationModel.setLocation_street(edtStree.getText().toString());
//                                locationModel.setAdded_date(edtDate.getText().toString());
//                                locationModel.setLocation_review(edtReview.getText().toString());
//
//                                dbManager.update(shortTermTaskModel.getId(),
//                                        shortTermTaskModel);
//                            }else {
//                                Toast.makeText(AddShortTermTask.this, "Please Select Image Also!", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
                        if(photoFile!=null){
                            shortTermTaskModel.setSTT_TASK_IMG(photoFile.getAbsolutePath());
                        }else {
                            shortTermTaskModel.setSTT_TASK_IMG("N/A");
                        }

                        String name = edt_taskName.getText().toString();
                        String locationName = edt_taskLocation.getText().toString();
                        String description = edt_description.getText().toString();
                        String time = edt_Time.getText().toString();
                        String date = edt_date.getText().toString();
                        shortTermTaskModel.setSTT_TASK_NAME(name);
                        shortTermTaskModel.setSTT_TASK_DATE(date);
                        shortTermTaskModel.setSTT_TASK_TIME(time);
                        shortTermTaskModel.setSTT_TASK_DESCRIPTION(description);
                        shortTermTaskModel.setSTT_TASK_LOCATION(locationName);
                        dbManager.insertSTT(shortTermTaskModel);
//                        }
                        Toast.makeText(AddShortTermTask.this, "Saved!!!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG,e.toString());
                        Toast.makeText(AddShortTermTask.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                }            }
        });
        edt_Time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddShortTermTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_Time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

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

            Uri outputFileUri = Uri.fromFile(photoFile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
        });
    }
    private boolean validFields() {
        String name = edt_taskName.getText().toString();
        String locationName = edt_taskLocation.getText().toString();
        String description = edt_description.getText().toString();
        String time = edt_Time.getText().toString();
        String date = edt_date.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name must not be empty", Toast.LENGTH_SHORT).show();
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

        if (time.isEmpty()) {
            Toast.makeText(this, "time must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void updateLabel(){
        String myFormat="MMM dd HH:mm:ss yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edt_date.setText(dateFormat.format(myCalendar.getTime()));
    }


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