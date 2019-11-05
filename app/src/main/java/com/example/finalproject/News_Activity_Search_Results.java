package com.example.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class News_Activity_Search_Results extends AppCompatActivity {
    public String titleAtt; //title attribute pulled from WEBHOSE.io
    ArrayList<News> newsListArticle = new ArrayList<>();
    String preUrl = "https://newsapi.org/v2/everything?apiKey=YOUR_KEY&q="; //prefix of url
    //String postUrl = "%20market%20language%3Aenglish"; //postfix of url
    String URL; //pre + search term + post
    String searchedArticle; //search intent
    private ProgressBar progressBar; //progress bar of Async
    private ListView newsfeedList; //news feed list
    private String positionUrl; //position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_search_results);

        Button retBtn = findViewById(R.id.returnbtn_hd); //return button
        newsfeedList = findViewById(R.id.news_feed_list1); //new feed list

        // search into
        Intent previousPage = getIntent();
        searchedArticle = previousPage.getStringExtra("searchedArticle");
        URL = preUrl + searchedArticle;// + postUrl; //webhose url

        //execute news feed api
        NewsFeedQuery wq = new NewsFeedQuery();
        wq.execute(URL);

        //progress bar
        progressBar = findViewById(R.id.progressBar_hd);
        progressBar.setVisibility(View.VISIBLE);

        //news feed list with on click
        newsfeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextPage = new Intent(News_Activity_Search_Results.this, News_Activity_NewsDetail.class);
                positionUrl = newsListArticle.get(position).getTitle();
                nextPage.putExtra("inputPosition", positionUrl);
                News_Activity_Search_Results.this.startActivity(nextPage);
            }
        });

        //return button with snackbar to return
        retBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {

                //Snackbar sb = Snackbar.make(retBtn, "Would you like to return? ", Snackbar.LENGTH_LONG)
                        //.setAction("Yes.", e -> Log.e("Toast", "Clicked return"));
                //sb.setAction("Yes.", f -> News_Activity_Search_Results.this.finish());
                //sb.show();
                finish();
            }
        });

    }

    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class NewsFeedQuery extends AsyncTask<String, Integer, String>
    {


        public String uuid; //uuid value
        public String title;


        @Override
        protected String doInBackground(String... params) {

            try {

                //get the string url:
                String myUrl = params[0];
                //create the network connection:
                java.net.URL url = new URL(URL);

                System.out.println("myUrl");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 ); // milliseconds
                conn.setConnectTimeout(15000  ); //milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream inStream = conn.getInputStream();
                //obtain response code, success if 200
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    Log.e("Conection to WEBHOSE", "Successful" );
                } else {
                    Log.e("Conection to WEBHOSE", "FAILED" );
                }

                conn.connect();

                //created a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //loop over the webhose.io XML:
                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.getEventType() == XmlPullParser.START_TAG)
                    {
                        String tagName = xpp.getName();
                        if(tagName.equals("title")) {
                            // need to call xpp.next() point to inside of the <uuid> tag;
                            // But if we are looking for an "attribute" of <uuid> tag, then do not need to call xpp.next()"
                            if(xpp.next() == XmlPullParser.TEXT) {
                                title = xpp.getText();
                                Log.e("AsyncTask", "Found parameter uuid: " + uuid);
                            }
                            // titleAtt = xpp.getAttributeValue(null, "cheese");
                            publishProgress(15);

                        }
                        else if(tagName.equals("url")) {
                            // same thing as above
                            if (xpp.next() == XmlPullParser.TEXT) {
                                titleAtt = xpp.getText();
//                                currentUrl = titleAtt;
//                                if(!previousURL.equals(currentUrl)){
                                Log.e("AsyncTask", "Found parameter titleAtt: " + titleAtt);

                                News new1 = new News(titleAtt, null, null);
                                newsListArticle.add(new1);
//                                    previousURL = currentUrl;
//                                }
                            }
                            // titleAtt = xpp.getAttributeValue(null, "cheese");
                            publishProgress(25);

                        }



/*                    else if(tagName.equals("dt"))
                    {
                        String definition = xpp.getText();
                        publishProgress(75);
                        Log.e("AsyncTask", "Found parameter definition: "+ definition);
                    }
                    else if(tagName.equals("dt"))
                    {
                        String definition = xpp.getText();
                        definitions.add(definition);
                        publishProgress(75);
                        Log.e("AsyncTask", "Found parameter definition: "+ definition);
                    }*/

                        /**
                         else if(tagName.equals("Temperature")) {
                         xpp.next(); //move to the text between opening and closing tags:
                         String temp = xpp.getText();
                         publishProgress(3); //tell android to call onProgressUpdate with 3 as parameter
                         }
                         **/
                    }
                    xpp.next(); //advance to next XML event
                }

                //Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin
            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            return "Finished task";
        }

        /**
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("AsyncTask", "update:" + values[0]);

//            int size = newsListArticle.size();
//            for (int i = 0; i < size; i++) {
//                NewsAdapter adt = new NewsAdapter(newsListArticle, getApplicationContext());
//                newsfeedList.setAdapter(adt);
//                adt.notifyDataSetChanged();

//            }

            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setProgress(15);
            progressBar.setProgress(25);
            progressBar.setProgress(50);
            progressBar.setProgress(75);
            progressBar.setProgress(100);
            progressBar.setMax(100);

        }

        /**
         * title, uuid, anticle displayed once connection is done
         * @param args
         */
        @Override
        protected void onPostExecute(String args) {
            Log.i("AsyncTask", "onPostExecute" );
//            titleView.setText(titleAtt);
//            uuidView.setText(uuid);
            NewsAdapter adt = new NewsAdapter(newsListArticle, getApplicationContext());
            newsfeedList.setAdapter(adt);
            adt.notifyDataSetChanged();
        }

    }


}
