package com.chengxusheji.domain;

import java.sql.Timestamp;
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
    private ProductInfo productBarCode;
    public ProductInfo getProductBarCode() {
        return productBarCode;
    }
    public void setProductBarCode(ProductInfo productBarCode) {
        this.productBarCode = productBarCode;
    }

    /*������Ա*/
    private SellPerson sellPerson;
    public SellPerson getSellPerson() {
        return sellPerson;
    }
    public void setSellPerson(SellPerson sellPerson) {
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
    private Timestamp sellDate;
    public Timestamp getSellDate() {
        return sellDate;
    }
    public void setSellDate(Timestamp sellDate) {
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