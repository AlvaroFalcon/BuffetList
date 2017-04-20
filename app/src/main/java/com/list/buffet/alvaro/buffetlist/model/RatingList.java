package com.list.buffet.alvaro.buffetlist.model;

/**
 * Created by Alvaro on 21/12/2016.
 */

public class RatingList {
    private String listName;
    private float rating;

    public RatingList(String listName, float rating) {
        this.listName = listName;
        this.rating = rating;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
