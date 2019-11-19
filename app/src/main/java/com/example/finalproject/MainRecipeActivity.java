package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class RecipeSearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button btnSearch, btnFavourite;
    private ListView liistView;
    private ProgressBar loading = null;
    private RecipeJSONAdapter recipeAdapter;
    protected static final String Activity_NAME = "RecipeSearchActivity";
    private String app_key = "fdfc2f97466caa0f5b142bc3b913c366";
    private static String food;
    private List<MyRecipe> newBeanList = new ArrayList<>();
    private MyDatabaseOpenHelper myHelper = new MyDatabaseOpenHelper(this);
    private String jsonUrl = "https://www.food2fork.com/api/search?key="+ app_key+ "&q=" + food+ "%20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);
        ListView listView = (ListView) findViewById(R.id.recipe_list);
        searchEditText = (EditText) findViewById(R.id.recipe_search);
        btnSearch = (Button) findViewById(R.id.recipe_searchButton);
        btnFavourite = (Button) findViewById(R.id.btn_favourite);
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

        btnFavourite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeSearchActivity.this, RecipeFavouriteList.class);
                startActivity(intent);
            }
        });



//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                openDialog();
//            }
//        });
//        Button button2 = (Button) findViewById(R.id.button2);
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

    private class RecipeAsyncTask extends AsyncTask<String, void, List<MyRecipe>>{
        public String jsonUrl = "https://www.food2fork.com/api/search?key="+ app_key+ "&q=" + food+ "%20";


        @Override
        protected List<MyRecipe> doInBackground(String... strings) {

            return MyRecipe = jsonData.getJsonDate();
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loading.setProgress(25);
        }

        @Override
        protected void onPostExecute(List<MyRecipe> result){
            super.onPostExecute(result);

        }
    }

}
