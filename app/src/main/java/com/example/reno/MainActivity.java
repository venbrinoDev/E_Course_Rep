package com.example.reno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1500;
    private FirebaseAuth mAuth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser() != null) {
                    //handle the already login user
                    Intent mainintent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(mainintent);
                    finish();

                }
                else {
                    Intent main = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(main);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);

    }

}