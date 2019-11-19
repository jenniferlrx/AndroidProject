package com.example.finalproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String list = "";
            while(bufferedReader.readLine()!=null){
                sb.append(list);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonArray= jsonObject.getJSONArray("recipes");
            int count = jsonObject.getInt("count");
            for(int i=0; i< count; i++){
                JSONObject aRecipe = jsonArray.getJSONObject(i);
                String title = aRecipe.getString("title");
                MyRecipe myRecipe = new MyRecipe();
                myRecipe.setTITLE(title);
                newRecipeList.add(myRecipe);
            }

        } catch (JSONException e) {

            return null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newRecipeList;
    }
}
