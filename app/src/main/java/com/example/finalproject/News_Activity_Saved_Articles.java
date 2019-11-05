package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class News_Activity_Saved_Articles extends AppCompatActivity {
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    News_DBHelper dbHelper; //db helper
    SQLiteDatabase db; //database
    ArrayList<News> newsArrayList; //news article array list
    SavedWordsAdapter adt; //adapter
    News selectID; //id position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_saved_list);

        //create array
        if (newsArrayList == null) {
            newsArrayList = new ArrayList<>();
        }

        //get database
        dbHelper = new News_DBHelper(this);
        db = dbHelper.getWritableDatabase();

        // find data and put into db
        this.findAllData(db);

        // list view of saved article
        ListView theList = (ListView) findViewById(R.id.saved_news_list_hd);
        // get fragment
        //boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        //adapter
        adt = new SavedWordsAdapter(newsArrayList);
        theList.setAdapter(adt);

        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View item, int position, long id) {
                Bundle dataToPass = new Bundle();
                dataToPass.putString(ITEM_SELECTED, newsArrayList.get(position).getTitle());
                dataToPass.putInt(ITEM_POSITION, position);
                // dataToPass.putLong(ITEM_ID, msgList.get(position).getId());
                dataToPass.putLong(ITEM_ID, id);

                Intent nextActivity = new Intent(News_Activity_Saved_Articles.this, News_Activity_Empty.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                News_Activity_Saved_Articles.this.startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition


            }
        });

    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMPTY_ACTIVITY && resultCode == RESULT_OK) {
            long deletedId = data.getLongExtra("deletedId", 0);
            deleteMessageId((int) deletedId);
        }
    }

    /**
     * delete the message from view list by it's id, and update the list
     *
     * @param id
     */

    public void deleteMessageId(int id) {
        Log.i("Delete:", " id=" + id);
        String str = "";
        Cursor c;
        String[] cols = {News_DBHelper.COL_ID, News_DBHelper.COL_TITLE};
        c = db.query(false, News_DBHelper.TABLE_NAME, cols, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            for (int i = 0; i < id; i++) {
                c.moveToNext();
            }
            str = c.getString(c.getColumnIndex(News_DBHelper.COL_ID));
        }
        int x = db.delete(News_DBHelper.TABLE_NAME, News_DBHelper.COL_ID + "=?", new String[]{str});
        Log.i("Cursor", "Deleted " + x + " rows");
        newsArrayList.remove(id);
        adt.notifyDataSetChanged();
    }

    /**
     * locate data
     */
    private void findAllData(SQLiteDatabase db) {
        Log.e("FindAllData ", "reached");
        //query all the results from the database:
        String[] columns = {News_DBHelper.COL_ID, News_DBHelper.COL_TITLE};
        Cursor results = db.query(false, News_DBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        // int idColIndex = results.getColumnIndex(NewsFeedDBHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(News_DBHelper.COL_TITLE);
        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {

            String title = results.getString(contentColumnIndex);
            // String id = results.getString(idColIndex);
            //  String body = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            newsArrayList.add(new News(title, null, null));
            // this.newsArrayList.clear();

        }
    }

    private void findUrl(SQLiteDatabase db) {
        String[] columns = {News_DBHelper.COL_TITLE};
        Cursor results = db.query(false, News_DBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        while (results.moveToNext()) {

        }
    }


    protected class SavedWordsAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;

        //reference to original data
        public SavedWordsAdapter(List<E> originalData) {
            dataCopy = originalData;
        }

        //array
        public SavedWordsAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }

        public SavedWordsAdapter() {
        }
        // return how many items to display
        @Override
        public int getCount() {
            return dataCopy.size();
        }

        // return the contents will show up in each row
        @Override
        public E getItem(int position) {
            return dataCopy.get(position);
        }


        /***
         *   Inflate view of list item for title
         *   @param position: locates the one that will be add to the bottom
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            selectID = (News) getItem(position);
            newView = inflater.inflate(R.layout.activity_news_word_list_item, parent, false);
            //  TextView idView = (TextView) newView.findViewById(R.id.news_id);
            TextView contentView = (TextView) newView.findViewById(R.id.news_title_listitem);

            //Get the string to go in row: position
            // int id = ((News) getItem(position)).getNewsID();
            String content = ((News) getItem(position)).getTitle();
            // String content1 = ((News) getItem(position)).getBody();

            //Set the text of the text view
            // idView.setText("    " + id);
            contentView.setText(content);
            //  contentView.setText(content1);

            return newView;
        }

        /**
         * get position
         *
         * @param position
         * @return
         */
        @Override
        public long getItemId(int position) {
            return (long) position;
        }
    }

}
