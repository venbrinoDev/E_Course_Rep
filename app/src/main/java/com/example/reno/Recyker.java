package com.example.reno;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reno.ui.SignoutActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Recyker extends AppCompatActivity implements DialogShowItem.MessageListener,RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    public FloatingActionButton pressAdd;
    public ArrayList<ExampleClasss> exampleClass;
    private RecyclerView mRecylerView;
    private ExampleAdapter mAgapter;
    private RecyclerView.LayoutManager mlayoutManager;
    public String peopleMessage;
    public String showing ;
    ArrayList <ExampleClasss> mexampleList;
    public static int  code =1;
    public View dialogView;
    public AlertDialog alertDialog;
     public DatePicker datePicker;
    public TimePicker timePicker;
    public PendingIntent pendingIntent;
    public AlarmManager alarmManager;
    public static int b;
    public Calendar cal;
    public long target;
    public CoordinatorLayout coordinatorLayout;
    public int where;
    public DBHelper dbHelper;
    private static final String TAG = "motoro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyker);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        loadData();
        buildRecyclerview();
        clickSet();
        dbHelper =new DBHelper(Recyker.this);
        coordinatorLayout=findViewById(R.id.idLayout);
        code=retrivePendingIntent();
    }


public int collectPending(String Posi){
    int pending = dbHelper.getPendingIntent(Posi);
    Log.d("where", "collectPending: "+pending);
    return pending;
}
    public void clickSet(){
        pressAdd = findViewById(R.id.addButton);
        pressAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
                DialogShow();
                pressAdd.setEnabled(false);
            }
        });
    }

        public void show(){
        dialogView = View.inflate(Recyker.this,R.layout.activity_trying_this,null);
        alertDialog = new AlertDialog.Builder(Recyker.this).create();
        datePicker =(DatePicker)dialogView.findViewById(R.id.date_picker);
        timePicker = (TimePicker)dialogView.findViewById(R.id.time_picker);

            alertDialog.setCancelable(false);
            alertDialog.setView(dialogView);
            alertDialog.show();
            setTime();
            clickCanlce();
            }


            public void clickCanlce(){
                Button remove = dialogView.findViewById(R.id.remove);
               remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        pressAdd.setEnabled(true);
                         isAlarmSet();
                    }
                });

            }

           public   void setTime(){
               final Button button = dialogView.findViewById(R.id.date_time_set);

               Calendar now = Calendar.getInstance();
               datePicker.init(
                       now.get(Calendar.YEAR),
                       now.get(Calendar.MONTH),
                       now.get(Calendar.DAY_OF_MONTH ),null
               );

               timePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
               timePicker.setCurrentMinute(now.get(Calendar.MINUTE));
               button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       pressAdd.setEnabled(true);
                       Calendar current = Calendar.getInstance();
                       cal = Calendar.getInstance();
                       int year=datePicker.getYear();
                       int month=datePicker.getMonth();
                       int dayOfMonth=datePicker.getDayOfMonth();
                       int CurrentHour=timePicker.getCurrentHour();
                       int CurrentMinute=timePicker.getCurrentMinute();
                       cal.set(year,month, dayOfMonth, CurrentHour, CurrentMinute,00);

                       if (cal.compareTo(current) <=0){
                           Toast.makeText(Recyker.this, "invalid Date and Time", Toast.LENGTH_SHORT).show();
                       }else {

                           int position = 0;
                           showItem(0);
                           alertDialog.dismiss();
                           AlertDialog();
                           isAlarmSet();
                       }
                   }
               });
           }

               public void saveData(){
               SharedPreferences sharedPreferences= getSharedPreferences("shared prefrences",MODE_PRIVATE);
               SharedPreferences.Editor editor= sharedPreferences.edit();
               Gson gson= new Gson();
               String json = gson.toJson(mexampleList);
               editor.putString("TXT",json);
               editor.apply();
           }
               public void loadData(){
               SharedPreferences sharedPreferences= getSharedPreferences("shared prefrences",MODE_PRIVATE);
               Gson gson= new Gson();
               String json = sharedPreferences.getString("TXT",null);
               Type type= new TypeToken<ArrayList<ExampleClasss>>(){}.getType();
               mexampleList=gson.fromJson(json,type);

               if(mexampleList ==null) {
                   mexampleList = new ArrayList<>();
               }
           }

    @Override
    public void applyText(String name) {
        peopleMessage= name;
    }


    public void savePenndingIntent(int pend){
        SharedPreferences AlarmPending= getSharedPreferences("AlarmPending",MODE_PRIVATE);
        SharedPreferences.Editor Pendingeditor= AlarmPending.edit();
        Pendingeditor.putInt("pending",pend);
        Pendingeditor.apply();
    }
    public int retrivePendingIntent(){
        SharedPreferences AlarmPending= getSharedPreferences("AlarmPending",MODE_PRIVATE);
        int pendingAlarm=AlarmPending.getInt("pending",1);
        return pendingAlarm;
    }


    public void  showItem(int position){
        mRecylerView.scrollToPosition(position);
        showing ="Time Set For : \n"+ cal.getTime().toLocaleString();
        mexampleList.add(position,new ExampleClasss(R.drawable.updated,showing,peopleMessage));
        mAgapter.notifyItemInserted(position);
        startAlarm(cal);


    }
