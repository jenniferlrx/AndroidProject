package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainRecipeActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Button btnSearch, btnFavourite;
    private ListView liistView;
    protected static final String Activity_NAME = "RecipeSearchActivity";
    private String app_key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ListView listView = (ListView) findViewById(R.id.recipe_list);

//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                openDialog();
//            }
//        });

//
//        Toast toast=Toast.makeText(getApplicationContext(),"Welcome!",Toast.LENGTH_SHORT);
//        toast.setMargin(50,50);
//        toast.show();
//
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
}