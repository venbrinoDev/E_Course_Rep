package com.example.reno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Appinfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);

    }
}
