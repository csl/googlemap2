package com.mapplace;

public class EvaluationXMLStruct
{
	  private String id = null;
	  private String loc_id = null;
    private String user_id = null;
    private String grade = null;
	  
	  public String getid()
	  {
	          return id;
	  }
	  public void setid(String mid)
	  {
	          this.id = mid;
	  }

	  public String getlocid()
	  {
	          return loc_id;
	  }
	  public void setloc_id(String id)
	  {
	          this.loc_id = id;
	  }

    public String getuserid()
    {
            return user_id;
    }
    public void setuser_id(String id)
    {
            this.user_id = id;
    }

    public String getgrade()
    {
            return grade;
    }
    public void setgrade(String mgrade)
    {
            this.grade = mgrade;
    }

}
