package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ECCSFdetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsf_detail);

        //get data from the list
        Intent info = getIntent();
        String title = info.getStringExtra("title");
        String latitude = info.getStringExtra("latitude");
        String longitude = info.getStringExtra("longitude");
        String address = info.getStringExtra("address");
        String phoneNo = info.getStringExtra("phoneNo");


        boolean fav = info.getBooleanExtra("fav",false);

        //get reference of views
        TextView titleView =  findViewById(R.id.car_text_title);
        TextView latitudeView = findViewById(R.id.text_latitude);
        TextView longitudeView = findViewById(R.id.text_longitude);
        TextView phoneNoView = findViewById(R.id.text_phoneNo);
        TextView addressView = findViewById(R.id.text_address);

        //set text into views
        titleView.setText("Title: " +title);
        latitudeView.setText("Latitude: " +latitude);
        longitudeView.setText("Longitude: " +longitude);
        addressView.setText("Address: " +address);
        if(phoneNo.equals("null") || phoneNo.equals("")){
            phoneNoView.setText("Phone Number: Not Available");
        }else phoneNoView.setText("Phone Number: " +phoneNo);


        Button addFavBtn = findViewById(R.id.car_btn_add_fav);
        Button delFavBtn = findViewById(R.id.car_btn_del_fav);
        Button backBtn = findViewById(R.id.car_btn_back);
        

        if(fav){
            addFavBtn.setVisibility(View.GONE);
        }else{
            delFavBtn.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(v-> finish());

        addFavBtn.setOnClickListener(v->{
            Intent addToFav = new Intent();
            addToFav.putExtra("longitude",longitude);
            addToFav.putExtra("latitude",latitude);
            addToFav.putExtra("address",address);
            addToFav.putExtra("phoneNo",phoneNo);
            addToFav.putExtra("title",title);
            addToFav.putExtra("addToFav",true);
            setResult(2,addToFav);
            finish();
        });

        delFavBtn.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.ECCSF_delete_confirm)
                    .setPositiveButton(R.string.ECCSF_yes, (dialog, id)-> {
                        Intent deleteFromFav = new Intent();
                        deleteFromFav.putExtra("longitude",longitude);
                        deleteFromFav.putExtra("latitude",latitude);
                        deleteFromFav.putExtra("deleteFromFav",true);
                        setResult(3,deleteFromFav);
                        finish();
                    })
                    .setNegativeButton(R.string.ECCSF_cancel,(dialog, id)-> {});
            builder.create().show();
        });
    }

}
