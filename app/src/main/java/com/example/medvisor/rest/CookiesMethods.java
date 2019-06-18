package com.example.medvisor.rest;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;


public class CookiesMethods {
    public static HashSet<String> getCookies(Context context) throws Exception {
        SharedPreferences mcpPreferences = getSharedPreferences(context);
        return (HashSet<String>) mcpPreferences.getStringSet("cookies", new HashSet<String>());
    }

    public static boolean setCookies(Context context, HashSet<String> cookies) throws Exception {
        SharedPreferences mcpPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = mcpPreferences.edit();
        return editor.putStringSet("cookies", cookies).commit();
    }
}