public void isAlarmSet(){

    RelativeLayout relativeLayout =findViewById(R.id.noSetAlarm);
    if (mAgapter.getItemCount()==0){
        relativeLayout.setVisibility(View.VISIBLE);
    }else{
        relativeLayout.setVisibility(View.INVISIBLE);
    }
}



     public  void  DialogShow(){
        DialogShowItem dialogShowItem = new DialogShowItem();
        dialogShowItem.show(getSupportFragmentManager(),"dialog Item");
     }
      public void cancleAlarm(int pendPosition){
        AlarmManager alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlertRecive.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(Recyker.this,pendPosition, intent, 0);
        pendingIntent.cancel();
        alarmManager. cancel(pendingIntent);
     }



    public void on(){
        Snackbar snackbar =Snackbar.make(coordinatorLayout,"Alarm is currently on",Snackbar.LENGTH_SHORT);
        snackbar.show();

    }

    public void off(){
        Snackbar snack =Snackbar.make(coordinatorLayout,"Alarm is currently off",Snackbar.LENGTH_SHORT);
        snack.show();

    }

    public void buildRecyclerview(){
        mRecylerView = findViewById(R.id.recyclerView);
        mRecylerView.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(this);
        mAgapter = new ExampleAdapter(mexampleList);
        mRecylerView.setLayoutManager(mlayoutManager);
        mRecylerView.setAdapter(mAgapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView( mRecylerView);

        mAgapter.setOnItemChecked(new ExampleAdapter.mOnitemChecked() {
            @Override
            public void OnswitchItem(int Position, Boolean isccheked) {
                ExampleClasss example=mexampleList.get(Position);
                final int deletedDate=example.getDate().hashCode();
                final String message=example.getmMESSAGE();
                int pendingPlace= collectPending(String.valueOf(deletedDate));
                if (isccheked) {
                    on();
                    resetAlarm(String.valueOf(deletedDate),message,pendingPlace);
                } else {
                    off();
                    cancleAlarm(pendingPlace);
                }
            }
        });
        mAgapter.setOnItemClickedListener(new ExampleAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(int Position) {


            }
        });
        isAlarmSet();
    }

    public void resetAlarm(String postion,  String ArrayPosition,int PendingNumber){
        Log.d("where", "resetPending"+PendingNumber);
        long c =dbHelper.getAlarmPosition(postion);
        Log.d("MODCOLO", "resetAlarm: " + c + " for position : " +postion +" Pneding number  "+PendingNumber);

        if(c <System.currentTimeMillis()){

        }else {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getBaseContext(), AlertRecive.class);
            String pro="Be Reminded You May Be Having a Class ,Practical,Church Service or An Event";
            intent.putExtra("coding",(ArrayPosition).isEmpty()?pro:ArrayPosition);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(Recyker.this,PendingNumber, intent, 0);
            Log.d("pending", "resetAlarm:"+b);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, c, pendingIntent);
            }
        }
    }
    public void startAlarm( Calendar targetCal){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlertRecive.class);
        String pro="Be Reminded You May Be Having a Class ,Practical,Church Service or An Event";
        intent.putExtra("coding",(peopleMessage.isEmpty())?pro:peopleMessage);
        b =code++;

        pendingIntent = PendingIntent.getBroadcast(Recyker.this,b, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
        }

    }

    public void AlertDialog(){
        AlertDialog.Builder building = new AlertDialog.Builder(this);
        building.setTitle(Html.fromHtml("<font color='#000000'>CONFIRM ALARM</font>"))
                .setCancelable(false)
                .setIcon(R.drawable.updated)
                .setMessage(Html.fromHtml("<font color='#000000'> Confirm your alarm by clicking setAlarm....enjoy your stay in reno</font>"))
                .setPositiveButton(Html.fromHtml("<font color='#010A5A'>Set Alarm</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentDate=mAgapter.getCurrentDate();
                        int hashCurrentDate=currentDate.hashCode();
                        String message=mAgapter.getYourMessage();
                        SaveAlarm(cal.getTimeInMillis(),String.valueOf(hashCurrentDate),b,message);
                        Log.d("where", "pending Saved" +b);
                    }
                });

        building.create();
        building.show();
    }

    public void SaveAlarm(long Time,String Positon,int PendingIntent,String message){
        Log.d("MODCOLO", "SaveAlarm:" + "Time  " +Time + " Position" +Positon);
        boolean isInserted=dbHelper.insertData(Time,Positon,PendingIntent,message);
        if(isInserted ==true){
            Toast.makeText(Recyker.this,"DATA ADDED",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Recyker.this,"DATA NOT INSERTED",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {

        ExampleClasss example=mexampleList.get(position);
        final int deletedDate=example.getDate().hashCode();
        final String message=example.getmMESSAGE();
        final int pendingPlace= collectPending(String.valueOf(deletedDate));
        Log.d("moody", "onSwiped:"+example.getDate());
        if (viewHolder instanceof ExampleAdapter.ExampleViewHolder) {
            // backup of removed item for undo purpose
            final ExampleClasss deletedItem = mexampleList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAgapter.RemoveItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "You just deleted this alarm!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mAgapter.RestoreItem(deletedItem, deletedIndex);
                    resetAlarm(String.valueOf(deletedDate),message,pendingPlace);
                    isAlarmSet();
                }
            });

            cancleAlarm(pendingPlace);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            isAlarmSet();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.ringtone, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        try {
            Boolean extre=getIntent().getBooleanExtra("EXTRA",false);
            if (extre==true) {
                Intent newIntent = new Intent(this, HomeActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(newIntent);
                overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
                finish();
            } else{
                super.onBackPressed();
                overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
            }

        } catch (Exception e) {
        Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
        savePenndingIntent(code);
    }


}


