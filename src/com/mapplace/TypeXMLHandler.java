package com.mapplace;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class TypeXMLHandler extends DefaultHandler
{
	//Check Data
  private final static int TYPEID = 1;
	private final static int TYPE = 2;
	
	private TypeXMLStruct type_xmls;
	
	private int type;
	private boolean hdata;

	public TypeXMLStruct getXMLStruct() 
	{
		return type_xmls;
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
        case TYPEID:
          type_xmls.settype_id(s);
          type = 0;
          break;    			
    		case TYPE:
    		  type_xmls.settype(type_xmls.gettype() + s);
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

		type = 0;
}

	@Override
	public void startDocument() throws SAXException 
	{
	  type_xmls = new TypeXMLStruct();
	  hdata = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.toLowerCase().equals("item")) 
		{
		  type_xmls = new TypeXMLStruct();
		  hdata = true;
		  Log.i("dream", "creat id");
		  return;
		}
		else if (localName.toLowerCase().equals("type_id")) 
		{
	     type = TYPEID;      
       return;
		}		
		else if (localName.toLowerCase().equals("type")) 
		{
	     type = TYPE;      
			 return;
		}
	
		type=0;
	}
}