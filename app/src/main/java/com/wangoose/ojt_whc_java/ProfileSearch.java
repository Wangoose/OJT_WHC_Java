package com.wangoose.ojt_whc_java;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSearch {

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    UserNameResult unResult;

    String username;

    public ProfileSearch(String userId) {

        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        rfInterface.getUserNameResult(userId).enqueue(new Callback<UserNameResult>() {
            @Override
            public void onResponse(Call<UserNameResult> call, Response<UserNameResult> response) {
                if (response.isSuccessful()) {
                    unResult = response.body();
                    username = unResult.getName() == null? "null" : unResult.getName();
                    System.out.println(username);
                }
            }

            @Override
            public void onFailure(Call<UserNameResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
