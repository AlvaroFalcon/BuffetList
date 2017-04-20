package com.list.buffet.alvaro.buffetlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

import com.list.buffet.alvaro.buffetlist.model.FoodList;
import com.list.buffet.alvaro.buffetlist.model.Rating;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class SavedListDetailsActivity extends AppCompatActivity {
    ListView listView;
    SavedList savedList;
    ArrayList<String> listItems;
    RatingBar ratingBar;
    ArrayAdapter<String> adapter;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list_details);
        initElements();
        initActions();
        addListElements();
        initToolbar();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SavedListActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle(savedList.getRestaurantName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_details_menu, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_list:
                removeList();
                removeRatingFromRealm(realm.where(Rating.class).equalTo("listName",savedList.getListName()).findAll());

                Intent intent = new Intent(getApplicationContext(), SavedListActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeRatingFromRealm(RealmResults<Rating> ratingList) {
        realm.beginTransaction();
        ratingList.deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void removeList() {
        RealmResults<FoodList> queryListName = getList();
        removeListFromRealm(queryListName);
    }

    private void addListElements() {
        listItems.addAll(savedList.getFoodList());
        if (!realm.where(Rating.class).equalTo("listName",savedList.getListName()).findAll().isEmpty()){
            ratingBar.setRating(realm.where(Rating.class).equalTo("listName",savedList.getListName()).findFirst().getListRating());
        }else{
            ratingBar.setRating(0);
        }
        adapter.notifyDataSetChanged();
    }

    private void initElements() {
        listItems            =      new ArrayList<>();
        listView             =      (ListView) findViewById(R.id.savedListDetail);
        adapter              =       new ArrayAdapter<>(this,
                                     android.R.layout.simple_list_item_1,
                                     listItems);
        Realm.init(this);
        ratingBar            =       (RatingBar) findViewById(R.id.rating_bar);
        realm                =       Realm.getDefaultInstance();
        listView.setAdapter(adapter);
        savedList = (SavedList) getIntent().getSerializableExtra("savedList");
    }

    private void initActions() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (realm.where(Rating.class).equalTo("listName",savedList.getListName()).findAll().isEmpty()){
                            Rating listRating = realm.createObject(Rating.class);
                            listRating.setListName(savedList.getListName());
                            listRating.setListRating(rating);
                            listRating.setId(getIncrementalID());
                        }else{
                            realm.where(Rating.class).equalTo("listName",savedList.getListName()).findFirst().setListRating(rating);
                        }
                    }
                });
            }
        });
    }

    private int getIncrementalID() {
        return realm.where(Rating.class).max("id").intValue() + 1;
    }

    private void removeListFromRealm(RealmResults<FoodList> queryListName) {
        realm.beginTransaction();
        queryListName.deleteAllFromRealm();
        realm.commitTransaction();
    }

    @NonNull
    private RealmResults<FoodList> getList() {
        return realm.where(FoodList.class)
                .equalTo("listName", savedList.getListName())
                .findAll();
    }
}
