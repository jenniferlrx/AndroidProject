package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CurrencyFavoriteActivity extends AppCompatActivity {
    ListView theList;
    //ArrayList<Currency> objects = new ArrayList<Currency>( );
    BaseAdapter myAdapter;
    MyDatabaseOpenHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<Currency> currencies;
    CurrencyActivity a=new CurrencyActivity();
    private int positionClicked=0;
    public static final String ITEM_FROM = "CFROM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_TO = "CTO";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    //Button deleteButton;
    //Button currencyDetailButton;

    /**
     * THis method build up the list view of this page, show the favorite list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_favorite);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
        theList = findViewById(R.id.currency_favorite_list);
        //currencyDetailButton=(Button)findViewById(R.id.currencyDetailButton);

        currencies=new ArrayList<>();
        dbHelper=new MyDatabaseOpenHelper(this);
        db=dbHelper.getWritableDatabase();
        String[] columns={MyDatabaseOpenHelper.COL_ID,MyDatabaseOpenHelper.COL_FROM,MyDatabaseOpenHelper.COL_TO};
        Cursor results=db.query(false,MyDatabaseOpenHelper.TABLE_NAME,columns, null, null, null, null,null,null );
        int colFrom=results.getColumnIndex(MyDatabaseOpenHelper.COL_FROM);
        int colTo=results.getColumnIndex(MyDatabaseOpenHelper.COL_TO);
        int colId=results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        while(results.moveToNext()){
            String columnFrom=results.getString(colFrom);
            String columnTo=results.getString(colTo);
            long id=results.getLong(colId);
            currencies.add(new Currency(columnFrom,columnTo,id));
        }
        theList.setAdapter( myAdapter = new MyListAdapter() );
        /*theList.setOnItemClickListener((parent,view,position, id)->{
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("DETAILS");
            normalDialog.setMessage("this is a detail information");
            normalDialog.show();

        });*/
        /* this is previous one before fragment
        theList.setOnItemClickListener((parent,view,position, id)->{
            positionClicked = position;
            Currency chosenOne=currencies.get(position);
            Log.d("22222222222222",chosenOne.getcFrom() );
            Intent goToDetailPage=new Intent(this, CurrencyDetail.class);

            goToDetailPage.putExtra("CurrencyFrom",chosenOne.getcFrom() );
            goToDetailPage.putExtra("CurrencyTo",chosenOne.getcTo() );
            goToDetailPage.putExtra("Id",chosenOne.getcId() );
            startActivityForResult(goToDetailPage,30);
        });*/
        theList.setOnItemClickListener((parent,view,position, id)->{
                    positionClicked = position;
                    Currency chosenOne=currencies.get(position);
                    Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_FROM, chosenOne.getcFrom() );
            dataToPass.putString(ITEM_TO, chosenOne.getcTo());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, chosenOne.getcId());
            Log.d("id", ""+chosenOne.getcId());
            Log.d("position", ""+positionClicked);

            if(isTablet)
            {
                CurrencyDetailFragment dFragment = new CurrencyDetailFragment(); //add a DetailFragment
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
                Intent nextActivity = new Intent(CurrencyFavoriteActivity.this, CurrencyEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }

        });


        /*currencyDetailButton=(Button)findViewById(R.id.currencyDetailButton);
        currencyDetailButton.setOnClickListener(clk->{
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("Alert");
            normalDialog.setMessage("Do you want to exit this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            normalDialog.show();

        });*/
        /*deleteButton=findViewById(R.id.currencyDeleteButton);
        deleteButton.setOnClickListener(clk->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //This is the builder pattern, just call many functions on the same object:
            AlertDialog dialog = builder.setTitle("Alert!")
                    .setMessage("Do you want to delete?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //If you click the "Delete" button
                            Currency c= (Currency) myAdapter.getItem(which);
                            int numDeleted = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[] {Long.toString(c.getcId())});

                            Log.i("ViewContact", "Deleted " + numDeleted + " rows");
                            currencies.remove(c);
                            myAdapter.notifyDataSetChanged();
                            //set result to PUSHED_DELETE to show clicked the delete button
                            //setResult(PUSHED_DELETE);
                            //go back to previous page:
                            finish();
                        }
                    })
                    //If you click the "Cancel" button:
                    .setNegativeButton("Cancel", (d,w) -> {  *//* nothing *//*})
                    .create();

            //then show the dialog
            dialog.show();

        });*/

    }



    public void deleteMessageId(int positionClicked)
    {
        Log.d("aaaaaaaaaaa",""+positionClicked);
        Long id=currencies.get(positionClicked).getcId();
       // Log.i("Delete this message:" , " id="+id);
        int numDeleted = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[] {Long.toString(id)});
        currencies.remove(positionClicked);
        myAdapter.notifyDataSetChanged();
    }


    /**
     * This method save some data into the intent, and wait for the result code from the detail page
     * to delete or not from this page
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                int positionReturn=data.getIntExtra(ITEM_POSITION,0);
                deleteMessageId(positionReturn);
            }
        }
        //If you're coming back from the view contact activity
      /* if(requestCode == 30)
        { if(resultCode==10){
            //switch(resultCode) {
                //if you clicked delete, remove the item you clicked from the array list and update the listview:

            currencies.remove(positionClicked);
            myAdapter.notifyDataSetChanged();
                  //  break;

                //if you clicked update, then the other activity should have sent back the new name and email in the Intent object:
                //update the selected object and update the listView:
//                case ViewContact.PUSHED_UPDATE:
//                    Contact oldContact = contactsList.get(positionClicked);
//                    oldContact.update(data.getStringExtra("Name"), data.getStringExtra("Email"));
                   // myAdapter.notifyDataSetChanged();

            }
        }*/
    }

    /**
     * THis method sets the view on the screen
     *
     */
    private class MyListAdapter extends BaseAdapter {

        // public MyListAdapter(Context context, int )
        public int getCount() {
            return currencies.size();
        } //This function tells how many objects to show

        public Currency getItem(int position) {
            return currencies.get(position);
        }  //This returns the Message object at position p

        public long getItemId(int p) {
            return p;
        }

        public View getView(int p, View recycled, ViewGroup parent) {
            View thisRow = null;//recycled;
            LayoutInflater inflater = getLayoutInflater();
            thisRow = inflater.inflate(R.layout.currency_favorite_detail, null);
            TextView itemfrom = thisRow.findViewById(R.id.currencyFrom);
            itemfrom.setText(getItem(p).getcFrom());
            TextView itemto = thisRow.findViewById(R.id.currencyTo);
            itemto.setText(getItem(p).getcTo());
            return thisRow;


        }
    }



}
