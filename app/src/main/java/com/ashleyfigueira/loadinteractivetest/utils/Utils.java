package com.ashleyfigueira.loadinteractivetest.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ashleyfigueira on 19/11/16.
 */
public class Utils
{
    public static final String DB_KEY = "DB_KEY";
    private static final String PREFS_NAME = "PATH_PREF";
    public static final String BUNDLE_KEY = "BUNDLE_KEY";
    public static final String INSTANCE_KEY = "INSTANCE_KEY";
    public static final String ESTABLISHMENT_NAME_KEY = "ESTABLISHMENT_NAME_KEY";
    public static final String ESTABLISHMENT_PHONE_KEY = "ESTABLISHMENT_PHONE_KEY";
    public static final String ESTABLISHMENT_ADDRESS_KEY = "ESTABLISHMENT_ADDRESS_KEY";
    public static final String ESTABLISHMENT_DESCRIPTION_KEY = "ESTABLISHMENT_DESCRIPTION_KEY";

    /**
     * Check network connectivity
     * @param context - context
     * @return true if their is a network, false otherwise.
     */
    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the value  from shared preferences
     * @param context - application context
     * @return a string representation of that path
     */
    public static String getFromSharedPref(Context context, String key)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String path = prefs.getString(key, "");
        return path;
    }

    /**
     * Save  to shared preferences
     * @param context - application context
     * @param key - string key to save
     * @param value - string value to save
     */
    public static void saveToSharedPref(Context context, String key, String value)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
