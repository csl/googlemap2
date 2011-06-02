package com.mapplace;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ShowImage extends Activity 
{ 
  private ImageView iview;
  private Button new_exit;

  private PictureXMLContainer PictureContainer;
  private String imageFileURL;
  private String image_key;
  
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
      // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);
      setContentView(R.layout.showimage);

      findViews();
      setListensers();
      
      Bundle bData = this.getIntent().getExtras();
      
      if (bData != null)
      {
        imageFileURL = bData.getString( "image_url" );
        
        //Loading Image
        try {
     
          if (imageFileURL != null)
           {
              URL url = new URL(imageFileURL);
              URLConnection conn = url.openConnection();
               
              HttpURLConnection httpConn = (HttpURLConnection)conn;
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
  }
  
  private void findViews() 
  {
    new_exit = (Button) findViewById(R.id.new_exit);
    iview = (ImageView) findViewById(R.id.imageview);
  }
  
  private void setListensers()
  {
    // TODO Auto-generated method stub
    new_exit.setOnClickListener(bexit);
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
  
  
