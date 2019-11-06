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

public class MainRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openDialog();
            }
        });
        ListView listView = (ListView) findViewById(R.id.restaurant_listview);

        Toast toast=Toast.makeText(getApplicationContext(),"Welcome!",Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

    public void openDialog(){
        DialogClass dialog = new DialogClass();
        dialog.show(getSupportFragmentManager(),"dialog box");
    }
}
