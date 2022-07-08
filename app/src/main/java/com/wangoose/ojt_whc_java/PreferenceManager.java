package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class PreferenceManager {

    public void setHashMap(Context context, HashMap<String, String> hashMapData, String hashMapName) {
        SharedPreferences sPref = context.getSharedPreferences("PREF_WHC", Context.MODE_PRIVATE);
        if (sPref != null) {
            JSONObject jsonObject = new JSONObject(hashMapData);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = sPref.edit();
            editor.remove(hashMapName).apply();
            editor.putString(hashMapName, jsonString);
            editor.apply();
        }
    }

    public HashMap<String, String> getHashMap(Context context, String hashMapName) {
        HashMap<String, String> outputMap = new HashMap<String, String>();
        SharedPreferences sPref = context.getSharedPreferences("PREF_WHC", Context.MODE_PRIVATE);
        try {
            if (sPref != null) {
                String jsonString = sPref.getString(hashMapName, (new JSONObject().toString()));
                JSONObject jsonObject = new JSONObject(jsonString);

                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }
}
