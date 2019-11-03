package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class ECCSFmain extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "Electrical Car Charging Station Finder";

    ArrayList<ChargingStation> stations = new ArrayList<ChargingStation>();
    static ECCSFDatabaseOpenHelper dbOpener;
    static SQLiteDatabase db;
    BaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsf_main);

        stations.add(new ChargingStation("IKEA", "45.349861", "-75.785074", "613-670-8060", "2685 Iris St"));
        stations.add(new ChargingStation("PARKING SERVICE", "45.3487237", "-75.7545402", "888-758-4389", "Tower Rd"));
        stations.add(new ChargingStation("PARKING SERVICE", "45.3471257", "-75.759737", "888-758-4389", "1408 Woodroffe Ave"));

        //get a database
        dbOpener = new ECCSFDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        Cursor cursor = null;
        for(ChargingStation station: stations){
           cursor= db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME, new String[]{ECCSFDatabaseOpenHelper.COL_ID},
                   ECCSFDatabaseOpenHelper.COL_LATITUDE+" =? AND "+ECCSFDatabaseOpenHelper.COL_LONGTITUDE + "=?",
                    new String[]{station.getLatitude(),station.getLongtitude()}, null, null, null, null);
            if(cursor.getCount()!=0){
                station.setFav(true);
            }
        }
        cursor.close();

        //get items from the layout
        ListView list = (ListView) findViewById(R.id.listView);
        Button searchBtn = (Button) findViewById(R.id.button_search);
        Button gotoFavBtn = (Button) findViewById(R.id.btn_goto_fav);
        EditText longtitude = (EditText) findViewById(R.id.edit_longtitude);
        EditText latitude = (EditText) findViewById(R.id.edit_latitude);

        gotoFavBtn.setOnClickListener(v->{
            Intent favPage = new Intent(ECCSFmain.this, ECCSFfav.class);
            startActivity(favPage);
        });

        //populate rows for the listview
        list.setAdapter(myAdapter = new MyListAdapter());
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent detail = new Intent(ECCSFmain.this, ECCSFdetail.class);
            detail.putExtra("title", stations.get(position).getTitle());
            detail.putExtra("latitude", stations.get(position).getLatitude());
            detail.putExtra("longtitude", stations.get(position).getLongtitude());
            detail.putExtra("phoneNo", stations.get(position).getPhoneNo());
            detail.putExtra("address", stations.get(position).getAddress());
            detail.putExtra("fav", stations.get(position).isFav());
            startActivity(detail);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        "You just saw "+data.getStringExtra("detail"))
        if (requestCode == 50 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "You are in search page",
                    Toast.LENGTH_LONG).show();
        }
        if (requestCode == 50 && resultCode == 50) {
            Toast.makeText(this, "You just saw the station around " + data.getStringExtra("detail"),
                    Toast.LENGTH_LONG).show();
        }
    }

    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return stations.size();
        }

        @Override
        public Object getItem(int i) {
            return stations.get(i);
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

            if(!stations.get(position).isFav()){
                saved.setText("");
            }

            title.setText(stations.get(position).getTitle());
            address.setText(stations.get(position).getAddress());
            return thisView;
        }
    }
}


