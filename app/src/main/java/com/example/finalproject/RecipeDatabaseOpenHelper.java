package com.example.finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;

/**
 * Database to save recipe info
 */
public class RecipeDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RecipeDatabaseFile";
    public static final int VERSOPM_NUM = 1;
    public static final String TABLE_NAME = "LovedRecipe";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_URL = "URL";
    public static final String COL_IMAGE_URL = "IMAGE_URL";
    public static final String COL_RECIPE_ID = "recipeID";

    private SQLiteDatabase database;

    public RecipeDatabaseOpenHelper(Activity ctx){
        super(ctx, DATABASE_NAME, null, VERSOPM_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, "+ COL_URL + " TEXT, " + COL_IMAGE_URL  + " TEXT, " + COL_RECIPE_ID+ " TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean deleteRow(SQLiteDatabase db, int id){
        return db.delete(TABLE_NAME, COL_ID + "=" + id, null) >0;
    }

    public boolean addData(String title, String url, String img_url,String recipeID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_URL, url);
        contentValues.put(COL_IMAGE_URL, img_url);
        contentValues.put(COL_RECIPE_ID, recipeID);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * to retrieve data from the database
     *
     * @return Cursor all data in the database.
     */
    public Cursor getDataFromDB() {
        database = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = database.rawQuery(query, null);
        return data;
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
