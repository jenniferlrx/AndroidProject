package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CurrencyFavoriteActivity extends AppCompatActivity {
    ListView theList;
    //ArrayList<Currency> objects = new ArrayList<Currency>( );
    BaseAdapter myAdapter;
    MyDatabaseOpenHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<Currency> currencies;
    CurrencyActivity a=new CurrencyActivity();
    //Button deleteButton;
    //Button currencyDetailButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_favorite);

        theList = findViewById(R.id.currency_favorite_list);
        //currencyDetailButton=(Button)findViewById(R.id.currencyDetailButton);

        currencies=new ArrayList<>();
        dbHelper=new MyDatabaseOpenHelper(this);
        db=dbHelper.getWritableDatabase();
        String[] columns={MyDatabaseOpenHelper.COL_ID,MyDatabaseOpenHelper.COL_FROM,MyDatabaseOpenHelper.COL_TO};
        Cursor results=db.query(false,MyDatabaseOpenHelper.TABLE_NAME,columns, null, null, null, null,null,null );
        int colFrom=results.getColumnIndex(MyDatabaseOpenHelper.COL_FROM);
        int colTo=results.getColumnIndex(MyDatabaseOpenHelper.COL_TO);
        int colId=results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        while(results.moveToNext()){
            String columnFrom=results.getString(colFrom);
            String columnTo=results.getString(colTo);
            long id=results.getLong(colId);
            currencies.add(new Currency(columnFrom,columnTo,id));
        }
        theList.setAdapter( myAdapter = new MyListAdapter() );
        /*theList.setOnItemClickListener((parent,view,position, id)->{
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("DETAILS");
            normalDialog.setMessage("this is a detail information");
            normalDialog.show();

        });*/
        theList.setOnItemClickListener((parent,view,position, id)->{
            Intent goToDetailPage=new Intent(this, CurrencyDetail.class);




        });
        /*currencyDetailButton=(Button)findViewById(R.id.currencyDetailButton);
        currencyDetailButton.setOnClickListener(clk->{
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

        });*/
        /*deleteButton=findViewById(R.id.currencyDeleteButton);
        deleteButton.setOnClickListener(clk->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //This is the builder pattern, just call many functions on the same object:
            AlertDialog dialog = builder.setTitle("Alert!")
                    .setMessage("Do you want to delete?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //If you click the "Delete" button
                            Currency c= (Currency) myAdapter.getItem(which);
                            int numDeleted = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[] {Long.toString(c.getcId())});

                            Log.i("ViewContact", "Deleted " + numDeleted + " rows");
                            currencies.remove(c);
                            myAdapter.notifyDataSetChanged();
                            //set result to PUSHED_DELETE to show clicked the delete button
                            //setResult(PUSHED_DELETE);
                            //go back to previous page:
                            finish();
                        }
                    })
                    //If you click the "Cancel" button:
                    .setNegativeButton("Cancel", (d,w) -> {  *//* nothing *//*})
                    .create();

            //then show the dialog
            dialog.show();

        });*/

    }


    private class MyListAdapter extends BaseAdapter {

        // public MyListAdapter(Context context, int )
        public int getCount() {
            return currencies.size();
        } //This function tells how many objects to show

        public Currency getItem(int position) {
            return currencies.get(position);
        }  //This returns the Message object at position p

        public long getItemId(int p) {
            return p;
        }

        public View getView(int p, View recycled, ViewGroup parent) {
            View thisRow = null;//recycled;
            LayoutInflater inflater = getLayoutInflater();
            thisRow = inflater.inflate(R.layout.currency_favorite_detail, null);
            TextView itemfrom = thisRow.findViewById(R.id.currencyFrom);
            itemfrom.setText(getItem(p).getcFrom());
            TextView itemto = thisRow.findViewById(R.id.currencyTo);
            itemto.setText(getItem(p).getcTo());
            return thisRow;


        }
    }



}
