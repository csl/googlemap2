package com.mapplace;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class LocationXMLHandler extends DefaultHandler
{
	//Check Data
	private final static int LOCID = 1;
  private final static int TYPEID = 2;
	private final static int NAME = 3;
	private final static int TELEPHONE = 4;
  private final static int WEBSITE = 5;
  private final static int ADDR = 6;
  private final static int LATI = 7;
  private final static int LONGIT = 8;
	
	private LocationXMLContainer location_xml;
	private LocationXMLStruct location_xmls;
	private MyGoogleMap mMyGoogleMap;
	private int type;

	public LocationXMLContainer getContainer() 
	{
		return location_xml;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		String s = new String(ch, start, length);
		
		switch (type) 
		{
    		case LOCID:
    		  location_xmls.setlocid(s);
    		  type = 0;
    			break;
        case TYPEID:
          location_xmls.settype_id(s);
          type = 0;
          break;    			
    		case NAME:
          location_xmls.setname(location_xmls.getname() + s);
          //MyGoogleMap.my.openOptionsDialog(new String(ch, start, length));
    			break;
    		case TELEPHONE:
          location_xmls.settelephone(s);
          type = 0;
    			break;
       case WEBSITE:
         location_xmls.setwebsite(s);
         type = 0;
          break;
       case ADDR:
         location_xmls.setaddress(location_xmls.getaddress() + s);
          break;
       case LATI:
         location_xmls.setlatitude(Double.parseDouble(s));
         type = 0;
          break;
       case LONGIT:
         location_xmls.setlongitude(Double.parseDouble(s));
         type = 0;
          break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		//end item
		if (localName.toLowerCase().equals("item")) 
		{
			location_xml.addXMLItem(location_xmls);
			return;
		}
		
		type = 0;
}

	@Override
	public void startDocument() throws SAXException 
	{
	  location_xml = new LocationXMLContainer();
    location_xmls = new LocationXMLStruct();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.toLowerCase().equals("item")) 
		{
		  location_xmls = new LocationXMLStruct();
			return;
		}
		else if (localName.toLowerCase().equals("loc_id")) 
		{
     type = LOCID;      
			return;
		}
		else if (localName.toLowerCase().equals("type_id")) 
		{
	     type = TYPEID;      
       return;
		}		
		else if (localName.toLowerCase().equals("name")) 
		{
	     type = NAME;      
			 return;
		}
    else if (localName.toLowerCase().equals("telephone")) 
    {
       type = TELEPHONE;      
       return;
    }
    else if (localName.toLowerCase().equals("website")) 
    {
      type = WEBSITE;      
      return;
    }
    else if (localName.toLowerCase().equals("address")) 
    {
      type = ADDR;      
      return;
    }
    else if (localName.toLowerCase().equals("latitude")) 
    {
      type = LATI;      
      return;
    }
    else if (localName.toLowerCase().equals("longitude")) 
    {
      type = LONGIT;      
      return;
    }
		
		type=0;
	}
}