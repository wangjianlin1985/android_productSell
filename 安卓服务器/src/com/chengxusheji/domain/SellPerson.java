package com.chengxusheji.domain;

import java.sql.Timestamp;
public class SellPerson {
    /*�ֻ���*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*��¼����*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*����*/
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    private TownInfo townId;
    public TownInfo getTownId() {
        return townId;
    }
    public void setTownId(TownInfo townId) {
        this.townId = townId;
    }

}