package com.example.navigationjournal.shortTermTasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationjournal.Models.LocationModel;
import com.example.navigationjournal.Models.ShortTermTaskModel;
import com.example.navigationjournal.R;
import com.example.navigationjournal.database.DBManager;
import com.example.navigationjournal.location_registrations.DisplayActivity;
import com.example.navigationjournal.location_registrations.RegisterActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TaskDisplay extends AppCompatActivity {
    private static final String TAG = "DisplayTaskActivity";
    private RecyclerView recyclerTasksModel;
    private TaskModelListAdapter taskModelListAdapter;
    private List<ShortTermTaskModel> allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_display);
        init();
    }
    private void init() {
        recyclerTasksModel = findViewById(R.id.recyclerTasks);


    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpLocationModelData();
    }
    /* Method to get data from database and show in list*/
    private void setUpLocationModelData() {
        getLocationModelData();
        setUpRecycler();
    }
    private void getLocationModelData() {
        final DBManager dbManager = new DBManager(this);
        allTasks = dbManager.getAllTask();
    }
    /*code to set LocationModel data in list*/
    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecycler() {
        if (allTasks != null) {
            if (taskModelListAdapter == null) {
                taskModelListAdapter = new TaskModelListAdapter();
                recyclerTasksModel.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            }
            taskModelListAdapter.setLocationModelList(allTasks);
            recyclerTasksModel.setAdapter(taskModelListAdapter);
            taskModelListAdapter.notifyDataSetChanged();
        }
    }

    // this function to update recycle list and set expired task to history.
    private void deleteTask(ShortTermTaskModel shortTermTaskModel){
        final DBManager dbManager = new DBManager(TaskDisplay.this);
        dbManager.insertSTT_HISTORY(shortTermTaskModel);
        dbManager.deleteSTTask(shortTermTaskModel.getSTT_ID());
        allTasks.remove(shortTermTaskModel);
        setUpRecycler();
    }
    class TaskModelListAdapter extends RecyclerView.Adapter<TaskModelListAdapter.TaskModelViewHolder> {

        List<ShortTermTaskModel> shortTermTaskModels;
        // List<Integer> LocationModelListToSetFavourite = new ArrayList<>();

        public TaskModelListAdapter setLocationModelList(List<ShortTermTaskModel> shortTermTaskModels) {
            this.shortTermTaskModels = shortTermTaskModels;
            return this;
        }

        @NonNull
        @Override
        public TaskModelListAdapter.TaskModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskModelListAdapter.TaskModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TaskModelListAdapter.TaskModelViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            ShortTermTaskModel shortTermTaskModel= shortTermTaskModels.get(adapterPosition);

            try {
                DateFormat df = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.US);
                Date date = df.parse(getCurrentDateTime());
                Date date2 = df.parse(shortTermTaskModel.getSTT_TASK_DATE());
                if (date != null && date.compareTo(date2) < 0) {
                    holder.txt_taskName.setText(shortTermTaskModel.getSTT_TASK_NAME());
                    holder.txt_description.setText(shortTermTaskModel.getSTT_TASK_DESCRIPTION());
                    if(shortTermTaskModel.getSTT_TASK_IMG().equals("N/A")){
                        holder.locationImg.setImageResource(R.drawable.map);
                    }else {
                        Bitmap myBitmap = BitmapFactory.decodeFile(shortTermTaskModel.getSTT_TASK_IMG());
                        holder.locationImg.setImageBitmap(myBitmap);
                    }
                    holder.txt_date.setText(String.valueOf(shortTermTaskModel.getSTT_TASK_DATE()));
                    holder.txt_Time.setText(shortTermTaskModel.getSTT_TASK_TIME());

//                Date date2 = dateFormat.parse(String.format(shortTermTaskModel.getSTT_TASK_DATE()));
                    LiveCountDown(date,date2,holder.txt_remainingTime);

                    holder.txt_taskLocation.setText(String.valueOf(shortTermTaskModel.getSTT_TASK_LOCATION()));
                }else{
                    deleteTask(shortTermTaskModel);
                    Toast.makeText(TaskDisplay.this, "Task "+shortTermTaskModel.getSTT_TASK_NAME()+" is expired!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d(TAG," Exception : "+e.getLocalizedMessage());
                e.printStackTrace();
            }

        }
        //this function to show live timing and also show
        @SuppressLint("SimpleDateFormat")
        private void LiveCountDown(Date date, Date date2, TextView txt_remainingTime){
            try {
                long timeInMilliseconds = date2.getTime() - date.getTime();
                new CountDownTimer(timeInMilliseconds, 1000){
                    public void onTick(long millisUntilFinished){


                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long daysInMilli = hoursInMilli * 24;

                        long elapsedDays = millisUntilFinished / daysInMilli;
                        millisUntilFinished = millisUntilFinished % daysInMilli;

                        long elapsedHours = millisUntilFinished / hoursInMilli;
                        millisUntilFinished = millisUntilFinished % hoursInMilli;

                        long elapsedMinutes = millisUntilFinished / minutesInMilli;
                        millisUntilFinished = millisUntilFinished % minutesInMilli;

                        long elapsedSeconds = millisUntilFinished / secondsInMilli;

                        txt_remainingTime.setText(elapsedDays + "days " + elapsedHours + "h " + elapsedMinutes + "min " + elapsedSeconds+"sec");
                    }
                    public  void onFinish(){
                        txt_remainingTime.setText("Task Has been Expired!");

                    }
                }.start();


            } catch (Exception e) {
                Log.d(TAG," Exception : "+e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        @SuppressLint("SimpleDateFormat")
        private String getCurrentDateTime(){
            DateFormat df = new SimpleDateFormat("MMM dd HH:mm:ss yyyy");
            return df.format(Calendar.getInstance().getTime());
        }
        @Override
        public int getItemCount() {
            return shortTermTaskModels.size();
        }

        public class TaskModelViewHolder extends RecyclerView.ViewHolder {

            private final TextView txt_date,txt_Time,txt_taskName,txt_taskLocation,txt_description,txt_remainingTime;
            private final ImageView locationImg;

            public TaskModelViewHolder(@NonNull View itemView) {
                super(itemView);
                locationImg = itemView.findViewById(R.id.taskImg);
                txt_date = itemView.findViewById(R.id.txtSelectedDate);
                txt_Time = itemView.findViewById(R.id.txtSelectedTime);
                txt_taskName = itemView.findViewById(R.id.txtName);
                txt_taskLocation = itemView.findViewById(R.id.txtLocationName);
                txt_description = itemView.findViewById(R.id.txtDescription);
                txt_remainingTime = itemView.findViewById(R.id.txtTimeRemain);
            }
        }



    }


}
