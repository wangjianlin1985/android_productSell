package com.mobileclient.domain;

import java.io.Serializable;

public class CountyInfo implements Serializable {
    /*县市编号*/
    private int cityId;
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    /*县市名称*/
    private String cityName;
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}