package com.example.finalproject;

import java.io.Serializable;

/**
 * this class is a news object class to set parameter of news
 *
 */
public class NewsArticleObject implements Serializable {

    private long id;
    private String title;
    private String articleUrl;
    private String imageUrl;
    private String description;

    /**
     * constructor without parameter
     */
    public NewsArticleObject() { }


    /**
     * constructor with 5 parameters
     * @param id
     * @param title
     * @param articleUrl
     * @param imageUrl
     * @param description
     */
    public NewsArticleObject(long id, String title, String articleUrl, String imageUrl, String description) {
        this.id = id;
        this.title=title;
        this.articleUrl=articleUrl;
        this.imageUrl=imageUrl;
        this.description=description;

    }


    public long getId() {return this.id;}
    public String getTitle() {return this.title;}
    public String getArticleUrl() {return this.articleUrl;}
    public String getDescription() {return this.description;}
    public String getImageUrl() {return this.imageUrl;}

    /**
     * set title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * set articleUrl
     * @param articleUrl
     */
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    /**
     * set description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * set imageUrl
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

