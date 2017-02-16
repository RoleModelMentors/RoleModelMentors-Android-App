package com.rolemodelmentors.android.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;

import com.rolemodelmentors.android.app.R;

public class MainActivity extends AppCompatActivity {

    CoordinatorLayout clayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clayout = (CoordinatorLayout) findViewById(R.id.clayout);

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor e = getPrefs.edit();
                //  Create a new boolean and preference and set it to true
                e.putBoolean("firstStart", true);
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);

                    //  Edit preference to make it false because we don't want this to run again
                    //e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }



            }
        });

        // Start the thread
        t.start();

        Button b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        });
    }
}
