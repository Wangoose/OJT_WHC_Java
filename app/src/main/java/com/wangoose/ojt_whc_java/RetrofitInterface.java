package com.wangoose.ojt_whc_java;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @Headers({
            "accept: application/vnd.github+json",
            "Authorization: token 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"
    })
    @GET("/users")
    Call<List<UserResult>> getUserResult();

    @Headers({
            "accept: application/vnd.github+json",
            "Authorization: token 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"
    })
    @GET("/users/{username}")
    Call<UserNameResult> getUserNameResult(@Path("username") String username);
}