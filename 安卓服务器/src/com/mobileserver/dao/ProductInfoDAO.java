package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.ProductInfo;
import com.mobileserver.util.DB;

public class ProductInfoDAO {

	public List<ProductInfo> QueryProductInfo(String barCode,int classId,String productName,Timestamp madeDate) {
		List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
		DB db = new DB();
		String sql = "select * from ProductInfo where 1=1";
		if (!barCode.equals(""))
			sql += " and barCode like '%" + barCode + "%'";
		if (classId != 0)
			sql += " and classId=" + classId;
		if (!productName.equals(""))
			sql += " and productName like '%" + productName + "%'";
		if(madeDate!=null)
			sql += " and madeDate='" + madeDate + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				ProductInfo productInfo = new ProductInfo();
				productInfo.setBarCode(rs.getString("barCode"));
				productInfo.setClassId(rs.getInt("classId"));
				productInfo.setProductName(rs.getString("productName"));
				productInfo.setMadeDate(rs.getTimestamp("madeDate"));
				productInfo.setFirstBeizhu(rs.getString("firstBeizhu"));
				productInfo.setSecondBeizhu(rs.getString("secondBeizhu"));
				productInfo.setThirdBeizhu(rs.getString("thirdBeizhu"));
				productInfoList.add(productInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return productInfoList;
	}
	/* �����Ʒ��Ϣ���󣬽��в�Ʒ��Ϣ�����ҵ�� */
	public String AddProductInfo(ProductInfo productInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����²�Ʒ��Ϣ */
			String sqlString = "insert into ProductInfo(barCode,classId,productName,madeDate,firstBeizhu,secondBeizhu,thirdBeizhu) values (";
			sqlString += "'" + productInfo.getBarCode() + "',";
			sqlString += productInfo.getClassId() + ",";
			sqlString += "'" + productInfo.getProductName() + "',";
			sqlString += "'" + productInfo.getMadeDate() + "',";
			sqlString += "'" + productInfo.getFirstBeizhu() + "',";
			sqlString += "'" + productInfo.getSecondBeizhu() + "',";
			sqlString += "'" + productInfo.getThirdBeizhu() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "��Ʒ��Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "��Ʒ��Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ����Ʒ��Ϣ */
	public String DeleteProductInfo(String barCode) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from ProductInfo where barCode='" + barCode + "'";
			db.executeUpdate(sqlString);
			result = "��Ʒ��Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "��Ʒ��Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݲ�Ʒ�������ȡ����Ʒ��Ϣ */
	public ProductInfo GetProductInfo(String barCode) {
		ProductInfo productInfo = null;
		DB db = new DB();
		String sql = "select * from ProductInfo where barCode='" + barCode + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				productInfo = new ProductInfo();
				productInfo.setBarCode(rs.getString("barCode"));
				productInfo.setClassId(rs.getInt("classId"));
				productInfo.setProductName(rs.getString("productName"));
				productInfo.setMadeDate(rs.getTimestamp("madeDate"));
				productInfo.setFirstBeizhu(rs.getString("firstBeizhu"));
				productInfo.setSecondBeizhu(rs.getString("secondBeizhu"));
				productInfo.setThirdBeizhu(rs.getString("thirdBeizhu"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return productInfo;
	}
	/* ���²�Ʒ��Ϣ */
	public String UpdateProductInfo(ProductInfo productInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update ProductInfo set ";
			sql += "classId=" + productInfo.getClassId() + ",";
			sql += "productName='" + productInfo.getProductName() + "',";
			sql += "madeDate='" + productInfo.getMadeDate() + "',";
			sql += "firstBeizhu='" + productInfo.getFirstBeizhu() + "',";
			sql += "secondBeizhu='" + productInfo.getSecondBeizhu() + "',";
			sql += "thirdBeizhu='" + productInfo.getThirdBeizhu() + "'";
			sql += " where barCode='" + productInfo.getBarCode() + "'";
			db.executeUpdate(sql);
			result = "��Ʒ��Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "��Ʒ��Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
