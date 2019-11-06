package com.example.finalproject;

import android.icu.text.CaseMap;

import androidx.appcompat.app.AppCompatActivity;

public class MyRecipe {
    protected long id;
    protected static String URL;
    protected static String TITLE;
    protected static byte[] IMAGE;

    public MyRecipe(String title, String URL, byte[] IMAHE){
        this(title, URL, IMAGE, 0);
    }

    public MyRecipe(String title, String URL, byte[] IMAHE, long id){
        this.TITLE = title;
        this.URL = URL;
        this.IMAGE = IMAGE;
        this.id = id;
    }


}
