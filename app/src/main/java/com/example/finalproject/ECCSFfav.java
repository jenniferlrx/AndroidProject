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
import android.widget.Toast;

import java.util.ArrayList;

public class ECCSFfav extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "Favorate Stations";
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

        //check delivered data
        Intent data = getIntent();

        //if the data need to be added to fav
        if(data.getBooleanExtra("addToFav",false) ) {
            String title = data.getStringExtra("title");
            String latitude = data.getStringExtra("latitude");
            String longtitude = data.getStringExtra("longtitude");
            String phoneNo = data.getStringExtra("phoneNo");
            String address = data.getStringExtra("address");

            cursor = db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME,
                    new String[]{ECCSFDatabaseOpenHelper.COL_ID},
                    ECCSFDatabaseOpenHelper.COL_LATITUDE +" = ? AND "+
                            ECCSFDatabaseOpenHelper.COL_LONGTITUDE + " = ? "
                    , new String[]{latitude,longtitude}, null, null, null, null);

            if(cursor.getCount()<1){
                ContentValues cv = new ContentValues();
                cv.put(ECCSFDatabaseOpenHelper.COL_TITLE, title);
                cv.put(ECCSFDatabaseOpenHelper.COL_LATITUDE,latitude);
                cv.put(ECCSFDatabaseOpenHelper.COL_LONGTITUDE,longtitude);
                cv.put(ECCSFDatabaseOpenHelper.COL_PHONENO, phoneNo);
                cv.put(ECCSFDatabaseOpenHelper.COL_ADDRESS, address);

                db.insert(ECCSFDatabaseOpenHelper.TABLE_NAME,null,cv);

                Toast.makeText(this, "Successfully added to favorate stations",
                        Toast.LENGTH_LONG).show();

            }
        cursor.close();
        }
        // if the data is needed to be deleted from favorate stations
        else if(data.getBooleanExtra("deleteFromFav",false)){
            String latitude = data.getStringExtra("latitude");
            String longtitude = data.getStringExtra("longtitude");

            cursor = db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME,
                    new String[]{ECCSFDatabaseOpenHelper.COL_ID},
                    ECCSFDatabaseOpenHelper.COL_LATITUDE +" = ? AND "+
                            ECCSFDatabaseOpenHelper.COL_LONGTITUDE + " = ? "
                    , new String[]{latitude,longtitude}, null, null, null, null);
            cursor.moveToFirst();
            if(cursor.getCount()>0){
                int id = cursor.getInt(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_ID));
                Log.i("id to be deleted is"+id,"eccsFav" );
                db.delete(ECCSFDatabaseOpenHelper.TABLE_NAME,ECCSFDatabaseOpenHelper.COL_ID +"=?",
                        new String[]{Long.toString(id)});
            }
        cursor.close();
        }

        //get data from db to be saved locally
        cursor= db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME,
                new String[]{
                        ECCSFDatabaseOpenHelper.COL_TITLE,
                        ECCSFDatabaseOpenHelper.COL_LATITUDE,
                        ECCSFDatabaseOpenHelper.COL_LONGTITUDE,
                        ECCSFDatabaseOpenHelper.COL_PHONENO,
                        ECCSFDatabaseOpenHelper.COL_ADDRESS},
                null, null, null, null, null, null);


        Log.i("fav", "onCreate:  "+cursor.getCount());
        printCursor(cursor);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String title = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_TITLE));
            String latitude = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_LATITUDE));
            String longtitude = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_LONGTITUDE));
            String phoneNo = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_PHONENO));
            String address = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_ADDRESS));
            ChargingStation station = new ChargingStation(title, latitude, longtitude, phoneNo, address);
            station.setFav(true);
            cursor.moveToNext();
            favStations.add(station);
//            Log.i("id listed"+id,"favs");
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

    static void printCursor(Cursor c){
        c.moveToFirst();
        c.moveToPrevious();
        int columnCounts = c.getColumnCount();
        String [] columnNames = c.getColumnNames();
        String colNames="";

        Log.i(ACTIVITY_NAME, "DATABASE VERSION NUMBER: "+ dbOpener.VERSION_NUM);

        Log.i(ACTIVITY_NAME, "NUMBER OF COLUMNS: "+ columnCounts);


        for (int i=0; i<columnCounts; i++){
            colNames +=(" "+columnNames[i]);
        }
        Log.i(ACTIVITY_NAME, "NAME OF COLUMNS: "+colNames);

        Log.i(ACTIVITY_NAME, "NUMBER OF RESULTS: "+ c.getCount());

//        int isSenderColumnIndex = c.getColumnIndex(ECCSFDatabaseOpenHelper.COL_ID);
//        int messageColIndex = c.getColumnIndex(ECCSFDatabaseOpenHelper.COL_MESSAGE);
//        int idColIndex = c.getColumnIndex(ECCSFDatabaseOpenHelper.COL_ID);
//
//        Log.i(ACTIVITY_NAME, "ROWS OF COLUMNS");
//        while(c.moveToNext())
//        {
//            boolean isSender = c.getInt(isSenderColumnIndex)>0;
//            String message = c.getString(messageColIndex);
//            long id = c.getLong(idColIndex);
//
//            //add the new Contact to the array list:
//            Log.i(ACTIVITY_NAME, "ID: "+id + "| isSender: "+isSender + "| text: " +message);
//        }


    }
}
