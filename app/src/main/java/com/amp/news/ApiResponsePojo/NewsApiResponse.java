package com.amp.news.ApiResponsePojo;


import com.amp.news.Models.News.NewsDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amal on 17/12/18.
 */

public class NewsApiResponse {
    @SerializedName("articles")
    private List<NewsDetail> newsDetails;

    public List<NewsDetail> getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(List<NewsDetail> newsDetails) {
        this.newsDetails = newsDetails;
    }
}
