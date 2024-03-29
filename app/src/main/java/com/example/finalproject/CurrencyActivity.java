package com.example.finalproject;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * This class presents the main page for currency exchange. It accepts amount of the currency,
 * which currency converts from, and which currency converts to.
 * It will show the live currency exchange rate and the converted currency amount.
 */
public class CurrencyActivity extends AppCompatActivity {
    /**
     * The variables includes all the views in this page.
     */
    private Toolbar toolbar;
    private String convertFrom="USD";
    private String convertTo="USD";
    private double convertAmount;
    private EditText amount;
    private ProgressBar progress;
    private ProgressBar progressBar;
    private int indexfrom;
    private int indexto;
    private int[] previous;
    private Spinner from;
    private Spinner to;
    private SharedPreferences prefs;
    private Button saveButton;
    private Button homeButton;
    private Button enterButton;
    private EditText rate;
    private ArrayList<Currency> currencyList;
    private MyDatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;
    private Button favoriteButton;
    private double exchangeRate;
    private MyNetworkQuery myNetworkQuery;
    private HttpURLConnection urlConnection;
    private InputStream inStream;
    private Handler handler = new Handler();
    private int max = 100, current = 0, step = 0;
    private final static String[] CURRENCIES = new String[] {
            "USD", "CAD", "EUR", "JPY", "CNY", "INR", "HKD","KRW"
    };

    /**
     * This method starts the main activity on this currency page, creates all the information on this page.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CURRENCIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyList=new ArrayList<>();
        homeButton=findViewById(R.id.CurrencyHomeButton);
        progressBar=(ProgressBar) this.findViewById(R.id.progressBar);
       // progress=(ProgressBar) this.findViewById(R.id.currencyProgressBar);
        from = (Spinner) findViewById(R.id.currencyFromSpinner);
        to = (Spinner) findViewById(R.id.currencyToSpinner);
        from.setAdapter(adapter);
        to.setAdapter(adapter);
        amount=findViewById(R.id.currencyAmountInput);
        prefs = getSharedPreferences("FileName", MODE_PRIVATE);
        previous=new int[3];
        previous[0] = prefs.getInt("amount",0);
        previous[1] = prefs.getInt("currencyfromIndex", 0);
        previous[2] = prefs.getInt("currencytoIndex", 0);

        amount.setText(String.valueOf(previous[0]));
        //Log.d("hgfhgfhgfhgfh","="+adapter.getPosition("JPY"));
        from.setSelection(previous[1] );
        to.setSelection(previous[2]);

        dbHelper=new MyDatabaseOpenHelper(this);
        db=dbHelper.getWritableDatabase();

        //convertFrom=CURRENCIES[from.getSelectedItemPosition()];
       toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
      /*  public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menuitem, menu);
            return true;
        }
*/

        /*amount.setOnClickListener(v->{
                    calculateExchange();
                }
        );*/
        progress = (ProgressBar) findViewById(R.id.currencyProgressBar);
       // progress.setVisibility(View.GONE);

        rate=findViewById(R.id.currencyRateOutput);
        enterButton=findViewById(R.id.CurrencyEnterButton);
        enterButton.setOnClickListener(clk->{
            calculateExchange();
                    Toast.makeText( CurrencyActivity.this,
                            R.string.currency_toast_enterButton , Toast.LENGTH_SHORT).show();
                }
        );

        saveButton=findViewById(R.id.CurrencySaveButton);
        saveButton.setOnClickListener(clk->{

                    Snackbar.make(saveButton,R.string.currency_snackbar_saveButton,Snackbar.LENGTH_SHORT).show();
                    ContentValues cv=new ContentValues();
                    cv.put(MyDatabaseOpenHelper.COL_FROM, convertFrom );
                    cv.put(MyDatabaseOpenHelper.COL_TO, convertTo);
                    long id=db.insert(MyDatabaseOpenHelper.TABLE_NAME,null, cv);
                    Currency newCurrency=new Currency(convertFrom,convertTo,id);
                    currencyList.add(newCurrency);
        }
        );

        favoriteButton=findViewById(R.id.CurrencyFavoriteButton);
        favoriteButton.setOnClickListener(clk->{
                    Intent goToFavoritePage = new Intent(CurrencyActivity.this, CurrencyFavoriteActivity.class);
                    // goToFavoritePage.putExtra("ReservedEmail", editText.getText().toString());
                    startActivity(goToFavoritePage);
                }
        );

