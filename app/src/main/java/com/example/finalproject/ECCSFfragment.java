package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ECCSFfragment extends Fragment {
    private boolean isTablet;
    private Bundle dataFromActivity;
    private String latitude;
    private String longitude;
    private String address;
    private String title;
    private String phoneNo;
    private boolean fav;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        latitude = dataFromActivity.getString("latitude");
        longitude = dataFromActivity.getString("longitude");
        title = dataFromActivity.getString("title");
        address = dataFromActivity.getString("address");
        phoneNo = dataFromActivity.getString("phoneNo");
        fav = dataFromActivity.getBoolean("fav");

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.activity_eccsf_detail, container, false);

        //get reference of views
        TextView titleView = result.findViewById(R.id.car_text_title);
        TextView latitudeView = result.findViewById(R.id.text_latitude);
        TextView longitudeView = result.findViewById(R.id.text_longitude);
        TextView phoneNoView = result.findViewById(R.id.text_phoneNo);
        TextView addressView = result.findViewById(R.id.text_address);

        //set text into views
        titleView.setText("Title: " + title);
        latitudeView.setText("Latitude: " + latitude);
        longitudeView.setText("Longitude: " + longitude);
        addressView.setText("Address: " + address);

        if (phoneNo.equals("null") || phoneNo.equals("")) {
            phoneNoView.setText("Phone Number: Not Available");
        } else phoneNoView.setText("Phone Number: " + phoneNo);


        // buttons
        Button addFavBtn = result.findViewById(R.id.car_btn_add_fav);
        Button delFavBtn = result.findViewById(R.id.car_btn_del_fav);
        Button backBtn = result.findViewById(R.id.car_btn_back);
        Button mapBtn = result.findViewById(R.id.car_btn_map);

        if (fav) {
            addFavBtn.setVisibility(View.GONE);
        } else {
            delFavBtn.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(v -> {

            if (isTablet) {
                ECCSFmain parent = (ECCSFmain) getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            } else {
                ECCSFdetail parent = (ECCSFdetail) getActivity();
                parent.finish();
            }
        });

        addFavBtn.setOnClickListener(v -> {
            if (isTablet) {
                ECCSFmain parent = (ECCSFmain) getActivity();
                parent.addStation(title, latitude, longitude, address, phoneNo);
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            } else {
                ECCSFdetail parent = (ECCSFdetail) getActivity();
                Intent addToFav = new Intent();
                addToFav.putExtra("longitude", longitude);
                addToFav.putExtra("latitude", latitude);
                addToFav.putExtra("address", address);
                addToFav.putExtra("phoneNo", phoneNo);
                addToFav.putExtra("title", title);
                addToFav.putExtra("addToFav", true);
                parent.setResult(2, addToFav);
                parent.finish();
            }

        });

        delFavBtn.setOnClickListener(v -> {
            if (isTablet) {
                ECCSFmain parent = (ECCSFmain) getActivity();
                parent.deleteStation(latitude, longitude);
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();

            } else {
                ECCSFdetail parent = (ECCSFdetail) getActivity();
                AlertDialog.Builder builder = new AlertDialog.Builder(parent);
                builder.setMessage(R.string.ECCSF_delete_confirm)
                        .setPositiveButton(R.string.ECCSF_yes, (dialog, id) -> {
                            Intent deleteFromFav = new Intent();
                            deleteFromFav.putExtra("longitude", longitude);
                            deleteFromFav.putExtra("latitude", latitude);
                            deleteFromFav.putExtra("deleteFromFav", true);
                            parent.setResult(3, deleteFromFav);
                            parent.finish();
                        })
                        .setNegativeButton(R.string.ECCSF_cancel, (dialog, id) -> {
                        });
                builder.create().show();
            }
        });

        mapBtn.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=17");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
        return result;

    }
}
