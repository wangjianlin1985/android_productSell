package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.SellPerson;
public class SellPersonListHandler extends DefaultHandler {
	private List<SellPerson> sellPersonList = null;
	private SellPerson sellPerson;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (sellPerson != null) { 
            String valueString = new String(ch, start, length); 
            if ("telephone".equals(tempString)) 
            	sellPerson.setTelephone(valueString); 
            else if ("password".equals(tempString)) 
            	sellPerson.setPassword(valueString); 
            else if ("name".equals(tempString)) 
            	sellPerson.setName(valueString); 
            else if ("countyId".equals(tempString)) 
            	sellPerson.setCountyId(new Integer(valueString).intValue());
            else if ("townId".equals(tempString)) 
            	sellPerson.setTownId(new Integer(valueString).intValue());
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("SellPerson".equals(localName)&&sellPerson!=null){
			sellPersonList.add(sellPerson);
			sellPerson = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		sellPersonList = new ArrayList<SellPerson>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("SellPerson".equals(localName)) {
            sellPerson = new SellPerson(); 
        }
        tempString = localName; 
	}

	public List<SellPerson> getSellPersonList() {
		return this.sellPersonList;
	}
}
