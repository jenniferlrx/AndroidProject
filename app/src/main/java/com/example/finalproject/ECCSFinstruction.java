package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ECCSFinstruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsfinstruction);

        Button btn = findViewById(R.id.car_btn_instruction_back);
        btn.setOnClickListener(v->finish());
    }
}
