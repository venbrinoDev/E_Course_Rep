package com.example.reno;
import android.content.Context;
import android.content.SharedPreferences;
public class SaveActivity {
    final static String FileName = "MyFileName";

    public static String readSharedSettings(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedpref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return sharedpref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedpref = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }
}