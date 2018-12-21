package com.amp.news.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.amp.news.Models.News.NewsDetail;

/**
 * Created by amal on 21/12/18.
 */

public class NewsDataSourceFactory extends DataSource.Factory<Integer, NewsDetail> {

    private MutableLiveData<PageKeyedDataSource<Integer, NewsDetail>> newsLiveDataSource = new MutableLiveData<>();
    private String category;

    public NewsDataSourceFactory(String category) {
        this.category = category;
    }

    @Override
    public DataSource<Integer, NewsDetail> create() {
        NewsDataSource newsDataSource = new NewsDataSource(category);
        newsLiveDataSource.postValue(newsDataSource);
        return newsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, NewsDetail>> getNewsLiveDataSource() {
        return newsLiveDataSource;
    }
}
