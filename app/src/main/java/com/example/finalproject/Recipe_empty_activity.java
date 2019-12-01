package com.example.finalproject;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Recipe_empty_activity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "EMPTY_ACTIVITY: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_empty);

        Bundle bundle = new Bundle();
        bundle.putString(RecipeSearchActivity.ITEM_SELECTED, getIntent().getStringExtra(RecipeSearchActivity.ITEM_SELECTED));
        bundle.putString(RecipeSearchActivity.ITEM_URL, getIntent().getStringExtra(RecipeSearchActivity.ITEM_URL));
        bundle.putString(RecipeSearchActivity.ITEM_IMAGE_URL, getIntent().getStringExtra(RecipeSearchActivity.ITEM_IMAGE_URL));
        bundle.putString(RecipeSearchActivity.ITEM_RECIPE_ID, getIntent().getStringExtra(RecipeSearchActivity.ITEM_RECIPE_ID));

//        Recipe_detailFragment fragment = new Recipe_detailFragment();
//        fragment.setArguments(bundle);
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.detail_frameLayout, fragment);

        //Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        // So far the screen is blank

        Recipe_detailFragment dFragment = new Recipe_detailFragment();
        dFragment.setArguments(bundle); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }


    @Override
    public void onStart() {
        Log.e(ACTIVITY_NAME, "%%%%%%%%%%% onStart()");
        super.onStart();
        //CALL THE DATABASE HELPER CLASS
        RecipeDatabaseOpenHelper dbOpener = new RecipeDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();
        Toast.makeText(this, "Database Opened and Acquired!!", Toast.LENGTH_SHORT).show();

        //QUERY ALL THE RESULTS FROM THE DATABASE:
        String[] columns = {RecipeDatabaseOpenHelper.COL_ID, RecipeDatabaseOpenHelper.COL_URL, RecipeDatabaseOpenHelper.COL_IMAGE_URL,RecipeDatabaseOpenHelper.COL_TITLE};
        //INITIALIZE CURSORS
        Cursor cursor = db.query(false, RecipeDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
    }

}
