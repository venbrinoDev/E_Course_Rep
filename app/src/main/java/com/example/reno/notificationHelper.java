

package com.example.reno;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class notificationHelper extends ContextWrapper {


    private  NotificationManager mManger;
    public static final String channelID="channelID";
    public static final String channelName="channel Name";
    public notificationHelper(Context base) {


        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }

    }




    @TargetApi( Build.VERSION_CODES.O)
    public  void createChannel(){
        NotificationChannel channel = new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public  NotificationManager getManager(){
        if(mManger == null){
            mManger = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  mManger;
    }
    public NotificationCompat.Builder getChannelNotification(){

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return  new NotificationCompat.Builder(getApplicationContext(),channelID)
                .setContentTitle(" E Course Rep Reminder")
                   .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false)
                .setVibrate(new long []{1000,1000,1000,1000,1000})
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.updated))
                .setSmallIcon(R.drawable.ic_access_alarm)
                .setSound(path);



    }


}
