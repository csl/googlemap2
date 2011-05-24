package com.mapplace;

public class ReflectionXMLStruct
{
	  private String loc_id = null;
	  private String re_id = null;
	  private String title = null;
    private String content = null;
    private String date = null;
	  
	  public String getlocid()
	  {
	          return loc_id;
	  }
	  public void setlocid(String id)
	  {
	          this.loc_id = id;
	  }
	  public String getreid()
	  {
	          return re_id;
	  }
	  public void setreid(String id)
	  {
	          this.re_id = id;
	  }

	  public String gettitle()
	  {
	          return title;
	  }
	  public void settitle(String ctitle)
	  {
	          this.title = ctitle;
	  }
	  
    public String getcontent()
    {
            return content;
    }
    public void setcontent(String ct)
    {
            this.content = ct;
    }
	  
    public String getdate()
    {
            return date;
    }
    public void setdate(String de)
    {
            this.date = de;
    }
}
