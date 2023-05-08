package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.SellInfo;
import com.mobileserver.util.DB;

public class SellInfoDAO {

	public List<SellInfo> QuerySellInfo(String productBarCode,String sellPerson,Timestamp sellDate) {
		List<SellInfo> sellInfoList = new ArrayList<SellInfo>();
		DB db = new DB();
		String sql = "select * from SellInfo where 1=1";
		if (!productBarCode.equals(""))
			sql += " and productBarCode = '" + productBarCode + "'";
		if (!sellPerson.equals(""))
			sql += " and sellPerson = '" + sellPerson + "'";
		if(sellDate!=null)
			sql += " and sellDate='" + sellDate + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				SellInfo sellInfo = new SellInfo();
				sellInfo.setSellId(rs.getInt("sellId"));
				sellInfo.setProductBarCode(rs.getString("productBarCode"));
				sellInfo.setSellPerson(rs.getString("sellPerson"));
				sellInfo.setSellCount(rs.getInt("sellCount"));
				sellInfo.setSellDate(rs.getTimestamp("sellDate"));
				sellInfo.setFirstBeizhu(rs.getString("firstBeizhu"));
				sellInfo.setSecondBeizhu(rs.getString("secondBeizhu"));
				sellInfo.setThirdBeizhu(rs.getString("thirdBeizhu"));
				sellInfoList.add(sellInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return sellInfoList;
	}
	/* ����������Ϣ���󣬽���������Ϣ�����ҵ�� */
	public String AddSellInfo(SellInfo sellInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����������Ϣ */
			String sqlString = "insert into SellInfo(productBarCode,sellPerson,sellCount,sellDate,firstBeizhu,secondBeizhu,thirdBeizhu) values (";
			sqlString += "'" + sellInfo.getProductBarCode() + "',";
			sqlString += "'" + sellInfo.getSellPerson() + "',";
			sqlString += sellInfo.getSellCount() + ",";
			sqlString += "'" + sellInfo.getSellDate() + "',";
			sqlString += "'" + sellInfo.getFirstBeizhu() + "',";
			sqlString += "'" + sellInfo.getSecondBeizhu() + "',";
			sqlString += "'" + sellInfo.getThirdBeizhu() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "������Ϣ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��������Ϣ */
	public String DeleteSellInfo(int sellId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SellInfo where sellId=" + sellId;
			db.executeUpdate(sqlString);
			result = "������Ϣɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* �������۱�Ż�ȡ��������Ϣ */
	public SellInfo GetSellInfo(int sellId) {
		SellInfo sellInfo = null;
		DB db = new DB();
		String sql = "select * from SellInfo where sellId=" + sellId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				sellInfo = new SellInfo();
				sellInfo.setSellId(rs.getInt("sellId"));
				sellInfo.setProductBarCode(rs.getString("productBarCode"));
				sellInfo.setSellPerson(rs.getString("sellPerson"));
				sellInfo.setSellCount(rs.getInt("sellCount"));
				sellInfo.setSellDate(rs.getTimestamp("sellDate"));
				sellInfo.setFirstBeizhu(rs.getString("firstBeizhu"));
				sellInfo.setSecondBeizhu(rs.getString("secondBeizhu"));
				sellInfo.setThirdBeizhu(rs.getString("thirdBeizhu"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return sellInfo;
	}
	/* ����������Ϣ */
	public String UpdateSellInfo(SellInfo sellInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update SellInfo set ";
			sql += "productBarCode='" + sellInfo.getProductBarCode() + "',";
			sql += "sellPerson='" + sellInfo.getSellPerson() + "',";
			sql += "sellCount=" + sellInfo.getSellCount() + ",";
			sql += "sellDate='" + sellInfo.getSellDate() + "',";
			sql += "firstBeizhu='" + sellInfo.getFirstBeizhu() + "',";
			sql += "secondBeizhu='" + sellInfo.getSecondBeizhu() + "',";
			sql += "thirdBeizhu='" + sellInfo.getThirdBeizhu() + "'";
			sql += " where sellId=" + sellInfo.getSellId();
			db.executeUpdate(sql);
			result = "������Ϣ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ϣ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
