package com.example.finalproject;

    import android.content.ContentValues;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.Button;
    import android.widget.Toast;
    import com.example.finalproject.R;
    import com.example.finalproject.RecipeDatabaseOpenHelper;
    import com.example.finalproject.Reciper_DetailFragment;

    import java.net.URL;

    import androidx.appcompat.app.AppCompatActivity;


public class EmptyActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "EMPTY_ACTIVITY: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_fragment);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        // So far the screen is blank

        Reciper_DetailFragment dFragment = new Reciper_DetailFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();

        Button buttonSaveRecipe = (Button) findViewById(R.id.saveRecipeButton);
        buttonSaveRecipe.setOnClickListener(clk -> {
//CALL THE DATABASE HELPER CLASS
            RecipeDatabaseOpenHelper dbOpener = new RecipeDatabaseOpenHelper(this);
            SQLiteDatabase db = dbOpener.getWritableDatabase();
// ADDS 1 ROW and 3 COLUMNS

            /*
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(COL_RECIPE, (getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentLocation, dFragment)
                    .addToBackStack("AnyName1")
                    .commit()));
            newRowValues.put(FLAG, (1));
            db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            System.out.println("New Row Values: " + newRowValues);
*/
        });
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
        String[] columns = {RecipeDatabaseOpenHelper.COL_ID, RecipeDatabaseOpenHelper.COL_RECIPE, RecipeDatabaseOpenHelper.FLAG,};
        //INITIALIZE CURSORS
        Cursor cursor = db.query(false, RecipeDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
    }

}
