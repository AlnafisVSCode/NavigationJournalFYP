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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationjournal.Models.ShortTermTaskModel;
import com.example.navigationjournal.Models.WishModel;
import com.example.navigationjournal.R;
import com.example.navigationjournal.database.DBManager;
import com.example.navigationjournal.shortTermTasks.TaskDisplay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WishListDeleteActivity extends AppCompatActivity {
    private static final String TAG = "DisplayWishListDeleteActivity";
    private RecyclerView recyclerWishModel;
    private WishModelListAdapter wishModelListAdapter;
    private List<WishModel> allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_delete);
        init();

    }
    private void init() {
        recyclerWishModel = findViewById(R.id.recyclerTasks);

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

    // this function to update recycle list and set expired task to history.
    private void deleteTask(int ID){
        if(ID>=0){

            SharedPreferences sharedpreference = getSharedPreferences("DeletedItem", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreference.edit();
            editor.putInt(String.valueOf(allTasks.get(ID).getWish_ID()), 1);
            editor.apply();


            allTasks.remove(ID);
            setUpRecycler();
            startActivity(new Intent(WishListDeleteActivity .this, DisplayWishList.class));
            finish();

        }else {
            Toast.makeText(this, "Please Select Wish from list to delete", Toast.LENGTH_SHORT).show();
        }

    }// this function to update recycle list and set expired task to history.
    private void deleteTask(int ID,WishModel model){
        if(ID>=0){
            final DBManager dbManager = new DBManager(this);
            int returnedValue = dbManager.updateWishList(model.getWish_ID(),model);
            if(returnedValue>0){
                allTasks.remove(ID);
                setUpRecycler();
                startActivity(new Intent(WishListDeleteActivity .this, DisplayWishList.class));
                    finish();
            }


        }else {
            Toast.makeText(this, "Please Select Wish from list to delete", Toast.LENGTH_SHORT).show();
        }

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
    class WishModelListAdapter extends RecyclerView.Adapter<WishModelListAdapter.TaskModelViewHolder> {

        List<WishModel> shortTermTaskModels;

        public WishModelListAdapter setLocationModelList(List<WishModel> shortTermTaskModels) {
            this.shortTermTaskModels = shortTermTaskModels;
            return this;
        }

        @NonNull
        @Override
        public WishModelListAdapter.TaskModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WishModelListAdapter.TaskModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishist, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull WishModelListAdapter.TaskModelViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                final int adapterPosition = holder.getAdapterPosition();
            WishModel shortTermTaskModel= shortTermTaskModels.get(position);
            SharedPreferences sh = getSharedPreferences("DeletedItem", Context.MODE_PRIVATE);
                int a = sh.getInt(String.valueOf(shortTermTaskModel.getWish_ID()), 0);
                if(sh!=null && a==1){
                holder.cardWishList.setVisibility(View.GONE);

            }
            else {
                    holder.cardWishList.setVisibility(View.VISIBLE);

                holder.txt_remainingTime.setVisibility(View.VISIBLE);


                    holder.txt_taskName.setText(shortTermTaskModel.getWish_NAME());
                    holder.txt_description.setText(shortTermTaskModel.getWish_DESCRIPTION());
                    if(!shortTermTaskModel.getWish_IMG().equals("N/A")){
                        Bitmap myBitmap = BitmapFactory.decodeFile(shortTermTaskModel.getWish_IMG());
                        holder.locationImg.setImageBitmap(myBitmap);
                    }
                    holder.txt_date.setText(String.valueOf(shortTermTaskModel.getWish_DATE()));

//
                    holder.txt_taskLocation.setText(String.valueOf(shortTermTaskModel.getWish_LOCATION()));


                    holder.txt_taskName.setText(shortTermTaskModel.getWish_NAME());
                    holder.txt_description.setText(shortTermTaskModel.getWish_DESCRIPTION());
                    if(shortTermTaskModel.getWish_IMG().equals("N/A")){
                        holder.locationImg.setImageResource(R.drawable.map);
                    }else {
                        Bitmap myBitmap = BitmapFactory.decodeFile(shortTermTaskModel.getWish_IMG());
                        holder.locationImg.setImageBitmap(myBitmap);
                    }
                    holder.txt_date.setText(String.valueOf(shortTermTaskModel.getWish_DATE()));

                    holder.txt_taskLocation.setText(String.valueOf(shortTermTaskModel.getWish_LOCATION()));
                    holder.checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
                        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");

                    });
                    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            shortTermTaskModel.setIsDeleted(1);
                            deleteTask(position);
                        }
                    });
                }    } catch (Exception e) {
                    Log.d(TAG," Exception : "+e.getLocalizedMessage());
                    e.printStackTrace();
                }


        }

        @Override
        public int getItemCount() {
            return shortTermTaskModels.size();
        }

        public class TaskModelViewHolder extends RecyclerView.ViewHolder {

            private final TextView txt_date,txt_Time,txt_taskName,txt_taskLocation,txt_description,txt_remainingTime;
            private  ImageView locationImg,btnDelete;
            private final CheckBox checkbox;
            private final CardView cardWishList;

            public TaskModelViewHolder(@NonNull View itemView) {
                super(itemView);
                cardWishList = itemView.findViewById(R.id.cardWishList);
                locationImg = itemView.findViewById(R.id.taskImg);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                txt_date = itemView.findViewById(R.id.txtSelectedDate);
                txt_Time = itemView.findViewById(R.id.txtSelectedTime);
                txt_taskName = itemView.findViewById(R.id.txtName);
                txt_taskLocation = itemView.findViewById(R.id.txtLocationName);
                txt_description = itemView.findViewById(R.id.txtDescription);
                txt_remainingTime = itemView.findViewById(R.id.txtTimeRemain);
                checkbox = itemView.findViewById(R.id.checkbox);

            }
        }



    }


}