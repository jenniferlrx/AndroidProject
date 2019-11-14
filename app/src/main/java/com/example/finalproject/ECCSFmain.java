package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ECCSFmain extends AppCompatActivity {

    private ArrayList<ChargingStation> stations = new ArrayList<ChargingStation>();
    private static ECCSFDatabaseOpenHelper dbOpener;
    private static SQLiteDatabase db;
    private BaseAdapter myAdapter;
    private ProgressBar pgsBar;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsf_main);



        //get a database
        dbOpener = new ECCSFDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();



        //get items from the layout
        list = (ListView) findViewById(R.id.listView);
        Button searchBtn = (Button) findViewById(R.id.button_search);
        Button gotoFavBtn = (Button) findViewById(R.id.btn_goto_fav);
        EditText longitude = (EditText) findViewById(R.id.edit_longitude);
        EditText latitude = (EditText) findViewById(R.id.edit_latitude);
        pgsBar = (ProgressBar) findViewById(R.id.bar);
        TextView pgsText = (TextView) findViewById(R.id.pgs_text);



        //go to fav page
        gotoFavBtn.setOnClickListener(v->{
            Intent favPage = new Intent(ECCSFmain.this, ECCSFfav.class);
            startActivity(favPage);
        });



        //search new stations
        searchBtn.setOnClickListener(v->{
            //set up progress bar
            pgsBar.setVisibility(View.VISIBLE);
            pgsBar.setProgress(0);
            StationFinder sf = new StationFinder();
            sf.execute();

        });


        //populate rows for the listview
        list.setAdapter(myAdapter = new MyListAdapter());
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent detail = new Intent(ECCSFmain.this, ECCSFdetail.class);
            detail.putExtra("title", stations.get(position).getTitle());
            detail.putExtra("latitude", stations.get(position).getLatitude());
            detail.putExtra("longitude", stations.get(position).getLongitude());
            detail.putExtra("phoneNo", stations.get(position).getPhoneNo());
            detail.putExtra("address", stations.get(position).getAddress());
            detail.putExtra("fav", stations.get(position).isFav());
            startActivity(detail);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    class StationFinder extends AsyncTask<String, Integer, ArrayList<ChargingStation>>{
        ArrayList<ChargingStation> newStations = new ArrayList<>();

        @Override                       //Type 1
        protected ArrayList<ChargingStation> doInBackground(String... strings) {

            String queryURL ="https://api.openchargemap.io/v3/poi/?output=json&countrycode=CA&latitude=45.347571&longitude=-75.756140&maxresults=10";

            try {
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                int progressCount = 0;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                    progressCount++;
                    if(progressCount%100 == 0){
                        publishProgress(progressCount/10);
                    }
                }
                String result = sb.toString();
                JSONArray jResults = new JSONArray(result);
                for (int i = 0; i < jResults.length(); i++){
                    JSONObject addressInfo = jResults.getJSONObject(i).getJSONObject("AddressInfo");
                    String title = addressInfo.getString("Title");
                    String latitude = addressInfo.getDouble("Latitude")+"";
                    String longitude = addressInfo.getDouble("Longitude")+"";
                    String phoneNo = addressInfo.getString("ContactTelephone1");
                    String address = addressInfo.getString("AddressLine1");
                    newStations.add(new ChargingStation(title,latitude, longitude, phoneNo, address));

                }

                urlConnection.disconnect();
                inStream.close();

            }
            catch(MalformedURLException mfe){ mfe.printStackTrace(); }
            catch(IOException ioe)          { ioe.printStackTrace(); }
            catch(JSONException jsone)      {jsone.printStackTrace();}

            return newStations;
        }

        @Override                   //Type 3
        protected void onPostExecute(ArrayList<ChargingStation> newStations) {
            super.onPostExecute(newStations);

            for(ChargingStation station: newStations){
                stations.add(new ChargingStation(station));
            }
            myAdapter.notifyDataSetChanged();
            pgsBar.setVisibility(View.GONE);
            Cursor cursor = null;
            //check if already saved
            if(stations.size()>0){
                for(ChargingStation station: stations){
                    cursor= db.query(false,ECCSFDatabaseOpenHelper.TABLE_NAME, new String[]{ECCSFDatabaseOpenHelper.COL_ADDRESS},
                            ECCSFDatabaseOpenHelper.COL_LATITUDE+" =? AND "+ECCSFDatabaseOpenHelper.COL_LONGITUDE + "=?",
                            new String[]{station.getLatitude(),station.getLongitude()}, null, null, null, null);
                    if(cursor.getCount()>0){
                        station.setFav(true);
                    }
                }
                cursor.close();
            }
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pgsBar.setProgress(values[0]);
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


