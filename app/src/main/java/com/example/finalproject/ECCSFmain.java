package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class ECCSFmain extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "Electrical Car Charging Station Finder";
    private  int i=0;
    ArrayList<ChargingStation> stations = new ArrayList<ChargingStation>();
    static ECCSFDatabaseOpenHelper dbOpener;
    static SQLiteDatabase db;
    BaseAdapter myAdapter;
    private Handler hdlr = new Handler();
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
        //check if already saved
        for(ChargingStation station: stations){
           cursor= db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME, new String[]{ECCSFDatabaseOpenHelper.COL_ADDRESS},
                   ECCSFDatabaseOpenHelper.COL_LATITUDE+" =? AND "+ECCSFDatabaseOpenHelper.COL_LONGTITUDE + "=?",
                    new String[]{station.getLatitude(),station.getLongtitude()}, null, null, null, null);
            if(cursor.getCount()>0){
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
        ProgressBar pgsBar = (ProgressBar) findViewById(R.id.p_Bar);
        TextView pgsText = (TextView) findViewById(R.id.pgs_text);

        pgsBar.setVisibility(View.GONE);
        //go to fav page
        gotoFavBtn.setOnClickListener(v->{
            Intent favPage = new Intent(ECCSFmain.this, ECCSFfav.class);
            startActivity(favPage);
        });

        //search new location
        searchBtn.setOnClickListener(v->{
            Snackbar.make(searchBtn, "Here are new results",Snackbar.LENGTH_LONG).show();
            i = pgsBar.getProgress();

            new Thread(new Runnable() {
                public void run() {
                    while (i < 100) {
                        pgsBar.setVisibility(View.VISIBLE);
                        i += 5;
                        // Update the progress bar and display the current value in text view
                        hdlr.post(new Runnable() {
                            public void run() {
                                pgsBar.setProgress(i);
                                pgsText.setText(i+"/"+pgsBar.getMax());
                            }
                        });
                        try {
                            // Sleep for 100 milliseconds to show the progress slowly.
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
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
            myAdapter.notifyDataSetChanged();
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
                saved.setVisibility(View.GONE);
            }

            title.setText(stations.get(position).getTitle());
            address.setText(stations.get(position).getAddress());
            return thisView;
        }
    }
}


