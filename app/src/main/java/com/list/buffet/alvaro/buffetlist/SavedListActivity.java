package com.list.buffet.alvaro.buffetlist;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.list.buffet.alvaro.buffetlist.model.FoodList;
import com.list.buffet.alvaro.buffetlist.model.Rating;
import com.list.buffet.alvaro.buffetlist.model.RatingList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class SavedListActivity extends AppCompatActivity {
    ArrayList<RatingList> listItems = new ArrayList<>();
    ArrayAdapter<RatingList> adapter;
    TextView noSavedListTextView;
    Map<Integer,SavedList> plateMap = new HashMap<>();
    ListView savedListView;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list);
        initElements();
        initActions();
        addElementsToList();
        showTextViewIfNoSavedLists();
        initToolbar();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_list_menu, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addButton:
                openNewListDialog();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        getSupportActionBar().setTitle(Tags.APPNAME);

    }

    private void showTextViewIfNoSavedLists() {
        if(adapter.isEmpty()){
            noSavedListTextView.setVisibility(View.VISIBLE);
        }
    }

    private void initElements() {
        noSavedListTextView     = (TextView)    findViewById(R.id.noSavedListTv);
        savedListView           = (ListView)    (findViewById(R.id.savedList));
        adapter                 =               new RatingListAdapter(this,
                                                R.layout.listview_item,
                                                listItems);
        Realm.init(this);
        realm                   =               Realm.getDefaultInstance();
        savedListView.setAdapter(adapter);
    }

    private void initActions() {
        savedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SavedListDetailsActivity.class);
                intent.putExtra("savedList", (Serializable) plateMap.get(position));
                startActivity(intent);
                finish();
            }
        });
    }
    private void openNewListDialog() {
        LayoutInflater inflater             =           LayoutInflater.from(SavedListActivity.this);
        View newListDialogView              =           inflater.inflate(R.layout.new_list_dialog, null);
        final EditText listName             =           (EditText) newListDialogView.findViewById(R.id.newListDialogNameField);
        final EditText listRestaurantName   =           (EditText) newListDialogView.findViewById(R.id.newListDialogRestaurantField);
        AlertDialog.Builder builder         =           new AlertDialog.Builder(this);
        builder.setTitle(Tags.NEW_LIST_TITTLE);
        builder.setView(newListDialogView);
        builder = setDialogButtonActions(builder,listName,listRestaurantName);
        builder.show();
    }

    private AlertDialog.Builder setDialogButtonActions(AlertDialog.Builder builder, final EditText listName, final EditText listRestaurantName) {
        builder.setPositiveButton(Tags.CREATE_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(notEmptyFields(listName,listRestaurantName)){
                    RealmResults<FoodList> queryListName = realm.where(FoodList.class)
                            .equalTo("listName", listName.getText().toString())
                            .findAll();
                    if (queryListName.size() == 0){
                        Intent intent = new Intent(getApplicationContext(), NewListActivity.class);
                        intent.putExtra("listName",listName.getText().toString());
                        intent.putExtra("listRestaurant",listRestaurantName.getText().toString());
                        startActivity(intent);
                        finish();
                    }else{
                        AppCommon.getInstance().makeToast(getApplicationContext(),Tags.ALREADY_EXIST_LIST_WITH_SAME_NAME_MESSAGE);
                    }
                }else {
                    AppCommon.getInstance().makeToast(getApplicationContext(),Tags.FILL_ALL_FIELDS_MESSAGE);
                }
            }
        });
        builder.setNegativeButton(Tags.CANCEL_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }

    private boolean notEmptyFields(EditText listName, EditText listRestaurantName) {
        if(AppCommon.getInstance().isEmptyField(listName)) return false;
        if (AppCommon.getInstance().isEmptyField(listRestaurantName)) return false;
        return true;
    }

    private void addElementsToList() {
        RealmResults<FoodList> foodLists = realm.where(FoodList.class).distinct("listName");
        for (int i = 0; i < foodLists.size(); i++) {
            if (!realm.where(Rating.class).equalTo("listName",getListName(foodLists,i)).findAll().isEmpty()){
                listItems.add(new RatingList(getListName(foodLists, i),realm.where(Rating.class).equalTo("listName",getListName(foodLists,i)).findFirst().getListRating()));
            }else{
                listItems.add(new RatingList(getListName(foodLists, i),0));
            }
        }
        saveListInMap();
        adapter.notifyDataSetChanged();
    }

    private String getListName(RealmResults<FoodList> foodLists, int i) {
        return foodLists.get(i).getListName();
    }

    private void saveListInMap() {
        for (int i = 0; i < listItems.size(); i++) {
            SavedList savedList = new SavedList();
            RealmResults<FoodList> queryListName = queryLists(i);
            for (int j = 0; j < queryListName.size(); j++) {
            }
            addPlates(savedList, queryListName);
            plateMap.put(i,savedList);
        }
    }

    private RealmResults<FoodList> queryLists(int i) {
        return realm.where(FoodList.class)
                .equalTo("listName", listItems.get(i).getListName())
                .findAll();
    }

    private void addPlates(SavedList savedList, RealmResults<FoodList> queryListName) {
        for (int j = 0; j < queryListName.size(); j++) {
            saveListData(savedList, queryListName, j);
        }
    }

    private void saveListData(SavedList savedList, RealmResults<FoodList> queryListName, int j) {
        savedList.setListName(getListName(queryListName, j));
        savedList.setRestaurantName(queryListName.get(j).getRestaurantName());
        savedList.addToList(queryListName.get(j).getFoodPlate());
    }

}
