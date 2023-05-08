package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.CountyInfo;
import com.mobileserver.util.DB;

public class CountyInfoDAO {

	public List<CountyInfo> QueryCountyInfo(String cityName) {
		List<CountyInfo> countyInfoList = new ArrayList<CountyInfo>();
		DB db = new DB();
		String sql = "select * from CountyInfo where 1=1";
		if (!cityName.equals(""))
			sql += " and cityName like '%" + cityName + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				CountyInfo countyInfo = new CountyInfo();
				countyInfo.setCityId(rs.getInt("cityId"));
				countyInfo.setCityName(rs.getString("cityName"));
				countyInfoList.add(countyInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return countyInfoList;
	}
	/* ����������Ϣ���󣬽���������Ϣ�����ҵ�� */
	public String AddCountyInfo(CountyInfo countyInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����������Ϣ */
			String sqlString = "insert into CountyInfo(cityName) values (";
			sqlString += "'" + countyInfo.getCityName() + "'";
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
	public String DeleteCountyInfo(int cityId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from CountyInfo where cityId=" + cityId;
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

	/* �������б�Ż�ȡ��������Ϣ */
	public CountyInfo GetCountyInfo(int cityId) {
		CountyInfo countyInfo = null;
		DB db = new DB();
		String sql = "select * from CountyInfo where cityId=" + cityId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				countyInfo = new CountyInfo();
				countyInfo.setCityId(rs.getInt("cityId"));
				countyInfo.setCityName(rs.getString("cityName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return countyInfo;
	}
	/* ����������Ϣ */
	public String UpdateCountyInfo(CountyInfo countyInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update CountyInfo set ";
			sql += "cityName='" + countyInfo.getCityName() + "'";
			sql += " where cityId=" + countyInfo.getCityId();
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
