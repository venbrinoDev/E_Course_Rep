package com.example.reno;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.content.Context.ALARM_SERVICE;


public class Boot extends BroadcastReceiver {
   public DBHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
         dbHelper=new DBHelper(context);
      if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
          Log.d("newmail", "BroadcastStarted");
          Toast.makeText(context,"broadcasting",Toast.LENGTH_LONG).show();

          Cursor res=dbHelper.getAllData();
          if(res.getCount()==0){
              Toast.makeText(context,"NO DATA",Toast.LENGTH_LONG).show();
          }

          while (res.moveToNext()){
              int pendingPosion=res.getInt(3);
              Long AlarmTringer =res.getLong(1);
              String message=res.getString(4);
              if (AlarmTringer >System.currentTimeMillis()) {
                  AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                  Intent i = new Intent(context, IntentBoot.class);
                  String pro="Be Reminded You May Be Having a Class ,Practical,Church Service or An Event";
                  i.putExtra("messageType",(message.isEmpty())?pro:message);
                  PendingIntent pendingIntent = PendingIntent.getBroadcast(context,pendingPosion, i, 0);
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                      alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,AlarmTringer, pendingIntent);
                  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                      alarmManager.setExact(AlarmManager.RTC_WAKEUP, AlarmTringer, pendingIntent);
                  } else {
                      alarmManager.set(AlarmManager.RTC_WAKEUP, AlarmTringer, pendingIntent);
                  }
              }
          }
      }
    }

}
