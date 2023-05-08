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
	/* ����������Ա���󣬽���������Ա�����ҵ�� */
	public String AddSellPerson(SellPerson sellPerson) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����������Ա */
			String sqlString = "insert into SellPerson(telephone,password,name,countyId,townId) values (";
			sqlString += "'" + sellPerson.getTelephone() + "',";
			sqlString += "'" + sellPerson.getPassword() + "',";
			sqlString += "'" + sellPerson.getName() + "',";
			sqlString += sellPerson.getCountyId() + ",";
			sqlString += sellPerson.getTownId() ;
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "������Ա��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ա���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��������Ա */
	public String DeleteSellPerson(String telephone) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from SellPerson where telephone='" + telephone + "'";
			db.executeUpdate(sqlString);
			result = "������Աɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Աɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* �����ֻ��Ż�ȡ��������Ա */
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
	/* ����������Ա */
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
			result = "������Ա���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "������Ա����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
