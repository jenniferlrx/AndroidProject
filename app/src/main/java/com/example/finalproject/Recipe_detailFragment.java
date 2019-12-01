package com.example.finalproject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Recipe_detailFragment extends Fragment {
    private TextView txtViewTitle;
    private TextView txtViewSourceURL;
    private ImageView viewImage;
    private TextView txtViewID;

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private String title;
    private String url;
    private String imgurl;
    private String recipe_id;
    private String activity_name;
    public static final int RESULT_SAVE = 222;
    public static final int RESULT_DELETE = 444;


    public void setTablet(boolean tablet) {isTablet = tablet;}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(RecipeSearchActivity.ITEM_ID );

        title = dataFromActivity.getString(RecipeSearchActivity.ITEM_SELECTED);
        url = dataFromActivity.getString(RecipeSearchActivity.ITEM_URL);
        imgurl = dataFromActivity.getString(RecipeSearchActivity.ITEM_IMAGE_URL);
        recipe_id = dataFromActivity.getString(RecipeSearchActivity.ITEM_RECIPE_ID);
        activity_name = dataFromActivity.getString(RecipeSearchActivity.ITEM_ACTIVITY_CALLING);



        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_recipe_row, container, false);
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        Button saveButton = (Button)result.findViewById(R.id.saveRecipeButton);
        if(activity_name.equals("RecipeSearchActivity")){
            deleteButton.setVisibility(View.GONE);
        }else if(activity_name.equals("RecipeFavouriteList")){
            saveButton.setVisibility(View.GONE);
        }

        txtViewTitle = (TextView) result.findViewById(R.id.recipe_txtViewTitle);
        txtViewTitle.setText(dataFromActivity.getString(RecipeSearchActivity.ITEM_SELECTED));

        txtViewSourceURL= (TextView) result.findViewById(R.id.recipe_txtViewSourceURL);
        txtViewSourceURL.setText(dataFromActivity.getString(RecipeSearchActivity.ITEM_URL));

        viewImage= (ImageView) result.findViewById(R.id.recipe_image);
//        getBitmapfromUrl(dataFromActivity.getString(RecipeSearchActivity.ITEM_IMAGE_URL),viewImage);
        Picasso.get().load(dataFromActivity.getString(RecipeSearchActivity.ITEM_IMAGE_URL)).into(viewImage);

        txtViewID = (TextView) result.findViewById(R.id.txtViewID);
        txtViewID.setText(dataFromActivity.getString(RecipeSearchActivity.ITEM_RECIPE_ID));


        // get the delete button, and add a click listener:

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
                parent.setResult(RESULT_DELETE, backToChatRoomActivity); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });


        saveButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                RecipeSearchActivity parent = (RecipeSearchActivity) getActivity();
                parent.addData(title,url,imgurl,recipe_id); //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                Recipe_empty_activity parent = (Recipe_empty_activity) getActivity();
                Intent backToChatRoomActivity = new Intent();
                backToChatRoomActivity.putExtra(RecipeSearchActivity.ITEM_SELECTED, dataFromActivity.getString(RecipeSearchActivity.ITEM_SELECTED ));
                backToChatRoomActivity.putExtra(RecipeSearchActivity.ITEM_URL, dataFromActivity.getString(RecipeSearchActivity.ITEM_URL ));
                backToChatRoomActivity.putExtra(RecipeSearchActivity.ITEM_IMAGE_URL, dataFromActivity.getString(RecipeSearchActivity.ITEM_IMAGE_URL ));
                backToChatRoomActivity.putExtra(RecipeSearchActivity.ITEM_RECIPE_ID, dataFromActivity.getString(RecipeSearchActivity.ITEM_RECIPE_ID ));
                parent.setResult(RESULT_SAVE, backToChatRoomActivity); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }

//    public void getBitmapfromUrl(String imageUrl, ImageView imageView)
//    {
//        new AsyncTask<String, Void, Bitmap>() {
//            @Override
//            protected Bitmap doInBackground(String... urls) {
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//            }
//        }.execute(imageUrl);
//    }
}
