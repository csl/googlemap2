package com.mapplace;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class PictureXMLHandler extends DefaultHandler
{
	//Check Data
  private final static int RE_ID = 1;
	private final static int IMAGE_PATH = 2;
	
	private PictureXMLContainer picture_xml;
	private PictureXMLStruct picture_xmls;
	
	private int type;

	public PictureXMLContainer getContainer() 
	{
		return picture_xml;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
	  
		String s = new String(ch, start, length);
		
		switch (type) 
		{
        case RE_ID:
          picture_xmls.setreid(s);
          type = 0;
          break;    			
    		case IMAGE_PATH:
          picture_xmls.setimagepath(s);
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
			picture_xml.addXMLItem(picture_xmls);
			return;
		}
}

	@Override
	public void startDocument() throws SAXException 
	{
	  picture_xml = new PictureXMLContainer();
    picture_xmls = new PictureXMLStruct();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.toLowerCase().equals("item")) 
		{
		  picture_xmls = new PictureXMLStruct();
			return;
		}
		else if (localName.toLowerCase().equals("re_id")) 
		{
	     type = RE_ID;      
       return;
		}		
		else if (localName.toLowerCase().equals("image_path")) 
		{
	     type = IMAGE_PATH;      
			 return;
		}
	
		type=0;
	}
}