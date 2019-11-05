package com.example.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class News_Activity_Empty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample


    }
}
