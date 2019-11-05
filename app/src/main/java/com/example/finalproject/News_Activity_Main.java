package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class News_Activity_Main extends AppCompatActivity {
    SharedPreferences sp; //edit search shared prefs
    TextView editSearch; //edit search view

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);

        //search button
        Button searchBtn = findViewById(R.id.search_btn);
        editSearch = findViewById(R.id.edit_search);

        Button saveBtn = findViewById(R.id.save_btn_hd);

        //edit tex shared preferences
        sp = getSharedPreferences("searchedArticle", Context.MODE_PRIVATE);
        String savedString = sp.getString("savedSearch", "");
        editSearch.setText(savedString);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                Intent nextPage = new Intent(News_Activity_Main.this, News_Activity_Search_Results.class);
                nextPage.putExtra("searchedArticle", editSearch.getText().toString());
                startActivity(nextPage);
                //startActivityForResult(nextPage, 30);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                Intent nextPage = new Intent(News_Activity_Main.this, News_Activity_Saved_Articles.class);
                startActivity(nextPage);
            }

        });


    }




    /**
     * shared prefs commit
     */
    @Override
    protected void onPause(){
        super.onPause();
        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "editSearch"
        String whatWasTyped = editSearch.getText().toString();
        // xml tag name is editSearch
        editor.putString("savedSearch", whatWasTyped);

        //write it to disk:
        editor.commit();
    }
}
