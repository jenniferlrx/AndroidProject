package com.example.finalproject;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class NewsFavourites extends AppCompatActivity {
    private ArrayList<NewsArticleObject> newsArticleFavouriteList;
    private ListView newsArticleFavouritesListView;
    private Button deleteFromFavourites;
    private NewsArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_favourites);
        newsArticleFavouriteList = new ArrayList<>();
        newsArticleFavouritesListView = findViewById(R.id.favourites_list_view);
        deleteFromFavourites = findViewById(R.id.deleteFromFavouritesButton);


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
