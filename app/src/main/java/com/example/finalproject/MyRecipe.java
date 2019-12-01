package com.example.finalproject;

/**
 * listview model- recipe
 */
public class MyRecipe {
    protected int id;
    protected  String URL;
    protected  String TITLE;
    protected  String IMAGE_URL;
    protected  String RECIPE_ID;

    public MyRecipe(String title){
        this.TITLE = title;
    }

    public MyRecipe(String title, String URL, String imgURL , String recipe_id){
        this(title, URL, imgURL, recipe_id, 0);
    }

    public MyRecipe(String title, String URL, String imgURL, String recipe_id, int id){
        this.TITLE = title;
        this.URL = URL;
        this.IMAGE_URL = imgURL;
        this.RECIPE_ID = recipe_id;
        this.id = id;
    }

    public int getID(){
        return id;
    }

    public void setTITLE(String TITLE){
        this.TITLE = TITLE;
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
        return this.IMAGE_URL;
    }

    public String getRECIPEID() {
        return this.RECIPE_ID;
    }
}
