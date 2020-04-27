package com.example.reno;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlertRecive extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context,ServiceReciver.class);
        context.startService(i);

        BuildNotification(context,intent);
    }

    public void BuildNotification(Context context,Intent intent){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK  , "mytag:backgroundwakelock");
        wl.acquire();

        String col = intent.getStringExtra("coding");
        notificationHelper notificationHelper = new notificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();

        //View Button
        Intent BackIntent = new Intent(context,ServiceReciver.class);
        BackIntent.setAction(ServiceReciver.ACTION_INTENT);
        PendingIntent BackPend=  PendingIntent.getService(context,20000,BackIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action BackAction= new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm,"VIEW ALARM",BackPend);
        nb.addAction(BackAction);



        //Dismiss Button
        Intent dismissIntent= new Intent(context,ServiceReciver.class);
        dismissIntent.setAction(ServiceReciver.ACTION_DISMISS);
        PendingIntent PendingDismiss= PendingIntent.getService(context,40000,dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action acttion= new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm,"DISMISS",PendingDismiss);
        nb.addAction(acttion);


        nb.setStyle(new NotificationCompat.BigTextStyle().bigText(col));
        nb.setContentText("From E_Course_Rep Reminder: \n"+col);
        notificationHelper.getManager().notify(1,nb.build());

        wl.release();
    }

}
