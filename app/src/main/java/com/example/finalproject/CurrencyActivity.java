package com.example.finalproject;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;



public class CurrencyActivity extends AppCompatActivity {
   Toolbar toolbar;
    private String convertFrom;
    private String convertTo;
    private double convertAmount;
    private EditText amount;
    private ProgressBar progress;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private int max = 100, current = 0, step = 0;
    private final static String[] CURRENCIES = new String[] {
            "USD", "CAD", "EUR", "JPY", "CNY", "INR", "HKD","KRW"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        progressBar=(ProgressBar) this.findViewById(R.id.progressBar);

       /* public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.toolbarmenu, menu);
            return true;
        }*/
       amount=findViewById(R.id.currencyAmountInput);

        amount.setOnClickListener(v->{
                    calculateExchange();
                }
                );
        progress = (ProgressBar) findViewById(R.id.currencyProgressBar);
        progress.setVisibility(View.GONE);

        Button enterButton=findViewById(R.id.CurrencyEnterButton);
        enterButton.setOnClickListener(clk->{
                    Toast.makeText( CurrencyActivity.this,
                            "You clicked on Enter Button" , Toast.LENGTH_SHORT).show();
                }
                );

        Button saveButton=findViewById(R.id.CurrencySaveButton);
        saveButton.setOnClickListener(clk->{
            Snackbar.make(saveButton,"Saved to your favorite list",Snackbar.LENGTH_SHORT).show();
                }
        );

        Button favoriteButton=findViewById(R.id.CurrencyFavoriteButton);
        favoriteButton.setOnClickListener(clk->{
                Intent goToFavoritePage = new Intent(CurrencyActivity.this, FavoriteActivity.class);
               // goToFavoritePage.putExtra("ReservedEmail", editText.getText().toString());
                startActivity(goToFavoritePage);
    }
        );



        Spinner from = (Spinner) findViewById(R.id.currencyFromSpinner);
        Spinner to = (Spinner) findViewById(R.id.currencyToSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CURRENCIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(adapter);
        to.setAdapter(adapter);
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);


                convertFrom=CURRENCIES[to.getSelectedItemPosition()];
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


                convertTo=CURRENCIES[to.getSelectedItemPosition()];
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

         mockProgessBar();
         result.setText("result");

     }
    }

    private void mockProgessBar(){

        progress.setMax(max);
        progress.setProgress(0);
        step = max / 10;

        new Thread(new Runnable() {
            int i = 1;

            @Override
            public void run() {

                try {
                    while (max != progress.getProgress()) {
                        Log.i("time", i + "");
                        i++;
                        progress.setProgress(current + step);
                        current = progress.getProgress();
                        Thread.sleep(10000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
