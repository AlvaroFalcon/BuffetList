package com.list.buffet.alvaro.buffetlist;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Alvaro on 05/11/2016.
 */

class AppCommon {
    private static AppCommon mInstance = null;


    private AppCommon() {
    }

    static AppCommon getInstance(){
        if(mInstance == null)
        {
            mInstance = new AppCommon();
        }
        return mInstance;
    }

    void makeToast(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public boolean isEmptyField(EditText field) {
        return field.getText().toString().equals(Tags.EMPTY_STRING);
    }
}
