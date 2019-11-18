package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ECCSFfav extends AppCompatActivity {
    private ArrayList<ChargingStation> favStations = new ArrayList<>();
    private static ECCSFDatabaseOpenHelper dbOpener;
    private static SQLiteDatabase db;
    private static BaseAdapter myAdapter;
    private Intent delData= new Intent();
    private int numOfDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsf_fav);

        //get database
        dbOpener = new ECCSFDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        //get references to the views
        Button back = findViewById(R.id.btn_back_to_main);
        TextView emptyView = findViewById(R.id.empty_list);
        emptyView.setVisibility(View.GONE);

        //set click listener for back to main button
        back.setOnClickListener(v->{
            setResult(6,delData);
           finish();
        });


        //get data from db to be saved locally
        Cursor cursor= db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME,
                new String[]{
                        ECCSFDatabaseOpenHelper.COL_TITLE,
                        ECCSFDatabaseOpenHelper.COL_LATITUDE,
                        ECCSFDatabaseOpenHelper.COL_LONGITUDE,
                        ECCSFDatabaseOpenHelper.COL_PHONENO,
                        ECCSFDatabaseOpenHelper.COL_ADDRESS},
                null, null, null, null, null, null);

        //save data from db to local variable
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String title = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_TITLE));
            String latitude = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_LATITUDE));
            String longitude = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_LONGITUDE));
            String phoneNo = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_PHONENO));
            String address = cursor.getString(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_ADDRESS));
            ChargingStation station = new ChargingStation(title, latitude, longitude, phoneNo, address);
            station.setFav(true);
            cursor.moveToNext();
            favStations.add(station);
        }
        cursor.close();

        // set up list view
        ListView favList = findViewById(R.id.listView_fav);

        myAdapter = new MyListAdapter();
        favList.setAdapter(myAdapter);

        if(myAdapter.getCount() == 0){
            emptyView.setVisibility(View.VISIBLE);
        }


        favList.setOnItemClickListener((parent, view, position, id) -> {
            Intent detail = new Intent(ECCSFfav.this, ECCSFdetail.class);
            detail.putExtra("title", favStations.get(position).getTitle());
            detail.putExtra("latitude", favStations.get(position).getLatitude());
            detail.putExtra("longitude", favStations.get(position).getLongitude());
            detail.putExtra("phoneNo", favStations.get(position).getPhoneNo());
            detail.putExtra("address", favStations.get(position).getAddress());
            detail.putExtra("fav", favStations.get(position).isFav());
            startActivityForResult(detail, 5);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            // from detail page, to delete one station from fav
            if (requestCode == 5 && resultCode == 3) {
                // if the data is needed to be deleted from favorite stations
                if (data.getBooleanExtra("deleteFromFav", false)) {
                    String latitude = data.getStringExtra("latitude");
                    String longitude = data.getStringExtra("longitude");

                    Cursor cursor = db.query(true, ECCSFDatabaseOpenHelper.TABLE_NAME,
                            new String[]{ECCSFDatabaseOpenHelper.COL_ID},
                            ECCSFDatabaseOpenHelper.COL_LATITUDE + " = ? AND " +
                                    ECCSFDatabaseOpenHelper.COL_LONGITUDE + " = ? "
                            , new String[]{latitude, longitude}, null, null, null, null);
                    cursor.moveToFirst();
                    if (cursor.getCount() > 0) {
                        int id = cursor.getInt(cursor.getColumnIndex(ECCSFDatabaseOpenHelper.COL_ID));
                        Log.i("id to be deleted is" + id, "eccsfmain");
                        db.delete(ECCSFDatabaseOpenHelper.TABLE_NAME, ECCSFDatabaseOpenHelper.COL_ID + "=?",
                                new String[]{Long.toString(id)});
                    }
                    cursor.close();

                    for(ChargingStation station: favStations){
                        if(station.getLongitude().equals(longitude)){
                            favStations.remove(station);
                            Toast.makeText(this, "Successfully deleted from favorite stations",
                                    Toast.LENGTH_LONG).show();
                            myAdapter.notifyDataSetChanged();

                            delData.putExtra("numOfDel", numOfDeleted);
                            delData.putExtra(numOfDeleted+"", latitude);
                            numOfDeleted++;
                            break;
                        }
                    }


                    View emptyView = findViewById(R.id.empty_list);
                    if(myAdapter.getCount() == 0){

                        emptyView.setVisibility(View.VISIBLE);
                    }else{
                        emptyView.setVisibility(View.GONE);
                    }
                }
        }


        }
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

            oldView = getLayoutInflater().inflate(R.layout.eccsf_listview_row, parent, false);

            TextView title = oldView.findViewById(R.id.row_title);
            TextView address = oldView.findViewById(R.id.row_address);
            TextView saved = oldView.findViewById(R.id.row_saved);
            saved.setVisibility(View.INVISIBLE);

            title.setText(favStations.get(position).getTitle());
            address.setText(favStations.get(position).getAddress());
            return oldView;
        }
    }
}
