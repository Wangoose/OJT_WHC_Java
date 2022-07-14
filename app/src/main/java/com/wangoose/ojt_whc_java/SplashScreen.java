package com.wangoose.ojt_whc_java;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    BookmarkMgmt bookmark;

    List<UserItem> userItemParcel;

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    SearchUsersResult bookmarkUserList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userItemParcel = new ArrayList<>();

        bookmark = new BookmarkMgmt(getApplicationContext());

        ImageView ivLogo = findViewById(R.id.ivSplashLogo);
        Animation fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ivLogo.startAnimation(fadeInAnim);

        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        LoadBookmarkTask bookmarkThread = new LoadBookmarkTask();
        bookmarkThread.start();

        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() { // 2초 delay 후 Intent 시작
            @Override
            public void run() {
                try {
                    bookmarkThread.join();

                    bookmarkUserList = new SearchUsersResult(userItemParcel);
                    bookmarkUserList.setBookmarkList(true);

                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.putExtra("bookmarkUserList", bookmarkUserList);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }, 2000); // Handler()
    } // onCreate()

    class LoadBookmarkTask extends Thread {
        @Override
        public void run() {
            for (String userId : bookmark.getBookmark().keySet()) {
                rfInterface.getUserNameResultToItems(userId).enqueue(new Callback<UserItem>() {
                    @Override
                    public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                        if (response.isSuccessful()) {
                            UserItem uResult = response.body();
                            userItemParcel.add(uResult);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserItem> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        }
    }
}