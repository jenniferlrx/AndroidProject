package com.example.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsDetails extends AppCompatActivity {
    private Button goToUrlButton;
    private Button addToFavouritesButton;
    private TextView articleDescription;
    private TextView articleTitle;
    private ImageView articleImage;
    private TextView articleUrl;
    private Intent lastIntent;
    private NewsArticleObject articleObject;

    /**
     * setup

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        articleTitle = findViewById(R.id.details_title);
        articleDescription = findViewById(R.id.details_description);
        articleImage = findViewById(R.id.details_image);
        articleUrl = findViewById(R.id.url_textview);
        lastIntent = getIntent();
        articleObject = (NewsArticleObject)lastIntent.getSerializableExtra("articleObject");
        /**
         * grab article object from intent
         * display article object information
         */
        articleTitle.setText(articleObject.getTitle());
        articleDescription.setText(articleObject.getDescription());
        Picasso.get().load(articleObject.getImageUrl()).into(articleImage);
        articleUrl.setText(articleObject.getArticleUrl());



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
