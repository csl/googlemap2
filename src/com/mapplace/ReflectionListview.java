package com.mapplace;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

public class ReflectionListview extends Activity implements OnItemClickListener
{
  //pass use
  static public String loc_id;
  
  //display use
  private ReflectionXMLContainer ReflectionContainer;
  private ArrayList<HashMap<String, Object>> Reflection_list;
  private ListView Reflection_view;
  private MyAdapter mSimpleAdapter;
  static public ReflectionXMLStruct lxs;
  
  private String startdate;
  private String enddate;
    
  public void onCreate(Bundle savedInstanceState) 
  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reflection_listview);

        //type_id = MyGoogleMap.mSelectedMapLocation.getLocationStruct().gettypeid();
        loc_id = MyGoogleMap.mSelectedMapLocation.getLocationStruct().getlocid();
        
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        enddate = df.format(today);
        
        Date today_dec_five = Calendar.getInstance().getTime();
        today_dec_five.setDate(today_dec_five.getDate() - 5);

        startdate = df.format(today_dec_five);
        
        String uriAPI = "http://2.raywebstory.appspot.com/search_reflections.jsp?loc_id=" + loc_id + 
        "&start_date=" + startdate +
        "&end_date=" + enddate;

        //XML Parser
        URL url = null;
        try{
            url = new URL(uriAPI);
            
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
                  
            XMLReader xml_reader = sp.getXMLReader();
                  
            //Using handler for XML
            ReflectionXMLHandler ReflectionXML = new ReflectionXMLHandler();
            xml_reader.setContentHandler(ReflectionXML);
            
            //open connection
            xml_reader.parse(new InputSource(url.openStream()));
            ReflectionContainer = ReflectionXML.getContainer();
         }
        catch(Exception e)
         {
          e.printStackTrace();
         }       

        Reflection_list = ReflectionContainer.getReflectionItems();
        
        //Display, Update data
          Reflection_view= (ListView) findViewById(R.id.RView);
         
          mSimpleAdapter = new MyAdapter(this, Reflection_list, R.layout.listview_style, 
             new String[]{"ItemTitle","ItemText"},  new int[]{R.id.topTextView, R.id.bottomTextView});  
         
         Reflection_view.setAdapter(mSimpleAdapter);  
         Reflection_view.setOnItemClickListener(this);
        
  }

    @Override  
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
  {  
      int count = 1;
      
      if (position == 0)
      {
        Intent intent = new Intent();
        intent.setClass(ReflectionListview.this, AddContent.class);
        //intent.putExtras(bundle);

        startActivity(intent);
        ReflectionListview.this.finish();

        return;
      }
      
      //arg2
      for (ReflectionXMLStruct item : ReflectionXMLContainer.reflection_item) 
      {    
        if (count == position)
          {
              lxs = item;
              break;
          }
        count++;
      }
      
      Intent intent = new Intent();
      intent.setClass(ReflectionListview.this, ShowReflection.class);
       
      startActivity(intent);
      ReflectionListview.this.finish();
  }  
 
 public class MyAdapter extends SimpleAdapter 
 {
        
        Map<Integer, Boolean> map;
       
        LayoutInflater mInflater;
       
        private List<? extends Map<String, ?>> mList;
       
        public MyAdapter(Context context, ArrayList<HashMap<String, Object>> data,
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
              //finish();
           }
           }
          )
      .show();
}
}