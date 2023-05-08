package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.CountyInfo;
import com.mobileclient.util.HttpUtil;

/*县市信息管理业务逻辑层*/
public class CountyInfoService {
	/* 添加县市信息 */
	public String AddCountyInfo(CountyInfo countyInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cityId", countyInfo.getCityId() + "");
		params.put("cityName", countyInfo.getCityName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CountyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询县市信息 */
	public List<CountyInfo> QueryCountyInfo(CountyInfo queryConditionCountyInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "CountyInfoServlet?action=query";
		if(queryConditionCountyInfo != null) {
			urlString += "&cityName=" + URLEncoder.encode(queryConditionCountyInfo.getCityName(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		CountyInfoListHandler countyInfoListHander = new CountyInfoListHandler();
		xr.setContentHandler(countyInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<CountyInfo> countyInfoList = countyInfoListHander.getCountyInfoList();
		return countyInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<CountyInfo> countyInfoList = new ArrayList<CountyInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				CountyInfo countyInfo = new CountyInfo();
				countyInfo.setCityId(object.getInt("cityId"));
				countyInfo.setCityName(object.getString("cityName"));
				countyInfoList.add(countyInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countyInfoList;
	}

	/* 更新县市信息 */
	public String UpdateCountyInfo(CountyInfo countyInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cityId", countyInfo.getCityId() + "");
		params.put("cityName", countyInfo.getCityName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CountyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除县市信息 */
	public String DeleteCountyInfo(int cityId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cityId", cityId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CountyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "县市信息信息删除失败!";
		}
	}

	/* 根据县市编号获取县市信息对象 */
	public CountyInfo GetCountyInfo(int cityId)  {
		List<CountyInfo> countyInfoList = new ArrayList<CountyInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("cityId", cityId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CountyInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				CountyInfo countyInfo = new CountyInfo();
				countyInfo.setCityId(object.getInt("cityId"));
				countyInfo.setCityName(object.getString("cityName"));
				countyInfoList.add(countyInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = countyInfoList.size();
		if(size>0) return countyInfoList.get(0); 
		else return null; 
	}
}
