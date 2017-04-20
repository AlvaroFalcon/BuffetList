package com.list.buffet.alvaro.buffetlist.model;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Index;

/**
 * Created by Alvaro on 25/10/2016.
 */

public class FoodList extends RealmObject {
    private int id;
    private String foodPlate;
    @Index
    private String listName;
    private String restaurantName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodPlate() {
        return foodPlate;
    }

    public void setFoodPlate(String foodPlate) {
        this.foodPlate = foodPlate;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
