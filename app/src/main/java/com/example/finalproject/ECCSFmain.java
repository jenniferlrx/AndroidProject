package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    /**
     * searched stations results
     */
    private static ArrayList<ChargingStation> searchedStations = new ArrayList<>();
    /**
     * database
     */
    private static SQLiteDatabase db;
    /**
     * database adapter
     */
    private static BaseAdapter myAdapter;
    /**
     * progress bar
     */
    private static ProgressBar pgsBar;

    /**
     * initialize, set click listeners, populate viewlist
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccsf_main);


        //get a database
        ECCSFDatabaseOpenHelper dbOpener = new ECCSFDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();


        //get items from the layout
        ListView list = findViewById(R.id.car_main_listView);
        Button searchBtn = findViewById(R.id.car_button_search);
        Button gotoFavBtn = findViewById(R.id.car_btn_goto_fav);
        EditText longitudeText = findViewById(R.id.edit_longitude);
        EditText latitudeText = findViewById(R.id.edit_latitude);
        pgsBar = findViewById(R.id.car_bar);

        //toolbar setup
        Toolbar toolbar = findViewById(R.id.car_main_toolbar);
        setSupportActionBar(toolbar);



        //go to fav page
        gotoFavBtn.setOnClickListener(v -> {
            Intent favPage = new Intent(ECCSFmain.this, ECCSFfav.class);
            startActivityForResult(favPage, 1);
        });


        //search new stations
        searchBtn.setOnClickListener(v -> {
            //set up progress bar
            pgsBar.setVisibility(View.VISIBLE);
            pgsBar.setProgress(0);
            StationFinder sf = new StationFinder();
            sf.execute(latitudeText.getText().toString(),longitudeText.getText().toString());

        });


        //populate rows for the listview
        list.setAdapter(myAdapter = new MyListAdapter());

        //set click listener for each item to open the detail page
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent detail = new Intent(ECCSFmain.this, ECCSFdetail.class);
            detail.putExtra("title", searchedStations.get(position).getTitle());
            detail.putExtra("latitude", searchedStations.get(position).getLatitude());
            detail.putExtra("longitude", searchedStations.get(position).getLongitude());
            detail.putExtra("phoneNo", searchedStations.get(position).getPhoneNo());
            detail.putExtra("address", searchedStations.get(position).getAddress());
            detail.putExtra("fav", searchedStations.get(position).isFav());
            startActivityForResult(detail, 1);
        });

    }

    /**
     * get data from previous activity and process the data
     *
     * @param requestCode - this activity request code is always 1
     * @param resultCode  - 2 for addToFav from detailpage, 3 for delete from detailpage,
     *                    6 from favpage
     * @param data        - data from previous activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        }
//from detail page to add station to fav/db
        else if (requestCode == 1 && resultCode == 2) {

            //if the data need to be added to fav
            if (data.getBooleanExtra("addToFav", false)) {
                String title = data.getStringExtra("title");
                String latitude = data.getStringExtra("latitude");
                String longitude = data.getStringExtra("longitude");
                String phoneNo = data.getStringExtra("phoneNo");
                String address = data.getStringExtra("address");

                ContentValues cv = new ContentValues();
                cv.put(ECCSFDatabaseOpenHelper.COL_TITLE, title);
                cv.put(ECCSFDatabaseOpenHelper.COL_LATITUDE, latitude);
                cv.put(ECCSFDatabaseOpenHelper.COL_LONGITUDE, longitude);
                cv.put(ECCSFDatabaseOpenHelper.COL_PHONENO, phoneNo);
                cv.put(ECCSFDatabaseOpenHelper.COL_ADDRESS, address);

                db.insert(ECCSFDatabaseOpenHelper.TABLE_NAME, null, cv);

                Toast.makeText(this, R.string.ECCSF_success_added,
                        Toast.LENGTH_SHORT).show();

                //update current list to reflect the saved stations
                for (ChargingStation station : searchedStations) {
                    if (station.getLongitude().equals(longitude)) {
                        station.setFav(true);
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        }
// from detail page to delete one station from fav
        else if (requestCode == 1 && resultCode == 3) {
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
                Toast.makeText(this, R.string.ECCSF_success_deleted,
                        Toast.LENGTH_SHORT).show();
                for (ChargingStation station : searchedStations) {
                    if (station.getLongitude().equals(longitude)) {
                        station.setFav(false);
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        }
        //from favpage
        else if (requestCode == 1 && resultCode == 6) {

            int numOfDel = data.getIntExtra("numOfDel", 0);
            String latitude;
            for (int i = 0; i <= numOfDel; i++) {
                latitude = data.getStringExtra(i + "");
                for (ChargingStation station : searchedStations) {
                    if (station.getLatitude().equals(latitude)) {
                        station.setFav(false);
                        break;
                    }
                }
            }
            myAdapter.notifyDataSetChanged();

        }
    }

    /**
     * gather data from remote server
     */
    static class StationFinder extends AsyncTask<String, Integer, ArrayList<ChargingStation>> {
        ArrayList<ChargingStation> newStations = new ArrayList<>();

        @Override                       //Type 1
        protected ArrayList<ChargingStation> doInBackground(String... strings) {
            String urlLatitude = "45.347571";
            String urlLongitude = "-75.756140";
            if(!strings[0].equals("")&& !strings[1].equals("")){
                urlLatitude = strings[0];
                urlLongitude = strings[1];
            }

            String queryURL = "https://api.openchargemap.io/v3/poi/?output=json&latitude="
                    +urlLatitude+"&longitude="+urlLongitude+"&maxresults=10";

            try {
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(2);
                InputStream inStream = urlConnection.getInputStream();

                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                int progressCount = 10;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    publishProgress(progressCount++);
                }
                String result = sb.toString();
                JSONArray jResults = new JSONArray(result);
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject addressInfo = jResults.getJSONObject(i).getJSONObject("AddressInfo");
                    String title = addressInfo.getString("Title");
                    String latitude = addressInfo.getDouble("Latitude") + "";
                    String longitude = addressInfo.getDouble("Longitude") + "";
                    String phoneNo = addressInfo.getString("ContactTelephone1");
                    String address = addressInfo.getString("AddressLine1");
                    newStations.add(new ChargingStation(title, latitude, longitude, phoneNo, address));
                }
                urlConnection.disconnect();
                inStream.close();
            } catch (MalformedURLException mfe) {
                mfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
            return newStations;
        }

        /**
         *
         * @param newStations
         */
        @Override                   //Type 3
        protected void onPostExecute(ArrayList<ChargingStation> newStations) {
            super.onPostExecute(newStations);

            searchedStations = newStations;

            myAdapter.notifyDataSetChanged();
            pgsBar.setVisibility(View.GONE);
            Cursor cursor = null;
            //check if already saved
            if (searchedStations.size() > 0) {
                for (ChargingStation station : searchedStations) {
                    cursor = db.query(false, ECCSFDatabaseOpenHelper.TABLE_NAME, new String[]{ECCSFDatabaseOpenHelper.COL_ADDRESS},
                            ECCSFDatabaseOpenHelper.COL_LATITUDE + " =? AND " + ECCSFDatabaseOpenHelper.COL_LONGITUDE + "=?",
                            new String[]{station.getLatitude(), station.getLongitude()}, null, null, null, null);
                    if (cursor.getCount() > 0) {
                        station.setFav(true);
                    }
                }
                cursor.close();
            }
        }

        /**
         *
         * @param values
         */
        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pgsBar.setProgress(values[0]);
        }
    }

    /**
     * list adapter to populate list
     */
    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return searchedStations.size();
        }

        @Override
        public Object getItem(int i) {
            return searchedStations.get(i);
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

            if (!searchedStations.get(position).isFav()) {
                saved.setVisibility(View.GONE);
            }
            title.setText(searchedStations.get(position).getTitle());
            address.setText(searchedStations.get(position).getAddress());
            return thisView;
        }
    }

    /**
     * menu setup
     * @param menu
     * @return
     */
    //toolbar setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     *menu items select actions
     * @param item - menu item
     * @return - alwasy <code>true</code>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // four applications
            case R.id.menuItemCar:
                Snackbar.make(pgsBar,R.string.ECCSF_toolbar_self, Snackbar.LENGTH_LONG).show();
                break;
//            case R.id.menuItemRecipe:
//                startActivity(new Intent(ECCSFmain.this, ECCSFmain.class));
//                break;
            case R.id.menuItemCurrency:
                startActivity(new Intent(ECCSFmain.this, CurrencyActivity.class));
                break;
            case R.id.menuItemNews:
                startActivity(new Intent(ECCSFmain.this, News_Activity_Main.class));
                break;


            case R.id.saved:
                Intent favPage = new Intent(ECCSFmain.this, ECCSFfav.class);
                startActivityForResult(favPage, 1);
                break;
            case R.id.help:
                AlertDialog.Builder  builder = new AlertDialog.Builder(this);

                builder.setMessage(R.string.ECCSF_help_content)
                        .setNegativeButton(R.string.ECCSF_back,(dialog, id)-> {});
                builder.create().show();
                break;
        }
        return true;
    }
}


