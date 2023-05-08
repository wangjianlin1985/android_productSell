package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.SellPerson;
import com.mobileclient.util.HttpUtil;

/*销售人员管理业务逻辑层*/
public class SellPersonService {
	/* 添加销售人员 */
	public String AddSellPerson(SellPerson sellPerson) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("telephone", sellPerson.getTelephone());
		params.put("password", sellPerson.getPassword());
		params.put("name", sellPerson.getName());
		params.put("countyId", sellPerson.getCountyId() + "");
		params.put("townId", sellPerson.getTownId() + "");
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellPersonServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询销售人员 */
	public List<SellPerson> QuerySellPerson(SellPerson queryConditionSellPerson) throws Exception {
		String urlString = HttpUtil.BASE_URL + "SellPersonServlet?action=query";
		if(queryConditionSellPerson != null) {
			urlString += "&telephone=" + URLEncoder.encode(queryConditionSellPerson.getTelephone(), "UTF-8") + "";
			urlString += "&name=" + URLEncoder.encode(queryConditionSellPerson.getName(), "UTF-8") + "";
			urlString += "&countyId=" + queryConditionSellPerson.getCountyId();
			urlString += "&townId=" + queryConditionSellPerson.getTownId();
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		SellPersonListHandler sellPersonListHander = new SellPersonListHandler();
		xr.setContentHandler(sellPersonListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<SellPerson> sellPersonList = sellPersonListHander.getSellPersonList();
		return sellPersonList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<SellPerson> sellPersonList = new ArrayList<SellPerson>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SellPerson sellPerson = new SellPerson();
				sellPerson.setTelephone(object.getString("telephone"));
				sellPerson.setPassword(object.getString("password"));
				sellPerson.setName(object.getString("name"));
				sellPerson.setCountyId(object.getInt("countyId"));
				sellPerson.setTownId(object.getInt("townId"));
				sellPersonList.add(sellPerson);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sellPersonList;
	}

	/* 更新销售人员 */
	public String UpdateSellPerson(SellPerson sellPerson) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("telephone", sellPerson.getTelephone());
		params.put("password", sellPerson.getPassword());
		params.put("name", sellPerson.getName());
		params.put("countyId", sellPerson.getCountyId() + "");
		params.put("townId", sellPerson.getTownId() + "");
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellPersonServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除销售人员 */
	public String DeleteSellPerson(String telephone) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("telephone", telephone);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellPersonServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "销售人员信息删除失败!";
		}
	}

	/* 根据手机号获取销售人员对象 */
	public SellPerson GetSellPerson(String telephone)  {
		List<SellPerson> sellPersonList = new ArrayList<SellPerson>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("telephone", telephone);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellPersonServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SellPerson sellPerson = new SellPerson();
				sellPerson.setTelephone(object.getString("telephone"));
				sellPerson.setPassword(object.getString("password"));
				sellPerson.setName(object.getString("name"));
				sellPerson.setCountyId(object.getInt("countyId"));
				sellPerson.setTownId(object.getInt("townId"));
				sellPersonList.add(sellPerson);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = sellPersonList.size();
		if(size>0) return sellPersonList.get(0); 
		else return null; 
	}
}
