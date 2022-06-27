package com.example.navigationjournal.wishList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationjournal.Models.ShortTermTaskModel;
import com.example.navigationjournal.Models.WishModel;
import com.example.navigationjournal.R;
import com.example.navigationjournal.WishListActivity;
import com.example.navigationjournal.database.DBManager;
import com.example.navigationjournal.shortTermTasks.TaskDisplay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DisplayWishList extends AppCompatActivity {
    private static final String TAG = "DisplayWishListActivity";
    private RecyclerView recyclerWishModel;
    private WishModelListAdapter wishModelListAdapter;
    private List<WishModel> allTasks;
private ImageView btn_SEarch;
private EditText edtSearch;
private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wish_list);
        init();
        btn_SEarch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchData = edtSearch.getText().toString();
                if (searchData.isEmpty()) {
                    Toast.makeText(DisplayWishList.this, "Please enter name correctly", Toast.LENGTH_SHORT).show();
                } else {
                    final DBManager dbManager = new DBManager(DisplayWishList.this);
                    allTasks = dbManager.getSearchDataForWishList(edtSearch.getText().toString());
                    setUpRecycler();
                }


            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DisplayWishList.this, WishListDeleteActivity.class));

            }
        });
    }
    private void init() {
        recyclerWishModel = findViewById(R.id.recyclerTasks);
        edtSearch = findViewById(R.id.edtSearch);
        btn_SEarch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);


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
        allTasks = dbManager.getAllList();
    }
    /*code to set LocationModel data in list*/
    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecycler() {
        if (allTasks != null) {
            if (wishModelListAdapter == null) {
                wishModelListAdapter = new WishModelListAdapter();
                recyclerWishModel.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            }
            wishModelListAdapter.setLocationModelList(allTasks);
            recyclerWishModel.setAdapter(wishModelListAdapter);
            wishModelListAdapter.notifyDataSetChanged();
        }
    }
    private void RemoveItemFromList(int index){
        allTasks.remove(index);
        setUpRecycler();

    }

    class WishModelListAdapter extends RecyclerView.Adapter<WishModelListAdapter.TaskModelViewHolder> {

        List<WishModel> shortTermTaskModels;
        // List<Integer> LocationModelListToSetFavourite = new ArrayList<>();

        public WishModelListAdapter setLocationModelList(List<WishModel> shortTermTaskModels) {
            this.shortTermTaskModels = shortTermTaskModels;
            return this;
        }

        @NonNull
        @Override
        public WishModelListAdapter.TaskModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WishModelListAdapter.TaskModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishist, parent, false));
        }
        // this function to update recycle list and set expired task to history.
        private void deleteTask(WishModel shortTermTaskModel){
            final DBManager dbManager = new DBManager(DisplayWishList.this);
            dbManager.insertSTT_HISTORY(shortTermTaskModel);
            dbManager.deleteSTWishList(shortTermTaskModel.getWish_ID());
            allTasks.remove(shortTermTaskModel);
            setUpRecycler();
        }
           @SuppressLint("NotifyDataSetChanged")
           private void filterTaskList(WishModel shortTermTaskModel){
               List<WishModel> newList = new ArrayList<>();
               List<WishModel> currentList =  shortTermTaskModels;
               for (WishModel channel: currentList){
                   int  channelID = channel.getWish_ID();
                   if (channelID != shortTermTaskModel.getWish_ID()){
                       newList.add(channel);
                   }
               }
               shortTermTaskModels.clear();
               shortTermTaskModels.addAll(newList);
               wishModelListAdapter.notifyDataSetChanged();
           //  notifyDataSetChanged();

        }

        @Override
        public void onBindViewHolder(@NonNull WishModelListAdapter.TaskModelViewHolder holder, int position) {
            try {
                WishModel shortTermTaskModel= shortTermTaskModels.get(position);
                SharedPreferences sh = getSharedPreferences("DeletedItem", Context.MODE_PRIVATE);
            int a = sh.getInt(String.valueOf(shortTermTaskModel.getWish_ID()), 0);
            if(sh!=null && a==1){
              //  filterTaskList(shortTermTaskModel);
            }else {
                holder.cardWishList.setVisibility(View.VISIBLE);
                Log.d(TAG, "Data ID " + shortTermTaskModel.getWish_ID() + " is not deleted!");


                DateFormat df = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.US);
                Date date = df.parse(getCurrentDateTime());
                Date date2 = df.parse(shortTermTaskModel.getWish_DATE());
                if (date != null && date.compareTo(date2) < 0) {
                    holder.txt_taskName.setText(shortTermTaskModel.getWish_NAME());
                    holder.txt_description.setText(shortTermTaskModel.getWish_DESCRIPTION());
                    if (!shortTermTaskModel.getWish_IMG().equals("N/A")) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(shortTermTaskModel.getWish_IMG());
                        holder.locationImg.setImageBitmap(myBitmap);
                    }
                    holder.txt_date.setText(String.valueOf(shortTermTaskModel.getWish_DATE()));

//                Date date2 = dateFormat.parse(String.format(shortTermTaskModel.getSTT_TASK_DATE()));
                    LiveCountDown(date, date2, holder.txt_remainingTime);

                    holder.txt_taskLocation.setText(String.valueOf(shortTermTaskModel.getWish_LOCATION()));
                } else {
                    deleteTask(shortTermTaskModel);
                    Toast.makeText(DisplayWishList.this, "Task " + shortTermTaskModel.getWish_NAME() + " is expired!", Toast.LENGTH_SHORT).show();
                }

                holder.txt_taskName.setText(shortTermTaskModel.getWish_NAME());
                holder.txt_description.setText(shortTermTaskModel.getWish_DESCRIPTION());
                holder.locationImg.setImageResource(R.drawable.map);

                if (!shortTermTaskModel.getWish_IMG().equals("N/A") || !shortTermTaskModel.getWish_IMG().isEmpty()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(shortTermTaskModel.getWish_IMG());
                    holder.locationImg.setImageBitmap(myBitmap);
                }
                holder.txt_date.setText(String.valueOf(shortTermTaskModel.getWish_DATE()));

                holder.txt_taskLocation.setText(String.valueOf(shortTermTaskModel.getWish_LOCATION()));

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
            Log.d(TAG,"Data size id total "+shortTermTaskModels.size());

            return shortTermTaskModels.size();
        }

        public class TaskModelViewHolder extends RecyclerView.ViewHolder {

            private final TextView txt_date,txt_Time,txt_taskName,txt_taskLocation,txt_description,txt_remainingTime;
            private final ImageView locationImg;
            private final CardView cardWishList;

            public TaskModelViewHolder(@NonNull View itemView) {
                super(itemView);
                cardWishList = itemView.findViewById(R.id.cardWishList);
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