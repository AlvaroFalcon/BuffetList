package com.list.buffet.alvaro.buffetlist;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.list.buffet.alvaro.buffetlist.model.FoodList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class NewListActivity extends AppCompatActivity {
    ImageButton addButton;
    private EditText numberOfOrderField, quantyField;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    String restaurantName;
    boolean hasChanges = false;
    ListView newListView;
    Map <String, Integer> plateMap;
    Map<Integer,Integer> orderMap;
    Realm realm;
    private String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);
        initElements();
        initInformation();
        initActions();
        initToolbar();

    }

    @Override
    public void onBackPressed() {
        if(hasChanges){
            showAlertDiscardDialog();
        }else{
            startActivity(new Intent(NewListActivity.this,SavedListActivity.class));
            finish();
        }
    }

    private void showAlertDiscardDialog() {
            LayoutInflater inflater             =           LayoutInflater.from(NewListActivity.this);
            AlertDialog.Builder builder         =           new AlertDialog.Builder(this);
            builder.setTitle(Tags.ALERT_DISCARD_DIALOG_TITTLE);
            builder = setAlertDiscardDialogActions(builder);
            builder.show();
    }

    private AlertDialog.Builder setAlertDiscardDialogActions(AlertDialog.Builder builder) {
        builder.setNegativeButton(Tags.CANCEL_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton(Tags.CONFIRM_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(NewListActivity.this,SavedListActivity.class));
                NewListActivity.super.onBackPressed();
            }
        });
        return builder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        menu.getItem(0).setEnabled(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clean_list:
                openConfirmDialog();
                break;
            case R.id.save_list:
                checkFields();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        getSupportActionBar().setTitle(restaurantName);
    }


    public void initElements() {
        orderMap                =           new HashMap<>();
        plateMap                =           new HashMap<>();
        addButton               =           (ImageButton) findViewById(R.id.addButton);
        listItems               =           new ArrayList<>();
        newListView             =           (ListView) findViewById(R.id.newList);
        numberOfOrderField      =           (EditText) findViewById(R.id.numberOfOrderField);
        quantyField             =           (EditText) findViewById(R.id.quantyField);
        adapter                 =           new ArrayAdapter<>(this,
                                            android.R.layout.simple_list_item_1,
                                            listItems);
        Realm.init(this);
        realm                   =           Realm.getDefaultInstance();
        newListView.setAdapter(adapter);
    }

    private void initActions() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAddFields();
            }
        });
        newListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showConfirmDeleteDialog(i);
                hasChanges = true;
                return true;
            }
        });
    }

    private void checkAddFields() {
        if (fieldsNotEmpty()){
            addOrderToMap();
            listItems.clear();
            addToAdapter();
            clearEditText();
            adapter.notifyDataSetChanged();
            hasChanges = true;
        }else{
            AppCommon.getInstance().makeToast(getApplicationContext(), Tags.FILL_ALL_FIELDS_MESSAGE);
        }
    }

    private void addToAdapter() {
        for(Map.Entry<Integer, Integer> entry : orderMap.entrySet()) {
            listItems.add(formatedLine(entry));
            plateMap.put(formatedLine(entry),entry.getKey());
        }
    }

    @NonNull
    private String formatedLine(Map.Entry<Integer, Integer> entry) {
        return entry.getValue() + Tags.FORMAT_LIST_ELEMENT+ entry.getKey();
    }

    private void addOrderToMap() {
        if(orderMap.get(orderNumber())!= null){
            sumOrder();
        }else{
            addOrder();
        }
    }

    private void addOrder() {
        orderMap.put(orderNumber(), orderQuanty());
    }

    private void sumOrder() {
        orderMap.put(orderNumber(),orderMap.get(orderNumber()) + orderQuanty());
    }

    private Integer orderQuanty() {
        return Integer.valueOf(getQuantyString());
    }

    private Integer orderNumber() {
        return Integer.valueOf(getOrderString());
    }

    private void checkFields() {
        if(notEmptyList()){
            AppCommon.getInstance().makeToast(getApplicationContext(), Tags.SAVED_MESSAGE);
            saveList();
            hasChanges = false;
        }else{
            AppCommon.getInstance().makeToast(getApplicationContext(),Tags.NO_ELEMENTS_IN_LIST);
        }
    }

    private void openConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle(Tags.CONFIRM_CLEAN_LIST)
                .setMessage(Tags.CLEAN_LIST_CONFIRM_MESSAGE)
                .setPositiveButton(Tags.CONFIRM_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listItems.clear();
                        orderMap.clear();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(Tags.CANCEL_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private AlertDialog.Builder setDialogButtonActions(AlertDialog.Builder builder) {
        builder.setNegativeButton(Tags.CANCEL_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton(Tags.CONFIRM_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listItems.clear();
                adapter.notifyDataSetChanged();
            }
        });
        return null;
    }

    private boolean fieldsNotEmpty() {
        if (AppCommon.getInstance().isEmptyField(numberOfOrderField)) return false;
        if (AppCommon.getInstance().isEmptyField(quantyField)) return false;
        return true;
    }

    private boolean notEmptyList() {
        return !listItems.isEmpty();
    }

    private void saveList() {
        realm.beginTransaction();
        realm.where(FoodList.class).equalTo("listName",listName).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < listItems.size(); i++) {
                    FoodList foodList = realm.createObject(FoodList.class);
                    setFoodListInfo(foodList,i);
                }
            }
        });
    }

    private void setFoodListInfo(FoodList foodList, int i) {
            foodList.setId(getIncrementalId());
            foodList.setFoodPlate(listItems.get(i));
            foodList.setRestaurantName(restaurantName);
            foodList.setListName(listName);

    }


    private void clearEditText() {
        quantyField.setText("");
        numberOfOrderField.setText("");
    }
    private void showConfirmDeleteDialog(final int position) {
        new AlertDialog.Builder(this)
                .setTitle(Tags.DELETE_PLATE_TITTLE)
                .setMessage(Tags.CONFIRM_DELETE_PLATE_MESSAGE)
                .setPositiveButton(Tags.CONFIRM_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderMap.remove(plateMap.get(getItem(position)));
                        adapter.remove(getItem(position));
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(Tags.CANCEL_BUTTON_MESSAGE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private String getItem(int position) {
        return adapter.getItem(position);
    }


    @NonNull
    private String getOrderString() {
        return numberOfOrderField.getText().toString();
    }

    @NonNull
    private String getQuantyString() {
        return quantyField.getText().toString();
    }

    private void initInformation() {
        restaurantName = getStringIntent("listRestaurant");
        listName = getStringIntent("listName");

    }
    private int getIncrementalId() {
        return realm.where(FoodList.class).max("id").intValue() + 1;
    }

    private String getStringIntent(String string) {
        return getIntent().getStringExtra(string);
    }


}
