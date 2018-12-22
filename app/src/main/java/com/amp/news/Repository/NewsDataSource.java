package com.amp.news.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.amp.news.Models.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Models.News.NewsDetail;
import com.amp.news.Networking.ApiInterface;
import com.amp.news.Networking.RetrofitApiClient;
import com.amp.news.Utils.Const;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amal on 21/12/18.
 */

public class NewsDataSource extends PageKeyedDataSource<Integer, NewsDetail> {

    public static final int PAGE_SIZE = 20;
    public static final int DATABASE_PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private ApiInterface apiInterface;
    private String category;
    private MutableLiveData<Boolean> isLoading;

    public NewsDataSource(String category, MutableLiveData<Boolean> isLoading) {
        this.category = category;
        this.apiInterface = RetrofitApiClient.getInstance().create(ApiInterface.class);
        this.isLoading = isLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, NewsDetail> callback) {
        Call<NewsApiResponse> call;
        if (category == null)
            call = apiInterface.getAllNews(null, Const.API_KEY, "google-news", PAGE_SIZE, FIRST_PAGE);
        else call = apiInterface.getAllNews(category, Const.API_KEY, null, PAGE_SIZE, FIRST_PAGE);
        isLoading.postValue(true);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (response.body() != null)
                    callback.onResult(response.body().getNewsDetails(), null, FIRST_PAGE + 1);
                    isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                isLoading.postValue(false);
            }
        });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, NewsDetail> callback) {
        Call<NewsApiResponse> call;
        if (category == null)
            call = apiInterface.getAllNews(null, Const.API_KEY, "google-news", PAGE_SIZE, params.key);
        else call = apiInterface.getAllNews(category, Const.API_KEY, null, PAGE_SIZE, params.key);
        isLoading.postValue(true);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {

                    //passing the loaded data
                    //and the previous page key
                    callback.onResult(response.body().getNewsDetails(), adjacentKey);
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, NewsDetail> callback) {
        Call<NewsApiResponse> call;
        if (category == null)
            call = apiInterface.getAllNews(null, Const.API_KEY, "google-news", PAGE_SIZE, params.key);
        else call = apiInterface.getAllNews(category, Const.API_KEY, null, PAGE_SIZE, params.key);
        isLoading.postValue(true);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (response.body() != null) {
                    //if the response has next page
                    //incrementing the next page number
                    Integer key;
                    if ((params.key * PAGE_SIZE) < response.body().getTotalResults())
                        key = params.key + 1;
                    else key = null;

                    //passing the loaded data and next page value
                    callback.onResult(response.body().getNewsDetails(), key);
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                isLoading.postValue(false);
            }
        });
    }
}
