package com.example.finalproject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ECCSFDatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ECCSFDatabaseFile";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "ChargingStations";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_LONGTITUDE = "longtitude";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_PHONENO = "phoneNo";
    public static final String COL_ADDRESS = "address";

    public ECCSFDatabaseOpenHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_LATITUDE + " TEXT, " +
                COL_LONGTITUDE + " TEXT, " + COL_PHONENO + " TEXT, " +
                COL_ADDRESS + " TEXT)");
        Log.i("database create", "onCreate: Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
