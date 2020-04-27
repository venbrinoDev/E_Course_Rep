package com.example.reno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.reno.databinding.ActivityNotificationItemBinding;

public class Notification_Item extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__item);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        NotificationDisplayClass notificationDisplayClass=getIntent().getParcelableExtra("items");
        ActivityNotificationItemBinding binding= DataBindingUtil.setContentView(this,
                R.layout.activity_notification__item
                );
        binding.setNotificationDisplayClass(notificationDisplayClass);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
    }
}
