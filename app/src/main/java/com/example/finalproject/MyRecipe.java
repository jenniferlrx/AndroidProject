package com.example.finalproject;

/**
 * listview model
 */
public class MyRecipe {
    protected long id;
    protected  String URL;
    protected  String TITLE;
    protected  String IMAGE_URL;
    protected String RECIPE_ID;

    public MyRecipe(String title){
        this.TITLE = title;
    }

    public MyRecipe(String title, String URL, String imgURL, String recipeID ){
        this(title, URL, imgURL, recipeID,0);
    }

    public MyRecipe(String title, String URL, String imgURL, String recipeid, long id){
        this.TITLE = title;
        this.URL = URL;
        this.IMAGE_URL = imgURL;
        this.id = id;
        this.RECIPE_ID = recipeid;
    }

    public long getID(){
        return id;
    }
    public void setTITLE(String TITLE){
        this.TITLE = TITLE;
    }

    public String getRecipeID(){
        return RECIPE_ID;
    }
    public void setRecipeID(String RECIPE_ID){
        this.RECIPE_ID = RECIPE_ID;
    }

    public String getTITLE(){
        return TITLE;
    }

    public void setURL(String URL){
        this.URL = URL;
    }
    public String getURL(){
        return URL;
    }

    public void setIMAGE_URL(String IMAGE_URL){
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getIMAGE_URL(){
        return IMAGE_URL;
    }
}
