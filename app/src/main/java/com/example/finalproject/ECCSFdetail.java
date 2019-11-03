package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ECCSFdetail extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "Detailed info for the Electrical Car Charging Station";
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
        boolean fav = info.getBooleanExtra("fav",false);

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

        Button addFavBtn = (Button) findViewById(R.id.btn_add_fav);
        Button backToMainBtn = (Button) findViewById(R.id.btn_back_to_main);

        addFavBtn.setOnClickListener(v->{
            Intent addToFav = new Intent(ECCSFdetail.this, ECCSFfav.class);
            addToFav.putExtra("longtitude",longtitude);
            addToFav.putExtra("latitude",latitude);
            addToFav.putExtra("address",address);
            addToFav.putExtra("phoneNo",phoneNo);
            addToFav.putExtra("title",title);
            addToFav.putExtra("addToFav",true);

            startActivity(addToFav);
        });

        backToMainBtn.setOnClickListener(v->{
            Intent backToMain = new Intent(ECCSFdetail.this, ECCSFmain.class);
            startActivity(backToMain);

        });
    }
}
