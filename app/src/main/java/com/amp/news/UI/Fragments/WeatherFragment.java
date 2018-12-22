package com.amp.news.UI.Fragments;


import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amp.news.Models.Weather.WeatherDetail;
import com.amp.news.R;
import com.amp.news.Utils.CustomInfoWindow;
import com.amp.news.ViewModels.WeatherViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 123;
    private GoogleMap mMap;
    private WeatherViewModel weatherViewModel;
    private Marker chennaiMarker, mumbaiMarker, delhiMarker, kolkataMarker, currentMarker;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);

        } else {
            // already permission granted
            weatherViewModel.getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                weatherViewModel.getCurrentLocation();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getActivity()));
        checkPermission();

        // Move camera to India and set it to whole India zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(weatherViewModel.INDIA, 4.6f));
        // Set marker on different location
        delhiMarker = mMap.addMarker(new MarkerOptions().position(weatherViewModel.DELHI).title("Delhi")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        kolkataMarker = mMap.addMarker(new MarkerOptions().position(weatherViewModel.KOLKATA).title("Kolkata")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mumbaiMarker = mMap.addMarker(new MarkerOptions().position(weatherViewModel.MUMBAI).title("Mumbai")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        chennaiMarker = mMap.addMarker(new MarkerOptions().position(weatherViewModel.CHENNAI).title("Chennai")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        setUpDataObservers();

    }

    private void setUpDataObservers() {
        weatherViewModel.getChennaiWeatherLiveData().observe(this, new Observer<WeatherDetail>() {
            @Override
            public void onChanged(@Nullable WeatherDetail weatherDetail) {
                chennaiMarker.setSnippet(new Gson().toJson(weatherDetail));
            }
        });
        weatherViewModel.getDelhiWeatherLiveData().observe(this, new Observer<WeatherDetail>() {
            @Override
            public void onChanged(@Nullable WeatherDetail weatherDetail) {
                delhiMarker.setSnippet(new Gson().toJson(weatherDetail));
            }
        });
        weatherViewModel.getKolkataWeatherLiveData().observe(this, new Observer<WeatherDetail>() {
            @Override
            public void onChanged(@Nullable WeatherDetail weatherDetail) {
                kolkataMarker.setSnippet(new Gson().toJson(weatherDetail));
            }
        });
        weatherViewModel.getMumbaiWeatherLiveData().observe(this, new Observer<WeatherDetail>() {
            @Override
            public void onChanged(@Nullable WeatherDetail weatherDetail) {
                mumbaiMarker.setSnippet(new Gson().toJson(weatherDetail));
            }
        });
        weatherViewModel.getCurrentLocationObserver().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                currentMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                weatherViewModel.getCurrentLocationWeatherInfo();
                weatherViewModel.getCurrentCityName();
            }
        });
        weatherViewModel.getCurrentLocationWeatherLiveData().observe(this, new Observer<WeatherDetail>() {
            @Override
            public void onChanged(@Nullable WeatherDetail weatherDetail) {
                currentMarker.setSnippet(new Gson().toJson(weatherDetail));
            }
        });
        weatherViewModel.getCurrentCityNameObservers().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                currentMarker.setTitle(s);
            }
        });

    }

}
