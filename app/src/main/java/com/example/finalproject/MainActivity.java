package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    /**
     * in
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imgBtnCar = findViewById(R.id.imgBtn_car);
        ImageButton imgBtnRecipe = findViewById(R.id.imgBtn_recipe);
        ImageButton imgBtnCurrency = findViewById(R.id.imgBtn_currency);
        ImageButton imgBtnNews = findViewById(R.id.imgBtn_news);

        imgBtnCar.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ECCSFmain.class));
        });
        imgBtnCurrency.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CurrencyActivity.class));
        });
        imgBtnRecipe.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, MainRecipeActivity.class));
        });

        imgBtnNews.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, News_Activity_Main.class));
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     *
     * @param menu
     * @return
     */
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
                startActivity(new Intent(MainActivity.this, ECCSFmain.class));
                break;
//            case R.id.menuItemRecipe:
//                startActivity(new Intent(MainActivity.this, .class));
//                break;
            case R.id.menuItemCurrency:
                startActivity(new Intent(MainActivity.this, CurrencyActivity.class));
                break;
            case R.id.menuItemNews:
               startActivity(new Intent(MainActivity.this, News_Activity_Main.class));
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
