package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class RecipeView extends Fragment {
    private TextView txtViewTitle;
    private TextView txtViewSourceURL;
    private TextView txtViewImage_url;
    private TextView txtViewRecipeID;
    private long id;
    private boolean isTablet;
    private SQLiteDatabase sqLiteDatabase;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View result = inflater.inflate(R.layout.activity_recipe_row, container, false);
        txtViewTitle = (TextView)result.findViewById(R.id.recipe_txtViewTitle);
        txtViewSourceURL= (TextView) result.findViewById(R.id.recipe_txtViewSourceURL);
        txtViewImage_url= (TextView) result.findViewById(R.id.recipe_txtViewImage_url);
        txtViewRecipeID= (TextView) result.findViewById(R.id.recipe_txtViewID);

        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);

        savedInstanceState = getArguments();
        txtViewTitle.setText(savedInstanceState.getString("title"));
        txtViewSourceURL.setText(savedInstanceState.getString("url"));
        txtViewImage_url.setText("imgUrl");
        txtViewRecipeID.setText(savedInstanceState.getString("recipeID"));

        // get the delete button, and add a click listener:
        Button deleteBut = (Button)result.findViewById(R.id.recipe_deletebutton);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                RecipeSearchActivity parent = (RecipeSearchActivity) getActivity();

                parent.deleteMessageId((int)id);
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                Recipe_emptyActivity Rep = (Recipe_emptyActivity) getActivity();
                Intent backToChatRoomActivity = new Intent();
                backToChatRoomActivity.putExtra("id", dataFromActivity.getLong(RecipeFinder.ITEM_ID ));

                parent.setResult(Activity.RESULT_OK, backToChatRoomActivity); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;

    }
}
