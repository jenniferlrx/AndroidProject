package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ECCSFdetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsf_detail);

        Intent info = getIntent();
        String title = info.getStringExtra("title");
        String latitude = info.getStringExtra("latitude");
        String longtitude = info.getStringExtra("longtitude");
        String address = info.getStringExtra("address");
        String phoneNo = info.getStringExtra("phoneNo");

        TextView titleView = (TextView) findViewById(R.id.text_title);
        TextView latitudeView = (TextView) findViewById(R.id.text_latitude);
        TextView longtitudeView = (TextView) findViewById(R.id.text_longtitude);
        TextView phoneNoView = (TextView) findViewById(R.id.text_phoneNo);
        TextView addressView = (TextView) findViewById(R.id.text_address);

        titleView.setText(title);
        latitudeView.setText(latitude);
        longtitudeView.setText(longtitude);
        addressView.setText(address);
        phoneNoView.setText(phoneNo);


    }
}
