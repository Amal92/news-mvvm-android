package com.amp.news.Models.Weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amal on 20/12/18.
 */

public class WeatherDetail {

    @SerializedName("weather")
    private List<Weather> weatherList;
    @SerializedName("main")
    private Temperature temperature;
    private String name;

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
