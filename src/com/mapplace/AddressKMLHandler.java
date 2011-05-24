package com.mapplace;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class AddressKMLHandler extends DefaultHandler
{
	//Check Data
  private final static int COUNTRYNAME  = 1;
  private final static int AAREANAME  = 2;
  private final static int LNAME  = 3;
	private final static int TFNAME = 4;
	
	private int type;
	private boolean hdata;
	
	private String addr;

	public String getaddress() 
	{
		return addr;
	}
	
	public boolean gethdata()
	{
	  return hdata;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	  
		String s = new String(ch, start, length);
		
		switch (type) 
		{
        case COUNTRYNAME:
          addr = addr + s;
          type = 0;
          break;    			
    		case AAREANAME:
          addr = addr + s;
          type = 0;
    			break;
        case LNAME:
          addr = addr + s;
          type = 0;
          break;
        case TFNAME:
          addr = addr + s;
          type = 0;
          break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		//end item
		if (localName.toLowerCase().equals("kml")) 
		{
			return;
		}
}

	@Override
	public void startDocument() throws SAXException 
	{
	  addr = "";
	  hdata = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.toLowerCase().equals("kml")) 
		{
		  hdata = true;
		  return;
		}
		else if (localName.equalsIgnoreCase("CountryName")) 
		{
	     type = COUNTRYNAME;      
       return;
		}		
		else if (localName.equalsIgnoreCase("AdministrativeAreaName")) 
		{
	     type = AAREANAME;      
			 return;
		}
    else if (localName.equalsIgnoreCase("LocalityName")) 
    {
       type = LNAME;      
       return;
    }   
    else if (localName.equalsIgnoreCase("ThoroughfareName")) 
    {
       type = TFNAME;      
       return;
    }
	
		type=0;
	}
}