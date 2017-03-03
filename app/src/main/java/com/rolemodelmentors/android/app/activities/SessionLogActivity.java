package com.rolemodelmentors.android.app.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rolemodelmentors.android.app.R;
import com.rolemodelmentors.android.app.interfaces.QuestionsSpreadsheetWebService;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SessionLogActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    String timeClock = null;
    static CoordinatorLayout clayout;
    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_log);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        clayout = (CoordinatorLayout) findViewById(R.id.clayout);
        final ProgressBar viewProgressBar = (ProgressBar)findViewById(R.id.progressHorizontal);

        prefs = getSharedPreferences("com.rolemodelmentors.android.app", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forView(findViewById(R.id.fab_send), "Session Send", "You can press this to send your session log")
                            // All options below are optional
                            .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                            .targetCircleColor(R.color.white)   // Specify a color for the target circle
                            .titleTextSize(25)                  // Specify the size (in sp) of the title text
                            .titleTextColor(R.color.white)      // Specify the color of the title text
                            .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                            .descriptionTextColor(R.color.red)  // Specify the color of the description text
                            .textColor(R.color.white)            // Specify a color for both the title and description text
                            .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                            .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                            .drawShadow(true)                   // Whether to draw a drop shadow or not
                            .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                            .tintTarget(false)                   // Whether to tint the target view's color
                            .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                            .targetRadius(60),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional

                        }
                    });
        }



        final Calendar c = Calendar.getInstance();
        Button sLog = (Button) findViewById(R.id.sLogs);
        sLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        SessionLogActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), "Timepickerdialog");


            }

        });
        final MaterialEditText menteeName = (MaterialEditText) findViewById(R.id.menteeField);
        final MaterialEditText sessionNotes = (MaterialEditText) findViewById(R.id.notesField);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/d/")
                .build();
        final QuestionsSpreadsheetWebService spreadsheetWebService = retrofit.create(QuestionsSpreadsheetWebService.class);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nameInput = user.getDisplayName();
                String menteeNameInput = menteeName.getText().toString();
                String notes = sessionNotes.getText().toString();
                if(menteeNameInput.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(clayout, "Mentee Name not entered", Snackbar.LENGTH_LONG);

                    snackbar.show();
                } else if(timeClock == null){
                    Snackbar snackbar = Snackbar
                            .make(clayout, "Please enter in your time", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }else{
                    viewProgressBar.setVisibility(View.VISIBLE);
                    Call<Void> completeQuestionnaireCall = spreadsheetWebService.completeQuestionnaire(nameInput, menteeNameInput, "3/3/2017", timeClock, notes);
                    completeQuestionnaireCall.enqueue(callCallback);
                }


            }
        });
    }

    private final Callback<Void> callCallback = new Callback<Void>() {

        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            Log.d("XXX", "Submitted. " + response);
            hideProg();
            Snackbar snackbar = Snackbar
                    .make(clayout, "Session Log Sent!", Snackbar.LENGTH_LONG);

            snackbar.show();

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.e("XXX", "Failed", t);
            hideProg();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final MaterialEditText menteeName = (MaterialEditText) findViewById(R.id.menteeField);
            final MaterialEditText sessionNotes = (MaterialEditText) findViewById(R.id.notesField);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://docs.google.com/forms/d/")
                    .build();
            final QuestionsSpreadsheetWebService spreadsheetWebService = retrofit.create(QuestionsSpreadsheetWebService.class);
            Snackbar snackbar = Snackbar
                    .make(clayout, "An error has occurred", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    };
    public void hideProg(){
        ProgressBar viewProgressBar = (ProgressBar)findViewById(R.id.progressHorizontal);
        viewProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        Log.d("RoleModelMentors", "Hour: " + hourOfDay + ", HourEnd: " + hourOfDayEnd);
        String timesheet;
        if(hourOfDay > hourOfDayEnd) {
            Log.d("RoleModelMentors", "Problems");

        }
        else{
            String mins;
            int hour = hourOfDayEnd - hourOfDay;
            int min = minuteEnd - minute;
            if(min == 0){
                mins = "00";
                timesheet = hour + ":" + mins;
            } else{
                timesheet = hour + ":" + min;

            }
            timeClock = timesheet;
            Log.d("RoleModelMentors", "Timesheet: " + timesheet);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

}
