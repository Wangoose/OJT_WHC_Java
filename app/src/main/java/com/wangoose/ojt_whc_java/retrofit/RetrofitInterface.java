package com.wangoose.ojt_whc_java.retrofit;

import com.wangoose.ojt_whc_java.dto.SearchUsersResult;
import com.wangoose.ojt_whc_java.dto.UserItem;
import com.wangoose.ojt_whc_java.dto.UserNameResult;

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
    getSearchResult(@Query("q") String q, @Query("per_page") int per_page);

    @Headers({
            "accept: application/vnd.github+json",
            "Authorization: " + token
    })
    @GET("/users/{username}")
    Call<UserItem> getUserNameResultToItems(@Path("username") String username);

    @Headers({
            "accept: application/vnd.github+json",
            "Authorization: " + token
    })
    @GET("/users/{username}")
    Call<UserNameResult> getUserNameResult(@Path("username") String username);
}