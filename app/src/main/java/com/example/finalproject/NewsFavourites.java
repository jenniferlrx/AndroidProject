package com.example.finalproject;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;


public class NewsFavourites extends AppCompatActivity {
    private ArrayList<NewsArticleObject> newsArticleFavouriteList;
    private ListView newsArticleFavouritesListView;
    private Button deleteFromFavourites;
    private NewsArticleAdapter adapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_favourites);

        deleteFromFavourites = findViewById(R.id.gobackToMainButton);

        newsArticleFavouriteList = new ArrayList<>();
        newsArticleFavouritesListView = findViewById(R.id.favourites_list_view);
        adapter = new NewsArticleAdapter(this, R.layout.activity_news_row, newsArticleFavouriteList);
        newsArticleFavouritesListView.setAdapter(adapter);


        NewsDatabaseOpenHelper dbOpener = new NewsDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        loadFavourites();

        newsArticleFavouritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsArticleObject item = (NewsArticleObject) parent.getItemAtPosition(position);
                Bundle dataToPass = new Bundle();
                dataToPass.putSerializable("Article", item);

                Intent nextActivity = new Intent(NewsFavourites.this, News_Empty_Activity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition


            }
        });
        newsArticleFavouritesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {


                AlertDialog.Builder builder = new AlertDialog.Builder(NewsFavourites.this);
                AlertDialog dialog = builder.setTitle("Alert!")
                        .setMessage("Really delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String test = "" + pos;
                                Log.v("position", test);
                                int query = db.delete(MyDatabaseOpenHelper.TABLE_NAME,
                                        MyDatabaseOpenHelper.COL_ID + "=?", new String[]{Long.toString(newsArticleFavouriteList.get(pos).getId())});
                                newsArticleFavouriteList.remove(pos);
                                adapter.notifyDataSetChanged();
                                //showSnackbar(menu, ("Article Deleted"), LENGTH_SHORT);

                            }
                        })
                        .setNegativeButton("Cancel", (d, w) -> {  /* nothing */})
                        .create();
                dialog.show();


                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {




            case R.id.overflow_help:
                AlertDialog.Builder helpAlertBuilder = new AlertDialog.Builder(NewsFavourites.this);
                helpAlertBuilder.setTitle("Help");
                helpAlertBuilder.setMessage(R.string.help);
                helpAlertBuilder.show();
                break;


        }


        return true;
    }
/*
    public void startRecipeActivity() {
        Intent recipeIntent = new Intent(this, RecipeFinder.class);
        startActivity(recipeIntent);

    }

    public void startCarChargingActivity() {
        Intent chargingActivity = new Intent(this, CarChargingStation.class);
        startActivity(chargingActivity);
    }

    public void startCurrencyActivity() {
        Intent currencyActivity = new Intent(this, CurrencyConverter.class);
        startActivity(currencyActivity);
    }
*/


    private void loadFavourites() {
        String[] columns = {NewsDatabaseOpenHelper.COL_ID, NewsDatabaseOpenHelper.COL_TITLE, NewsDatabaseOpenHelper.COL_DESCRIPTION,
                NewsDatabaseOpenHelper.COL_ARTICLEURL, NewsDatabaseOpenHelper.COL_IMAGEURL};
        Cursor results = db.query(false, NewsDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        int titleColumnIndex = results.getColumnIndex(NewsDatabaseOpenHelper.COL_TITLE);
        int descriptionColumnIndex = results.getColumnIndex(NewsDatabaseOpenHelper.COL_DESCRIPTION);
        int idColumnIndex = results.getColumnIndex(NewsDatabaseOpenHelper.COL_ID);
        int articleUrlColumnIndex = results.getColumnIndex(NewsDatabaseOpenHelper.COL_ARTICLEURL);
        int imageUrlColumnIndex = results.getColumnIndex(NewsDatabaseOpenHelper.COL_IMAGEURL);
        while (results.moveToNext()) {

            String title = results.getString(titleColumnIndex);
            String description = results.getString(descriptionColumnIndex);
            String articleUrl = results.getString(articleUrlColumnIndex);
            String imageUrl = results.getString(imageUrlColumnIndex);
            long id = results.getLong(idColumnIndex);


            newsArticleFavouriteList.add(new NewsArticleObject(id, title, articleUrl, imageUrl, description));
        }
        adapter.notifyDataSetChanged();
    }

    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
