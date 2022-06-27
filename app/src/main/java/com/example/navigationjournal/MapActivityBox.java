package com.example.navigationjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.navigationjournal.navigation.NavigationLauncherActivity;
import com.example.navigationjournal.wishList.AddWishList;
import com.google.firebase.auth.FirebaseAuth;

public class MapActivityBox extends AppCompatActivity {
    private Button logout;
    private CardView RegisterLocation;
    private CardView WishList;
    private CardView shortTermTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_box);

        logout = (Button) findViewById(R.id.signOut);
        RegisterLocation = (CardView) findViewById(R.id.linearRegisterLocation);
        shortTermTask = (CardView) findViewById(R.id.shortTermTask);
        WishList = (CardView) findViewById(R.id.cardWishList);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapActivityBox.this, MainActivity.class)); //This would log out the user then redirect mapActivty to mainactivity
            }
        });
        RegisterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapActivityBox.this, RegisterLocation.class)); //This would log out the user then redirect mapActivty to register location
            }
        });
        shortTermTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapActivityBox.this, ShortTermTaskActivity.class)); //This would log out the user then redirect mapActivty to short term task page
            }
        }); WishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapActivityBox.this, WishListActivity.class)); //This would log out the user then redirect mapActivty to wishlist page
            }
        });


    }

    public void mapsOpen(View view) {
        Intent i = new Intent(this, NavigationLauncherActivity.class);
        startActivity(i);
    }
}