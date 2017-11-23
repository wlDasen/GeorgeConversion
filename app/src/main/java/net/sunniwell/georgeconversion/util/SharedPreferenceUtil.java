package net.sunniwell.georgeconversion.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 2017/11/9.
 */

public class SharedPreferenceUtil {
    private static final String TAG = "jpd-SPrefUtil";
    private static SharedPreferences mPrefs = null;

    private SharedPreferenceUtil() {}

    public static SharedPreferences getInstance(Context context) {
        if (mPrefs == null){
            synchronized (SharedPreferenceUtil.class) {
                if (mPrefs == null) {
                    mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                }
            }
        }
        return mPrefs;
    }
    public static String getString(Context context, String params, String defValue) {
        return getInstance(context).getString(params, defValue);
    }
    public static boolean getBoolean(Context context, String params, boolean defValue) {
        return getInstance(context).getBoolean(params, defValue);
    }
    public static void setString(Context context, String params, String value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putString(params, value);
        editor.apply();
    }
    public static void setBoolean(Context context, String params, boolean value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putBoolean(params, value);
        editor.apply();
    }
    public static int getInt(Context context, String params, int defValue) {
        return getInstance(context).getInt(params, defValue);
    }
    public static void setInt(Context context, String params, int value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putInt(params, value);
        editor.apply();
    }
    public static long getLong(Context context, String params, long defValue) {
        return getInstance(context).getLong(params, defValue);
    }
    public static void setLong(Context context, String params, long value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putLong(params, value);
        editor.apply();
    }
}