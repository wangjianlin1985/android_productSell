package com.mobileserver.domain;

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
    private int countyId;
    public int getCountyId() {
        return countyId;
    }
    public void setCountyId(int countyId) {
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