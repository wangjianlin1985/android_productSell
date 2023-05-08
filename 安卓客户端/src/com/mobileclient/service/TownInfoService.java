package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.TownInfo;
import com.mobileclient.util.HttpUtil;

/*乡镇信息管理业务逻辑层*/
public class TownInfoService {
	/* 添加乡镇信息 */
	public String AddTownInfo(TownInfo townInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("townId", townInfo.getTownId() + "");
		params.put("countyId", townInfo.getCountyId() + "");
		params.put("townName", townInfo.getTownName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TownInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询乡镇信息 */
	public List<TownInfo> QueryTownInfo(TownInfo queryConditionTownInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "TownInfoServlet?action=query";
		if(queryConditionTownInfo != null) {
			urlString += "&countyId=" + queryConditionTownInfo.getCountyId();
			urlString += "&townName=" + URLEncoder.encode(queryConditionTownInfo.getTownName(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		TownInfoListHandler townInfoListHander = new TownInfoListHandler();
		xr.setContentHandler(townInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<TownInfo> townInfoList = townInfoListHander.getTownInfoList();
		return townInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<TownInfo> townInfoList = new ArrayList<TownInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				TownInfo townInfo = new TownInfo();
				townInfo.setTownId(object.getInt("townId"));
				townInfo.setCountyId(object.getInt("countyId"));
				townInfo.setTownName(object.getString("townName"));
				townInfoList.add(townInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return townInfoList;
	}

	/* 更新乡镇信息 */
	public String UpdateTownInfo(TownInfo townInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("townId", townInfo.getTownId() + "");
		params.put("countyId", townInfo.getCountyId() + "");
		params.put("townName", townInfo.getTownName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TownInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除乡镇信息 */
	public String DeleteTownInfo(int townId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("townId", townId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TownInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "乡镇信息信息删除失败!";
		}
	}

	/* 根据乡镇编号获取乡镇信息对象 */
	public TownInfo GetTownInfo(int townId)  {
		List<TownInfo> townInfoList = new ArrayList<TownInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("townId", townId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TownInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				TownInfo townInfo = new TownInfo();
				townInfo.setTownId(object.getInt("townId"));
				townInfo.setCountyId(object.getInt("countyId"));
				townInfo.setTownName(object.getString("townName"));
				townInfoList.add(townInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = townInfoList.size();
		if(size>0) return townInfoList.get(0); 
		else return null; 
	}
}
