package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.TownInfo;
public class TownInfoListHandler extends DefaultHandler {
	private List<TownInfo> townInfoList = null;
	private TownInfo townInfo;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (townInfo != null) { 
            String valueString = new String(ch, start, length); 
            if ("townId".equals(tempString)) 
            	townInfo.setTownId(new Integer(valueString).intValue());
            else if ("countyId".equals(tempString)) 
            	townInfo.setCountyId(new Integer(valueString).intValue());
            else if ("townName".equals(tempString)) 
            	townInfo.setTownName(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("TownInfo".equals(localName)&&townInfo!=null){
			townInfoList.add(townInfo);
			townInfo = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		townInfoList = new ArrayList<TownInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("TownInfo".equals(localName)) {
            townInfo = new TownInfo(); 
        }
        tempString = localName; 
	}

	public List<TownInfo> getTownInfoList() {
		return this.townInfoList;
	}
}
