package com.chengxusheji.domain;

import java.sql.Timestamp;
public class CountyInfo {
    /*���б��*/
    private int cityId;
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    /*��������*/
    private String cityName;
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}