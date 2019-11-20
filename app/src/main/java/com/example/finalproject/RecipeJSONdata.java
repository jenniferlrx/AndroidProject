package com.example.finalproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeJSONdata {
    private List<MyRecipe> newRecipeList;
    private String title;
    MyRecipe myRecipe;


    public List<MyRecipe> getJSONdata(String jsonUrl){
        String list = "";
        newRecipeList = new ArrayList<>();
        try{
            URL url = new URL(jsonUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            while((list = reader.readLine())!=null){
                sb.append(list);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonArray= jsonObject.getJSONArray("recipes");
            int count = jsonObject.getInt("count");
            for(int i=0; i< count; i++){
                JSONObject aRecipe = jsonArray.getJSONObject(i);
                title = aRecipe.getString("title");
                MyRecipe myRecipe = new MyRecipe(title);
                newRecipeList.add(myRecipe);
            }
            connection.disconnect();
            is.close();

        } catch (JSONException e) {

            return null;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newRecipeList;
    }
}
