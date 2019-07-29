package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.SellInfo;
import com.mobileclient.util.HttpUtil;

/*销售信息管理业务逻辑层*/
public class SellInfoService {
	/* 添加销售信息 */
	public String AddSellInfo(SellInfo sellInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sellId", sellInfo.getSellId() + "");
		params.put("productBarCode", sellInfo.getProductBarCode());
		params.put("sellPerson", sellInfo.getSellPerson());
		params.put("sellCount", sellInfo.getSellCount() + "");
		params.put("sellDate", sellInfo.getSellDate().toString());
		params.put("firstBeizhu", sellInfo.getFirstBeizhu());
		params.put("secondBeizhu", sellInfo.getSecondBeizhu());
		params.put("thirdBeizhu", sellInfo.getThirdBeizhu());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询销售信息 */
	public List<SellInfo> QuerySellInfo(SellInfo queryConditionSellInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "SellInfoServlet?action=query";
		if(queryConditionSellInfo != null) {
			urlString += "&productBarCode=" + URLEncoder.encode(queryConditionSellInfo.getProductBarCode(), "UTF-8") + "";
			urlString += "&sellPerson=" + URLEncoder.encode(queryConditionSellInfo.getSellPerson(), "UTF-8") + "";
			if(queryConditionSellInfo.getSellDate() != null) {
				urlString += "&sellDate=" + URLEncoder.encode(queryConditionSellInfo.getSellDate().toString(), "UTF-8");
			}
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		SellInfoListHandler sellInfoListHander = new SellInfoListHandler();
		xr.setContentHandler(sellInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<SellInfo> sellInfoList = sellInfoListHander.getSellInfoList();
		return sellInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<SellInfo> sellInfoList = new ArrayList<SellInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SellInfo sellInfo = new SellInfo();
				sellInfo.setSellId(object.getInt("sellId"));
				sellInfo.setProductBarCode(object.getString("productBarCode"));
				sellInfo.setSellPerson(object.getString("sellPerson"));
				sellInfo.setSellCount(object.getInt("sellCount"));
				sellInfo.setSellDate(Timestamp.valueOf(object.getString("sellDate")));
				sellInfo.setFirstBeizhu(object.getString("firstBeizhu"));
				sellInfo.setSecondBeizhu(object.getString("secondBeizhu"));
				sellInfo.setThirdBeizhu(object.getString("thirdBeizhu"));
				sellInfoList.add(sellInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sellInfoList;
	}

	/* 更新销售信息 */
	public String UpdateSellInfo(SellInfo sellInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sellId", sellInfo.getSellId() + "");
		params.put("productBarCode", sellInfo.getProductBarCode());
		params.put("sellPerson", sellInfo.getSellPerson());
		params.put("sellCount", sellInfo.getSellCount() + "");
		params.put("sellDate", sellInfo.getSellDate().toString());
		params.put("firstBeizhu", sellInfo.getFirstBeizhu());
		params.put("secondBeizhu", sellInfo.getSecondBeizhu());
		params.put("thirdBeizhu", sellInfo.getThirdBeizhu());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除销售信息 */
	public String DeleteSellInfo(int sellId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sellId", sellId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "销售信息信息删除失败!";
		}
	}

	/* 根据销售编号获取销售信息对象 */
	public SellInfo GetSellInfo(int sellId)  {
		List<SellInfo> sellInfoList = new ArrayList<SellInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sellId", sellId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "SellInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				SellInfo sellInfo = new SellInfo();
				sellInfo.setSellId(object.getInt("sellId"));
				sellInfo.setProductBarCode(object.getString("productBarCode"));
				sellInfo.setSellPerson(object.getString("sellPerson"));
				sellInfo.setSellCount(object.getInt("sellCount"));
				sellInfo.setSellDate(Timestamp.valueOf(object.getString("sellDate")));
				sellInfo.setFirstBeizhu(object.getString("firstBeizhu"));
				sellInfo.setSecondBeizhu(object.getString("secondBeizhu"));
				sellInfo.setThirdBeizhu(object.getString("thirdBeizhu"));
				sellInfoList.add(sellInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = sellInfoList.size();
		if(size>0) return sellInfoList.get(0); 
		else return null; 
	}
}
