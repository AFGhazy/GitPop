package com.blink22.android.gitpop;

import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ahmedghazy on 7/18/18.
 */

public class GitPopFetcher {
    private static final String TAG = "GitPopFetcher";

    Retrofit mRetrofit;
    GitPopService mGitPopService;
    public GitPopFetcher() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(logging);

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
                .build();
        mGitPopService = mRetrofit.create(GitPopService.class);

    }

    public void getReposForUser(String user, Callback<List<Repo>> callback) {
        mGitPopService.listReposForUser(user).enqueue(callback);
    }

    public void getMostPopularReposForTheLastWeek(Callback<List<Repo>> callback) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.DATE, -7);
//        Date dateBefore7Days = cal.getTime();
//        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
//        Log.i(TAG, dt1.format(dateBefore7Days));
        Map<String, String> params = new HashMap<String, String>();
//        params.put("sort", "stars");
//        params.put("order", "desc");
//        params.put("q", "created:>2018-07-01");
        mGitPopService.listReposForMostPopularSince(params).enqueue(callback);
    }

    public void getContributers(String repo, Callback<List<User>> callback) {
        mGitPopService.listContributersFor(repo).enqueue(callback);
    }
}
