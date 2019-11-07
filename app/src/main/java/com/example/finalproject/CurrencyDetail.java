package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_currency_favorite_list_detail);

        MyDatabaseOpenHelper opener = new MyDatabaseOpenHelper(this);
        db =  opener.getWritableDatabase();
        fromPreviousPage = getIntent();
        cFrom = fromPreviousPage.getStringExtra("CurrencyFrom");
        String cTo = fromPreviousPage.getStringExtra("CurrencyTo");
        long id = fromPreviousPage.getLongExtra("Id", -1);
        Log.d("3333333333333333333333", cFrom);
        TextView cFromText=findViewById(R.id.currencyFavoriteFromText);
        cFromText.setText(cFrom);


        TextView cToText=findViewById(R.id.currencyFavoriteToText);
        cToText.setText(cTo);

        returnButton=(Button)findViewById(R.id.CurrencyReturnButton);
        returnButton.setOnClickListener(clk->{
            finish();
                } );

        deleteButton=(Button)findViewById(R.id.CurrencyDeleteButton);
        deleteButton.setOnClickListener(clk->{

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                //This is the builder pattern, just call many functions on the same object:
                AlertDialog dialog = builder.setTitle("Alert!")
                        .setMessage("Do you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
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
                        .setNegativeButton("Cancel", (d,w) -> {  })
                    .create();

                            //then show the dialog
                            dialog.show();

                        });
                }




}
