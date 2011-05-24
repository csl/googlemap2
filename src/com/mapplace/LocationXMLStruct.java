package com.mapplace;

public class LocationXMLStruct
{
	  private String loc_id = "";
	  private String type_id = "";
	  private String name = "";
    private String telephone = "";
    private String website = "";
    private String address = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
	  
	  public String getlocid()
	  {
	          return loc_id;
	  }
	  public void setlocid(String id)
	  {
	          this.loc_id = id;
	  }
	  public String gettypeid()
	  {
	          return type_id;
	  }
	  public void settype_id(String id)
	  {
	          this.type_id = id;
	  }

	  public String getname()
	  {
	          return name;
	  }
	  public void setname(String name)
	  {
	          this.name = name;
	  }
	  
    public String gettelephone()
    {
            return telephone;
    }
    public void settelephone(String te)
    {
            this.telephone = te;
    }
	  
    public String getwebsite()
    {
            return website;
    }
    public void setwebsite(String we)
    {
            this.website = we;
    }

    public String getaddress()
    {
            return address;
    }
    public void setaddress(String addr)
    {
            this.address = addr;
    }
    
    public double getlatitude()
    {
            return latitude;
    }
    public void setlatitude(double le)
    {
            this.latitude = le;
    }

    public double getlongitude()
    {
            return longitude;
    }
    public void setlongitude(double leg)
    {
            this.longitude = leg;
    }

}
