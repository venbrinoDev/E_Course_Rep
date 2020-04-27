/*
package com.example.alarmmange;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class Alarmset extends AppCompatActivity {
    Boot boot;


    public DatePicker PickDate;
    public TimePicker PickTime;
    public Button setYourTime;
    public TextView showTime;
    public static int  code =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boot = new Boot();
        registerReceiver(boot, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));

        PickDate = findViewById(R.id.pickDate);
        PickTime = findViewById(R.id.pickTime);
        setYourTime = findViewById(R.id.setYourTime);
        showTime = findViewById(R.id.showTime);

        Calendar now = Calendar.getInstance();
        PickDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH ),null
        );

        PickTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        PickTime.setCurrentMinute(now.get(Calendar.MINUTE));

        setYourTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar current = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();
                cal.set(PickDate.getYear(),
                        PickDate.getMonth(),
                        PickDate.getDayOfMonth(),
                        PickTime.getCurrentHour(),
                        PickTime.getCurrentMinute(),00
                );

                if (cal.compareTo(current) <=0){
                    Toast.makeText(Alarmset.this, "invalid Date and Time", Toast.LENGTH_SHORT).show();
                }else {

                    startAlarm(cal);
                }

            }
        });
    }


    public void startAlarm( Calendar targetCal){
        showTime.setText("\n\n***\n"
                +"Alarm is set@" + targetCal.getTime() + "\n" + "***\n");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlertRecive.class);
        int b = code++;
        intent.putExtra("code",b);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Alarmset.this,b, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
        }

    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}

*/