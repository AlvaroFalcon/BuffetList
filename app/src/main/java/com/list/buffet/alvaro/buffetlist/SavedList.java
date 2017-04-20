package com.list.buffet.alvaro.buffetlist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alvaro on 26/10/2016.
 */

public class SavedList implements Serializable{
    String restaurantName = "";
    String listName = "";
    ArrayList<String> foodList = new ArrayList<>();

    public SavedList() {
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ArrayList<String> getFoodList() {
        return foodList;
    }
    public void addToList(String plate){
        this.foodList.add(plate);
    }
    public void setFoodList(ArrayList<String> foodList) {
        this.foodList = foodList;
    }
}
