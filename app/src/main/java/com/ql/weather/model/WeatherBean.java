package com.ql.weather.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class WeatherBean implements Serializable {
    private String date;
    private String high;
    private String weather;
    private String low;
    private String wind;
    private String city;
    private String richu;
    private String Nhigh;
    private String Nweather;
    private String Nlow;
    private String Nwind;
    private String NwindNum;
    private String Nriluo;
    private String week;
    private String nongli;
    private String time;
    private String temperature;
    private String humidity;
    private String info;

    public WeatherBean() {
    }

    public WeatherBean(String date, String high, String weather, String low, String wind, String city, String richu, String nhigh, String nweather, String nlow, String nwind, String nwindNum, String nriluo, String week, String nongli, String time, String temperature, String humidity, String info) {
        this.date = date;
        this.high = high;
        this.weather = weather;
        this.low = low;
        this.wind = wind;
        this.city = city;
        this.richu = richu;
        Nhigh = nhigh;
        Nweather = nweather;
        Nlow = nlow;
        Nwind = nwind;
        NwindNum = nwindNum;
        Nriluo = nriluo;
        this.week = week;
        this.nongli = nongli;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.info = info;
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

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRichu() {
        return richu;
    }

    public void setRichu(String richu) {
        this.richu = richu;
    }

    public String getNhigh() {
        return Nhigh;
    }

    public void setNhigh(String nhigh) {
        Nhigh = nhigh;
    }

    public String getNweather() {
        return Nweather;
    }

    public void setNweather(String nweather) {
        Nweather = nweather;
    }

    public String getNlow() {
        return Nlow;
    }

    public void setNlow(String nlow) {
        Nlow = nlow;
    }

    public String getNwind() {
        return Nwind;
    }

    public void setNwind(String nwind) {
        Nwind = nwind;
    }

    public String getNwindNum() {
        return NwindNum;
    }

    public void setNwindNum(String nwindNum) {
        NwindNum = nwindNum;
    }

    public String getNriluo() {
        return Nriluo;
    }

    public void setNriluo(String nriluo) {
        Nriluo = nriluo;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getNongli() {
        return nongli;
    }

    public void setNongli(String nongli) {
        this.nongli = nongli;
    }
}
