package com.example.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {
    private String locationLabel;
    private int icon;
    private long time;
    private double temperature;
    private String iconPhrase;
    private double precipChance;
    private String Summary;


    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        Date dateTime = new Date(time * 1000);
        return  formatter.format(dateTime);
    }

    public CurrentWeather(String locationLabel, int icon, long time, double temperature, String iconPhrase, double precipChance, String summary, String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.iconPhrase = iconPhrase;
        this.precipChance = precipChance;
        Summary = summary;
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    private String timeZone;

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getIconPhrase() {
        return iconPhrase;
    }

    public void setIconPhrase(String iconPhrase) {
        this.iconPhrase = iconPhrase;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public CurrentWeather() {
    }

    public int getIconId(){
        int iconId = R.drawable.clear_day;

        switch (iconPhrase) {
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

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }
}
