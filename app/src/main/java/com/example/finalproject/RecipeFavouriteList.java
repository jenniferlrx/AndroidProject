package com.example.finalproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeFavouriteList extends AppCompatActivity {
    private static final String TAG = "NutritionFavouriteList";
    private RecipeDatabaseOpenHelper recipeDatabaseHelper;
    private ListView fListView;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<MyRecipe> listData;
    private MyOwnAdapter myAdapter;
//    private ArrayAdapter adapter;
    public static String selectedTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_favlist);
        fListView = (ListView) findViewById(R.id.favListView);

        Log.d(TAG, "populateListView: Displaying data in the ListView ");
        recipeDatabaseHelper = new RecipeDatabaseOpenHelper(this);
        Cursor data = recipeDatabaseHelper.getDataFromDB();
        int titleColumnIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_TITLE);
        int urlColIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_URL);
        int imgurlColIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_IMAGE_URL);
        int recipeidColIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_RECIPE_ID);

        listData = new ArrayList<>();
        while(data.moveToNext()){
            String title = data.getString(titleColumnIndex);
            String url = data.getString(urlColIndex);
            String imgurl = data.getString(imgurlColIndex);
            String recipeid = data.getString(recipeidColIndex);
            listData.add(new MyRecipe(title,url,imgurl,recipeid));
        }
//        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData); // show the items on the list view
//        fListView.setAdapter(adapter);

        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTitle = parent.getItemAtPosition(position).toString();
            }
        });
        myAdapter = new MyOwnAdapter();
        myAdapter.notifyDataSetChanged();
    }

    //This class needs 4 functions to work properly:
    protected class MyOwnAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return listData.size();
        }

        public MyRecipe getItem(int position){
            return listData.get(position);
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.activity_recipe_row, parent, false );

            MyRecipe thisRow = getItem(position);
            TextView rowName = (TextView)newView.findViewById(R.id.recipe_fav_row);

            rowName.setText("Name:" + thisRow.getTITLE());
            return newView;
        }

        public long getItemId(int position)
        {
            return getItem(position).getID();
        }
    }

}
