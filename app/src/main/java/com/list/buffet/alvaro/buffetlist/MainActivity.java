package com.list.buffet.alvaro.buffetlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.list.buffet.alvaro.buffetlist.model.FoodList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public Button newListButton, savedListButton;
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        initActions();
    }

    private void initActions() {
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewListDialog();
            }
        });

        savedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SavedListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initElements() {
        newListButton           =           (Button) findViewById(R.id.newListButton);
        savedListButton         =           (Button) findViewById(R.id.savedListButton);
    }

    private void openNewListDialog() {
        LayoutInflater inflater             =           LayoutInflater.from(MainActivity.this);
        View newListDialogView              =           inflater.inflate(R.layout.new_list_dialog, null);
        final EditText listName             =           (EditText) newListDialogView.findViewById(R.id.newListDialogNameField);
        final EditText listRestaurantName   =           (EditText) newListDialogView.findViewById(R.id.newListDialogRestaurantField);
        AlertDialog.Builder builder         =           new AlertDialog.Builder(this);
        Realm.init(this);
        realm                               =           Realm.getDefaultInstance();
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

}
