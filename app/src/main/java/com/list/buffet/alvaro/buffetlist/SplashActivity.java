package com.list.buffet.alvaro.buffetlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        final ProgressBar loadingProgress = (ProgressBar) findViewById(R.id.loadingProgressBar);
            Thread startLoadingThread = new Thread(){
                @Override
                public void run() {
                    try {
                        loadingProgress.setVisibility(View.VISIBLE);
                        sleep(3000);
                        Intent intent = new Intent(getApplicationContext(),SavedListActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        startLoadingThread.start();
    }
}
