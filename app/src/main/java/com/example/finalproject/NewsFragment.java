package com.example.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

/**
 * This class is a page with detail news of favorite
 * can using delete function
 */
public class NewsFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;

    private long id;
    private String title;
    private String description;

    private TextView articleDescription;
    private TextView articleTitle;
    private ImageView articleImage;
    private TextView articleUrl;

    private Intent lastIntent;

    private Button addToFavouritesButton;
    private Button openInBrowser;
    private Button backBtn;

    private NewsArticleObject articleObject;
    private NewsDatabaseOpenHelper dbOpener;

    /**
     * set a tablet
     * @param tablet
     */
    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }

    /**
     * main method to create a page
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOpener = new NewsDatabaseOpenHelper(NewsFragment.super.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        articleObject = (NewsArticleObject) bundle.getSerializable("Article");

        View result = inflater.inflate(R.layout.activity_news_details, container, false);
        articleTitle = result.findViewById(R.id.details_title);
        articleDescription = result.findViewById(R.id.details_description);
        articleImage = result.findViewById(R.id.details_image);
        articleUrl = result.findViewById(R.id.url_textview);

        articleTitle.setText(articleObject.getTitle());
        articleDescription.setText(articleObject.getDescription());
        Picasso.get().load(articleObject.getImageUrl()).into(articleImage);
        articleUrl.setText(articleObject.getArticleUrl());

        openInBrowser = result.findViewById(R.id.go_to_url_button);
        addToFavouritesButton = result.findViewById(R.id.add_to_favourites_button);
        backBtn = result.findViewById(R.id.goback);

        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //response the go back button
        backBtn.setOnClickListener(v -> {
            if (isTablet) {
                News_Activity_Main parent = (News_Activity_Main) getActivity();
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            } else {
                News_Empty_Activity parent = (News_Empty_Activity) getActivity();
                parent.finish();
            }
        });

        //response the add to favourite button
        addToFavouritesButton.setOnClickListener(fav -> {

            ContentValues newRowValues = new ContentValues();
            newRowValues.put(NewsDatabaseOpenHelper.COL_TITLE, articleObject.getTitle());
            newRowValues.put(NewsDatabaseOpenHelper.COL_DESCRIPTION, articleObject.getDescription());
            newRowValues.put(NewsDatabaseOpenHelper.COL_ARTICLEURL, articleObject.getArticleUrl());
            newRowValues.put(NewsDatabaseOpenHelper.COL_IMAGEURL, articleObject.getImageUrl());
            long newId = db.insert(NewsDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            AlertDialog.Builder builder = new AlertDialog.Builder(NewsFragment.super.getActivity());
            AlertDialog dialog = builder.setMessage("Successfully Saved News to Favourites!")
                    .setPositiveButton("OK", (d, w) -> {  /* nothing */})
                    .create();
            dialog.show();

        });


        //response the opem browser button
        openInBrowser.setOnClickListener(browser -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleObject.getArticleUrl()));
            startActivity(browserIntent);
        });

        return result;
    }


}
