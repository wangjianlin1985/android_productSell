package com.chengxusheji.domain;

import java.sql.Timestamp;
public class ProductInfo {
    /*��Ʒ������*/
    private String barCode;
    public String getBarCode() {
        return barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /*�������*/
    private ProductClass classId;
    public ProductClass getClassId() {
        return classId;
    }
    public void setClassId(ProductClass classId) {
        this.classId = classId;
    }

    /*��Ʒ����*/
    private String productName;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /*��������*/
    private Timestamp madeDate;
    public Timestamp getMadeDate() {
        return madeDate;
    }
    public void setMadeDate(Timestamp madeDate) {
        this.madeDate = madeDate;
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