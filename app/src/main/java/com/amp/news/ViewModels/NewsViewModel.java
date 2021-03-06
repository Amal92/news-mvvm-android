package com.amp.news.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.amp.news.Models.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Models.News.NewsDetail;
import com.amp.news.Repository.NewsDataRepository;
import com.amp.news.Repository.NewsDataSource;
import com.amp.news.Repository.NewsDataSourceFactory;

/**
 * Created by amal on 18/12/18.
 */

public class NewsViewModel extends AndroidViewModel {

    LiveData<PagedList<NewsDetail>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, NewsDetail>> liveDataSource;
    NewsDataSourceFactory newsDataSourceFactory;
    private LiveData<NewsApiResponse> newsApiResponseLiveData;
    private NewsDataRepository newsDataRepository;
    private LiveData<PagedList<NewsDetail>> savedNews;
    private MutableLiveData<NewsDetail> deletedNewsArticle;
    private MutableLiveData<Boolean> isLoading;
    private PagedList.Callback callback = new PagedList.Callback() {
        @Override
        public void onChanged(int position, int count) {

        }

        @Override
        public void onInserted(int position, int count) {

        }

        @Override
        public void onRemoved(int position, int count) {

        }
    };


    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsDataRepository = NewsDataRepository.getInstance(application);
        savedNews = newsDataRepository.getAllSavedNews();
        deletedNewsArticle = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    public LiveData<PagedList<NewsDetail>> getNews(String category) {
        newsDataSourceFactory = new NewsDataSourceFactory(category, isLoading);
        liveDataSource = newsDataSourceFactory.getNewsLiveDataSource();
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(NewsDataSource.PAGE_SIZE)
                        .build();

        itemPagedList = new LivePagedListBuilder(newsDataSourceFactory, pagedListConfig)
                .build();

        return itemPagedList;
    }

    public void insertArticle(NewsDetail newsDetail) {
        newsDataRepository.insertNews(newsDetail);
    }

    public LiveData<PagedList<NewsDetail>> getSavedNews() {
        return savedNews;
    }

    public boolean checkIfNewsExists(NewsDetail newsDetail) {
        if (newsDataRepository.getAllSavedNews(newsDetail).isEmpty())
            return false;
        else return true;
    }

    public void deleteArticle(NewsDetail newsDetail) {
        deletedNewsArticle.setValue(newsDetail);
        newsDataRepository.deleteNews(newsDetail);
    }

    public MutableLiveData<NewsDetail> getDeletedNewsArticle() {
        return deletedNewsArticle;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * To refresh list
     */
    public void inValidateDataSource() {
        itemPagedList.getValue().getDataSource().invalidate();
        // or below (both works)
        //newsDataSourceFactory.invalidateDataSource();

    }

}
