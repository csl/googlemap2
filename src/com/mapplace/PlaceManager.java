package com.mapplace; 

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class PlaceManager {

  private GeoPoint  mPoint;
  private String    mName;
  
  private ArrayList<MapLocation> mapLocations;
  private LocationXMLContainer LocationContainer;

  public PlaceManager() 
  {
    mapLocations = new ArrayList<MapLocation>();
  }
  
  public ArrayList<MapLocation> DataHandler(int strict, GeoPoint now_gps)
  {
    int index=1;
    strict = 1;
    while (strict >= index)
    {
      String uriAPI = "http://2.raywebstory.appspot.com/search_location.jsp?type_id=" +  index;
  
      //XML Parser
      URL url = null;
      try{
          url = new URL(uriAPI);
          
          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();
                
          XMLReader xml_reader = sp.getXMLReader();
                
          //Using handler for XML
          LocationXMLHandler LocationXML = new LocationXMLHandler();
          xml_reader.setContentHandler(LocationXML);
          
          //open connection
          xml_reader.parse(new InputSource(url.openStream()));
          LocationContainer = LocationXML.getContainer();
          LocationContainer.getALLMapLocation(mapLocations, now_gps);
       }
      catch(Exception e)
       {
        e.printStackTrace();
       }       
      index++;
    }
    
    for (MapLocation item : mapLocations)
    {
      Log.i("toDO", Double.toString(item.getDist()));
    }
    Collections.sort(mapLocations, new Comparator<MapLocation>() {
      public int compare(MapLocation o1, MapLocation o2) 
      {
        if (o1.getDist() > o2.getDist())
          return 1;
        else if (o1.getDist() == o2.getDist())
          return 0;
        else
          return -1;
        
      }
    });
    
    return mapLocations;
  }

}
