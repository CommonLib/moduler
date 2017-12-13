package com.architecture.extend.baselib.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.architecture.extend.baselib.config.AppConfig;

/**
 * Created by azhang335 on 11/1/16.
 */

public class SharedPreferencesUtil {

    private static final String SHARED_PATH = AppConfig.SHARED_FILE_NAME;
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        if(sharedPreferences==null){
            sharedPreferences = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void putInt(Context context,String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static int getInt(Context context,String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }

    public static void putString(Context context,String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context,String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key,null);
    }

    public static void putBoolean(Context context,String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(Context context,String key,boolean defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key,defValue);
    }

    public static void remove(Context context,String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
    }
}
