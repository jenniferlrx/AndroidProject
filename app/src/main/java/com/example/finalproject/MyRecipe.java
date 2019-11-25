package com.example.finalproject;

/**
 * listview model
 */
public class MyRecipe {
    protected long id;
    protected  String URL;
    protected  String TITLE;
    protected  String IMAGE_URL;

    public MyRecipe(String title){
        this.TITLE = title;
    }

    public MyRecipe(String title, String URL, String imgURL ){
        this(title, URL, imgURL,0);
    }

    public MyRecipe(String title, String URL, String imgURL, long id){
        this.TITLE = title;
        this.URL = URL;
        this.IMAGE_URL = imgURL;
        this.id = id;
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

    public String getIMAGE_URL(String IMAGE_URL){
        return IMAGE_URL;
    }
}
