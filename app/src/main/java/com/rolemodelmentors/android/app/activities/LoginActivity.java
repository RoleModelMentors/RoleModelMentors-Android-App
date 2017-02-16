package com.rolemodelmentors.android.app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rolemodelmentors.android.app.R;

import utilities.NetworkUtilities;

public class LoginActivity extends AppCompatActivity {
    CoordinatorLayout clayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        clayout = (CoordinatorLayout) findViewById(R.id.clayout);
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();


        checkInternet();
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText usernameEditText = (EditText) findViewById(R.id.emailField);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordField);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = usernameEditText.getText().toString();
                //TODO encrypt password
                String passwd = passwordEditText.getText().toString();


                if (email.isEmpty() && passwd.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(clayout, "You did not enter an email or password", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                else if (email.isEmpty() && !passwd.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(clayout, "You did not enter an email", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                else if (!email.isEmpty() && passwd.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(clayout, "You did not enter a password", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                else if (!email.isEmpty() && !passwd.isEmpty()){
                    if(isEmailValid(email)){
                        loadMainActivity();
                    }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(clayout, "Email address not valid", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }
            }

        });

        TextView signup = (TextView) findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);

            }
        });

    }
    private void loadMainActivity(){

    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void checkInternet(){
        if(!NetworkUtilities.isConnected(getApplicationContext())){
            Snackbar snackbar = Snackbar
                    .make(clayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkInternet();
                        }
                    });
            snackbar.show();
        }
        else{
            Log.d("RoleModelMentors","You Passed");
        }
    }
}
