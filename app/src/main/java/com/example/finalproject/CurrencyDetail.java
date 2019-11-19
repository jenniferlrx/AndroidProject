package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class CurrencyDetail extends AppCompatActivity {
private Button returnButton;
private Button deleteButton;
    ListView theList;
    //ArrayList<Currency> objects = new ArrayList<Currency>( );
    BaseAdapter myAdapter;
    MyDatabaseOpenHelper dbHelper;
    SQLiteDatabase db;
    //ArrayList<Currency> currencies;
    CurrencyActivity a=new CurrencyActivity();
    Currency c;
    Intent fromPreviousPage;
    String cFrom="nonono";
    String cTo;
    HttpURLConnection urlConnection;
    InputStream inStream;
    double exchangeRate;
    private MyNetworkQuery myNetworkQuery;

    /**
     * creates the basic page. build up the database, and receive information from the previous page
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_currency_favorite_list_detail);

        MyDatabaseOpenHelper opener = new MyDatabaseOpenHelper(this);
        db =  opener.getWritableDatabase();
        fromPreviousPage = getIntent();
        cFrom = fromPreviousPage.getStringExtra("CurrencyFrom");
        cTo = fromPreviousPage.getStringExtra("CurrencyTo");
        long id = fromPreviousPage.getLongExtra("Id", -1);
        Log.d("3333333333333333333333", cFrom);
        TextView cFromText=findViewById(R.id.currencyFavoriteFromText);
        cFromText.setText(cFrom);


        TextView cToText=findViewById(R.id.currencyFavoriteToText);
        cToText.setText(cTo);
        myNetworkQuery=new MyNetworkQuery();
        myNetworkQuery=new MyNetworkQuery();
        myNetworkQuery.execute();

        returnButton=(Button)findViewById(R.id.CurrencyReturnButton);
        returnButton.setOnClickListener(clk->{
            finish();
                } );

        deleteButton=(Button)findViewById(R.id.CurrencyDeleteButton);
        deleteButton.setOnClickListener(clk->{

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                //This is the builder pattern, just call many functions on the same object:
                AlertDialog dialog = builder.setTitle(R.string.currency_alert_title)
                        .setMessage(R.string.currency_alert_message)
                        .setPositiveButton(R.string.currency_alert_delete_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //If you click the "Delete" button
                                //c= (Currency) myAdapter.getItem(which);
                                int numDeleted = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[] {Long.toString(id)});
                                Log.i("currency detail", "Deleted " + numDeleted + " rows");
                                //currencies.remove(c);
                                //myAdapter.notifyDataSetChanged();
                                //set result to PUSHED_DELETE to show clicked the delete button
                                setResult(10);
                                //go back to previous page:
                                finish();
                            }
                        })
                        //If you click the "Cancel" button:
                        .setNegativeButton(R.string.currency_alert_cancel_button, (d,w) -> {  })
                    .create();

                            //then show the dialog
                            dialog.show();

                        });
                }


    /**
     * This method builds connection to the URL and check the rates
     */
    private class MyNetworkQuery extends AsyncTask<String, String, String> {
        //HttpURLConnection urlConnection;
        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "https://api.exchangeratesapi.io/latest?base=" + cFrom + "&symbols=" + cTo;
            // String queryURL = "https://api.exchangeratesapi.io/latest?base=USD&symbols=JPY";


            //String queryURL = "https://api.exchangeratesapi.io/latest";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                inStream = urlConnection.getInputStream();
                //urlConnection = (HttpsURLConnection)url.openConnection();
                //InputStream inStream = urlConnection.getInputStream();
                //Set up the JSON object parser:
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jsonObject = new JSONObject(result);
                JSONObject rateObject = jsonObject.getJSONObject("rates");
                exchangeRate = rateObject.getDouble(cTo);
                Log.d("111111111111111rate", "" + exchangeRate);

            } catch (MalformedURLException mfe) {
                mfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //What is returned here will be passed as a parameter to onPostExecute:
            Log.d("111111111111111rate", "" + exchangeRate);
            return ret;
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:
            TextView rate = findViewById(R.id.currencyFavoriteRateText);
            rate.setText(String.valueOf(exchangeRate));

        }

        @Override                       //Type 2
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:

        }


    }
}
