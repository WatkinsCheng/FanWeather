package com.zhao.fanweather.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Zhao on 2016/5/6.
 */
public class WeatherInfo {
    private String wendu;
    private String city;
    private String note;
    private int aqi;
    private List<Weather> list=new LinkedList<Weather>();

    public WeatherInfo(String city) {
        this.city = city;
    }

    public void addWeather(Weather weather) {
        list.add(weather);
    }

    public String getWendu() {
        return wendu;
    }



    public void setNote(String note) {
        this.note = note;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getCity() {
        return city;
    }


    public String getNote() {
        return note;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public Weather get(int i) {
        return list.get(i);
    }
}
