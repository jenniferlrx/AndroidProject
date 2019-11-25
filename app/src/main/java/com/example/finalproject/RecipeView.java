package com.example.finalproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeView extends AppCompatActivity {
    private TextView txtViewTitle;
    private TextView txtViewSourceURL;
    private TextView txtViewImage_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_row);

        txtViewTitle = (TextView) findViewById(R.id.recipe_txtViewTitle);
        txtViewSourceURL= (TextView) findViewById(R.id.recipe_txtViewSourceURL);
        txtViewImage_url= (TextView) findViewById(R.id.recipe_txtViewImage_url);
    }
}
