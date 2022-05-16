package com.example.weatherapp;

public class WeatherModelRV {
    private String time;
    private String temperature;
    private String icon;
    private String humidity;

    public WeatherModelRV(String time, String temperature, String icon, String humidity) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
