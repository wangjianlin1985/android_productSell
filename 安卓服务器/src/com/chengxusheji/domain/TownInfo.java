package com.chengxusheji.domain;

import java.sql.Timestamp;
public class TownInfo {
    /*������*/
    private int townId;
    public int getTownId() {
        return townId;
    }
    public void setTownId(int townId) {
        this.townId = townId;
    }

    /*��������*/
    private CountyInfo countyId;
    public CountyInfo getCountyId() {
        return countyId;
    }
    public void setCountyId(CountyInfo countyId) {
        this.countyId = countyId;
    }

    /*��������*/
    private String townName;
    public String getTownName() {
        return townName;
    }
    public void setTownName(String townName) {
        this.townName = townName;
    }

}