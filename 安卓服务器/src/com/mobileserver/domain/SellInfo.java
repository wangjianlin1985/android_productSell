package com.mobileserver.domain;

public class SellInfo {
    /*销售编号*/
    private int sellId;
    public int getSellId() {
        return sellId;
    }
    public void setSellId(int sellId) {
        this.sellId = sellId;
    }

    /*产品名称*/
    private String productBarCode;
    public String getProductBarCode() {
        return productBarCode;
    }
    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }

    /*销售人员*/
    private String sellPerson;
    public String getSellPerson() {
        return sellPerson;
    }
    public void setSellPerson(String sellPerson) {
        this.sellPerson = sellPerson;
    }

    /*销售数量*/
    private int sellCount;
    public int getSellCount() {
        return sellCount;
    }
    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    /*销售日期*/
    private java.sql.Timestamp sellDate;
    public java.sql.Timestamp getSellDate() {
        return sellDate;
    }
    public void setSellDate(java.sql.Timestamp sellDate) {
        this.sellDate = sellDate;
    }

    /*备注1*/
    private String firstBeizhu;
    public String getFirstBeizhu() {
        return firstBeizhu;
    }
    public void setFirstBeizhu(String firstBeizhu) {
        this.firstBeizhu = firstBeizhu;
    }

    /*备注2*/
    private String secondBeizhu;
    public String getSecondBeizhu() {
        return secondBeizhu;
    }
    public void setSecondBeizhu(String secondBeizhu) {
        this.secondBeizhu = secondBeizhu;
    }

    /*备注3*/
    private String thirdBeizhu;
    public String getThirdBeizhu() {
        return thirdBeizhu;
    }
    public void setThirdBeizhu(String thirdBeizhu) {
        this.thirdBeizhu = thirdBeizhu;
    }

}