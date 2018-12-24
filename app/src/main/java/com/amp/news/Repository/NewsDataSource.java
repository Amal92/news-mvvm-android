package com.amp.news.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.amp.news.Models.ApiResponsePojo.ErrorBody;
import com.amp.news.Models.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Models.News.NewsDetail;
import com.amp.news.Networking.ApiInterface;
import com.amp.news.Networking.RetrofitApiClient;
import com.amp.news.Utils.Const;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by amal on 21/12/18.
 */

/**
 * Fetches news data in chunks using pages. PageKeyedDataSource is used since we need to pass page number and
 * page size to get value in chunks.
 */
public class NewsDataSource extends PageKeyedDataSource<Integer, NewsDetail> {

    public static final int PAGE_SIZE = 20;
    public static final int DATABASE_PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private ApiInterface apiInterface;
    private String category;
    private MutableLiveData<Boolean> isLoading;
    private Converter<ResponseBody, ErrorBody> errorConverter =
            RetrofitApiClient.getInstance().responseBodyConverter(ErrorBody.class, new Annotation[0]);

    public NewsDataSource(String category, MutableLiveData<Boolean> isLoading) {
        this.category = category;
        this.apiInterface = RetrofitApiClient.getInstance().create(ApiInterface.class);
        this.isLoading = isLoading;
    }

    /**
     * Loads the initial page and sets the next page count.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, NewsDetail> callback) {
        Call<NewsApiResponse> call;
        if (category == null)
            call = apiInterface.getAllNews(null, Const.NEWS_API_KEY, "google-news", PAGE_SIZE, FIRST_PAGE);
        else
            call = apiInterface.getAllNews(category, Const.NEWS_API_KEY, null, PAGE_SIZE, FIRST_PAGE);
        isLoading.postValue(true);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (response.body() != null && response.code() == 200)
                    callback.onResult(response.body().getNewsDetails(), null, FIRST_PAGE + 1);
                else {
                    /*
                      Use another mutableLiveData to listen and respond for errors on view.
                      On error response check item count and display error message accordingly
                     */
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                isLoading.postValue(false);
            }
        });

    }

    /**
     * Since we iterate from first page, this method won't be called but nice to be defined.
     */
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, NewsDetail> callback) {
        Call<NewsApiResponse> call;
        if (category == null)
            call = apiInterface.getAllNews(null, Const.NEWS_API_KEY, "google-news", PAGE_SIZE, params.key);
        else
            call = apiInterface.getAllNews(category, Const.NEWS_API_KEY, null, PAGE_SIZE, params.key);
        isLoading.postValue(true);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null && response.code() == 200) {

                    //passing the loaded data
                    //and the previous page key
                    callback.onResult(response.body().getNewsDetails(), adjacentKey);
                } else {
                    /*
                      Use another mutableLiveData to listen and respond for errors on view.
                      On error response check item count and display error message accordingly
                     */
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                isLoading.postValue(false);
            }
        });
    }

    /**
     * The last data chunk is determined by comparing total downloaded data vs total available data.
     * Total available data is got by - response.body().getTotalResults() since the api passes the param "totalResults" in
     * the response with total available count.
     */

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, NewsDetail> callback) {
        Call<NewsApiResponse> call;
        if (category == null)
            call = apiInterface.getAllNews(null, Const.NEWS_API_KEY, "google-news", PAGE_SIZE, params.key);
        else
            call = apiInterface.getAllNews(category, Const.NEWS_API_KEY, null, PAGE_SIZE, params.key);
        isLoading.postValue(true);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    //if the response has next page
                    //incrementing the next page number
                    Integer key;
                    if ((params.key * PAGE_SIZE) < response.body().getTotalResults())
                        key = params.key + 1;
                    else key = null;

                    //passing the loaded data and next page value
                    callback.onResult(response.body().getNewsDetails(), key);
                } else {
                    /*
                      Use another mutableLiveData to listen and respond for errors on view.
                      On error response check item count and display error message accordingly
                     */
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
