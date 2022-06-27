package com.example.navigationjournal.wishList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigationjournal.Models.ShortTermTaskModel;
import com.example.navigationjournal.Models.WishModel;
import com.example.navigationjournal.R;
import com.example.navigationjournal.database.DBManager;
import com.example.navigationjournal.shortTermTasks.HistoryTasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DeletedWishList extends AppCompatActivity {
    private static final String TAG = "DisplayHistoryTaskActivity";
    private RecyclerView recyclerTasksModel;
    private TaskModelListAdapter taskModelListAdapter;
    private List<WishModel> allTasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_wish_list);
        init();
    }
    private void init() {
        recyclerTasksModel = findViewById(R.id.recyclerTasks);
    }
    /*code to set LocationModel data in list*/
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
    class TaskModelListAdapter extends RecyclerView.Adapter<TaskModelListAdapter.TaskModelViewHolder> {

        List<WishModel>  shortTermTaskModels;
//        List<Integer> LocationModelListToSetFavourite = new ArrayList<>();

        public TaskModelListAdapter setLocationModelList(List<WishModel> shortTermTaskModels) {
            this.shortTermTaskModels = shortTermTaskModels;
            return  this;
        }

        @NonNull
        @Override
        public TaskModelListAdapter.TaskModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskModelListAdapter.TaskModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_history_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TaskModelListAdapter.TaskModelViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            WishModel shortTermTaskModel= shortTermTaskModels.get(adapterPosition);

            try {
                holder.txt_taskName.setText(shortTermTaskModel.getWish_NAME());
                holder.txt_description.setText(shortTermTaskModel.getWish_DESCRIPTION());
                if(shortTermTaskModel.getWish_IMG().equals("N/A")){
                    holder.locationImg.setImageResource(R.drawable.map);
                }else {
                    Bitmap myBitmap = BitmapFactory.decodeFile(shortTermTaskModel.getWish_IMG());
                    holder.locationImg.setImageBitmap(myBitmap);
                }
                holder.txt_date.setText(String.valueOf(shortTermTaskModel.getWish_DATE()));
                holder.txt_Time.setVisibility(View.GONE);
                holder.txt_taskLocation.setText(String.valueOf(shortTermTaskModel.getWish_LOCATION()));


            } catch (Exception e) {
                e.printStackTrace();
            }

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
        allTasks = dbManager.getAllWishHistory();
    }
}