package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.TownInfo;
import com.mobileserver.util.DB;

public class TownInfoDAO {

	public List<TownInfo> QueryTownInfo(int countyId,String townName) {
		List<TownInfo> townInfoList = new ArrayList<TownInfo>();
		DB db = new DB();
		String sql = "select * from TownInfo where 1=1";
		if (countyId != 0)
			sql += " and countyId=" + countyId;
		if (!townName.equals(""))
			sql += " and townName like '%" + townName + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				TownInfo townInfo = new TownInfo();
				townInfo.setTownId(rs.getInt("townId"));
				townInfo.setCountyId(rs.getInt("countyId"));
				townInfo.setTownName(rs.getString("townName"));
				townInfoList.add(townInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return townInfoList;
	}
	/* 传入乡镇信息对象，进行乡镇信息的添加业务 */
	public String AddTownInfo(TownInfo townInfo) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新乡镇信息 */
			String sqlString = "insert into TownInfo(countyId,townName) values (";
			sqlString += townInfo.getCountyId() + ",";
			sqlString += "'" + townInfo.getTownName() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "乡镇信息添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "乡镇信息添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除乡镇信息 */
	public String DeleteTownInfo(int townId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from TownInfo where townId=" + townId;
			db.executeUpdate(sqlString);
			result = "乡镇信息删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "乡镇信息删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据乡镇编号获取到乡镇信息 */
	public TownInfo GetTownInfo(int townId) {
		TownInfo townInfo = null;
		DB db = new DB();
		String sql = "select * from TownInfo where townId=" + townId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				townInfo = new TownInfo();
				townInfo.setTownId(rs.getInt("townId"));
				townInfo.setCountyId(rs.getInt("countyId"));
				townInfo.setTownName(rs.getString("townName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return townInfo;
	}
	/* 更新乡镇信息 */
	public String UpdateTownInfo(TownInfo townInfo) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update TownInfo set ";
			sql += "countyId=" + townInfo.getCountyId() + ",";
			sql += "townName='" + townInfo.getTownName() + "'";
			sql += " where townId=" + townInfo.getTownId();
			db.executeUpdate(sql);
			result = "乡镇信息更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "乡镇信息更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
