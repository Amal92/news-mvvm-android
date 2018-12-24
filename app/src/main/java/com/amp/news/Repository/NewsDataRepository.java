package com.amp.news.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;

import com.amp.news.Database.NewsDao;
import com.amp.news.Database.NewsRoomDatabase;
import com.amp.news.Models.News.NewsDetail;
import com.amp.news.Networking.ApiInterface;
import com.amp.news.Networking.RetrofitApiClient;

import java.util.List;

/**
 * Created by amal on 18/12/18.
 */

public class NewsDataRepository {

    private static NewsDataRepository Instance = null;
    private ApiInterface apiInterface;
    LiveData<PagedList<NewsDetail>> savedNews;
    private NewsDao newsDao;

    public NewsDataRepository(Application application) {
        this.apiInterface = RetrofitApiClient.getInstance().create(ApiInterface.class);
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        newsDao = db.wordDao();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPageSize(20).build();
        savedNews = (new LivePagedListBuilder(newsDao.getAllSavedNews(), pagedListConfig))
                .build();
    }

    public static NewsDataRepository getInstance(Application application) {
        if (Instance == null)
            Instance = new NewsDataRepository(application);
        return Instance;
    }

    /**
     * Gets the data stored in database queried by the url param. This method is used to check if a particle news
     * article is already saved to display the corresponding bookmark status.
     * @param newsDetail: the data to be queried.
     * @return
     */
    public List<NewsDetail> getAllSavedNews(NewsDetail newsDetail) {
        return newsDao.getAllSavedNews(newsDetail.getUrl());
    }

    /**
     * Save news article to database
     * @param newsDetail: details of which article to save to database.
     */
    public void insertNews(NewsDetail newsDetail) {
        new insertAsyncTask(newsDao).execute(newsDetail);
    }

    public LiveData<PagedList<NewsDetail>> getAllSavedNews() {
        return savedNews;
    }

    /**
     * Delete news article from databse
     * @param newsDetail: article to delete
     */
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

