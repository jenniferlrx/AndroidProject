package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RecipeSearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button btnSearch;
    private ListView liistView;
    private ProgressBar loading;
    private RecipeJSONAdapter recipeAdapter;
    private ListView listView;
    protected static final String Activity_NAME = "RecipeSearchActivity";
    private String app_key = "fdfc2f97466caa0f5b142bc3b913c366";
    private static String food;
    private List<MyRecipe> myRecipe = new ArrayList<>();
    private MyDatabaseOpenHelper myHelper = new MyDatabaseOpenHelper(this);
    private String jsonUrl = "https://www.food2fork.com/api/search?key="+ app_key+ "&q=" + food+ "%20";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);
        listView = (ListView) findViewById(R.id.list_result);
        searchEditText = (EditText) findViewById(R.id.recipe_search);
        btnSearch = (Button) findViewById(R.id.recipe_searchButton);
        loading = (ProgressBar) findViewById(R.id.progressBar);

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                openDialog();
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Snackbar.make(view, "The First SnackBar Button was clicked.", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    /**
     * call dialogclass and show dialog
     * */
    public void openDialog(){
        DialogClass dialog = new DialogClass();
        dialog.show(getSupportFragmentManager(),"dialog box");
    }

    public void toastMsg(String msg){
        Toast toast= Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

    public void addData(String title, String url){

    }

    private class RecipeAsyncTask extends AsyncTask<String, Integer, List<MyRecipe>>{
        public String jsonUrl = "https://www.food2fork.com/api/search?key="+ app_key+ "&q=" + food+ "%20";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.getItem(4).setVisible(false);

        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // four applications
            case R.id.menuItemCar:
                startActivity(new Intent(RecipeSearchActivity.this, ECCSFmain.class));
                break;
//            case R.id.menuItemRecipe:
//                startActivity(new Intent(RecipeSearchActivity.this, .class));
//                break;
            case R.id.menuItemCurrency:
                startActivity(new Intent(RecipeSearchActivity.this, CurrencyActivity.class));
                break;
            case R.id.menuItemNews:
                startActivity(new Intent(RecipeSearchActivity.this, News_Activity_Main.class));
                break;
            //saved for each application, hide in this page
            case R.id.saved:
                break;
            //show author, version, help instruction
            case R.id.help:
                break;
        }
        return true;
    }

}
