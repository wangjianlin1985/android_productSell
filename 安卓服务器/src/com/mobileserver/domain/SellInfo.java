package com.mobileserver.domain;

public class SellInfo {
    /*���۱��*/
    private int sellId;
    public int getSellId() {
        return sellId;
    }
    public void setSellId(int sellId) {
        this.sellId = sellId;
    }

    /*��Ʒ����*/
    private String productBarCode;
    public String getProductBarCode() {
        return productBarCode;
    }
    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }

    /*������Ա*/
    private String sellPerson;
    public String getSellPerson() {
        return sellPerson;
    }
    public void setSellPerson(String sellPerson) {
        this.sellPerson = sellPerson;
    }

    /*��������*/
    private int sellCount;
    public int getSellCount() {
        return sellCount;
    }
    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    /*��������*/
    private java.sql.Timestamp sellDate;
    public java.sql.Timestamp getSellDate() {
        return sellDate;
    }
    public void setSellDate(java.sql.Timestamp sellDate) {
        this.sellDate = sellDate;
    }

    /*��ע1*/
    private String firstBeizhu;
    public String getFirstBeizhu() {
        return firstBeizhu;
    }
    public void setFirstBeizhu(String firstBeizhu) {
        this.firstBeizhu = firstBeizhu;
    }

    /*��ע2*/
    private String secondBeizhu;
    public String getSecondBeizhu() {
        return secondBeizhu;
    }
    public void setSecondBeizhu(String secondBeizhu) {
        this.secondBeizhu = secondBeizhu;
    }

    /*��ע3*/
    private String thirdBeizhu;
    public String getThirdBeizhu() {
        return thirdBeizhu;
    }
    public void setThirdBeizhu(String thirdBeizhu) {
        this.thirdBeizhu = thirdBeizhu;
    }

}