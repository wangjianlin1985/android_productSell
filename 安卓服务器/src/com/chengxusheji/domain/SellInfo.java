package com.chengxusheji.domain;

import java.sql.Timestamp;
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
    private ProductInfo productBarCode;
    public ProductInfo getProductBarCode() {
        return productBarCode;
    }
    public void setProductBarCode(ProductInfo productBarCode) {
        this.productBarCode = productBarCode;
    }

    /*销售人员*/
    private SellPerson sellPerson;
    public SellPerson getSellPerson() {
        return sellPerson;
    }
    public void setSellPerson(SellPerson sellPerson) {
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
    private Timestamp sellDate;
    public Timestamp getSellDate() {
        return sellDate;
    }
    public void setSellDate(Timestamp sellDate) {
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