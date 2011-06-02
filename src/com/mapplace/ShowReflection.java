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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ShowReflection extends Activity 
{ 
  private TextView ititle;
  private Gallery gallery;
  private ImageView iview;
  private TextView icontent;
  private Button new_exit;

  private String nre_id;
  private String ntitle;
  private String ncontent;
 
  private PictureXMLContainer PictureContainer;
  private String imageFileURL;
  private String image_key;
  private cImageAdapter adapter;
  
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
      
      adapter = new cImageAdapter(this);
      adapter.RemoteImages = new String[PictureContainer.getSize()];
      
      for (int i=0; i<PictureContainer.getSize(); i++) 
      {
        image_key = PictureContainer.getImagePath(i);
        adapter.RemoteImages[i] = "http://2.raywebstory.appspot.com/ShowImage?image_key=" + image_key;
      }
      
      gallery.setAdapter(adapter);

      gallery.setOnItemClickListener(new OnItemClickListener() {

        public void onItemClick(AdapterView parent, View v, int position, long id) {

            //openOptionsDialog(Integer.toString(position));
          
          Bundle bData = new Bundle();          
          Intent newAct = new Intent();
          newAct.setClass( ShowReflection.this, ShowImage.class );
          bData.putString("image_url", adapter.RemoteImages[position]);
          newAct.putExtras( bData );

          startActivity( newAct );          
        }      
      });
      
      getReflectionInfo();
  }
  
  private void findViews() 
  {
    new_exit = (Button) findViewById(R.id.new_exit);
    gallery = (Gallery) findViewById(R.id.gallery);
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
      ititle.setText(ntitle);
      icontent.setText(ncontent);
  }
  
  private Button.OnClickListener bexit = new Button.OnClickListener() {
    public void onClick(View v) 
    {
        finish();
    }
  };
     
   public class cImageAdapter extends BaseAdapter {
          /** The parent context */
          private Context myContext;
   
          /** URL-Strings to some remote images. */
          public String[] RemoteImages;
         
          /** Simple Constructor saving the 'parent' context. */
          public cImageAdapter(Context c) { this.myContext = c; }
   
          /** Returns the amount of images we have defined. */
          public int getCount() { return this.RemoteImages.length; }
   
          /* Use the array-Positions as unique IDs */
          public Object getItem(int position) { return position; }
          public long getItemId(int position) { return position; }

          public View getView(int position, View convertView, ViewGroup parent) {
              ImageView i = new ImageView(this.myContext);
   
              try {
                                  /* Open a new URL and get the InputStream to load data from it. */
                                  URL aURL = new URL(RemoteImages[position]);
                                  URLConnection conn = aURL.openConnection();
                                  conn.connect();
                                  InputStream is = conn.getInputStream();
                                  /* Buffered is always good for a performance plus. */
                                  BufferedInputStream bis = new BufferedInputStream(is);
                                  /* Decode url-data to a bitmap. */
                                  Bitmap bm = BitmapFactory.decodeStream(bis);
                                  
                                  if (bm == null)
                                  {
                                    openOptionsDialog("pic null");
                                  }
                                  bis.close();
                                  is.close();
                                  /* Apply the Bitmap to the ImageView that will be returned. */
                                  i.setImageBitmap(bm);
                          } catch (IOException e) {
                                  //i.setImageResource(R.drawable.error);
                                  Log.e("DEBUGTAG", "Remtoe Image Exception", e);
                          }
             
              /* Image should be scaled as width/height are set. */
              i.setScaleType(ImageView.ScaleType.FIT_CENTER);
                          
              /* Set the Width/Height of the ImageView. */
              i.setLayoutParams(new Gallery.LayoutParams(150, 150));
              
              return i;
          }
   
          /** Returns the size (0.0f to 1.0f) of the views
           * depending on the 'offset' to the center. */
          public float getScale(boolean focused, int offset) {
                  /* Formula: 1 / (2 ^ offset) */
              return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
          }
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
               finish();
             }
             }
            )
        .show();
      }

  
}
  
  
