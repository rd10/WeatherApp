package com.example.stormy.weather;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Hour implements Serializable {

    private long time;
    private String iconPhrase;
    private double temperature;
    private int icon;
    private String timeZone;

    public Hour() {
    }

    public Hour(long time, String iconPhrase, double temperature, int icon, String timeZone) {
        this.time = time;
        this.iconPhrase = iconPhrase;
        this.temperature = temperature;
        this.icon = icon;
        this.timeZone = timeZone;
    }

    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        Date dateTime = new Date(time * 1000);
        return formatter.format(dateTime);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getIconPhrase() {
        return iconPhrase;
    }

    public void setIconPhrase(String iconPhrase) {
        this.iconPhrase = iconPhrase;
    }

    public int getTemperature() {
        return (int)Math.round(temperature);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getIcon() {
        return Forecast.getIconId(iconPhrase);
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
