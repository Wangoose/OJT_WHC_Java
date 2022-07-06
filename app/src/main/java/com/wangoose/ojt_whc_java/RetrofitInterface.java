package com.wangoose.ojt_whc_java;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @Headers({
            "accept: application/vnd.github+json"
    })
    @GET("https://api.github.com/users")
    Call<List<UserResult>> getUserResult();
}