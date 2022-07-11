package com.wangoose.ojt_whc_java;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    // Github API Personal token
    String token = "token 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";

    @Headers({
            "accept: application/vnd.github+json",
            "Authorization: " + token
    })
    @GET("/search/users")
    Call<SearchUsersResult>
    getSearchResult(@Query("q") String q, @Query("per_page") String per_page,
                    @Query("page") String page);

    @Headers({
            "accept: application/vnd.github+json",
            "Authorization: " + token
    })
    @GET("/users/{username}")
    Call<UserNameResult> getUserNameResult(@Path("username") String username);
}