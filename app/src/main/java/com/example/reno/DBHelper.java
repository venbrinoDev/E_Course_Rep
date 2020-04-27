package com.example.reno;

import android.database.Cursor;
import android .database.sqlite.SQLiteDatabase;
import android .database.sqlite.SQLiteCursor;
import android .database.sqlite.SQLiteOpenHelper;
import android .content.Context;
import android .content.ContentValues;

import androidx.annotation.Nullable;

public class DBHelper  extends SQLiteOpenHelper{
    private  static final int DATABASE_VERSION = 1;
    private  static final String DATABASE_NAME= "TIME TABLE.db";
    public static final String COL_1="ID";
    public static final String TABLE_SCHOOL = "stundent";
    public static final String ALARM_TIME="TIME";
    public static final String ALARM_POSITION="ALARM";
    public static final String PENDING_INTENT="PENDING";
    public static final String MESSAGE="AGE";


    public DBHelper(Context context){

        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+TABLE_SCHOOL +"(ID INTEGER PRIMARY KEY AUTOINCREMENT,TIME INTEGER,ALARM TEXT,PENDING INTEGER,AGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCHOOL );
        onCreate(db);
    }


    public  boolean insertData(long time,String positon,int pendingIntent,String message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(PENDING_INTENT,pendingIntent);
        contentValues.put(ALARM_POSITION,positon);
        contentValues.put(ALARM_TIME,time);
        contentValues.put(MESSAGE,message);
        Long result=db.insert(TABLE_SCHOOL,null,contentValues);
        if (result==-1){
            return  false;
        }else{
            return  true;
        }
    }

    public  Integer DeleteData(String id){
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(TABLE_SCHOOL,"ID = ?",new String[]{id});
    }
    public long getAlarmPosition(String position){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=null;
        long code=0;
        try {
            String selectQuery ="SELECT * FROM " + TABLE_SCHOOL + " WHERE " +
                    "ALARM" + " = " + position;
            cursor=db.rawQuery(selectQuery,null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                code = cursor.getLong(1);

            }
            return code;

        }finally {
            cursor.close();
        }
    }
    public int getPendingIntent(String position){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor=null;
        int code=0;
        try {
            String selectQuery ="SELECT * FROM " + TABLE_SCHOOL + " WHERE " +
                    "ALARM" + " = " + position;
            cursor=db.rawQuery(selectQuery,null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                code = cursor.getInt(3);

            }
            return code;

        }finally {
            cursor.close();
        }
    }
    public  Cursor getAllData(){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_SCHOOL,null);
        return  res;
    }

}

