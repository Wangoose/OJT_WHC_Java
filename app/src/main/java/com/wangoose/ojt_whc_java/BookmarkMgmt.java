package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BookmarkMgmt {

    Context context;

    HashMap<String, Boolean> bookmark;

    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    public BookmarkMgmt(Context context) {
        this.context = context;
        bookmark = new HashMap<>();
        sPref = context.getSharedPreferences("PREF_BOOKMARK", Context.MODE_PRIVATE);
        editor = sPref.edit();

        // json HashMap load, reference : https://yoon-0506.tistory.com/61?category=746169
        try {
            String jsonString = sPref.getString("bookmark", (new JSONObject()).toString());
            JSONObject jsonObject = new JSONObject(jsonString);

            Iterator<String> keyStr = jsonObject.keys();
            while (keyStr.hasNext()) {
                String key = keyStr.next();
                boolean value = (boolean) jsonObject.get(key);
                bookmark.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // Constructor

    public void addBookmark(String userId) {
        bookmark.put(userId, true);
        saveBookmarkPref();
    }

    public void removeBookmark(String userId) {
        bookmark.remove(userId);
        saveBookmarkPref();
    }

    public boolean isBookmarked(String userId) {
        return bookmark.containsKey(userId);
    }

    public HashMap<String, Boolean> getBookmark() {
        return bookmark;
    }

    public void saveBookmarkPref() {
        JSONObject jsonObject = new JSONObject(bookmark);
        String jsonString = jsonObject.toString();
        editor.remove("bookmark").commit();
        editor.putString("bookmark", jsonString);
        editor.apply();
    }
}