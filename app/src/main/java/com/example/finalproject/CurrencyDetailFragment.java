package com.example.finalproject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CurrencyDetailFragment extends Fragment{
    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private int positionClicked;
    private Context c;
    private Fragment frag;

    String currencyFrom;
    String currencyTo;
    HttpURLConnection urlConnection;
    InputStream inStream;
    double exchangeRate;
    private MyNetworkQuery myNetworkQuery;
    View result;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(CurrencyFavoriteActivity.ITEM_ID );
        positionClicked=dataFromActivity.getInt(CurrencyFavoriteActivity.ITEM_POSITION);
        Log.d("position1111111111", ""+positionClicked);

        c=getActivity();
        frag=this;
        // Inflate the layout for this fragment
        result =  inflater.inflate(R.layout.activitiy_currency_favorite_list_detail, container, false);
currencyFrom=dataFromActivity.getString(CurrencyFavoriteActivity.ITEM_FROM);
currencyTo=dataFromActivity.getString(CurrencyFavoriteActivity.ITEM_TO);
        //show the message
        TextView cFrom = (TextView)result.findViewById(R.id.currencyFavoriteFromText);
        cFrom.setText(dataFromActivity.getString(CurrencyFavoriteActivity.ITEM_FROM));

        //show the id:
        TextView cTo = (TextView)result.findViewById(R.id.currencyFavoriteToText);
        cTo.setText(dataFromActivity.getString(CurrencyFavoriteActivity.ITEM_TO));
        myNetworkQuery=new MyNetworkQuery();
        myNetworkQuery.execute();

        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.CurrencyDeleteButton);
        Button returnButton = (Button)result.findViewById(R.id.CurrencyReturnButton);
        Button calButton = (Button)result.findViewById(R.id.CurrencyCalButton);
        deleteButton.setOnClickListener( clk -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog dialog = builder.setTitle(R.string.currency_alert_title)
                    .setMessage(R.string.currency_alert_message)
                    .setPositiveButton(R.string.currency_alert_delete_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if (isTablet) { //both the list and details are on the screen:
                                CurrencyFavoriteActivity parent = (CurrencyFavoriteActivity) getActivity();
                                parent.deleteMessageId(positionClicked); //this deletes the item and updates the list
                                Log.d("position122222222222", ""+positionClicked);
                                //now remove the fragment since you deleted it from the database:
                                // this is the object to be removed, so remove(this):
                                parent.getSupportFragmentManager().beginTransaction().remove(frag).commit();
                            }
                            //for Phone:
                            else //You are only looking at the details, you need to go back to the previous list page
                            {
                                CurrencyEmptyActivity parent = (CurrencyEmptyActivity) getActivity();
                                Intent backToFragmentExample = new Intent();
                                backToFragmentExample.putExtra(CurrencyFavoriteActivity.ITEM_ID, dataFromActivity.getLong(CurrencyFavoriteActivity.ITEM_ID));
                                backToFragmentExample.putExtra(CurrencyFavoriteActivity.ITEM_POSITION, dataFromActivity.getInt(CurrencyFavoriteActivity.ITEM_POSITION));
                                Log.d("fffffffffffffffff",""+dataFromActivity.getInt(CurrencyFavoriteActivity.ITEM_POSITION));
                                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                                parent.finish(); //go back
                            }
                        }
                        }).setNegativeButton(R.string.currency_alert_cancel_button, (d,w) -> {


                    }).create();

                        //then show the dialog
                            dialog.show();
        });

        returnButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                CurrencyFavoriteActivity parent = (CurrencyFavoriteActivity)getActivity();
                Intent backToFragmentExample = new Intent();
               // parent.deleteMessageId((int)id); //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
               // parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                parent.setResult(Activity.RESULT_CANCELED, backToFragmentExample);
                parent.finish();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                CurrencyEmptyActivity parent = (CurrencyEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
               // backToFragmentExample.putExtra(CurrencyFavoriteActivity.ITEM_ID, dataFromActivity.getLong(CurrencyFavoriteActivity.ITEM_ID ));
                parent.setResult(Activity.RESULT_CANCELED, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });


        calButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                CurrencyFavoriteActivity parent = (CurrencyFavoriteActivity)getActivity();
                Intent backToFragmentExample = new Intent();
                // parent.deleteMessageId((int)id); //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                // parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                parent.setResult(Activity.RESULT_CANCELED, backToFragmentExample);
                parent.finish();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                CurrencyEmptyActivity parent = (CurrencyEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                // backToFragmentExample.putExtra(CurrencyFavoriteActivity.ITEM_ID, dataFromActivity.getLong(CurrencyFavoriteActivity.ITEM_ID ));
                parent.setResult(Activity.RESULT_CANCELED, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });





        return result;
    }


    /**
     * This method builds connection to the URL and check the rates
     */
    private class MyNetworkQuery extends AsyncTask<String, String, String> {
        //HttpURLConnection urlConnection;
        @Override                       //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "https://api.exchangeratesapi.io/latest?base=" + currencyFrom + "&symbols=" + currencyTo;
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
                exchangeRate = rateObject.getDouble(currencyTo);
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
            TextView rate = result.findViewById(R.id.currencyFavoriteRateText);
            rate.setText(String.valueOf(exchangeRate));

        }

        @Override                       //Type 2
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:

        }


    }

}
