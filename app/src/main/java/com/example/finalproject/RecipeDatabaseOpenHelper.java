package com.example.finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;

public class RecipeDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RecipeDatabaseFile";
    public static final int VERSOPM_NUM = 1;
    public static final String TABLE_NAME = "LovedRecipe";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "NAME";
    public static final String COL_URL = "URL";
    public static final String COL_IMAGE = "IMAGE";

    public RecipeDatabaseOpenHelper(Activity ctx){
        super(ctx, DATABASE_NAME, null, VERSOPM_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT, "+ COL_URL + " TEXT, " + COL_IMAGE  + " BLOB)" );
    }

//    public void addRecipeFa(MyRecipe recipe){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("Key_TITLE",MyRecipe.TITLE );
//        values.put("Key_URL",MyRecipe.URL );
//        values.put("Key_");
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
