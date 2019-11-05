package com.example.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class News_Activity_NewsDetail extends AppCompatActivity {

    String inputPosition; //position for clicks
    News_DBHelper dbHelper; //db helper
    SQLiteDatabase db; //database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Button saveButton = findViewById(R.id.save_button_detail);

        //intent from searches
        Intent previousPage = getIntent();
        inputPosition = previousPage.getStringExtra("inputPosition");

        //web view
        WebView webView = findViewById(R.id.webview_hd);
        webView.loadUrl(inputPosition);

        //save button to write to db
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                dbHelper = new News_DBHelper(News_Activity_NewsDetail.this);
                db = dbHelper.getWritableDatabase();
                News_Activity_NewsDetail.this.saveArticle(db, inputPosition);
            }
        });
    }

    /**
     * save the article to db
     * @param db
     * @param inputPosition
     */
    private void saveArticle(SQLiteDatabase db, String inputPosition){
        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string word content in the word_content column:
        newRowValues.put(dbHelper.COL_TITLE, inputPosition);
        //insert into the database:
        long newId = db.insert(dbHelper.TABLE_NAME, null, newRowValues);
        String saveMsg = "";
        if(newId>0){
            saveMsg = "Article saved successfully!.";
        }else{
            saveMsg = "Error saving.";
        }
        // show toast bar if saved successful
        Toast.makeText(News_Activity_NewsDetail.this, saveMsg, Toast.LENGTH_LONG).show();
    }

}
