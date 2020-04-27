package com.example.reno.messenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //COLUMNS
    static final String ID = "id";
    static final String PHONE = "phone";

    //DB PROPERTIES
    static final String DB_NAME = "reno_numbers";
    static final String TB_NAME = "reno_numbers_table";
    static final int DB_VERSION = 1;

    //CREATE TB STMT
    static final String CREATE_TB = "CREATE TABLE " + TB_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "phone TEXT NOT NULL);";

    //DROP TB STMT
    static final String DROP_TB = "DROP TABLE IF EXISTS " + TB_NAME;

    SQLiteDatabase myDb = getWritableDatabase();

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TB);
        onCreate(db);
    }

    public Boolean add(String phone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PHONE, phone);
        long result = myDb.insert(TB_NAME, null, contentValues);
        return result > 0;
    }

    public Cursor retrieve() {
        String[] columns = {ID, PHONE};
        return myDb.query(TB_NAME, columns, null, null, null, null, ID + " DESC");
    }

    public Boolean checkIfPhoneExist(String phone) {
        boolean phoneExist = false;
        String[] cols = {ID, PHONE};
        String query = "SELECT * FROM " + TB_NAME + " WHERE " + PHONE + " =?";
        Cursor cursor = myDb.rawQuery(query, new String[]{String.valueOf(phone)});
        if (cursor.moveToFirst()) {
            phoneExist = true;
        }
        else {
            phoneExist = false;
        }
        cursor.close();
        return phoneExist;
    }

    public Boolean update(String phone, int id) {
        ContentValues cv = new ContentValues();
        cv.put(PHONE, phone);
        int result = myDb.update(TB_NAME, cv, ID + " =?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Boolean delete(int id) {
        int result = myDb.delete(TB_NAME, ID + " =?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
