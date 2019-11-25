package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The class defines the detail page which shows a station's map, location, title, address, phone number
 *
 * @author jennifer yuan
 * @version 1.0
 */
public class ECCSFdetail extends AppCompatActivity {
    /**
     * initialize the page
     *
     * @param savedInstanceState
     */
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

        boolean fav = info.getBooleanExtra("fav", false);

        //get reference of views
        TextView titleView = findViewById(R.id.car_text_title);
        TextView latitudeView = findViewById(R.id.text_latitude);
        TextView longitudeView = findViewById(R.id.text_longitude);
        TextView phoneNoView = findViewById(R.id.text_phoneNo);
        TextView addressView = findViewById(R.id.text_address);

        //set text into views
        titleView.setText("Title: " + title);
        latitudeView.setText("Latitude: " + latitude);
        longitudeView.setText("Longitude: " + longitude);
        addressView.setText("Address: " + address);

        if (phoneNo.equals("null") || phoneNo.equals("")) {
            phoneNoView.setText("Phone Number: Not Available");
        } else phoneNoView.setText("Phone Number: " + phoneNo);

        // buttons
        Button addFavBtn = findViewById(R.id.car_btn_add_fav);
        Button delFavBtn = findViewById(R.id.car_btn_del_fav);
        Button backBtn = findViewById(R.id.car_btn_back);
        Button mapBtn = findViewById(R.id.car_btn_map);

        if (fav) {
            addFavBtn.setVisibility(View.GONE);
        } else {
            delFavBtn.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(v -> finish());

        addFavBtn.setOnClickListener(v -> {
            Intent addToFav = new Intent();
            addToFav.putExtra("longitude", longitude);
            addToFav.putExtra("latitude", latitude);
            addToFav.putExtra("address", address);
            addToFav.putExtra("phoneNo", phoneNo);
            addToFav.putExtra("title", title);
            addToFav.putExtra("addToFav", true);
            setResult(2, addToFav);
            finish();
        });

        delFavBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.ECCSF_delete_confirm)
                    .setPositiveButton(R.string.ECCSF_yes, (dialog, id) -> {
                        Intent deleteFromFav = new Intent();
                        deleteFromFav.putExtra("longitude", longitude);
                        deleteFromFav.putExtra("latitude", latitude);
                        deleteFromFav.putExtra("deleteFromFav", true);
                        setResult(3, deleteFromFav);
                        finish();
                    })
                    .setNegativeButton(R.string.ECCSF_cancel, (dialog, id) -> {
                    });
            builder.create().show();
        });

        mapBtn.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+longitude+"?z=15");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);

        });
    }

}
