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
	/* 传入产品信息对象，进行产品信息的添加业务 */
	public String AddProductInfo(ProductInfo productInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新产品信息 */
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
			result = "产品信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "产品信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除产品信息 */
	public String DeleteProductInfo(String barCode) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from ProductInfo where barCode='" + barCode + "'";
			db.executeUpdate(sqlString);
			result = "产品信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "产品信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据产品条形码获取到产品信息 */
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
	/* 更新产品信息 */
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
			result = "产品信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "产品信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
