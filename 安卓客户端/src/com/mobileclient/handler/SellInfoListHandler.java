package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.SellInfo;
public class SellInfoListHandler extends DefaultHandler {
	private List<SellInfo> sellInfoList = null;
	private SellInfo sellInfo;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (sellInfo != null) { 
            String valueString = new String(ch, start, length); 
            if ("sellId".equals(tempString)) 
            	sellInfo.setSellId(new Integer(valueString).intValue());
            else if ("productBarCode".equals(tempString)) 
            	sellInfo.setProductBarCode(valueString); 
            else if ("sellPerson".equals(tempString)) 
            	sellInfo.setSellPerson(valueString); 
            else if ("sellCount".equals(tempString)) 
            	sellInfo.setSellCount(new Integer(valueString).intValue());
            else if ("sellDate".equals(tempString)) 
            	sellInfo.setSellDate(Timestamp.valueOf(valueString));
            else if ("firstBeizhu".equals(tempString)) 
            	sellInfo.setFirstBeizhu(valueString); 
            else if ("secondBeizhu".equals(tempString)) 
            	sellInfo.setSecondBeizhu(valueString); 
            else if ("thirdBeizhu".equals(tempString)) 
            	sellInfo.setThirdBeizhu(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("SellInfo".equals(localName)&&sellInfo!=null){
			sellInfoList.add(sellInfo);
			sellInfo = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		sellInfoList = new ArrayList<SellInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("SellInfo".equals(localName)) {
            sellInfo = new SellInfo(); 
        }
        tempString = localName; 
	}

	public List<SellInfo> getSellInfoList() {
		return this.sellInfoList;
	}
}
