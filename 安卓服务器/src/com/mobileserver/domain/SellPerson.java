package com.mobileserver.domain;

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
    private int countyId;
    public int getCountyId() {
        return countyId;
    }
    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    /*��������*/
    private int townId;
    public int getTownId() {
        return townId;
    }
    public void setTownId(int townId) {
        this.townId = townId;
    }

}