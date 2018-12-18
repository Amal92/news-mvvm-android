package com.amp.news.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.amp.news.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Networking.ApiInterface;
import com.amp.news.Networking.RetrofitApiClient;
import com.amp.news.Utils.Const;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amal on 18/12/18.
 */

public class NewsDataRepository {

    ApiInterface apiInterface;

    public NewsDataRepository() {
        this.apiInterface = RetrofitApiClient.getInstance().create(ApiInterface.class);
    }

    /*public static NewsDataRepository getInstance(){

    }*/

    public LiveData<NewsApiResponse> getNews(String category) {
        final MutableLiveData<NewsApiResponse> data = new MutableLiveData<>();

        Call<NewsApiResponse> call;
        if (category == null)
            call = apiInterface.getAllNews(null, Const.API_KEY, "google-news");
        else call = apiInterface.getAllNews(category, Const.API_KEY, null);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {

            }
        });
        return data;
    }

}

