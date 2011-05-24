package com.mapplace;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class UserXMLHandler extends DefaultHandler
{
	//Check Data
  private final static int USERID = 1;
	private final static int NAME = 2;
	
	private UserXMLStruct user_xmls;
	
	private int type;
	private boolean hdata;

	public UserXMLStruct getXMLStruct() 
	{
		return user_xmls;
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
        case USERID:
          user_xmls.setuser_id(s);
          type = 0;
          break;    			
    		case NAME:
    		  user_xmls.setname(s);
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
			return;
		}
}

	@Override
	public void startDocument() throws SAXException 
	{
	  user_xmls = new UserXMLStruct();
	  hdata = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.toLowerCase().equals("item")) 
		{
		  user_xmls = new UserXMLStruct();
		  hdata = true;
		  Log.i("dream", "creat id");
		  return;
		}
		else if (localName.toLowerCase().equals("user_id")) 
		{
	     type = USERID;      
       return;
		}		
		else if (localName.toLowerCase().equals("name")) 
		{
	     type = NAME;      
			 return;
		}
	
		type=0;
	}
}