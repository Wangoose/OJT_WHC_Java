package com.wangoose.ojt_whc_java;

import android.content.Context;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSearch {

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    Context context;

    RviewHolder holder;

    PreferenceManager preferenceManager;

    HashMap<String, String> hashMap;

    UserNameResult unResult;

    int followers, following;

    String userId, username, bio;

    public ProfileSearch(Context context, RviewHolder holder, String userId) {
        this.context = context;
        this.holder = holder;
        this.userId = userId;
        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        rfInterface.getUserNameResult(userId).enqueue(new Callback<UserNameResult>() {
            @Override
            public void onResponse(Call<UserNameResult> call, Response<UserNameResult> response) {
                if (response.isSuccessful()) {
                    unResult = response.body();
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
        username = unResult.getName() == null? "null" : unResult.getName();
        bio = unResult.getBio();
        followers = unResult.getFollowers();
        followers = unResult.getFollowing();

        Glide.with(context)
                .load(unResult.getAvatarUrl())
                .circleCrop()
                .into(holder.ivAvatar);

        holder.tvName.setText(username);
        holder.tvUserId.setText(userId);
        holder.tvBio.setText(bio);

    }
}
