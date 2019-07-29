package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.SellPerson;
import com.mobileserver.util.DB;

public class SellPersonDAO {

	public List<SellPerson> QuerySellPerson(String telephone,String name,int countyId,int townId) {
		List<SellPerson> sellPersonList = new ArrayList<SellPerson>();
		DB db = new DB();
		String sql = "select * from SellPerson where 1=1";
		if (!telephone.equals(""))
			sql += " and telephone like '%" + telephone + "%'";
		if (!name.equals(""))
			sql += " and name like '%" + name + "%'";
		if (countyId != 0)
			sql += " and countyId=" + countyId;
		if (townId != 0)
			sql += " and townId=" + townId;
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				SellPerson sellPerson = new SellPerson();
				sellPerson.setTelephone(rs.getString("telephone"));
				sellPerson.setPassword(rs.getString("password"));
				sellPerson.setName(rs.getString("name"));
				sellPerson.setCountyId(rs.getInt("countyId"));
				sellPerson.setTownId(rs.getInt("townId"));
				sellPersonList.add(sellPerson);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return sellPersonList;
	}
	/* 传入销售人员对象，进行销售人员的添加业务 */
	public String AddSellPerson(SellPerson sellPerson) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新销售人员 */
			String sqlString = "insert into SellPerson(telephone,password,name,countyId,townId) values (";
			sqlString += "'" + sellPerson.getTelephone() + "',";
			sqlString += "'" + sellPerson.getPassword() + "',";
			sqlString += "'" + sellPerson.getName() + "',";
			sqlString += sellPerson.getCountyId() + ",";
			sqlString += sellPerson.getTownId() ;
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "销售人员添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "销售人员添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除销售人员 */
	public String DeleteSellPerson(String telephone) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SellPerson where telephone='" + telephone + "'";
			db.executeUpdate(sqlString);
			result = "销售人员删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "销售人员删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据手机号获取到销售人员 */
	public SellPerson GetSellPerson(String telephone) {
		SellPerson sellPerson = null;
		DB db = new DB();
		String sql = "select * from SellPerson where telephone='" + telephone + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				sellPerson = new SellPerson();
				sellPerson.setTelephone(rs.getString("telephone"));
				sellPerson.setPassword(rs.getString("password"));
				sellPerson.setName(rs.getString("name"));
				sellPerson.setCountyId(rs.getInt("countyId"));
				sellPerson.setTownId(rs.getInt("townId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return sellPerson;
	}
	/* 更新销售人员 */
	public String UpdateSellPerson(SellPerson sellPerson) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update SellPerson set ";
			sql += "password='" + sellPerson.getPassword() + "',";
			sql += "name='" + sellPerson.getName() + "',";
			sql += "countyId=" + sellPerson.getCountyId() + ",";
			sql += "townId=" + sellPerson.getTownId();
			sql += " where telephone='" + sellPerson.getTelephone() + "'";
			db.executeUpdate(sql);
			result = "销售人员更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "销售人员更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
