package com.example.finalproject;

import java.io.Serializable;

/**
 * NewsArticleObject class
 * Object method for handling of news articles
 */
public class NewsArticleObject implements Serializable {

    /**
     *
     * @param id - database id
     * @param title - article title
     * @param articleUrl - article url
     * @param imageUrl - article image url
     * @param description - article description
     */
    private long id;
    private String title;
    private String articleUrl;
    private String imageUrl;
    private String description;


    public NewsArticleObject() {
        //empty constructor

    }


    public NewsArticleObject(long id, String title, String articleUrl, String imageUrl, String description) {
        this.id = id;
        this.title=title;
        this.articleUrl=articleUrl;
        this.imageUrl=imageUrl;
        this.description=description;

    }


    public long getId() {return this.id;} /** @return id*/
    public String getTitle() {return this.title;}   /** @return title*/
    public String getArticleUrl() {return this.articleUrl;} /** @return articleUrl*/
    public String getDescription() {return this.description;}   /** @return description*/
    public String getImageUrl() {return this.imageUrl;} /** @return imageUrl*/

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

