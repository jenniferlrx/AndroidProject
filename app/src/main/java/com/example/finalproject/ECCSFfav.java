package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ECCSFfav extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "Favorate Electrical Car Charging Stations";
    ArrayList<ChargingStation> favStations = new ArrayList<ChargingStation>();
    static ECCSFDatabaseOpenHelper dbOpener;
    static SQLiteDatabase db;
    BaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsf_fav);

        //get database
        dbOpener = new ECCSFDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        Cursor cursor = null;

        //check if new data is required to add
        Intent dataToAdd = getIntent();
        if(dataToAdd.getBooleanExtra("addToFav",false) ) {
            String title = dataToAdd.getStringExtra("title");
            String latitude = dataToAdd.getStringExtra("latitude");
            String longtitude = dataToAdd.getStringExtra("longtitude");
            String phoneNo = dataToAdd.getStringExtra("phoneNo");
            String address = dataToAdd.getStringExtra("address");

            cursor = db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME,
                    new String[]{ECCSFDatabaseOpenHelper.COL_ID},
                    ECCSFDatabaseOpenHelper.COL_LATITUDE +" = ? AND "+
                            ECCSFDatabaseOpenHelper.COL_LONGTITUDE + " = ? "
                    , new String[]{latitude,longtitude}, null, null, null, null);
            if(cursor.getCount()==0){
                ContentValues cv = new ContentValues();
                cv.put(ECCSFDatabaseOpenHelper.COL_TITLE, title);
                cv.put(ECCSFDatabaseOpenHelper.COL_LATITUDE,latitude);
                cv.put(ECCSFDatabaseOpenHelper.COL_LONGTITUDE,longtitude);
                cv.put(ECCSFDatabaseOpenHelper.COL_PHONENO, phoneNo);
                cv.put(ECCSFDatabaseOpenHelper.COL_ADDRESS, address);
                db.insert(ECCSFDatabaseOpenHelper.TABLE_NAME,null,cv);
            }
        }

        //get data to be listed
        cursor= db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME,
                new String[]{ECCSFDatabaseOpenHelper.COL_TITLE,ECCSFDatabaseOpenHelper.COL_LATITUDE,
                        ECCSFDatabaseOpenHelper.COL_LONGTITUDE, ECCSFDatabaseOpenHelper.COL_PHONENO,
                        ECCSFDatabaseOpenHelper.COL_ADDRESS},
                null, null, null, null, null, null);

        cursor.moveToFirst();
        Log.i("fav", "onCreate:  "+cursor.getCount());
        while( !cursor.isAfterLast()){
            String title = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_TITLE));
            String latitude = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_LATITUDE));
            String longtitude = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_LONGTITUDE));
            String phoneNo = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_PHONENO));
            String address = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_ADDRESS));
            ChargingStation station = new ChargingStation(title, latitude, longtitude, phoneNo, address);
            station.setFav(true);
            cursor.moveToNext();
            favStations.add(station);
        }
        cursor.close();

        // set up list view
        ListView favList = (ListView)findViewById(R.id.listView_fav);
        favList.setAdapter(myAdapter = new MyListAdapter());

    }


    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return favStations.size();
        }

        @Override
        public Object getItem(int i) {
            return favStations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent) {
            View thisView = oldView;
            thisView = getLayoutInflater().inflate(R.layout.eccsf_listview_row, parent, false);

            TextView title = thisView.findViewById(R.id.row_title);
            TextView address = thisView.findViewById(R.id.row_address);
            TextView saved = thisView.findViewById(R.id.row_saved);

            title.setText(favStations.get(position).getTitle());
            address.setText(favStations.get(position).getAddress());
            return thisView;
        }
    }
}
