package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Recipe_detailFragment extends Fragment {
    private TextView txtViewTitle;
    private TextView txtViewSourceURL;
    private ImageView viewImage;

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private Button buttonSaveRecipe;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(RecipeSearchActivity.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_recipe_row, container, false);

        txtViewTitle = (TextView) result.findViewById(R.id.recipe_txtViewTitle);
        txtViewTitle.setText(dataFromActivity.getString(RecipeSearchActivity.ITEM_SELECTED));

        txtViewSourceURL= (TextView) result.findViewById(R.id.recipe_txtViewSourceURL);
        txtViewSourceURL.setText(dataFromActivity.getString(RecipeSearchActivity.ITEM_URL));

        viewImage= (ImageView) result.findViewById(R.id.recipe_image);
        URL url = null;
        try {
            url = new URL(dataFromActivity.getString(RecipeSearchActivity.ITEM_IMAGE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewImage.setImageBitmap(bmp);

        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                RecipeSearchActivity parent = (RecipeSearchActivity) getActivity();
                parent.deleteMessageId((int)id); //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                Recipe_empty_activity parent = (Recipe_empty_activity) getActivity();
                Intent backToChatRoomActivity = new Intent();
                backToChatRoomActivity.putExtra(RecipeSearchActivity.ITEM_ID, dataFromActivity.getLong(RecipeSearchActivity.ITEM_ID ));

                parent.setResult(Activity.RESULT_OK, backToChatRoomActivity); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }
}
