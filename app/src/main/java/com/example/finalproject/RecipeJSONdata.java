package com.example.finalproject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeJSONdata {
    private List<MyRecipe> newRecipeList = new ArrayList<>();


    public List<MyRecipe> getJSONdata(String jsonUrl){
        try{
            URL url = new URL(jsonUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
