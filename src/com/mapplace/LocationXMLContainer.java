package com.mapplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.maps.GeoPoint;

public class LocationXMLContainer 
{

  static public List<LocationXMLStruct> location_item;
  
  private int size;
	
  public ArrayList<HashMap<String, Object>> getLocationItems() 
	{
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();
		
		size = 0;
		for (LocationXMLStruct item : location_item) 
		{			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemTitle", "´ºÂI: " + item.getname());
			map.put("ItemText", "");
			listitem.add(map);
			size++;
		}
		
		return listitem;
	}
  
  public void getALLMapLocation(ArrayList<MapLocation> al, GeoPoint now_gps) 
  {
    
    if (now_gps == null) return;
    
    size = 0;
    MapLocation new_obj;
    
    for (LocationXMLStruct item : location_item) 
    {    
      new_obj = new MapLocation(item.getname(), item.getlatitude(), item.getlongitude(), Integer.valueOf(item.getlocid()), item);
      new_obj.calDist(now_gps);
      al.add(new_obj);
      size++;
    }
  }

	
	public LocationXMLContainer() 
	{
	  size = 0;
	  location_item = new ArrayList<LocationXMLStruct>();
	}
	
	public int getSize()
	{
	  return size;	  
	}

	public void addXMLItem(LocationXMLStruct item) 
	{
	  location_item.add(item);
	}
	
}
