package com.example.reno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotificationDBHelper extends SQLiteOpenHelper{
    private  static final int DATABASE_VERSION =7;
    private  static final String DATABASE_NAME= "NOTIFICATION.db";
    public static final String COL_1="ID";
    public static final String TABLE_NOTIFICATION = "notification";
    public static final String NOTIFICATION_TITLE="TITLE";
    public static final String NOTIFICATION_MESSAGE="MESSAGE";
    public static final String NOTIFICATION_TIME="TIME";
    public static final String NOTIFICATION_IDENTIFIER="IDENTIFIER";
    public static final String NOTIFICATION_IMAGE="URL";


    public NotificationDBHelper(Context context){

        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+TABLE_NOTIFICATION +"(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,MESSAGE TEXT,TIME TEXT,IDENTIFIER TEXT,URL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTIFICATION );
        onCreate(db);
    }


    public  boolean insertData(String title,String message,String time,String identifier,String imageUrl){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(NOTIFICATION_TITLE,title);
        contentValues.put(NOTIFICATION_MESSAGE,message);
        contentValues.put(NOTIFICATION_TIME,time);
        contentValues.put(NOTIFICATION_IDENTIFIER,identifier);
        contentValues.put(NOTIFICATION_IMAGE,imageUrl);
        Long result=db.insert(TABLE_NOTIFICATION,null,contentValues);
        if (result==-1){
            return  false;
        }else{
            return  true;
        }
    }

    public  Integer DeleteData(String id){
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(TABLE_NOTIFICATION,"IDENTIFIER = ?",new String[]{id});
    }

    public  Cursor getAllData(){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NOTIFICATION,null);
        return  res;
    }

}