        homeButton.setOnClickListener(clk->{
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("Alert");
            normalDialog.setMessage("Do you want to exit this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

             normalDialog.show();

        });


        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                indexfrom=from.getSelectedItemPosition();
                convertFrom=CURRENCIES[indexfrom];
                Log.d("from=",convertFrom);
               // calculateExchange();

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                calculateExchange();
            }

        });


        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                indexto=to.getSelectedItemPosition();
                convertTo=CURRENCIES[indexto];
                Log.d("to=",convertTo);
               // calculateExchange();
                progress.setVisibility(View.GONE);

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                calculateExchange();
            }

        });


        // get selected item position
        //int selectedPosition = adapter.getSelectedItemPosition();
    }

    /**
     * THis method create the menu item on the top of the page
     * @param menu
     * @return true if there is no problem on create the menu item, return false if it fails
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem, menu);
        return true;
    }

    /**
     * THis method leads to the menu item choices
     * @param item
     * @return true if any of the items has been chosen
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.choice4:
                alertExample();
                break;
            case R.id.choice5:
                startActivity(new Intent(CurrencyActivity.this, CurrencyFavoriteActivity.class));

                break;
            case R.id.choice1:
                startActivity(new Intent(CurrencyActivity.this, ECCSFmain.class));
                break;
            case R.id.choice2:
                startActivity(new Intent(CurrencyActivity.this, News_Activity_Main.class));
                break;
            case R.id.choice3:
                startActivity(new Intent(CurrencyActivity.this, RecipeSearchActivity.class));

                break;
        }
        return true;
    }

    /**
     * This is the alert message for the Home button
     */
    public void alertExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.currency_view_extra_stuff, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(R.string.currency_help_menu_message);
      /*  .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText et = (EditText)middle.findViewById(R.id.view_edit_text);
                Log.d("111111111111","2222222");
                et.setText("You clicked on the overflow menu");
            }
        })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);*/

        builder.create().show();
    }

    /**
     * this is a private class for asynctask which builds the connection to the URL and fetch data
     */
    private class MyNetworkQuery extends AsyncTask<String, String, String> {
        /**
         * this method starts a thread run on the background to build connection and fetch data.
          * @param strings
         * @return a string ret
         */
        @Override                       //Type 1
            protected String doInBackground(String ... strings) {
            try
            {
                Thread.sleep( 2 * 1000 );
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
                String ret = null;
                String queryURL = "https://api.exchangeratesapi.io/latest?base="+convertFrom+"&symbols="+convertTo;

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
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject jsonObject=new JSONObject(result);
                JSONObject rateObject=jsonObject.getJSONObject("rates");
                exchangeRate = rateObject.getDouble(convertTo);
                Log.d("111111111111111rate",""+exchangeRate);

            }
            catch(MalformedURLException mfe){
                mfe.printStackTrace();
            }
            catch(IOException ioe)          {
                    ioe.printStackTrace();
            }
            catch (JSONException e) {
                    e.printStackTrace();
            }
            //What is returned here will be passed as a parameter to onPostExecute:
            Log.d("111111111111111rate",""+exchangeRate);
            return ret;
        }

        /**
         * This method calls the setResult method to set the view on the screen
         * @param sentFromDoInBackground
         */
        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:
            setResult();

        }

        /**
         * This method will be called after onbackground.
         * @param values
         */
        @Override                       //Type 2
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:

        }
    }

    /**
     * This method calcuate the amount into the target currency and initializes the asyntask
     */
    private void calculateExchange(){
        if(amount.getText().toString().isEmpty())return;
        progress.setVisibility(View.VISIBLE);
        convertAmount=Double.valueOf(amount.getText().toString());
        Log.d("22222222222 amount",convertAmount+"");
        if (convertFrom!=null&&convertTo!=null){
            myNetworkQuery=new MyNetworkQuery();
            myNetworkQuery.execute();
                    }
//    try {
//        Thread.sleep(6000);
//    }catch(Throwable t){
//
//    }
//
//        progress.setVisibility(View.GONE);
//


    }

    /**
     * This method sets the result to the screen
     */
    private void setResult(){
        Log.d("calculate=","from="+convertFrom+" to="+convertTo);
        Log.d("rate in calculate",""+exchangeRate);
        EditText result=findViewById(R.id.currencyResultOutput);

        double convertResult=exchangeRate*convertAmount;
        Log.d("2222222222result",convertResult+"");
        //mockProgessBar();
        result.setText(String.valueOf(convertResult));
        rate.setText(String.valueOf(exchangeRate));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("amount",Integer.valueOf(amount.getText().toString()));
        editor.putInt("currencyfromIndex", indexfrom);
        Log.d("indexfrom value", String.valueOf(indexfrom));
        editor.putInt("currencytoIndex", indexto);
        Log.d("indexto value", String.valueOf(indexto));
       // editor.putString("Rate", String.valueOf(exchangeRate));
        editor.commit();
        progress.setVisibility(View.INVISIBLE);
    }



}
