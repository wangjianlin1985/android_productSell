package com.chengxusheji.domain;

import java.sql.Timestamp;
public class TownInfo {
    /*乡镇编号*/
    private int townId;
    public int getTownId() {
        return townId;
    }
    public void setTownId(int townId) {
        this.townId = townId;
    }

    /*所在县市*/
    private CountyInfo countyId;
    public CountyInfo getCountyId() {
        return countyId;
    }
    public void setCountyId(CountyInfo countyId) {
        this.countyId = countyId;
    }

    /*乡镇名称*/
    private String townName;
    public String getTownName() {
        return townName;
    }
    public void setTownName(String townName) {
        this.townName = townName;
    }

}