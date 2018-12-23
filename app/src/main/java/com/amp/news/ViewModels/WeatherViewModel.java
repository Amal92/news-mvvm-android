package com.amp.news.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amp.news.Models.Weather.WeatherDetail;
import com.amp.news.Repository.WeatherDataRepository;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by amal on 20/12/18.
 */

public class WeatherViewModel extends AndroidViewModel {

    public final LatLng INDIA = new LatLng(21.7679, 78.8718);
    public final LatLng DELHI = new LatLng(28.644800, 77.216721);
    public final LatLng KOLKATA = new LatLng(22.572645, 88.363892);
    public final LatLng MUMBAI = new LatLng(19.0760, 72.8777);
    public final LatLng CHENNAI = new LatLng(13.0827, 80.2707);

    private WeatherDataRepository weatherDataRepository;

    private LiveData<WeatherDetail> chennaiWeatherLiveData;
    private LiveData<WeatherDetail> kolkataWeatherLiveData;
    private LiveData<WeatherDetail> mumbaiWeatherLiveData;
    private LiveData<WeatherDetail> delhiWeatherLiveData;
    private MediatorLiveData<WeatherDetail> currentLocationWeatherLiveData = new MediatorLiveData<>();
    private MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private MutableLiveData<String> currentLocationCityName = new MutableLiveData<>();

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherDataRepository = new WeatherDataRepository(application);

        chennaiWeatherLiveData = weatherDataRepository.getWeatherInfo(CHENNAI.latitude, CHENNAI.longitude);
        kolkataWeatherLiveData = weatherDataRepository.getWeatherInfo(KOLKATA.latitude, KOLKATA.longitude);
        mumbaiWeatherLiveData = weatherDataRepository.getWeatherInfo(MUMBAI.latitude, MUMBAI.longitude);
        delhiWeatherLiveData = weatherDataRepository.getWeatherInfo(DELHI.latitude, DELHI.longitude);
    }

    public LiveData<WeatherDetail> getChennaiWeatherLiveData() {
        return chennaiWeatherLiveData;
    }

    public LiveData<WeatherDetail> getKolkataWeatherLiveData() {
        return kolkataWeatherLiveData;
    }

    public LiveData<WeatherDetail> getMumbaiWeatherLiveData() {
        return mumbaiWeatherLiveData;
    }

    public LiveData<WeatherDetail> getDelhiWeatherLiveData() {
        return delhiWeatherLiveData;
    }

    public MutableLiveData<Location> getCurrentLocationObserver() {
        return currentLocationLiveData;
    }

    public MediatorLiveData<WeatherDetail> getCurrentLocationWeatherLiveData() {
        return currentLocationWeatherLiveData;
    }

    public void getCurrentLocation() {
        weatherDataRepository.getLastKnowLocation(currentLocationLiveData);
    }

    public void getCurrentLocationWeatherInfo() {
        Location location = currentLocationLiveData.getValue();
        currentLocationWeatherLiveData.addSource(weatherDataRepository.getWeatherInfo(location.getLatitude(), location.getLongitude()), new Observer<WeatherDetail>() {
            @Override
            public void onChanged(@Nullable WeatherDetail weatherDetail) {
                currentLocationWeatherLiveData.setValue(weatherDetail);
            }
        });
    }

    public MutableLiveData<String> getCurrentCityNameObservers() {
        return currentLocationCityName;
    }

    public void getCurrentCityName() {
        Location location = currentLocationLiveData.getValue();
        weatherDataRepository.getCurrentCityName(location.getLatitude(), location.getLongitude(),currentLocationCityName);
    }

}
