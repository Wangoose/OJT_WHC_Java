package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSearch {

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    Context context;

    RviewHolder holder;

    PreferenceMgmt pref_username, pref_bio;

    UserNameResult unResult;

//    int followers, following;

    String userId, username, bio;

    public ProfileSearch(Context context, RviewHolder holder, String userId) {
        this.context = context;
        this.holder = holder;
        this.userId = userId;
        pref_username = new PreferenceMgmt(context, "PREF_USERNAME");
        pref_bio = new PreferenceMgmt(context, "PREF_USERBIO");
        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        rfInterface.getUserNameResult(userId).enqueue(new Callback<UserNameResult>() {
            @Override
            public void onResponse(Call<UserNameResult> call, Response<UserNameResult> response) {
                if (response.isSuccessful()) {
                    unResult = response.body();
                    Log.d("testlog", "API Loaded : " + userId);
                    profileSearchResult();
                }
            }

            @Override
            public void onFailure(Call<UserNameResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void profileSearchResult() {

        if (pref_username.containsPref(userId)) { // 로딩된 적이 없는 경우
            username = unResult.getName() == null? "" : unResult.getName();
            bio = unResult.getBio() == null? "" : unResult.getBio();
            pref_username.setPref(userId, username);
            pref_bio.setPref(userId, bio);
            Log.d("testlog", "pref setted : " + username + ", "+  bio);
        }
//        followers = unResult.getFollowers();
//        following = unResult.getFollowing();

        username = pref_username.getPref(userId);
        bio = pref_bio.getPref(userId);

        Glide.with(context)
                .load(unResult.getAvatarUrl())
                .circleCrop()
                .into(holder.ivAvatar);

        holder.tvName.setText(username);
        holder.tvUserId.setText(userId);
        holder.tvBio.setText(bio);

    }
}
