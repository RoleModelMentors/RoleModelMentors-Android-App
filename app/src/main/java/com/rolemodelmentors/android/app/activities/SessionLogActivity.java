package com.rolemodelmentors.android.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.firebase.crash.FirebaseCrash;
import com.rolemodelmentors.android.app.R;

public class SessionLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_log);

        final SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
            @Override
            public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {

            }

            @Override
            public void onCancelled() {
                // Handle click on `Cancel` button
            }
        };

        final SublimePicker mSublimePicker;

        mSublimePicker = (SublimePicker) findViewById(R.id.sublime_picker);
        // Passing `null` to apply default options


        Button sLog = (Button) findViewById(R.id.sLogs);
        sLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseCrash.log("Activity created");

                mSublimePicker.initializePicker(null, mListener);
            }

        });
    }
}
