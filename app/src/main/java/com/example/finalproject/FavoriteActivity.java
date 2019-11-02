package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    ListView theList;
    ArrayList<Currency> objects = new ArrayList<Currency>( );
    BaseAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_favorite);

        theList = findViewById(R.id.currency_favorite_list);
        theList.setAdapter( myAdapter = new MyListAdapter() );




    }


    private class MyListAdapter extends BaseAdapter {

        // public MyListAdapter(Context context, int )
        public int getCount() {
            return objects.size();
        } //This function tells how many objects to show

        public Currency getItem(int position) {
            return objects.get(position);
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
