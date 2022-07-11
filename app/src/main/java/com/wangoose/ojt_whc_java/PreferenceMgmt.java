package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceMgmt {

    Context context;

    String targetPref;

    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    public PreferenceMgmt(Context context, String targetPref) {
        this.context = context;
        this.targetPref = targetPref;
        sPref = context.getSharedPreferences(targetPref, Context.MODE_PRIVATE);
        editor = sPref.edit();
    }

    public void setPref(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getPref(String key) {
        return sPref.getString(key, "ㅹ");
    }

    public boolean containsPref(String key) {
        return sPref.contains(key);
    }

    public void removePref(String key) {
        editor.remove(key);
        editor.apply();
    }

    public void clearPref() {
        editor.clear();
        editor.apply();
    }
}