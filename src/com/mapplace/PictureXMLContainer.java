package com.mapplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.maps.GeoPoint;

public class PictureXMLContainer 
{

  static public List<PictureXMLStruct> picture_item;
  
  private int size;
	
	public PictureXMLContainer() 
	{
	  size = 0;
	  picture_item = new ArrayList<PictureXMLStruct>();
	}

  public String getImagePath(int index)
  {
    return picture_item.get(index).getimagepath();    
  }
	
	
	public int getSize()
	{
	  return size;	  
	}

	public void addXMLItem(PictureXMLStruct item) 
	{
	  picture_item.add(item);
	}
	
}
