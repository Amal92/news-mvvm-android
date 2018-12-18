package com.amp.news.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsDataRepository = new NewsDataRepository();
    }

    public LiveData<NewsApiResponse> getNews(String category){
        newsApiResponseLiveData = newsDataRepository.getNews(category);
        return newsApiResponseLiveData;
    }


}
