package com.example.navigationjournal.location_registrations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.example.navigationjournal.R;
import com.example.navigationjournal.database.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {
    private static final String TAG = "DisplayActivity";

    private RecyclerView recyclerLocationModels;
    private LocationModelListAdapter LocationModelListAdapter;
    private List<LocationModel> allLocations;
    private boolean showFav = false;
    private boolean isEditMode = false;
    private Context context;
    private boolean isSearchMode = false;
    private EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        //setUpLocationModelData();
        context = this;

        //requesting action from components- starting activities
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.containsKey("fav")) {
                    showFav = bundle.getBoolean("fav");
                }
                if (bundle.containsKey("edit_mode")) {
                    isEditMode = bundle.getBoolean("edit_mode");
                }
                if (bundle.containsKey("search_mode")) {
                    isSearchMode = bundle.getBoolean("search_mode");
                }
            }
        }
//        Toast.makeText(context, "Search Name of the location saved!", Toast.LENGTH_LONG).show();


        init();
        //add to favourites
        findViewById(R.id.btnAddFav).setOnClickListener(view -> {
            if (!isSearchMode) {
                if (LocationModelListAdapter != null) {
                    for (Integer integer : LocationModelListAdapter.LocationModelListToSetFavourite) {
                        Log.d(TAG, "onCreate: " + integer);
                        for (LocationModel allLocationModel : allLocations) {
                            if (allLocationModel.getId() == integer)
                                allLocationModel.setFavourite(showFav ? 0 : 1);
                                new DBManager(this).update(allLocationModel.getId(), allLocationModel);
                        }
                    }

                    LocationModelListAdapter.LocationModelListToSetFavourite.clear();
                    setUpLocationModelData();
                }
            } else {
                String searchData = edtSearch.getText().toString(); //Otherwise search ID of location
                if (searchData.isEmpty()) {
                    Toast.makeText(context, "Please enter location", Toast.LENGTH_SHORT).show();
                } else {
                    getLocationModelData(searchData);
                    setUpRecycler();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSearchMode)
            setUpLocationModelData();
    }

    /* Method to get data from database and show in list*/
    private void setUpLocationModelData() {
        getLocationModelData("");
        setUpRecycler();
    }

    /*code to set LocationModel data in list*/
    private void setUpRecycler() {
        if (allLocations != null) {
            if (LocationModelListAdapter == null) {
                LocationModelListAdapter = new LocationModelListAdapter();
                recyclerLocationModels.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            }
            LocationModelListAdapter.setLocationModelList(allLocations);
            recyclerLocationModels.setAdapter(LocationModelListAdapter);
            LocationModelListAdapter.notifyDataSetChanged();
        }
    }

    private void getLocationModelData(String searchText) {
        final DBManager dbManager = new DBManager(this);
        if (showFav)
            allLocations = dbManager.getFavouriteLocations();
        else if (isSearchMode) {
            allLocations = dbManager.getListOfLocations(searchText);
        } else
            allLocations = dbManager.getAllLocation();
    }


    /* Method to convert UI element to variable*/
    private void init() {
        recyclerLocationModels = findViewById(R.id.recyclerMovies);
        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.setVisibility(View.GONE);
        if (showFav) ((Button) findViewById(R.id.btnAddFav)).setText("Remove from Favourite");



        if (isSearchMode) {
            ((Button) findViewById(R.id.btnAddFav)).setText("Search Saved Location");
            edtSearch.setVisibility(View.VISIBLE);
        }
    }

    //The recyler viewholder
    class LocationModelListAdapter extends RecyclerView.Adapter<LocationModelListAdapter.LocationModelViewHolder> {

        List<LocationModel> LocationModelList;
        List<Integer> LocationModelListToSetFavourite = new ArrayList<>();

        public LocationModelListAdapter setLocationModelList(List<LocationModel> LocationModelList) {
            this.LocationModelList = LocationModelList;
            return this;
        }

        @NonNull
        @Override
        public LocationModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LocationModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_location_list, parent, false));
        }
        //part of the recycler view
        @Override
        public void onBindViewHolder(@NonNull LocationModelViewHolder holder, int position) {
            final int adapterPosition = holder.getAdapterPosition();
            LocationModel locationModel = LocationModelList.get(adapterPosition);

            Log.d(TAG, "onBindViewHolder: " + locationModel.toString());
            holder.txtName.setText(locationModel.getLocationName());
            holder.txtStreet.setText(locationModel.getLocation_street());
            try {
                if(locationModel.getLocation_img().equals("N/A")){
                    holder.locationImg.setImageResource(R.drawable.map);
                }else {
                    Bitmap myBitmap = BitmapFactory.decodeFile(locationModel.getLocation_img());
                    holder.locationImg.setImageBitmap(myBitmap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                holder.txtCity.setText(String.valueOf(locationModel.getLocation_city()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.txtDates.setText(locationModel.getAdded_date());
            try {
                holder.txtRating.setText(String.valueOf(locationModel.getLocation_rating()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.txtReview.setText(locationModel.getLocation_review());
            holder.checkbox.setChecked(LocationModelListToSetFavourite.contains(adapterPosition));

            holder.checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
                Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
                if (b) {
                    LocationModelListToSetFavourite.add(locationModel.getId());
                } else {
                    if (LocationModelListToSetFavourite.contains(locationModel.getId()))
                        LocationModelListToSetFavourite.remove(locationModel.getId());
                }
            });

            holder.txtName.setOnClickListener(view -> {
               if (isEditMode) {
                    startActivity(new Intent(context, RegisterActivity.class)
                            .putExtra("edit_mode", true)
                            .putExtra("LocationModel_index", locationModel.getId()));
                }
            });

        }
        //to view all location in the lists
        @Override
        public int getItemCount() {
            return LocationModelList.size();
        }

        public class LocationModelViewHolder extends RecyclerView.ViewHolder {

            private final TextView txtName, txtStreet, txtCity, txtRating, txtDates, txtReview;
            private final CheckBox checkbox;
            private final ImageView locationImg;

            public LocationModelViewHolder(@NonNull View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txtName);
                locationImg = itemView.findViewById(R.id.locationImg);
                txtStreet = itemView.findViewById(R.id.txtStreet);
                txtCity = itemView.findViewById(R.id.txtCity);
                txtRating = itemView.findViewById(R.id.txtRating);
                txtReview = itemView.findViewById(R.id.txtReview);
                txtDates = itemView.findViewById(R.id.txtDates);
                checkbox = itemView.findViewById(R.id.checkbox);
            }
        }
    }


}