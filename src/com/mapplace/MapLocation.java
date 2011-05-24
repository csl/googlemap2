package com.mapplace; 

import com.google.android.maps.GeoPoint;

/** Class to hold our location information */
public class MapLocation {

	private GeoPoint	mPoint;
	private String		mName;
	private String   mloc_id;
	private LocationXMLStruct lstruct;
	private double distance;

	public MapLocation(String name, double latitude, double longitude, int locid, LocationXMLStruct ls) 
	{
		this.mName = name;
		mPoint = new GeoPoint((int)(latitude*1e6),(int)(longitude*1e6));
		mloc_id = Integer.toString(locid);
		
		distance = 0.0;
    lstruct = ls;
	}

	public GeoPoint getPoint() {
		return mPoint;
	}

	public String getName() {
		return mName;
	}
	
  public String getlocid() {
    return mloc_id;
  }

  public LocationXMLStruct getLocationStruct() {
    return lstruct;
  }
  
  private double ConvertDegreeToRadians(double degrees)
  {
    return (Math.PI/180)*degrees;
  }
  
  public double GetDistance(GeoPoint gp1, GeoPoint gp2)
  {
    double Lat1r = ConvertDegreeToRadians(gp1.getLatitudeE6()/1E6);
    double Lat2r = ConvertDegreeToRadians(gp2.getLatitudeE6()/1E6);
    double Long1r= ConvertDegreeToRadians(gp1.getLongitudeE6()/1E6);
    double Long2r= ConvertDegreeToRadians(gp2.getLongitudeE6()/1E6);

    double R = 6371;
    double d = Math.acos(Math.sin(Lat1r)*Math.sin(Lat2r)+
               Math.cos(Lat1r)*Math.cos(Lat2r)*
               Math.cos(Long2r-Long1r))*R;
    return d*1000;
  }
  
  public void calDist(GeoPoint gps)
  {
    distance =  GetDistance(gps, mPoint);
  }

  public double getDist()
  {
    return distance;
  }

}
