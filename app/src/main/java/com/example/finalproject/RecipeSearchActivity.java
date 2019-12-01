package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RecipeSearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button btnSearch;
    private ProgressBar loading;
    private RecipeJSONAdapter recipeAdapter;
    private ListView listView;
    protected static final String Activity_NAME = "RecipeSearchActivity";
    private String app_key = "fdfc2f97466caa0f5b142bc3b913c366";
    private String food;
    private List<MyRecipe> myRecipe = new ArrayList<>();
    private RecipeDatabaseOpenHelper myHelper = new RecipeDatabaseOpenHelper(this);
//    private String jsonUrl = "http://torunski.ca/FinalProject"+ food + ".json";
    private String jsonUrl = "https://www.food2fork.com/api/search?key=fdfc2f97466caa0f5b142bc3b913c366&q="+food+"%20";
    private SharedPreferences sharedPreferences;

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ITEM_URL = "URL";
    public static final String ITEM_IMAGE_URL = "IMAGE_URL";
    public static final String ITEM_RECIPE_ID = "recipeID";
    public static final String ITEM_ACTIVITY_CALLING = "Activity";
    public static final int EMPTY_ACTIVITY = 345;
    /**
     * set content view, set sharedperferences and set all button click action
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        listView = (ListView) findViewById(R.id.list_result);
        searchEditText = (EditText) findViewById(R.id.recipe_search);
        btnSearch = (Button) findViewById(R.id.recipe_searchButton);
        loading = (ProgressBar) findViewById(R.id.progressBar);

        //read from file
        sharedPreferences = getSharedPreferences("searchHistory", MODE_PRIVATE);
        String search = sharedPreferences.getString("userSearch", "");
        searchEditText.setText(search);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                food = searchEditText.getText().toString();

                if(food!=null && !food.isEmpty()){
                    new RecipeAsyncTask().execute(jsonUrl);
                }else{
                    toastMsg(getString(R.string.prompt_to_enter));
                }
            }
        });

        // START FRAGMENT
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        listView.setOnItemClickListener((parent, view, position, id) -> {

//            MyRecipe recipe =(MyRecipe) myRecipe.get(position);

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, myRecipe.get(position).getTITLE());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putString(ITEM_RECIPE_ID, myRecipe.get(position).getRECIPEID() );
            dataToPass.putLong(ITEM_ID, id);
            dataToPass.putString(ITEM_ACTIVITY_CALLING, "RecipeSearchActivity");

            dataToPass.putString(ITEM_URL, myRecipe.get(position).getURL());
            dataToPass.putString(ITEM_IMAGE_URL, myRecipe.get(position).getIMAGE_URL());


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
                Intent nextActivity = new Intent(RecipeSearchActivity.this, Recipe_empty_activity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
//            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
//            {
//                long id = data.getLongExtra(ITEM_ID, 0);
//                deleteMessageId((int)id);
            }if(resultCode == Recipe_detailFragment.RESULT_SAVE){
                String title = data.getExtras().getString(ITEM_SELECTED);
                String url = data.getStringExtra(ITEM_URL);
                String imgurl = data.getExtras().getString(ITEM_IMAGE_URL);
                String recipeid = data.getStringExtra(ITEM_RECIPE_ID);
                boolean insertData = addData(title,url,imgurl,recipeid);
                    if (insertData) {
                        toastMsg(getString(R.string.recipe_insert));
                    } else {
                        toastMsg(getString(R.string.recipe_insert_error));
                    }
            }
    }

    /**
     * set text on search text
     */
    @Override
    protected void onPause(){
        super.onPause();
        //save user input
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userSearch", searchEditText.getText().toString());
        editor.commit();
    }

    /**
     * call dialogclass and show dialog
     * */
    public void openDialog(){
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("This is a recipe search API");
        normalDialog.setMessage("You can put key word to search in the search engine, and click item from the recipe list for details. ")
                .setPositiveButton("Ok, got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        normalDialog.create().show();
    }

    /**
     * set toast massage
     * @param msg user defined toast string
     */
    public void toastMsg(String msg){
        Toast toast= Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

    /**
     * add data to db
     * @param title
     * @param url
     */

    public boolean addData(String title, String url, String imgUrl, String recipeID){
        boolean insertData = myHelper.addData(title, url, imgUrl, recipeID);
        return insertData;
    }

    /**
     * get list
     * @return
     */
    public List<MyRecipe> getData(){
        return myRecipe;
    }

    /**
     * doing async call to get data from website
     */
    public class RecipeAsyncTask extends AsyncTask<String, Integer, List<MyRecipe>>{
//        private String jsonUrl = "http://torunski.ca/FinalProject"+ food+ ".json";
        private String jsonUrl = "https://www.food2fork.com/api/search?key=fdfc2f97466caa0f5b142bc3b913c366&q="+food+"%20";
        public RecipeJSONdata jsonData = new RecipeJSONdata();

        @Override
        protected List<MyRecipe> doInBackground(String... strings) {
            myRecipe = jsonData.getJSONdata(jsonUrl);
            return myRecipe;
        }

        @Override
        public void onProgressUpdate(Integer... results){
            super.onProgressUpdate();
            loading.setVisibility(View.VISIBLE);
            loading.setProgress(results[0]);
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<MyRecipe> result){
            super.onPostExecute(result);
            recipeAdapter = new RecipeJSONAdapter(RecipeSearchActivity.this, result);
            listView.setAdapter(recipeAdapter);
            recipeAdapter.notifyDataSetChanged();
            loading.setVisibility(View.INVISIBLE);
        }
    }

    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        myRecipe.remove(id);
        recipeAdapter.notifyDataSetChanged();
    }

    /**
     * create  option menu
     * @param menu set menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);
//        menu.getItem(5).setVisible(false);
        return true;
    }

    /**
     *set selected action on menu
     * @param item set item clickable
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // four applications
            case R.id.menuItemCar:
                startActivity(new Intent(RecipeSearchActivity.this, ECCSFmain.class));
                break;
            case R.id.menuItemRecipe:
                startActivity(new Intent(RecipeSearchActivity.this, RecipeSearchActivity.class));
                break;
            case R.id.menuItemCurrency:
                startActivity(new Intent(RecipeSearchActivity.this, CurrencyActivity.class));
                break;
            case R.id.menuItemNews:
                startActivity(new Intent(RecipeSearchActivity.this, News_Activity_Main.class));
                break;
            //saved for each application, hide in this page
            case R.id.saved:
                startActivity(new Intent(RecipeSearchActivity.this, RecipeFavouriteList.class));
                break;
            //show author, version, help instruction
            case R.id.help:
                openDialog();
                break;
        }
        return true;
    }
}
