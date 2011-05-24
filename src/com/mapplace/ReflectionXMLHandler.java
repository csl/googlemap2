package com.mapplace;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class ReflectionXMLHandler extends DefaultHandler
{
	//Check Data
	private final static int LOCID = 1;
  private final static int RE_ID = 2;
	private final static int TITLE = 3;
	private final static int CONTENT = 4;
  private final static int DATE = 5;
	
	private ReflectionXMLContainer reflection_xml;
	private ReflectionXMLStruct reflection_xmls;
	
	private int type;

	public ReflectionXMLContainer getContainer() 
	{
		return reflection_xml;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	  
		String s = new String(ch, start, length);
		
		switch (type) 
		{
    		case LOCID:
    		  reflection_xmls.setlocid(s);
    		  type = 0;
    			break;
        case RE_ID:
          reflection_xmls.setreid(s);
          type = 0;
          break;    			
    		case TITLE:
          reflection_xmls.settitle(s);
          type = 0;
    			break;
    		case CONTENT:
          reflection_xmls.setcontent(s);
          type = 0;
    			break;
       case DATE:
         reflection_xmls.setdate(s);
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
			reflection_xml.addXMLItem(reflection_xmls);
			return;
		}
}

	@Override
	public void startDocument() throws SAXException 
	{
	  reflection_xml = new ReflectionXMLContainer();
    reflection_xmls = new ReflectionXMLStruct();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.toLowerCase().equals("item")) 
		{
		  reflection_xmls = new ReflectionXMLStruct();
			return;
		}
		else if (localName.toLowerCase().equals("loc_id")) 
		{
     type = LOCID;      
			return;
		}
		else if (localName.toLowerCase().equals("re_id")) 
		{
	     type = RE_ID;      
       return;
		}		
    else if (localName.toLowerCase().equals("title")) 
    {
       type = TITLE;      
       return;
    }   
		else if (localName.toLowerCase().equals("content")) 
		{
	     type = CONTENT;      
			 return;
		}
    else if (localName.toLowerCase().equals("date")) 
    {
       type = DATE;      
       return;
    }
	
		type=0;
	}
}