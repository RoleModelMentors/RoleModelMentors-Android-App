package com.rolemodelmentors.android.app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rolemodelmentors.android.app.R;

import utilities.NetworkUtilities;

public class LoginActivity extends AppCompatActivity {
    CoordinatorLayout clayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        clayout = (CoordinatorLayout) findViewById(R.id.clayout);
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();

        checkInternet();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("RoleModelMentors", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("RoleModelMentors", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

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
                        signIn(email,passwd);
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
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
    private void signIn(String email, String password) {
        Log.d("RoleModelMentors", "signIn:" + email);


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("RoleModelMentors", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("RoleModelMentors", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()) {
                            Log.d("RoleModelMentors","Signin success");
                            loadMainActivity();
                        }

                    }
                });
        // [END sign_in_with_email]
    }
}
