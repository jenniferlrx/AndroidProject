package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;


/**
 * Main News Activity class that provides the interface for user to search news
 * author: Shengqiang Huang
 */
public class News_Activity_Main extends AppCompatActivity {

    private NewsArticleAdapter adapter;
    private ArrayList<NewsArticleObject> newsArticleList;
    private ListView newsArticleListView;
    private Button searchButton;
    private Button favouritesButton;
    private EditText searchEditText;
    private String NEWS_URL;
    private ProgressBar myProgressBar;
    private Toolbar main_menu;
    private SharedPreferences sharedPref;

    /**
     * onCreate method to run the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        myProgressBar = findViewById(R.id.progress_bar);
        myProgressBar.setVisibility(View.VISIBLE);

        newsArticleList = new ArrayList<>();
        searchEditText = findViewById(R.id.search_editText);
        searchButton = findViewById(R.id.searchButton);
        favouritesButton = findViewById(R.id.go_to_favorites);
        newsArticleListView = findViewById(R.id.articlesListView);

        main_menu = findViewById(R.id.main_menu_news);

        setSupportActionBar(main_menu);

        adapter = new NewsArticleAdapter(this, R.layout.activity_news_row, newsArticleList);
        adapter.setListData(newsArticleList);
        newsArticleListView.setAdapter(adapter);

        sharedPref = getSharedPreferences("News", MODE_PRIVATE);
        searchEditText.setText(sharedPref.getString("search", ""));


        /**
         * response when user clicks "Search" button
         */
        searchButton.setOnClickListener(v ->  {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("search", searchEditText.getText().toString());
                editor.commit();

                showSnackbar(v, ("Searching: " + searchEditText.getText().toString()), LENGTH_SHORT);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(searchButton.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                NEWS_URL = "https://newsapi.org/v2/everything?apiKey=d8006601883348ecb35a8c8a210753bd&q=" + searchEditText.getText().toString() ;


                /**
                 * create alert dialog for user after search button pressed
                 */
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(News_Activity_Main.this);
                alertBuilder.setTitle("Search : " + searchEditText.getText());
                alertBuilder.setMessage("Clear or add new search");
                /**
                 * function to run if users hits the 'Add' button
                 */
                alertBuilder.setNegativeButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        new AsyncHttpTask().execute(NEWS_URL);
                    }
                });
                /**
                 * function to run if users hits the 'clear' button
                 */
                alertBuilder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialogInterface, int i) {
                        newsArticleList.clear();
                        adapter.notifyDataSetChanged();
                        new AsyncHttpTask().execute(NEWS_URL);

                    }
                });

                /**
                 * if this is list is not empty, show alert dialog
                 *
                 */
                if (newsArticleList.size() > 0) {
                    alertBuilder.show();
                } else {
                    /**
                     *  list is empty, just query the search, no need for alert dialog
                     */
                    new AsyncHttpTask().execute(NEWS_URL);
                }
                /**
                 * notify adapter of changes and refresh listview
                 */
                adapter.notifyDataSetChanged();



        });

        /**
         * response when user clicks "Go to Favourites" button
         */
        favouritesButton.setOnClickListener(favourites -> {
            Intent favouritesIntent = new Intent(this, NewsFavourites.class);
            startActivity(favouritesIntent);
        });



        /**
         * response when user selects an article item in the Listview
         */
        newsArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsArticleObject item = (NewsArticleObject) parent.getItemAtPosition(position);
                Bundle dataToPass = new Bundle();
                dataToPass.putSerializable("Article", item);

                if (isTablet) {
                    NewsFragment dFragment = new NewsFragment(); //add a DetailFragment
                    dFragment.setArguments(dataToPass); //pass it a bundle for information
                    dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                            .addToBackStack("AnyName") //make the back button undo the transaction
                            .commit(); //actually load the fragment.
                } else //isPhone
                {
                    Intent nextActivity = new Intent(News_Activity_Main.this, News_Empty_Activity.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivity(nextActivity); //make the transition
                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.menuItemCar:
                startActivity(new Intent(News_Activity_Main.this, ECCSFmain.class));
                break;
            case R.id.menuItemRecipe:
                //startActivity(new Intent(News_Activity_Main.this, RecipeSearchActivity.class));
                break;
            case R.id.menuItemCurrency:
                startActivity(new Intent(News_Activity_Main.this, CurrencyActivity.class));
                break;
            case R.id.menuItemNews:
                startActivity(new Intent(News_Activity_Main.this, News_Activity_Main.class));
                break;

            case R.id.overflow_help:
                helpDialog();
                break;
        }
        return true;
    }

    public void helpDialog()
    {
        View middle = getLayoutInflater().inflate(R.layout.activity_news_help_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Help")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }

    /**
     * function to handle newsapi connection on another thread
     */
    public class AsyncHttpTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpsURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();

                if (result != null) {
                    String response = streamToString(urlConnection.getInputStream());
                    parseResult(response);
                    return result;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * side-task completed, closing changes on GUI thread
         * tell my adapter to update and refresh the listview if new articles were found
         */
        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                adapter.notifyDataSetChanged();
                Toast.makeText(News_Activity_Main.this, "Data successfully Loaded", LENGTH_SHORT).show();
                myProgressBar.setVisibility(View.GONE);

            } else {
                Toast.makeText(News_Activity_Main.this, "Failed to load data!", LENGTH_SHORT).show();
            }

        }

        /**
         * update progress bar with  values
         *
         * @param values
         */
        protected void onProgressUpdate(Integer... values) {

            //Update GUI stuff only (the ProgressBar):
            myProgressBar.setVisibility(View.VISIBLE);
            myProgressBar.setProgress(values[0]);
        }



        /**
         * method to parse result received from url connection
         * create new article objects from parsed data and add them to the list
         *
         * @param result
         */
        private void parseResult(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray("articles");
                NewsArticleObject item;
                Float progress;
                for (int i = 0; i < posts.length(); i++) {
                    /**
                     * for every article found
                     * extract desired information
                     * create new article object
                     */
                    JSONObject post = posts.optJSONObject(i);
                    String title = post.optString("title");
                    String image = post.optString("urlToImage");
                    String description = post.optString("description");
                    String url = post.optString("url");
                    item = new NewsArticleObject();
                    item.setTitle(title);
                    item.setImageUrl(image);
                    item.setArticleUrl(url);
                    item.setDescription(description);
                    /**
                     * add new article object to arrayList
                     */
                    newsArticleList.add(item);

                    /**
                     * show progress as a total of articles loaded out of total articles received
                     */
                    progress = ((i + 1) * 100f) / posts.length();
                    Log.d("load percent :", progress.toString());
                    publishProgress(progress.intValue());

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * function to take in the inputstream from urlconnection and build a parseable string
     *
     * @param newsStream
     * @return result
     * @throws IOException
     */
    String streamToString(InputStream newsStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(newsStream));
        String data;
        String result = "";

        while ((data = bufferedReader.readLine()) != null) {
            result += data;
        }

        if (null != newsStream) {
            newsStream.close();
        }


        return result;
    }



    /**
     * show snackbar
     */
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

