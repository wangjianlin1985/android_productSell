package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.CountyInfo;
public class CountyInfoListHandler extends DefaultHandler {
	private List<CountyInfo> countyInfoList = null;
	private CountyInfo countyInfo;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (countyInfo != null) { 
            String valueString = new String(ch, start, length); 
            if ("cityId".equals(tempString)) 
            	countyInfo.setCityId(new Integer(valueString).intValue());
            else if ("cityName".equals(tempString)) 
            	countyInfo.setCityName(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("CountyInfo".equals(localName)&&countyInfo!=null){
			countyInfoList.add(countyInfo);
			countyInfo = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		countyInfoList = new ArrayList<CountyInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("CountyInfo".equals(localName)) {
            countyInfo = new CountyInfo(); 
        }
        tempString = localName; 
	}

	public List<CountyInfo> getCountyInfoList() {
		return this.countyInfoList;
	}
}
