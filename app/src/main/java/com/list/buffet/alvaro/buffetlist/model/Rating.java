package com.list.buffet.alvaro.buffetlist.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by Alvaro on 19/12/2016.
 */

public class Rating extends RealmObject {
    @Index
    private int id;
    private String listName;
    private float listRating;

    public float getListRating() {
        return listRating;
    }

    public void setListRating(float listRating) {
        this.listRating = listRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
