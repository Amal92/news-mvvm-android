package com.amp.news.Models.Weather;

import com.amp.news.Models.ApiResponsePojo.ErrorBody;
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
    private Throwable error;
    private ErrorBody errorBody;

    public WeatherDetail(Throwable error) {
        this.error = error;
    }

    public WeatherDetail(ErrorBody errorBody) {
        this.errorBody = errorBody;
    }

    public WeatherDetail() {
    }

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

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public ErrorBody getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(ErrorBody errorBody) {
        this.errorBody = errorBody;
    }
}
