package com.example.finalproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


/**
 * class to send bundles from empty activity to the fragment
 */
public class Recipe_empty_activity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "EMPTY_ACTIVITY: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_empty);

        Bundle bundle = new Bundle();
        bundle.putInt(RecipeSearchActivity.ITEM_ID, getIntent().getIntExtra(RecipeSearchActivity.ITEM_ID, -1) );
        bundle.putString(RecipeSearchActivity.ITEM_SELECTED, getIntent().getStringExtra(RecipeSearchActivity.ITEM_SELECTED));
        bundle.putString(RecipeSearchActivity.ITEM_URL, getIntent().getStringExtra(RecipeSearchActivity.ITEM_URL));
        bundle.putString(RecipeSearchActivity.ITEM_IMAGE_URL, getIntent().getStringExtra(RecipeSearchActivity.ITEM_IMAGE_URL));
        bundle.putString(RecipeSearchActivity.ITEM_RECIPE_ID, getIntent().getStringExtra(RecipeSearchActivity.ITEM_RECIPE_ID));
        bundle.putString(RecipeSearchActivity.ITEM_ACTIVITY_CALLING, getIntent().getStringExtra(RecipeSearchActivity.ITEM_ACTIVITY_CALLING));

        Recipe_detailFragment dFragment = new Recipe_detailFragment();
        dFragment.setArguments(bundle); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }

}
