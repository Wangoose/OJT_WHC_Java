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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    BookmarkMgmt bookmark;

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    SearchUsersResult bookmarkUserList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bookmark = new BookmarkMgmt(getApplicationContext());

        ImageView ivLogo = findViewById(R.id.ivSplashLogo);
        Animation fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ivLogo.startAnimation(fadeInAnim);

        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        bookmarkUserList = new SearchUsersResult();
        bookmarkUserList.createEmptyItems(); // 빈 ArrayList 배열로 초기화
        bookmarkUserList.setBookmarkList(true);

        LoadBookmarkTask bookmarkThread = new LoadBookmarkTask();
        bookmarkThread.start();

        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() { // 2초 delay 후 인텐트 시작
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("bookmarkUserList", bookmarkUserList);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                            bookmarkUserList.addItems(uResult);
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