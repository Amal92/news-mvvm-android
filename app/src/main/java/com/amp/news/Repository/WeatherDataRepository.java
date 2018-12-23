package com.amp.news.Repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.amp.news.Models.Weather.WeatherDetail;
import com.amp.news.Networking.ApiInterface;
import com.amp.news.Networking.RetrofitApiClient;
import com.amp.news.Utils.Const;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amal on 20/12/18.
 */

public class WeatherDataRepository {

    private static WeatherDataRepository Instance = null;
    private ApiInterface apiInterface;
    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder;

    public WeatherDataRepository(Application application) {
        this.apiInterface = RetrofitApiClient.getWeatherInstance().create(ApiInterface.class);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
        geocoder = new Geocoder(application, Locale.getDefault());
    }

    public static WeatherDataRepository getInstance(Application application) {
        if (Instance == null)
            Instance = new WeatherDataRepository(application);
        return Instance;
    }

    public LiveData<WeatherDetail> getWeatherInfo(double latitude, double longitude) {
        final MutableLiveData<WeatherDetail> data = new MutableLiveData<>();

        Call<WeatherDetail> call = apiInterface.getWeatherInfo(latitude, longitude, Const.WEATHER_API_KEY);
        call.enqueue(new Callback<WeatherDetail>() {
            @Override
            public void onResponse(Call<WeatherDetail> call, Response<WeatherDetail> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherDetail> call, Throwable t) {

            }
        });

        return data;
    }

    @SuppressLint("MissingPermission")
    public void getLastKnowLocation(final MutableLiveData<Location> data) {

        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                data.setValue(location);
            }
        });

    }

    public void getCurrentCityName(double latitude, double longitude, MutableLiveData<String> data) {
        //  final MutableLiveData<String> data = new MutableLiveData<>();
        new MyAsyncTask(data, geocoder).execute(latitude, longitude);

    }

    private static class MyAsyncTask extends AsyncTask<Double, Void, Void> {

        private MutableLiveData<String> data;
        private Geocoder geocoder;

        public MyAsyncTask(MutableLiveData<String> data, Geocoder geocoder) {
            this.data = data;
            this.geocoder = geocoder;
        }

        @Override
        protected Void doInBackground(final Double... params) {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(params[0], params[1], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String cityName = addresses.get(0).getLocality();
            data.postValue(cityName);
            return null;
        }
    }

}
