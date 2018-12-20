package com.amp.news.Networking;


import com.amp.news.ApiResponsePojo.NewsApiResponse;
import com.amp.news.Models.Weather.WeatherDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by amal on 14/12/18.
 */

public interface ApiInterface {

    @GET("top-headlines")
    Call<NewsApiResponse> getAllNews(@Query("category") String category, @Query("apiKey") String apiKey, @Query("sources") String source);

    @GET("weather?units=metric")
    Call<WeatherDetail> getWeatherInfo(@Query("lat") double latitude, @Query("lon") double longitude, @Query("appid") String apiKey);

}
