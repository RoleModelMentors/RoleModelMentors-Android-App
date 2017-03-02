package com.rolemodelmentors.android.app.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.firebase.crash.FirebaseCrash;
import com.rolemodelmentors.android.app.R;

import java.util.Calendar;

public class SessionLogActivity extends AppCompatActivity {
    SublimePicker mSublimePicker;
    SublimeOptions mSublimeOptions;

    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {

        }

        @Override
        public void onCancelled() {
            // Handle click on `Cancel` button
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_log);


        final Calendar c = Calendar.getInstance();
        Button sLog = (Button) findViewById(R.id.sLogs);
        sLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseCrash.log("Activity created");

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                try{
                    mSublimePicker.initializePicker(null, mListener);
                    mSublimeOptions.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
                    mSublimeOptions.setDateParams(year, month, dayOfMonth);
                } catch (NullPointerException e){
                    Log.d("RoleModelMentors","Error: " + e);
                    FirebaseCrash.logcat(Log.ERROR, "RoleModelMentors", "NPE caught");
                    FirebaseCrash.report(e);
                    Toast.makeText(getApplicationContext(), "Something bad happened. Reporting :(",
                            Toast.LENGTH_LONG).show();
                }


            }

        });
    }
}
