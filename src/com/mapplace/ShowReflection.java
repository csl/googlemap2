package com.mapplace;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ShowReflection extends Activity 
{ 
  private TextView ititle;
  private ImageView iview;
  private TextView icontent;
  private Button new_exit;

  private String nre_id;
  private String ntitle;
  private String ncontent;
 
  private PictureXMLContainer PictureContainer;
  private String imageFileURL;
  private String image_key;
  
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
      // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);
      setContentView(R.layout.showcontent);

      findViews();
      setListensers();
      
      nre_id = ReflectionListview.lxs.getlocid();
      ntitle = ReflectionListview.lxs.gettitle();
      ncontent = ReflectionListview.lxs.getcontent();
      
      //request image
      String uriAPI = "http://2.raywebstory.appspot.com/search_images.jsp?re_id=" + nre_id;
      //openOptionsDialog(uriAPI);
      //XML Parser
      URL url = null;
      try{
          url = new URL(uriAPI);
          
          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();
                
          XMLReader xml_reader = sp.getXMLReader();
                
          //Using handler for XML
          PictureXMLHandler PictureXML = new PictureXMLHandler();
          xml_reader.setContentHandler(PictureXML);
          
          //open connection
          xml_reader.parse(new InputSource(url.openStream()));
          PictureContainer = PictureXML.getContainer();
       }
      catch(Exception e)
       {
        e.printStackTrace();
       }       
      
      image_key = PictureContainer.getImagePath(0);
      imageFileURL = "http://2.raywebstory.appspot.com/ShowImage?image_key=" + image_key;
      //openOptionsDialog(imageFileURL);
      //imageFileURL = null;
      getReflectionInfo();
  }
  
  private void findViews() 
  {
    new_exit = (Button) findViewById(R.id.new_exit);
    iview = (ImageView) findViewById(R.id.imageview);
    ititle = (TextView) findViewById(R.id.ntitle);
    icontent = (TextView) findViewById(R.id.ncontent);
  }
  
  private void setListensers()
  {
    // TODO Auto-generated method stub
    new_exit.setOnClickListener(bexit);
  }
  
  private void getReflectionInfo()
  {
    
    try {
      ititle.setText(ntitle);
      icontent.setText(ncontent);
 
      if (imageFileURL != null)
       {
          URL url = new URL(imageFileURL);
          URLConnection conn = url.openConnection();
           
          HttpURLConnection  httpConn = (HttpURLConnection)conn;
          httpConn.setRequestMethod("GET");
          httpConn.connect();
    
          if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) 
          {
           InputStream inputStream = httpConn.getInputStream();
            
           Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
           inputStream.close();
           iview.setImageBitmap(bitmap);
           if (bitmap == null)
             openOptionsDialog("pic is null");
           
          }
       }
     } catch (MalformedURLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
     } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
     }
    
  
  }
  
  private Button.OnClickListener bexit = new Button.OnClickListener() {
    public void onClick(View v) 
    {
        finish();
    }
  };  
  
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
           finish();
         }
         }
        )
    .show();
  }

  
}
  
  
