package com.mobileclient.domain;

import java.io.Serializable;

public class TownInfo implements Serializable {
    /*乡镇编号*/
    private int townId;
    public int getTownId() {
        return townId;
    }
    public void setTownId(int townId) {
        this.townId = townId;
    }

    /*所在县市*/
    private int countyId;
    public int getCountyId() {
        return countyId;
    }
    public void setCountyId(int countyId) {
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