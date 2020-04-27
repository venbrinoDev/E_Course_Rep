package com.example.reno;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class MyFirebaseInstanceService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size()>0) {
            String imageUrl=remoteMessage.getData().get("imageUrl");
            if (imageUrl.isEmpty()){
                imageUrl="";
            }else{
                imageUrl=remoteMessage.getData().get("imageUrl");
            }
            showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"),imageUrl);
            Log.d("messing","imageUrl  "+imageUrl);

        }
        if (remoteMessage.getNotification()!=null)  {
            String imageUrl="";
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),imageUrl);

        }


    }

    private void showNotification(String title, String body,String ImageUrl){


        NotificationDBHelper notificationDBHelper = new NotificationDBHelper(this);
        String saveTitle=title;
        String saveBody=body;

        //Creating uniqueId
        String saveTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        long randomTime=System.currentTimeMillis();
        long  randomNumer=new Random().nextLong();
        long SumOfRandom=randomTime+randomNumer;
        String identifier=String.valueOf(SumOfRandom);

        //Inserting Data
        boolean isInserted =notificationDBHelper.insertData(saveTitle,saveBody,saveTime,identifier,ImageUrl);
        if(isInserted == true){
            Log.d("messing", "true");
        }else{
            Log.d("messing", "false");
        }

        NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID="com.example.reno.test";
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel= new NotificationChannel(NOTIFICATION_CHANNEL_ID,"notification",
                    NotificationManager.IMPORTANCE_DEFAULT );
            notificationChannel.setDescription("ECourse Rep");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }


        Intent startApp=new Intent(this,HomeActivity.class);
        PendingIntent StartPending=PendingIntent.getActivity(this,110101,startApp,PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long []{1000,1000,1000,1000,1000})
                .setSmallIcon(R.drawable.ic_notifications)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.notification))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentTitle(title)
                .setContentInfo("info")
                .setContentIntent(StartPending)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText(body)
                .setSound(uri);


        int Ran= new Random().nextInt();
        notificationManager.notify(Ran,notificationBuilder.build());

    }

}

