package com.example.stormy.weather;

import com.example.stormy.R;

public class Forecast {

    private Current current;
    private Hour[] hourlyForecast;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Hour[] getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;

        switch (iconString) {
            case "Mostly clear":
            case "Sunny":
            case "Mostly Sunny":
            case "Hazy Sunshine":
                iconId = R.drawable.clear_day ;
                break;
            case "Showers":
            case "Mostly Cloudy w/ Showers":
            case "Partly Cloudy w/ Showers":
            case "Partly Sunny w/ Showers":
            case "T-Storms":
            case "Mostly Cloudy w/ T-Storms":
            case "Partly Cloudy w/ T-Storms":
            case "Partly Sunny w/ T-Storms":
            case "Rain":
            case "Flurries":
            case "Mostly Cloudy w/ Flurries":
            case "Partly Sunny w/ Flurries":
                iconId = R.drawable.rain;
                break;
            case "Snow":
            case "Mostly Cloudy w/ Snow":
            case "Ice":
            case "Freezing Rain":
            case "Rain and Snow":
            case "Cold":
                iconId = R.drawable.snow;
                break;
            case "Sleet":
                iconId = R.drawable.sleet;
                break;
            case "Windy":
                iconId = R.drawable.wind;
                break;
            case "Fog":
                iconId = R.drawable.fog;
                break;
            case "Mostly Cloudy":
            case "Cloudy":
            case "Dreary (Overcast)":
                iconId = R.drawable.cloudy;
                break;
            case "Partly Sunny":
            case "Intermittent Clouds":
                iconId = R.drawable.partly_cloudy;
                break;
            case "Partly Cloudy":
                iconId = R.drawable.cloudy_night;
                break;
            case "Clear":
            case "Mostly Clear":
            case "Hazy Moonlight":
                iconId = R.drawable.clear_night;
                break;
        }
        return iconId;
    }
}
