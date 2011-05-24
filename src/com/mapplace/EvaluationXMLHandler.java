package com.mapplace;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EvaluationXMLHandler extends DefaultHandler
{
	//Check Data
  private final static int ID = 1;
  private final static int LOCID = 2;
  private final static int USERID = 3;
	private final static int GRADE = 4;
	
	private EvaluationXMLStruct evaluation_xmls;
	
	private int type;
	private boolean hdata;

	public EvaluationXMLStruct getXMLStruct() 
	{
		return evaluation_xmls;
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
        case ID:
          evaluation_xmls.setid(s);
          type = 0;
          break;    			
    		case LOCID:
    		  evaluation_xmls.setloc_id(s);
          type = 0;
    			break;
        case USERID:
          evaluation_xmls.setuser_id(s);
          type = 0;
          break;
        case GRADE:
          evaluation_xmls.setgrade(s);
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
	  evaluation_xmls = new EvaluationXMLStruct();
	  hdata = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.toLowerCase().equals("item")) 
		{
		  evaluation_xmls = new EvaluationXMLStruct();
		  hdata = true;
		  return;
		}
		else if (localName.toLowerCase().equals("id")) 
		{
	     type = ID;      
       return;
		}		
		else if (localName.toLowerCase().equals("loc_id")) 
		{
	     type = LOCID;      
			 return;
		}
    else if (localName.toLowerCase().equals("user_id")) 
    {
       type = USERID;      
       return;
    }   
    else if (localName.toLowerCase().equals("grade")) 
    {
       type = GRADE;      
       return;
    }
	
		type=0;
	}
}