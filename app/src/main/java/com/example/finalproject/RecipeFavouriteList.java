package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

/**
 * favrouite recipe list
 */
public class RecipeFavouriteList extends AppCompatActivity {
    private static final String TAG = "NutritionFavouriteList";
    private RecipeDatabaseOpenHelper recipeDatabaseHelper = new RecipeDatabaseOpenHelper(this);;
    private ListView fListView;
//    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<MyRecipe> listData;
    private MyOwnAdapter myAdapter = new MyOwnAdapter();
    public static String selectedTitle;
    public TextView rowName;
    public static int EMPTY_ACTITY = 345;
    private RecipeSearchActivity search = new RecipeSearchActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_favlist);
        fListView = (ListView) findViewById(R.id.favListView);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        Log.d(TAG, "populateListView: Displaying data in the ListView ");
        recipeDatabaseHelper = new RecipeDatabaseOpenHelper(this);
        Cursor data = recipeDatabaseHelper.getDataFromDB();
        int titleColumnIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_TITLE);
        int urlColIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_URL);
        int imgurlColIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_IMAGE_URL);
        int recipeidColIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_RECIPE_ID);
        int idColIndex = data.getColumnIndex(RecipeDatabaseOpenHelper.COL_ID);

        listData = new ArrayList<>();
        while(data.moveToNext()){
            String title = data.getString(titleColumnIndex);
            String url = data.getString(urlColIndex);
            String imgurl = data.getString(imgurlColIndex);
            String recipeid = data.getString(recipeidColIndex);
            int id = data.getInt(idColIndex);
            myAdapter.addData(title,url,imgurl,recipeid, id);
        }
        fListView.setAdapter(myAdapter);

        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle dataToPass = new Bundle();

                dataToPass.putInt(RecipeSearchActivity.ITEM_POSITION, position);

                dataToPass.putInt(RecipeSearchActivity.ITEM_ID, listData.get(position).getID());
                dataToPass.putString(RecipeSearchActivity.ITEM_SELECTED, listData.get(position).getTITLE());
                dataToPass.putString(RecipeSearchActivity.ITEM_RECIPE_ID, listData.get(position).getRECIPEID() );
                dataToPass.putString(RecipeSearchActivity.ITEM_URL, listData.get(position).getURL());
                dataToPass.putString(RecipeSearchActivity.ITEM_IMAGE_URL, listData.get(position).getIMAGE_URL());
                dataToPass.putString(RecipeSearchActivity.ITEM_ACTIVITY_CALLING, "RecipeFavouriteList");


                if(isTablet)
                {
                    Recipe_detailFragment dFragment = new Recipe_detailFragment(); //add a DetailFragment
                    dFragment.setArguments( dataToPass ); //pass it a bundle for information
                    dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                            .addToBackStack("AnyName") //make the back button undo the transaction
                            .commit(); //actually load the fragment.
                }
                else //isPhone
                {
                    Intent nextActivity = new Intent(RecipeFavouriteList.this, Recipe_empty_activity.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivityForResult(nextActivity, EMPTY_ACTITY); //make the transition
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMPTY_ACTITY) {

            if (resultCode == Recipe_detailFragment.RESULT_DELETE) {
                int id = data.getIntExtra(RecipeSearchActivity.ITEM_ID, -1);
                deleteMessageId(id);
            }
        }
    }


    /**
     * delete row according to row id
     * @param id
     */
    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        listData.remove(id);
        myAdapter.notifyDataSetChanged();

        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        if(!recipeDatabaseHelper.deleteRow(db, id)){
            Log.e("RecipeFav", "delete false");
        };
    }

    //This class needs 4 functions to work properly:

    /**
     * adapter class
     */
    public class MyOwnAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public MyRecipe getItem(int position){
            return listData.get(position);
        }

        @Override
        public View getView(int position, View oldView, ViewGroup parent)
        {
            MyRecipe thisRow = getItem(position);
            if(oldView == null) {
                LayoutInflater inflater = getLayoutInflater();
                oldView = inflater.inflate(R.layout.activity_recipefav_row, null);
                rowName = oldView.findViewById(R.id.recipe_fav_row);
                rowName.setText(thisRow.getTITLE());
            }
            return oldView;
        }
        @Override
        public long getItemId(int position)
        {
            return getItem(position).getID();
        }

        public void addData(String title, String url, String imgurl, String recipeid, int dbID){
            listData.add(new MyRecipe(title, url, imgurl,recipeid, dbID ));
            notifyDataSetChanged();
        }
    }

}
