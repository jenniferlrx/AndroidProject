package com.example.finalproject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * The class defines the database open helper. Define the used table, current version
 *
 * @author jennifer yuan
 * @version 1.0
 */
public class ECCSFDatabaseOpenHelper extends SQLiteOpenHelper {
    /**
     * database name
     */
    public static final String DATABASE_NAME = "ECCSFDatabaseFile";
    /**
     * version number
     */
    public static final int VERSION_NUM = 18;
    /**
     * table name
     */
    public static final String TABLE_NAME = "ChargingStations";
    /**
     * column name for id
     */
    public static final String COL_ID = "id";
    /**
     * column name for title
     */
    public static final String COL_TITLE = "title";
    /**
     * column name for longitude
     */
    public static final String COL_LONGITUDE = "longitude";
    /**
     * column name for latitude
     */
    public static final String COL_LATITUDE = "latitude";
    /**
     * column name for phone number
     */
    public static final String COL_PHONENO = "phoneNo";
    /**
     * column name for address
     */
    public static final String COL_ADDRESS = "address";

    /**
     * default constructor
     *
     * @param ctx
     */
    public ECCSFDatabaseOpenHelper(Activity ctx) {
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * the steps for the db to create the table
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_LATITUDE + " TEXT, "
                + COL_LONGITUDE + " TEXT, "
                + COL_PHONENO + " TEXT, "
                + COL_ADDRESS + " TEXT)");
    }

    /**
     * when version number increases, recreate the table
     *
     * @param db         - database
     * @param oldVersion - old version number
     * @param newVersion - new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     * when version number decreases, recreate the table
     *
     * @param db         - database
     * @param oldVersion - old version number
     * @param newVersion - new version number
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:" + newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
