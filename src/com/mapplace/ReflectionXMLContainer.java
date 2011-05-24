package com.mapplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class ReflectionXMLContainer 
{

  static public List<ReflectionXMLStruct> reflection_item;
  
  private int size;
	
  public ArrayList<HashMap<String, Object>> getReflectionItems() 
	{
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("ItemTitle", "新增心得");
    map.put("ItemText", "=========");
    listitem.add(map);
    
		size = 1;
		for (ReflectionXMLStruct item : reflection_item) 
		{			
		  Log.i("dream", item.gettitle());
			map = new HashMap<String, Object>();
			map.put("ItemTitle", "心得標題: " + item.gettitle());
			map.put("ItemText", Integer.toString(size));
			listitem.add(map);
			size++;		
		}

		
		return listitem;
	}

	
	public ReflectionXMLContainer() 
	{
	  size = 0;
	  reflection_item = new ArrayList<ReflectionXMLStruct>();
	}
	
	public int getSize()
	{
	  return size;	  
	}

	public void addXMLItem(ReflectionXMLStruct item) 
	{
	  reflection_item.add(item);
	}
	
}
