package com.amp.news.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.amp.news.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Database.NewsDao;
import com.amp.news.Database.NewsRoomDatabase;
import com.amp.news.Models.News.NewsDetail;
import com.amp.news.Networking.ApiInterface;
import com.amp.news.Networking.RetrofitApiClient;
import com.amp.news.Utils.Const;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amal on 18/12/18.
 */

public class NewsDataRepository {

    private static NewsDataRepository Instance = null;
    ApiInterface apiInterface;
    LiveData<List<NewsDetail>> savedNews;
    private NewsDao newsDao;

    public NewsDataRepository(Application application) {
        this.apiInterface = RetrofitApiClient.getInstance().create(ApiInterface.class);
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        newsDao = db.wordDao();
        savedNews = newsDao.getAllSavedNews();
    }

    public static NewsDataRepository getInstance(Application application) {
        if (Instance == null)
            Instance = new NewsDataRepository(application);
        return Instance;
    }

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

    public List<NewsDetail> getAllSavedNews(NewsDetail newsDetail) {
        return newsDao.getAllSavedNews(newsDetail.getUrl());
    }

    public void insertNews(NewsDetail newsDetail) {
        new insertAsyncTask(newsDao).execute(newsDetail);
    }

    public LiveData<List<NewsDetail>> getAllSavedNews() {
        return savedNews;
    }

    public void deleteNews(NewsDetail newsDetail) {
        new deleteAsyncTask(newsDao).execute(newsDetail);
    }

    private static class insertAsyncTask extends AsyncTask<NewsDetail, Void, Void> {

        private NewsDao mAsyncTaskDao;

        insertAsyncTask(NewsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NewsDetail... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<NewsDetail, Void, Void> {

        private NewsDao mAsyncTaskDao;

        deleteAsyncTask(NewsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NewsDetail... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}

