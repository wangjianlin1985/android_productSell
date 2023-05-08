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
	/* 传入县市信息对象，进行县市信息的添加业务 */
	public String AddCountyInfo(CountyInfo countyInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新县市信息 */
			String sqlString = "insert into CountyInfo(cityName) values (";
			sqlString += "'" + countyInfo.getCityName() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "县市信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "县市信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除县市信息 */
	public String DeleteCountyInfo(int cityId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from CountyInfo where cityId=" + cityId;
			db.executeUpdate(sqlString);
			result = "县市信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "县市信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据县市编号获取到县市信息 */
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
	/* 更新县市信息 */
	public String UpdateCountyInfo(CountyInfo countyInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update CountyInfo set ";
			sql += "cityName='" + countyInfo.getCityName() + "'";
			sql += " where cityId=" + countyInfo.getCityId();
			db.executeUpdate(sql);
			result = "县市信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "县市信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
