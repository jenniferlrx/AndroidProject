package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imgBtnCar = (ImageButton) findViewById(R.id.imgBtn_car);
        ImageButton imgBtnRecipe = (ImageButton) findViewById(R.id.imgBtn_recipe);
        ImageButton imgBtnCurrency = (ImageButton) findViewById(R.id.imgBtn_currency);
        ImageButton imgBtnNews = (ImageButton) findViewById(R.id.imgBtn_news);

        imgBtnCar.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, ECCSFmain.class));
        });

        imgBtnNews.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, News_Activity_Main.class));
        });

    }
}
