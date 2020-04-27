package com.example.reno;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

public class ServiceReciver extends Service {
  private static final String URI_BASE=ServiceReciver.class.getSimpleName() +".";
  public static final String ACTION_DISMISS= URI_BASE+"ACTION_DISMISS";
    public static final String ACTION_INTENT= URI_BASE+"ACTION_INTENT";

  private Ringtone ringtone;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null){
            return  START_REDELIVER_INTENT;
        }

        String action = intent.getAction();

        if (ACTION_DISMISS.equals(action)) {
            dismissRingtone();

        } else  if (ACTION_INTENT.equals(action)){
            dismissRingtone();
            Intent going = new Intent(this ,Recyker.class);
            going.putExtra("EXTRA",true);
            startActivity(going);


        }
        else {
            Uri alarmUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri==null){
                alarmUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
            ringtone= RingtoneManager.getRingtone(getApplicationContext(),alarmUri);
            ringtone.play();
            ringtone.setLooping(true);

        }




        return START_NOT_STICKY;
    }
    public void dismissRingtone(){
        Intent i = new Intent(this,ServiceReciver.class);
        stopService(i);

        NotificationManager notificationManager=(NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    @Override
    public void onDestroy() {
        ringtone.stop();
    }
}
