package com.example.finalproject;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
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

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;


public class CurrencyActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String convertFrom;
    private String convertTo;
    private double convertAmount;
    private EditText amount;
    private ProgressBar progress;
    private ProgressBar progressBar;
    private int indexfrom;
    private int indexto;
    int[] previous;
    private Spinner from;
    private Spinner to;
    SharedPreferences prefs;
    private Button saveButton;
    private Button homeButton;
    ArrayList<Currency> currencyList;
    MyDatabaseOpenHelper dbHelper;
    SQLiteDatabase db;
    private Button favoriteButton;

    private Handler handler = new Handler();
    private int max = 100, current = 0, step = 0;
    private final static String[] CURRENCIES = new String[] {
            "USD", "CAD", "EUR", "JPY", "CNY", "INR", "HKD","KRW"
    };

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
        Log.d("hgfhgfhgfhgfh","="+adapter.getPosition("JPY"));
        from.setSelection(previous[1] );
        to.setSelection(previous[2]);

        dbHelper=new MyDatabaseOpenHelper(this);
        db=dbHelper.getWritableDatabase();








        //convertFrom=CURRENCIES[from.getSelectedItemPosition()];
       // toolbar = (Toolbar) findViewById(R.id.my_toolbar);
      //  setSupportActionBar(toolbar);
       /* public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.toolbarmenu, menu);
            return true;
        }*/


        amount.setOnClickListener(v->{
                    calculateExchange();
                }
        );
        progress = (ProgressBar) findViewById(R.id.currencyProgressBar);
       // progress.setVisibility(View.GONE);

        Button enterButton=findViewById(R.id.CurrencyEnterButton);
        enterButton.setOnClickListener(clk->{
                    Toast.makeText( CurrencyActivity.this,
                            "You clicked on Enter Button" , Toast.LENGTH_SHORT).show();
                }
        );

        saveButton=findViewById(R.id.CurrencySaveButton);
        saveButton.setOnClickListener(clk->{
            //Log.d("llllllllllllllll","dddddd");
                    Snackbar.make(saveButton,"Saved to your favorite list",Snackbar.LENGTH_SHORT).show();
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





        //123getValueFromPref();



        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                indexfrom=from.getSelectedItemPosition();
                convertFrom=CURRENCIES[indexfrom];
                Log.d("from=",convertFrom);
                calculateExchange();

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });

        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                indexto=to.getSelectedItemPosition();
                convertTo=CURRENCIES[indexto];
                Log.d("to=",convertTo);
                calculateExchange();
                progress.setVisibility(View.GONE);

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });


        // get selected item position
        //int selectedPosition = adapter.getSelectedItemPosition();
    }
    private void calculateExchange(){
        if(amount.getText().toString().isEmpty())return;
        progress.setVisibility(View.VISIBLE);
        convertAmount=Double.valueOf(amount.getText().toString());
        if (convertFrom!=null&&convertTo!=null){
            Log.d("calculate=","from="+convertFrom+" to="+convertTo);
            EditText result=findViewById(R.id.currencyResultOutput);

            //mockProgessBar();
            result.setText("result");
             SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("amount",Integer.valueOf(amount.getText().toString()));
            editor.putInt("currencyfromIndex", indexfrom);
            Log.d("indexfrom value", String.valueOf(indexfrom));
            editor.putInt("currencytoIndex", indexto);
            Log.d("indexto value", String.valueOf(indexto));
            editor.commit();


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

    private void mockProgessBar(){



        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                        Thread.sleep(10000);
                    //progress.setVisibility(View.GONE);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
