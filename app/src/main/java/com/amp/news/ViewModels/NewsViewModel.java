package com.amp.news.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.amp.news.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Models.News.NewsDetail;
import com.amp.news.Repository.NewsDataRepository;

import java.util.List;

/**
 * Created by amal on 18/12/18.
 */

public class NewsViewModel extends AndroidViewModel {

    private LiveData<NewsApiResponse> newsApiResponseLiveData;
    private NewsDataRepository newsDataRepository;
    private LiveData<List<NewsDetail>> savedNews;
    private MutableLiveData<NewsDetail> deletedNewsArticle;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsDataRepository = NewsDataRepository.getInstance(application);
        savedNews = newsDataRepository.getAllSavedNews();
        deletedNewsArticle = new MutableLiveData<>();
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

    public boolean checkIfNewsExists(NewsDetail newsDetail) {
        if (newsDataRepository.getAllSavedNews(newsDetail).isEmpty())
            return false;
        else return true;
    }

    public void deleteArticle(NewsDetail newsDetail) {
        deletedNewsArticle.setValue(newsDetail);
        newsDataRepository.deleteNews(newsDetail);
    }

    public MutableLiveData<NewsDetail> getDeletedNewsArticle(){
        return deletedNewsArticle;
    }


}
