package com.rolemodelmentors.android.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.rolemodelmentors.android.app.R;

public class MainActivity extends AppCompatActivity {

    CoordinatorLayout clayout;
    private String email;
    Drawer result;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clayout = (CoordinatorLayout) findViewById(R.id.clayout);
        final ActionBar actionBar = getSupportActionBar();
        mAuth = FirebaseAuth.getInstance();

        //setSupportActionBar(toolbar);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //  Declare a new thread to do a preference check
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
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
                    //t.start();
                }
            }
        };

        //Sets the actionbar with hamburger
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_menu).color(Color.WHITE).sizeDp(IconicsDrawable.ANDROID_ACTIONBAR_ICON_SIZE_DP).paddingDp(IconicsDrawable.ANDROID_ACTIONBAR_ICON_SIZE_PADDING_DP));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            TextView emailText = (TextView) findViewById(R.id.mentorEmail);
            emailText.setText(email + "!");
            if(name == null){
                new MaterialDialog.Builder(this)
                        .title("Hi There!")
                        .content("Since this is your first time logging in, please enter a new password")
                        .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("Password", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                String nm = input.toString();
                                updatePassword(nm);
                            }
                        }).show();
                new MaterialDialog.Builder(this)
                        .title("Hi There!")
                        .content("Since this is your first time logging in, please enter your name")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Name", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                String nm = input.toString();
                                updateUserName(nm,user);

                            }
                        }).show();


            }
        }


        Button b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

        });
        drawerCreate(); //Initalizes Drawer


    }
    public void updateUserName(String nm, FirebaseUser user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nm)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("RoleModelMentors", "User profile updated.");
                        }
                    }
                });
    }
    public void updatePassword(String pass){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("RoleModelMentors", "User password updated.");
                        }
                    }
                });
    }
    //Method to create the Navigation Drawer
    public void drawerCreate(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = "null";
        String email = "null";
        Uri photoUrl = null;
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();
        }


        //Creates navigation drawer header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(getDrawable(R.drawable.ic_logo_white))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        //Creates navigation drawer items
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("About").withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withName("Payments").withIcon(GoogleMaterial.Icon.gmd_attach_money);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withName("Session Log").withIcon(GoogleMaterial.Icon.gmd_timer);
        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withName("About").withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withName("Sign Out").withIcon(GoogleMaterial.Icon.gmd_lock);
        SecondaryDrawerItem item8 = new SecondaryDrawerItem().withName("Account").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        //Create navigation drawer

        result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        item1,
                        item4,
                        item5,
                        item8,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item7
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("RoleModelMentors", "The drawer is at position " + position);
                        //About button
                        if (position == 3) {
                            FirebaseCrash.log("Activity created");
                            Intent intent = new Intent(getApplicationContext(), SessionLogActivity.class);
                            startActivity(intent);
                        }else if (position == 4){
                            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                            startActivity(intent);
                        }else if (position == 8) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            LoginActivity.signedOut();
                            startActivity(intent);
                            Log.d("drawer", "Signed out");
                        }
                        return false;
                    }
                })
                .build();
        result.setSelection(item1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(result.isDrawerOpen()){
                    result.closeDrawer();
                }
                else{
                    result.openDrawer();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
