package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.ProductInfo;
public class ProductInfoListHandler extends DefaultHandler {
	private List<ProductInfo> productInfoList = null;
	private ProductInfo productInfo;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (productInfo != null) { 
            String valueString = new String(ch, start, length); 
            if ("barCode".equals(tempString)) 
            	productInfo.setBarCode(valueString); 
            else if ("classId".equals(tempString)) 
            	productInfo.setClassId(new Integer(valueString).intValue());
            else if ("productName".equals(tempString)) 
            	productInfo.setProductName(valueString); 
            else if ("madeDate".equals(tempString)) 
            	productInfo.setMadeDate(Timestamp.valueOf(valueString));
            else if ("firstBeizhu".equals(tempString)) 
            	productInfo.setFirstBeizhu(valueString); 
            else if ("secondBeizhu".equals(tempString)) 
            	productInfo.setSecondBeizhu(valueString); 
            else if ("thirdBeizhu".equals(tempString)) 
            	productInfo.setThirdBeizhu(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("ProductInfo".equals(localName)&&productInfo!=null){
			productInfoList.add(productInfo);
			productInfo = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		productInfoList = new ArrayList<ProductInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("ProductInfo".equals(localName)) {
            productInfo = new ProductInfo(); 
        }
        tempString = localName; 
	}

	public List<ProductInfo> getProductInfoList() {
		return this.productInfoList;
	}
}
