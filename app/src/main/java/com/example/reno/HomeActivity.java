package com.example.reno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.reno.ui.SignoutActivity;
import com.example.reno.ui.home.HomeFragment;


import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    public RadioButton defaultcol, orange, black, red;
    private BroadcastReceiver MyReceiver = null;
    TextView textviewname, textviewemail;
    ImageView ImageUserPhoto;
    private long backPressedTime;
    private Toast backToast;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mAuth;
    private AlertDialog dialog;
    FirebaseUser currentUser;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        FirebaseMessaging.getInstance().subscribeToTopic("ALL_TOPIC");
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
           if (task.isSuccessful()){
               String token=task.getResult().getToken();
               Log.d("tokey", "onComplete:"+"\t"+token);
               getSharedPreferences("tokenShared",MODE_PRIVATE).edit().putString("newToken",token).apply();
               DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
               databaseReference.child(uid).child("usertoken").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Log.d("tokey", "upload successfully");
                       }else {
                           Log.d("tokey", "upload Error");
                       }
                   }
               });
           }else{
               Toast.makeText(getApplicationContext(),"Switch connection on pls",Toast.LENGTH_SHORT).show();
           }

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        orange = (RadioButton)findViewById(R.id.secondmain);
        defaultcol = (RadioButton)findViewById(R.id.firstmain);


        MyReceiver = new MyReceiver();
        broadcastIntent();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            uid = mCurrentUser.getUid();
        }
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView1.getHeaderView(0);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);
        ImageView picload = findViewById(R.id.action_profile);
        navUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        picload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = String.valueOf(dataSnapshot.child("name").getValue());
                String email = String.valueOf(dataSnapshot.child("email").getValue());


                NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView1.getHeaderView(0);
                TextView navUsername = headerView.findViewById(R.id.nav_username);
                TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
                ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);
                ImageView picload = findViewById(R.id.action_profile);


                navUserMail.setText(name);
                navUsername.setText(email);

                navUserPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(HomeActivity.this,ProfileActivity.class);
                        startActivity(profileIntent);
                        DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                });
                picload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(HomeActivity.this,ProfileActivity.class);
                        startActivity(profileIntent);
                        DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                });



                if (currentUser.getPhotoUrl()== null) {
                    navUserPhoto.setBackgroundResource(R.drawable.profile);
                    picload.setBackgroundResource(R.drawable.profile);
                }
                else {
                    try {
                        Glide.with(HomeActivity.this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
                        Glide.with(HomeActivity.this).load(currentUser.getPhotoUrl()).into(picload);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_settings){
            Intent myintent = new Intent(HomeActivity.this,
                    SignoutActivity.class);
            startActivity(myintent);
            return false;
        }
        if (id == R.id.action_about){
            Intent aboutinent = new Intent(HomeActivity.this,
                    AboutActivity.class);
            startActivity(aboutinent);
            return false;
        }
        if (id == R.id.action_theme){
            displayDialogs();
        }

        return super.onOptionsItemSelected(item);

    }

    public void debug(){

    }
    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void displayDialogs() {
        dialog = new AlertDialog.Builder(this).create();
        View dialogView = this.getLayoutInflater().inflate(R.layout.changethemedialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        dialog.show();
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.firstmain:
                if (checked){
                    Toast.makeText(HomeActivity.this, "Night mood enebled", Toast.LENGTH_SHORT).show();
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    ((RadioButton) view).setChecked(true);
                    dialog.cancel();
                }
                break;
        }

    }
    public void onSecondRadioButtonClicked(View view){
        boolean secondchecked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.secondmain:
                if (secondchecked){
                    Toast.makeText(HomeActivity.this, "Day mood enebled", Toast.LENGTH_SHORT).show();
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    ((RadioButton) view).setChecked(true);
                    dialog.cancel();
                }
                break;
        }
    }
    
}

