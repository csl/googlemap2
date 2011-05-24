package com.mapplace;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import java.lang.Integer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup;

public class LocationListview extends Activity implements OnItemClickListener
{
  //pass use
  private Bundle bunde;
  private Intent intent;
  private int type_id;
  private String string_type_id;

  //display use
  private LocationXMLContainer LocationContainer;
  private ArrayList<HashMap<String, Object>> Location_list;
  private ListView Location_view;
  private MymAdapter mSimpleAdapter;
  static public LocationXMLStruct lxs;

    
  public void onCreate(Bundle savedInstanceState) 
  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_listview);

        //Fetch data form Inquire    
        intent=this.getIntent();
        bunde = intent.getExtras();
        type_id = bunde.getInt("type_id"); 

        string_type_id = Integer.toString(type_id);
        String uriAPI = "http://2.raywebstory.appspot.com/search_location.jsp?type_id=" +  string_type_id;

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
         }
        catch(Exception e)
         {
          e.printStackTrace();
         }       
        
        //Display, Update data    
        Location_list = LocationContainer.getLocationItems();
        
       if (LocationContainer.getSize() != 0)
        {
         Location_view=(ListView)findViewById(R.id.LocationView);
         
         mSimpleAdapter = new MymAdapter(this, Location_list, R.layout.listview_style, 
             new String[]{"ItemTitle","ItemText"}, 
             
         new int[]{R.id.topTextView,R.id.bottomTextView});  
         
         Location_view.setAdapter(mSimpleAdapter);  
         Location_view.setOnItemClickListener(this);
         
        }
       else
       {
         openOptionsDialog("無景點資料");
       }
  }

    @Override  
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
  {  
      int count = 0;
      
      //arg2
      for (LocationXMLStruct item : LocationXMLContainer.location_item) 
        {    
        if (count == position)
          {
              lxs = item;
              break;
          }
        count++;
        }
      
      Bundle bundle = new Bundle();
      bundle.putInt("modify", 1);
      
      Intent intent = new Intent();
      intent.setClass(LocationListview.this, AddPlace.class);
      intent.putExtras(bundle);
       
      startActivity(intent);
      LocationListview.this.finish();
  }  
 
 public class MymAdapter extends SimpleAdapter 
 {
        
        Map<Integer, Boolean> map;
       
        LayoutInflater mInflater;
       
        private List<? extends Map<String, ?>> mList;
       
        public MymAdapter(Context context, ArrayList<HashMap<String, Object>> data,
                        int resource, String[] from, int[] to) 
        {
                super(context, data, resource, from, to);

                map = new HashMap<Integer, Boolean>();
                mInflater = LayoutInflater.from(context);
                mList = data;
                for(int i = 0; i < data.size(); i++) 
                  {
                        map.put(i, false);
                  }
        }
       
        @Override
        public int getCount() {
                return mList.size();
        }

        @Override
        public Object getItem(int position) {
                return position;
        }

        @Override
        public long getItemId(int position) {
                return position;
        }
       
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
                if(convertView == null) {
                        convertView = mInflater.inflate(R.layout.listview_style, null);
                }
                TextView tN = (TextView) convertView.findViewById(R.id.topTextView);
                tN.setText((String)mList.get(position).get("ItemTitle"));
               
                TextView tP = (TextView) convertView.findViewById(R.id.bottomTextView);
                tP.setText((String)mList.get(position).get("ItemText"));
               
                return convertView;
        }
       
}

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
              finish();
           }
           }
          )
      .show();
}
}