package com.mapplace;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddPlace extends Activity 
{ 
  private Button mSubmit;
  private Button mDelete;
  private Spinner type_id;
  private EditText name;
  private EditText telephone;
  private EditText website;
  private EditText address;
  private TextView gps_data;
    
  private double geoLatitude;
  private double geoLongitude;
  
  private Bundle bunde;
  private Intent intent;
  private int modify;
  
  //display use
  LocationXMLContainer LocationContainer;
  private ArrayList<HashMap<String, Object>> Location_list;
  private ListView booking_view;
  
  private static final int MENU_START = Menu.FIRST  ;
  private static final int MENU_EXIT = Menu.FIRST +1 ;

  private int typeindex;

  
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addplace);
    
    modify = 0;

    //get GPS data
    geoLatitude = 0;
    geoLongitude = 0;
    
    //Fetch data form Inquire    
    intent = this.getIntent();
    bunde = intent.getExtras();
    
    if (bunde != null)
    {
      modify = bunde.getInt("modify");
    }
    
    if (modify == 1)
    {
      gps_data = (TextView) findViewById(R.id.show);
      gps_data.setText("­×§ï¸ê®Æ");
    }
    else
    {
      geoLatitude = bunde.getDouble("geoLatitude");
      geoLongitude = bunde.getDouble("geoLongitude");
    }
    
    findViews();
    setListensers();      
  }
  
  
  private void setListensers()
  {
    // TODO Auto-generated method stub
    mSubmit.setOnClickListener(add_place);
    mDelete.setOnClickListener(delete_place);
  }


  private void findViews() 
  {
    mSubmit = (Button) findViewById(R.id.new_submit);
    mDelete = (Button) findViewById(R.id.new_delete);
    type_id = (Spinner) findViewById(R.id.type_id);
    name = (EditText) findViewById(R.id.name);
    telephone = (EditText) findViewById(R.id.telephone);
    website = (EditText) findViewById(R.id.website);
    address = (EditText) findViewById(R.id.address);
    gps_data = (TextView) findViewById(R.id.gps);
    
    String lgame[] = this.getResources().getStringArray(R.array.typeid_list);
    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lgame);
    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    type_id.setAdapter(adapter2);
    
    if (modify == 1)
    {
      type_id.setSelection(Integer.valueOf(LocationListview.lxs.gettypeid())-1);
      name.setText(LocationListview.lxs.getname());
      telephone.setText(LocationListview.lxs.gettelephone());
      website.setText(LocationListview.lxs.getwebsite());
      address.setText(LocationListview.lxs.getaddress());
      gps_data.setText(LocationListview.lxs.getlatitude() + "," + LocationListview.lxs.getlongitude());
      geoLatitude = LocationListview.lxs.getlatitude();
      geoLongitude = LocationListview.lxs.getlongitude();
      mDelete.setEnabled(true);
    }
    else
    {
      int error = 0;
      
      //fetch address
      String uriAPI = "http://ditu.google.com/maps/geo?q=" + geoLatitude + "," + geoLongitude + "&output=xml&oe=utf8&hl=zh-TW&sensor=true&key=0SYKmJXBYRPCs8u8gX7Xerqlhrv2KH-w9DByzgg";
      String Gaddress = "";
      
      //XML Parser
      URL url = null;
      try{
          url = new URL(uriAPI);
          
          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();
                
          XMLReader xml_reader = sp.getXMLReader();
                
          //Using handler for XML
          AddressKMLHandler AddrKML = new AddressKMLHandler();
          xml_reader.setContentHandler(AddrKML);
          
          //open connection
          xml_reader.parse(new InputSource(url.openStream()));
          
          Gaddress = AddrKML.getaddress();
       }
       catch(Exception e)
       {
        e.printStackTrace();
        error = 1;
       }
       
      if (error == 0)
      {
        address.setText(Gaddress);        
      }
      
      gps_data.setText(geoLatitude + "," + geoLongitude);
      mDelete.setEnabled(false);
    }
  }
  
  public boolean onCreateOptionsMenu(Menu menu)
  {

    super.onCreateOptionsMenu(menu);
    
    menu.add(0 , MENU_START, 0 ,R.string.menu_modify).setIcon(R.drawable.mappin_red)
    .setAlphabeticShortcut('S');
    
    return true;  
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    Intent intent = new Intent() ;
    
    switch (item.getItemId())
      { 
          case MENU_START:  
          
          final CharSequence[] type_id = this.getResources().getStringArray(R.array.typeid_list);
          int checked = 0;
          
          typeindex = checked;
          
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("choice type");  
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
            
            Bundle bundle = new Bundle();
            bundle.putInt("type_id", typeindex+1);
            //bundle.putStringArrayList("mybooking_list",mybooking_list);

            Intent intent = new Intent();
            intent.setClass(AddPlace.this, LocationListview.class);
            
            //Bundle, assign Intent
            intent.putExtras(bundle);
            startActivity(intent);            
            dialog.cancel();
           } 
         }); 
           
         AlertDialog alert = builder.create();  
         alert.show();
           
          
          return true;
      
          case MENU_EXIT:
             break ;
      }
    
  return true ;
  }

  public String toUnicode(String str) {

    StringBuffer result = new StringBuffer();

    for (int i = 0; i < str.length(); i++) {

      char chr1 = (char) str.charAt(i);

      result.append("&#" + Integer.toString((int) chr1) + ";");

    }
    return result.toString();
  }
  
  private Button.OnClickListener add_place = new Button.OnClickListener() {
    public void onClick(View v) 
    {
      int error = 0;
      String uriAPI = "";
      
      try 
      {
          String sname = (name.getText().toString());
          String stelephone = (telephone.getText().toString());
          String swebsite = (website.getText().toString());
          String saddress = (address.getText().toString());
          int type_nid = type_id.getSelectedItemPosition() + 1;

        
          if (sname.equals("") || stelephone.equals("") ||swebsite.equals("") ||saddress.equals(""))
            {
              Toast.makeText(AddPlace.this,
              getString(R.string.new_name_null),
              Toast.LENGTH_SHORT).show();
            }
          else 
           {
            //send URL
            if (modify == 0)
              {
              uriAPI = "http://2.raywebstory.appspot.com/insert_location.jsp?type_id=" + type_nid + 
              "&name=" + sname + 
              "&telephone=" + stelephone + 
              "&website=" + swebsite  +
              "&address=" + saddress +
              "&latitude=" + geoLatitude + "&longitude=" + geoLongitude;
              }
            else
             {
              uriAPI = "http://2.raywebstory.appspot.com/update_location.jsp?loc_id=" + LocationListview.lxs.getlocid() + 
              "&type_id=" + type_nid +
              "&name=" + sname + 
              "&telephone=" + stelephone + 
              "&website=" + swebsite  +
              "&address=" + saddress +
              "&latitude=" + geoLatitude + "&longitude=" + geoLongitude;              
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
           }
      } 
      
      catch (Exception err) 
      {
            Toast.makeText(AddPlace.this, getString(R.string.new_fail),
            Toast.LENGTH_SHORT).show();
            
            error = 1;
      }
      
      if (error == 0)
      {
        if (modify == 0)
          openOptionsDialog("Insert Location OK");
        else
          openOptionsDialog("Modify Location OK");
      }
    }
  };

  private Button.OnClickListener delete_place = new Button.OnClickListener() {
    public void onClick(View v) 
    {
      int error = 0;
      String uriAPI = "";
      
      try 
      {
              uriAPI = "http://2.raywebstory.appspot.com/delete_location.jsp?loc_id=" + 
                        LocationListview.lxs.getlocid();              
              
              HttpGet httpRequest = new HttpGet(uriAPI); 
              try 
              { 
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
                if(httpResponse.getStatusLine().getStatusCode() == 200)  
                  { 
                  String strResult = EntityUtils.toString(httpResponse.getEntity());
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
      } 
      catch (Exception err) 
      {
            Toast.makeText(AddPlace.this, getString(R.string.new_fail),
            Toast.LENGTH_SHORT).show();
            
            error = 1;
      }
      
      if (error == 0)
      {
          openOptionsDialog("Delete Location OK");
      }
        
    }
  };

  //show message
  private void openOptionsDialog(String info)
{
    new AlertDialog.Builder(this)
    .setTitle("message")
    .setMessage(info)
    .setPositiveButton("OK",
        new DialogInterface.OnClickListener()
        {
         public void onClick(DialogInterface dialoginterface, int i)
         {
           if (modify == 1) finish();
         }
         }
        )
    .show();
}

  
}
  
  
