package com.example.finalproject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *  create news database
 */
public class NewsDatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "news";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "NewsArticles";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_ARTICLEURL = "ARTICLEURL";
    public static final String COL_IMAGEURL = "IMAGEURL";

    /**
     * constructor
     * @param ctx
     */
    public NewsDatabaseOpenHelper(Activity ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    /**
     * method to create a database
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_DESCRIPTION + " TEXT, "
                + COL_ARTICLEURL + " TEXT, " + COL_IMAGEURL + " TEXT)");
    }

    /**
     * upgrades a database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    /**
     * downgrades a database
     * @param db database
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

