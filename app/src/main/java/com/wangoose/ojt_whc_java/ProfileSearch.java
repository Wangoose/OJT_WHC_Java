package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSearch {

    BookmarkMgmt bookmark;

    Context context;

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    RviewHolder holder;

    String userId;

    UserItem userItem;

    UserNameResult unResult;

    public ProfileSearch(Context context, RviewHolder holder, UserItem userItem) {
        this.context = context;
        this.holder = holder;
        this.userItem = userItem; // RecyclerView item[position]들을 Call-by-Reference
        userId = userItem.getLogin();
        bookmark = new BookmarkMgmt(context);

        if (!userItem.isLoaded()) { // 프로필 최초 로딩 시 Profile 상세 정보 API 호출
            rfClient = RetrofitClient.getInstance();
            rfInterface = RetrofitClient.getRetrofitInterface();

            rfInterface.getUserNameResult(userId).enqueue(new Callback<UserNameResult>() {
                @Override
                public void onResponse(Call<UserNameResult> call, Response<UserNameResult> response) {
                    if (response.isSuccessful()) {
                        unResult = response.body();
                        Log.d("testlog", "API Loaded : " + userId);
                        saveUserProfile();
                    }
                }

                @Override
                public void onFailure(Call<UserNameResult> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            setRviewItems();
        }
    }

    public void saveUserProfile() {
        if (!userItem.isLoaded()) { // 최초 로딩 시 Profile 상세 정보 저장
            userItem.setName(unResult.getName());
            userItem.setBio(unResult.getBio());
            userItem.setFollowers(unResult.getFollowers());
            userItem.setFollowing(unResult.getFollowing());
            userItem.setLoaded(true);
        }
        setRviewItems();
    }

    public void setRviewItems() {
        Glide.with(context)
                .load(userItem.getAvatarUrl())
                .circleCrop()
                .into(holder.ivAvatar);

        holder.chkbox.setChecked(bookmark.isBookmarked(userId));
        holder.tvName.setText(userItem.getName());
        holder.tvUserId.setText(userId);
        holder.tvBio.setText(userItem.getBio());
    }
}