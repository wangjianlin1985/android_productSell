package com.chengxusheji.domain;

import java.sql.Timestamp;
public class SellPerson {
    /*手机号*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*登录密码*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*姓名*/
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*所在县市*/
    private CountyInfo countyId;
    public CountyInfo getCountyId() {
        return countyId;
    }
    public void setCountyId(CountyInfo countyId) {
        this.countyId = countyId;
    }

    /*所在乡镇*/
    private TownInfo townId;
    public TownInfo getTownId() {
        return townId;
    }
    public void setTownId(TownInfo townId) {
        this.townId = townId;
    }

}