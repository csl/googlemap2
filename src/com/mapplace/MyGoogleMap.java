package com.mapplace;

//import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List; 
import java.util.Locale; 

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
import android.content.Intent; 
//import android.graphics.drawable.Drawable;
import android.location.Address; 
import android.location.Criteria; 
import android.location.Geocoder; 
import android.location.Location; 
import android.location.LocationListener; 
import android.location.LocationManager; 
import android.os.Bundle; 
//import android.util.Log;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.RatingBar;
import android.widget.Toast;
//import android.widget.Toast;

import com.google.android.maps.GeoPoint; 
//import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity; 
import com.google.android.maps.MapController; 
import com.google.android.maps.MapView; 
//import com.google.android.maps.Overlay;
//import com.google.android.maps.OverlayItem;

public class MyGoogleMap extends MapActivity 
{ 
  //private TextView mTextView01;
  static public MyGoogleMap my;
  private MyGoogleMap mMyGoogleMap = this;
  private String strLocationProvider = ""; 
  
  private LocationManager mLocationManager01; 
  private Location mLocation01; 
  private MapController mMapController01; 
  private MapView mMapView; 
  
  private MyOverLay overlay;
  private List<MapLocation> mapLocations;

  private Button mButton01,mButton02,mButton03,mButton04,mButton05;
  private int intZoomLevel=0;//geoLatitude,geoLongitude; 
  private GeoPoint nowGeoPoint;
  
  private UserXMLStruct user_xmls;
  private String name = "";
  private String user_id = "";
  private String egrade = "";
  private int showPoint;
  
  private int typeindex;  
  private String loc_id; 
  private int emodify;
  private int eid;
  
  public static  MapLocation mSelectedMapLocation;  
  
  @Override 
  protected void onCreate(Bundle icicle) 
  { 
    // TODO Auto-generated method stub 
    super.onCreate(icicle); 
    setContentView(R.layout.main2); 

    my = this;
    
    //show 3-10 point
    showPoint = 4;
    
    typeindex = 0;
    emodify = 0;
    eid = 0;
    loc_id = "";
    
    boolean datain = false;
    //get IEMI for user_id
    user_id = getIEMI();
   
    //Search user_id
    String uriAPI = "http://2.raywebstory.appspot.com/search_user.jsp?user_id=" +  user_id;

    //XML Parser
    URL url = null;
    try{
        url = new URL(uriAPI);
        
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
              
        XMLReader xml_reader = sp.getXMLReader();
              
        //Using handler for XML
        UserXMLHandler UserXML = new UserXMLHandler();
        xml_reader.setContentHandler(UserXML);
        
        //open connection
        xml_reader.parse(new InputSource(url.openStream()));
        
        user_xmls = UserXML.getXMLStruct();
        datain = UserXML.gethdata();
     }
     catch(Exception e)
     {
      e.printStackTrace();
     }       
    
     if (datain == false)
     {
       //show input diag
       final EditText input = new EditText(this);

       AlertDialog.Builder alert = new AlertDialog.Builder(this);

       alert.setTitle("GoogleMAP");
       alert.setMessage("請輸入您的名字");

       // Set an EditText view to get user input 
       alert.setView(input);

       alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
       public void onClick(DialogInterface dialog, int whichButton) 
       {
         int error = 0;
         
         name = input.getText().toString();
         //register
         String uriAPI = "http://2.raywebstory.appspot.com/insert_user.jsp?user_id=" + user_id  
                                                               + "&name=" + name;
         HttpGet httpRequest = new HttpGet(uriAPI); 
         try 
         { 
           HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
           if(httpResponse.getStatusLine().getStatusCode() == 200)  
             { 
             String strResult = EntityUtils.toString(httpResponse.getEntity());
             //strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);
             //mTextView1.setText(strResult); 
             } 
           else 
             { 
             //mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString()); 
             } 
         } 
         catch (ClientProtocolException e) 
         {  
           //mTextView1.setText(e.getMessage().toString()); 
           e.printStackTrace(); 
           error = 1;
         } 
         catch (IOException e) 
         {  
           //mTextView1.setText(e.getMessage().toString()); 
           e.printStackTrace(); 
           error = 1;
         } 
         catch (Exception e) 
         {  
           //mTextView1.setText(e.getMessage().toString()); 
           e.printStackTrace();
           error = 1;
         }  

         if (error == 1)
           Toast.makeText(MyGoogleMap.this, getString(R.string.new_fail),
             Toast.LENGTH_SHORT).show();
         else
           openOptionsDialog(name + "你好, 歡迎使用!!");
         
       }
       });

       alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {
           // Canceled.
         }
       });

       alert.show();      
       
     }
     else
     {
       name = user_xmls.getname();
       openOptionsDialog(name + "你好, 歡迎使用!!");
     }
      
    mMapView = (MapView)findViewById(R.id.myMapView1); 
    mMapController01 = mMapView.getController(); 
     
    mMapView.setSatellite(false);
    mMapView.setStreetView(true);
    mMapView.setEnabled(true);
    mMapView.setClickable(true);
     
    intZoomLevel = 15; 
    mMapController01.setZoom(intZoomLevel); 
     
    mLocationManager01 =  
    (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
     
    getLocationProvider(); 
     
    nowGeoPoint = getGeoByLocation(mLocation01); 
    
    if (nowGeoPoint == null)
    {
      openOptionsDialog("no GPS rec");    
    }
    
    nowGeoPoint = new GeoPoint(23, 120);
    
    refreshMapViewByGeoPoint(nowGeoPoint, 
                       mMapView, intZoomLevel); 
     
    mLocationManager01.requestLocationUpdates 
    (strLocationProvider, 2000, 10, mLocationListener01); 
     
    getMapLocations(true);
    
    overlay = new MyOverLay(this);
    mMapView.getOverlays().add(overlay);
    //mMapController01.setCenter(getMapLocations(true).get(0).getPoint());

    mButton01 = (Button)findViewById(R.id.myButton1); 
    mButton01.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v) 
      { 
        //nowGeoPoint
        double geoLatitude = 0.0; 
        double geoLongitude = 0.0; 

        if (nowGeoPoint != null)
        {
          geoLatitude = (int)nowGeoPoint.getLatitudeE6()/1E6; 
          geoLongitude = (int)nowGeoPoint.getLongitudeE6()/1E6;
        }
        
        Bundle bundle = new Bundle();
        bundle.putInt("modify", 0);
        bundle.putDouble("geoLatitude", geoLatitude);
        bundle.putDouble("geoLongitude", geoLongitude);

        Intent intent = new Intent();
        intent.setClass(MyGoogleMap.this, AddPlace.class);
        intent.putExtras(bundle);

        startActivity(intent);
      } 
    }); 
     
    mButton02 = (Button)findViewById(R.id.myButton2); 
    mButton02.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        intZoomLevel++; 
        if(intZoomLevel>mMapView.getMaxZoomLevel()) 
        { 
          intZoomLevel = mMapView.getMaxZoomLevel(); 
        } 
        mMapController01.setZoom(intZoomLevel); 
      } 
    }); 
     

    mButton03 = (Button)findViewById(R.id.myButton3); 
    mButton03.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub 
        intZoomLevel--; 
        if(intZoomLevel<1) 
        { 
          intZoomLevel = 1; 
        } 
        mMapController01.setZoom(intZoomLevel); 
      } 
    });

    //Satellite
    mButton04 = (Button)findViewById(R.id.myButton4); 
    mButton04.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v) 
      { 
        // TODO Auto-generated method stub
       String str = mButton04.getText().toString();
        
       if (str.equals("衛星"))
       {
        mButton04.setText("街道");
        mMapView.setStreetView(false);
        mMapView.setSatellite(true);
        mMapView.setTraffic(false);
       }
       else
       {
         mButton04.setText("衛星");
         mMapView.setStreetView(true);
         mMapView.setSatellite(false);
         mMapView.setTraffic(false);
       }
      } 
    }); 

    mButton05 = (Button)findViewById(R.id.myButton5); 
    mButton05.setOnClickListener(new Button.OnClickListener() 
    { 
      public void onClick(View v) 
      {
        final EditText input = new EditText(mMyGoogleMap);

        AlertDialog.Builder alert = new AlertDialog.Builder(mMyGoogleMap);

        alert.setTitle("設定GoogleMAP");
        alert.setMessage("請輸入地圖上近到遠顯示多少點");
        
        input.setText(Integer.toString(showPoint));
        
        // Set an EditText view to get user input 
        alert.setView(input);
        
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) 
        {
          try
          {
            showPoint = Integer.valueOf(input.getText().toString());
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
          getMapLocations(true);
          //mMapController01.setCenter(getMapLocations(true).get(0).getPoint());
        }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
          }
        });

        alert.show();      
        
      } 
    });
   
   /* GeoPoint gp = new GeoPoint((int)geoLatitude,(int)geoLongitude);
    Drawable dr = getResources().getDrawable
    (
      android.R.drawable.arrow 
     );
    dr.setBounds(-15,-15,15, 15);
    
    MyItemOverlay mOverlay01 = new MyItemOverlay(dr,gp);
    List<Overlay> overlays = mMapView.getOverlays();
    overlays.add(mOverlay01);*/
  }
  
  public List<MapLocation> getMapLocations(boolean doit) 
  {
    if (mapLocations == null || doit == true) 
    {
      mapLocations = new ArrayList<MapLocation>();
      
      PlaceManager placemgr = new PlaceManager();
      ArrayList<MapLocation> mapLocationsbuf = placemgr.DataHandler(3, nowGeoPoint);
      
      //show place
      int count = 0;
      for (MapLocation item : mapLocationsbuf)
      {
        if (showPoint < count) break;
        mapLocations.add(item);
        //openOptionsDialog(count + ", " + Double.toString(item.getDist()));
        count++;
      }
      
    }
    return mapLocations;
  }

 
  public final LocationListener mLocationListener01 =  
  new LocationListener() 
  { 
    public void onLocationChanged(Location location) 
    { 
      // TODO Auto-generated method stub 
       
      mLocation01 = location; 
      nowGeoPoint = getGeoByLocation(location); 
      refreshMapViewByGeoPoint(nowGeoPoint, 
            mMapView, intZoomLevel); 
    } 

    public void onProviderDisabled(String provider) 
    { 
      // TODO Auto-generated method stub 
      mLocation01 = null; 
    } 
     
    public void onProviderEnabled(String provider) 
    { 
      // TODO Auto-generated method stub 
       
    } 
     
    public void onStatusChanged(String provider, 
                int status, Bundle extras) 
    { 
      // TODO Auto-generated method stub 
       
    } 
  }; 
   
  private GeoPoint getGeoByLocation(Location location) 
  { 
    GeoPoint gp = null; 
    try 
    { 
      if (location != null) 
      { 
        double geoLatitude = location.getLatitude()*1E6; 
        double geoLongitude = location.getLongitude()*1E6; 
        gp = new GeoPoint((int) geoLatitude, (int) geoLongitude); 
      } 
    } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    } 
    return gp; 
  } 
   
  private GeoPoint getGeoByAddress(String strSearchAddress) 
  { 
    GeoPoint gp = null; 
    try 
    { 
      if(strSearchAddress!="") 
      { 
        Geocoder mGeocoder01 = new Geocoder 
        (MyGoogleMap.this, Locale.getDefault()); 
         
        List<Address> lstAddress = mGeocoder01.getFromLocationName
                           (strSearchAddress, 10);
        if (!lstAddress.isEmpty()) 
        { 
          /*for (int i = 0; i < lstAddress.size(); ++i)
          {
            Address adsLocation = lstAddress.get(i);
            //Log.i(TAG, "Address found = " + adsLocation.toString()); 
            double geoLatitude = adsLocation.getLatitude();
            double geoLongitude = adsLocation.getLongitude();
          } */
          Address adsLocation = lstAddress.get(0); 
          double geoLatitude = adsLocation.getLatitude()*1E6; 
          double geoLongitude = adsLocation.getLongitude()*1E6; 
          gp = new GeoPoint((int) geoLatitude, (int) geoLongitude); 
        }
        
      } 
    } 
    catch (Exception e) 
    {  
      e.printStackTrace();  
    } 
    return gp; 
  } 
   
  public static void refreshMapViewByGeoPoint 
  (GeoPoint gp, MapView mapview, int zoomLevel) 
  { 
    try 
    { 
      mapview.displayZoomControls(true); 
      MapController myMC = mapview.getController(); 
      myMC.animateTo(gp); 
      myMC.setZoom(zoomLevel); 
      mapview.setSatellite(false); 
    } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    } 
  } 
   
  public static void refreshMapViewByCode 
  (double latitude, double longitude, 
      MapView mapview, int zoomLevel) 
  { 
    try 
    { 
      GeoPoint p = new GeoPoint((int) latitude, (int) longitude); 
      mapview.displayZoomControls(true); 
      MapController myMC = mapview.getController(); 
      myMC.animateTo(p); 
      myMC.setZoom(zoomLevel); 
      mapview.setSatellite(false); 
    } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    } 
  } 
   
  private String GeoPointToString(GeoPoint gp) 
  { 
    String strReturn=""; 
    try 
    { 
      if (gp != null) 
      { 
        double geoLatitude = (int)gp.getLatitudeE6()/1E6; 
        double geoLongitude = (int)gp.getLongitudeE6()/1E6; 
        strReturn = String.valueOf(geoLatitude)+","+
          String.valueOf(geoLongitude); 
      } 
    } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    } 
    return strReturn; 
  }
  
  private void EvaluationHandler(int id, int modify, int grade)
  {
    emodify = modify;
    eid = id;
    final RatingBar input = new RatingBar(mMyGoogleMap);
    
    AlertDialog.Builder alert = new AlertDialog.Builder(mMyGoogleMap);

    alert.setTitle("Evaluation");
    alert.setMessage("choice evaluation");
   
    input.setNumStars(5);
    input.setStepSize(1);
    
    if (modify == 1)
    {
      input.setRating(grade);
    }
    
    alert.setView(input);
    
    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int whichButton) 
    {
      String uriAPI="";
      int error=0;
      
      try
      {
        if (emodify == 0)
        {
          uriAPI = "http://2.raywebstory.appspot.com/insert_evaluation.jsp?loc_id=" + loc_id 
          + "&user_id=" + user_id
           + "&grade=" + input.getRating(); 
        }
        else
        {
          uriAPI = "http://2.raywebstory.appspot.com/update_evaluation.jsp?id=" + eid
          + "&loc_id=" + loc_id 
          + "&user_id=" + user_id
          + "&grade=" + input.getRating();
        }
        
        HttpGet httpRequest = new HttpGet(uriAPI); 
        try 
        { 
          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
          if(httpResponse.getStatusLine().getStatusCode() == 200)  
            { 
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            //strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);
            //mTextView1.setText(strResult); 
            } 
          else 
            { 
            //mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString()); 
            } 
        } 
        catch (ClientProtocolException e) 
        {  
          //mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
          error = 1;
        } 
        catch (IOException e) 
        {  
          //mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace(); 
          error = 1;
        } 
        catch (Exception e) 
        {  
          //mTextView1.setText(e.getMessage().toString()); 
          e.printStackTrace();
          error = 1;
        }  

        if (error == 0)
        {
          if (emodify == 0)
          {
            openOptionsDialog("Evaluation Location OK");
          }
          else
          {
            openOptionsDialog("Evaluation Location Modify OK");               
          }
        }
        
        dialog.cancel();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      //mMapController01.setCenter(getMapLocations(true).get(0).getPoint());
    }
    });

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        // Canceled.
      }
    });

    alert.show();      
    
  }

  public void showPlaceDiag(MapLocation mSMapLocation)
  {
    final CharSequence[] type_id = this.getResources().getStringArray(R.array.place_list);
    int checked = 0;
    
    mSelectedMapLocation = mSMapLocation;
    
    typeindex = checked;
    
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("choice place");  
     //builder.setCancelable(false);
    
    builder.setSingleChoiceItems(type_id, checked, new DialogInterface.OnClickListener() { 
      public void onClick(DialogInterface dialog, int which) 
      {
        typeindex = which;
      } 
   }); 
    
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
      public void onClick(DialogInterface dialog, int which) 
      {

        if (typeindex == 0)
        {
          dialog.cancel();
          showPlaced();
        }
        else if (typeindex == 1)
        {
            dialog.cancel();
            showReflection();
        }
         else if (typeindex == 2)
         {
           showEvaluationDiag(mSelectedMapLocation.getlocid());
           dialog.cancel();
         }
     } 
   }); 
     
   AlertDialog alert = builder.create();  
   alert.show();    
      
  }
  
  public void showPlaced()
  {
    Intent intent = new Intent();
    intent.setClass(MyGoogleMap.this, ShowLocation.class);
    //intent.putExtras(bundle);

    startActivity(intent);

  }

  public void showReflection()
  {
    Intent intent = new Intent();
    intent.setClass(MyGoogleMap.this, ReflectionListview.class);
    //intent.putExtras(bundle);

    startActivity(intent);

  }
  
  public void showEvaluationDiag(String locid)
  {
    
    loc_id = locid;

    EvaluationXMLStruct EvalueXML = null;
    boolean datain = false;
    
    String uriAPI = "http://2.raywebstory.appspot.com/search_evaluation.jsp?loc_id=" + loc_id 
    + "&user_id=" + user_id;
    
    //XML Parser
    URL url = null;
    try{
        url = new URL(uriAPI);
        
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
              
        XMLReader xml_reader = sp.getXMLReader();
              
        //Using handler for XML
        EvaluationXMLHandler EvaluationXML = new EvaluationXMLHandler();
        xml_reader.setContentHandler(EvaluationXML);
        
        //open connection
        xml_reader.parse(new InputSource(url.openStream()));
        
        EvalueXML = EvaluationXML.getXMLStruct();
        datain = EvaluationXML.gethdata();
     }
     catch(Exception e)
     {
      e.printStackTrace();
     }       
    
    if (datain == false)
    {
      EvaluationHandler(0, 0, 0);
    }   
    else
    {
      egrade = EvalueXML.getgrade();
      eid = Integer.valueOf(EvalueXML.getid());
      
      new AlertDialog.Builder(this)
      .setTitle("此景點評估過")
      .setMessage("之前評估" + egrade + "星級, 是否要重新評估?")
      .setPositiveButton("Yes",
          new DialogInterface.OnClickListener() {
          
            public void onClick(DialogInterface dialoginterface, int i) 
            {
              EvaluationHandler(eid, 1, Integer.valueOf(egrade));              
            }
      }
      )
   
      .setNegativeButton("No",
          new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialoginterface, int i) {
          }
          
      }
      )
      .show();
    }
  }
  
  public String getIEMI()
  {
    return  ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
  }
   
  public void getLocationProvider() 
  { 
    try 
    { 
      Criteria mCriteria01 = new Criteria(); 
      mCriteria01.setAccuracy(Criteria.ACCURACY_FINE); 
      mCriteria01.setAltitudeRequired(false); 
      mCriteria01.setBearingRequired(false); 
      mCriteria01.setCostAllowed(true); 
      mCriteria01.setPowerRequirement(Criteria.POWER_LOW); 
      strLocationProvider =  
      mLocationManager01.getBestProvider(mCriteria01, true); 
       
      mLocation01 = mLocationManager01.getLastKnownLocation (strLocationProvider); //?
    } 
    catch(Exception e) 
    { 
      //mTextView01.setText(e.toString()); 
      e.printStackTrace(); 
    } 
  }
  
 /* private class MyItemOverlay extends ItemizedOverlay<OverlayItem>
  {
    private List<OverlayItem> items = new ArrayList<OverlayItem>();
    public MyItemOverlay(Drawable defaultMarker , GeoPoint gp)
    {
      super(defaultMarker);
      items.add(new OverlayItem(gp,"Title","Snippet"));
      populate();
    }
    
    @Override
    protected OverlayItem createItem(int i)
    {
      return items.get(i);
    }
    
    @Override
    public int size()
    {
      return items.size();
    }
    
    @Override
    protected boolean onTap(int pIndex)
    {
      Toast.makeText
      (
        Flora_Expo.this,items.get(pIndex).getSnippet(),
        Toast.LENGTH_LONG
      ).show();
      return true;
    }
  }*/
   
  @Override 
  protected boolean isRouteDisplayed() 
  { 
    // TODO Auto-generated method stub 
    return false; 
  } 
  
  //show message
  public void openOptionsDialog(String info)
  {
    new AlertDialog.Builder(this)
    .setTitle("message")
    .setMessage(info)
    .setPositiveButton("OK",
        new DialogInterface.OnClickListener()
        {
         public void onClick(DialogInterface dialoginterface, int i)
         {
         }
         }
        )
    .show();
  }
}
