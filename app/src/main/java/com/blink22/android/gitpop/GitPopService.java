package com.blink22.android.gitpop;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by ahmedghazy on 7/18/18.
 */

public interface GitPopService {
    @GET("users/{user}/repos")
    Call<List<Repo>> listReposForUser(@Path("user") String user);

    @GET("/search/repositories?q=language:java")
    Call<List<Repo>> listReposForMostPopularSince(@QueryMap Map<String, String> params);

    @GET("/repos/{repo}/contributors")
    Call<List<User>> listContributersFor(@Path(value = "repo", encoded = true) String repo);
}
