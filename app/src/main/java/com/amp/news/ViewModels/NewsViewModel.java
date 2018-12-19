package com.amp.news.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.amp.news.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Models.NewsDetail;
import com.amp.news.Repository.NewsDataRepository;

import java.util.List;

/**
 * Created by amal on 18/12/18.
 */

public class NewsViewModel extends AndroidViewModel {

    private LiveData<NewsApiResponse> newsApiResponseLiveData;
    private NewsDataRepository newsDataRepository;
    private LiveData<List<NewsDetail>> savedNews;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsDataRepository = NewsDataRepository.getInstance(application);
        savedNews = newsDataRepository.getAllSavedNews();
    }

    public LiveData<NewsApiResponse> getNews(String category) {
        newsApiResponseLiveData = newsDataRepository.getNews(category);
        return newsApiResponseLiveData;
    }

    public void insertArticle(NewsDetail newsDetail) {
        newsDataRepository.insertNews(newsDetail);
    }

    public LiveData<List<NewsDetail>> getSavedNews() {
        return savedNews;
    }

}
