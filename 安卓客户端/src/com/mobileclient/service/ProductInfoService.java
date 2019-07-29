package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.ProductInfo;
import com.mobileclient.util.HttpUtil;

/*产品信息管理业务逻辑层*/
public class ProductInfoService {
	/* 添加产品信息 */
	public String AddProductInfo(ProductInfo productInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barCode", productInfo.getBarCode());
		params.put("classId", productInfo.getClassId() + "");
		params.put("productName", productInfo.getProductName());
		params.put("madeDate", productInfo.getMadeDate().toString());
		params.put("firstBeizhu", productInfo.getFirstBeizhu());
		params.put("secondBeizhu", productInfo.getSecondBeizhu());
		params.put("thirdBeizhu", productInfo.getThirdBeizhu());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ProductInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询产品信息 */
	public List<ProductInfo> QueryProductInfo(ProductInfo queryConditionProductInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ProductInfoServlet?action=query";
		if(queryConditionProductInfo != null) {
			urlString += "&barCode=" + URLEncoder.encode(queryConditionProductInfo.getBarCode(), "UTF-8") + "";
			urlString += "&classId=" + queryConditionProductInfo.getClassId();
			urlString += "&productName=" + URLEncoder.encode(queryConditionProductInfo.getProductName(), "UTF-8") + "";
			if(queryConditionProductInfo.getMadeDate() != null) {
				urlString += "&madeDate=" + URLEncoder.encode(queryConditionProductInfo.getMadeDate().toString(), "UTF-8");
			}
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ProductInfoListHandler productInfoListHander = new ProductInfoListHandler();
		xr.setContentHandler(productInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<ProductInfo> productInfoList = productInfoListHander.getProductInfoList();
		return productInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ProductInfo productInfo = new ProductInfo();
				productInfo.setBarCode(object.getString("barCode"));
				productInfo.setClassId(object.getInt("classId"));
				productInfo.setProductName(object.getString("productName"));
				productInfo.setMadeDate(Timestamp.valueOf(object.getString("madeDate")));
				productInfo.setFirstBeizhu(object.getString("firstBeizhu"));
				productInfo.setSecondBeizhu(object.getString("secondBeizhu"));
				productInfo.setThirdBeizhu(object.getString("thirdBeizhu"));
				productInfoList.add(productInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productInfoList;
	}

	/* 更新产品信息 */
	public String UpdateProductInfo(ProductInfo productInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barCode", productInfo.getBarCode());
		params.put("classId", productInfo.getClassId() + "");
		params.put("productName", productInfo.getProductName());
		params.put("madeDate", productInfo.getMadeDate().toString());
		params.put("firstBeizhu", productInfo.getFirstBeizhu());
		params.put("secondBeizhu", productInfo.getSecondBeizhu());
		params.put("thirdBeizhu", productInfo.getThirdBeizhu());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ProductInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除产品信息 */
	public String DeleteProductInfo(String barCode) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barCode", barCode);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ProductInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "产品信息信息删除失败!";
		}
	}

	/* 根据产品条形码获取产品信息对象 */
	public ProductInfo GetProductInfo(String barCode)  {
		List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("barCode", barCode);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ProductInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ProductInfo productInfo = new ProductInfo();
				productInfo.setBarCode(object.getString("barCode"));
				productInfo.setClassId(object.getInt("classId"));
				productInfo.setProductName(object.getString("productName"));
				productInfo.setMadeDate(Timestamp.valueOf(object.getString("madeDate")));
				productInfo.setFirstBeizhu(object.getString("firstBeizhu"));
				productInfo.setSecondBeizhu(object.getString("secondBeizhu"));
				productInfo.setThirdBeizhu(object.getString("thirdBeizhu"));
				productInfoList.add(productInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = productInfoList.size();
		if(size>0) return productInfoList.get(0); 
		else return null; 
	}
}
