package com.amp.news.Networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amal on 14/12/18.
 *
 *
 * Singleton class to get the instance to retrofit
 */

public class RetrofitApiClient {

    private static String BASE_URL = "https://newsapi.org/v2/";
    private static String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofit_weather = null;

    public static Retrofit getInstance(){
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }

    public static Retrofit getWeatherInstance() {
        if (retrofit_weather == null)
            retrofit_weather = new Retrofit.Builder()
                    .baseUrl(WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit_weather;
    }

}
