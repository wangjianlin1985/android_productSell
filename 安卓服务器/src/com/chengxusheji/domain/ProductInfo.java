package com.chengxusheji.domain;

import java.sql.Timestamp;
public class ProductInfo {
    /*产品条形码*/
    private String barCode;
    public String getBarCode() {
        return barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /*所属类别*/
    private ProductClass classId;
    public ProductClass getClassId() {
        return classId;
    }
    public void setClassId(ProductClass classId) {
        this.classId = classId;
    }

    /*产品名称*/
    private String productName;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /*生产日期*/
    private Timestamp madeDate;
    public Timestamp getMadeDate() {
        return madeDate;
    }
    public void setMadeDate(Timestamp madeDate) {
        this.madeDate = madeDate;
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