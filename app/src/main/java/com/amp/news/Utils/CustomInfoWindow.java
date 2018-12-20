package com.amp.news.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amp.news.Models.Weather.WeatherDetail;
import com.amp.news.R;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

/**
 * Created by amal on 20/12/18.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private View view;
    private Context context;

    public CustomInfoWindow(Context context) {
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, view);
        return view;
    }

    private void renderWindowText(Marker marker, View view) {
        TextView temp_tv = view.findViewById(R.id.temp_tv);
        TextView location_tv = view.findViewById(R.id.location_tv);
        WeatherIconView my_weather_icon = view.findViewById(R.id.my_weather_icon);
        Gson gson = new Gson();
        WeatherDetail weatherDetail = gson.fromJson(marker.getSnippet(), WeatherDetail.class);
        location_tv.setText(marker.getTitle());
        temp_tv.setText(String.format("%s°C", weatherDetail.getTemperature().getTemp()));
        switch (weatherDetail.getWeatherList().get(0).getIcon()) {
            case "01d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_day_sunny));
                break;
            case "02d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_cloudy_gusts));
                break;
            case "03d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_cloud_down));
                break;
            case "04d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_cloudy));
                break;
            case "09d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_day_showers));
                break;
            case "10d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_day_rain_mix));
                break;
            case "11d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_day_thunderstorm));
                break;
            case "13d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_day_snow));
                break;
            case "50d":
                my_weather_icon.setIconResource(context.getString(R.string.wi_day_fog));
                break;
            case "01n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_clear));
                break;
            case "02n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_cloudy));
                break;
            case "03n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_cloudy_gusts));
                break;
            case "04n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_cloudy));
                break;
            case "09n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_alt_showers));
                break;
            case "10n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_cloudy_gusts));
                break;
            case "11n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_rain));
                break;
            case "13n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_snow));
                break;
            case "50n":
                my_weather_icon.setIconResource(context.getString(R.string.wi_night_fog));
                break;
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
