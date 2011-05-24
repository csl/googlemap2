package com.mapplace;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class AddContent extends Activity
{
  private Button mSubmit;
  private Button picupload;

  private EditText title;
  private EditText content;
  private TextView date;

  static String re_id;
  private String cdate;
  private String loc_id;

  private Bundle bunde;
  private Intent intent;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.newcontent);

    re_id = String.valueOf(new Date().getTime());

    findViews();
    setListensers();
  }

  private void setListensers()
  {
    // TODO Auto-generated method stub
    mSubmit.setOnClickListener(add_place);
    picupload.setOnClickListener(pic_upload);
  }

  private void findViews()
  {
    mSubmit = (Button) findViewById(R.id.new_submit);
    picupload = (Button) findViewById(R.id.picupload);
    title = (EditText) findViewById(R.id.ntitle);
    content = (EditText) findViewById(R.id.ncontent);

    mSubmit.setEnabled(false);
  }
  
  public String toUnicode(String str) {

    StringBuffer result = new StringBuffer();

    for (int i = 0; i < str.length(); i++) {

      char chr1 = (char) str.charAt(i);

      result.append("\\&#" + Integer.toString((int) chr1) + ";");

    }
    return result.toString();
  }


  private Button.OnClickListener add_place = new Button.OnClickListener()
  {
    public void onClick(View v)
    {
      int error = 0;
      String uriAPI = "";

      try
      {
        String stitle = (title.getText().toString());
        String scontent = (content.getText().toString());

        if (stitle.equals("") || scontent.equals(""))
        {
          Toast.makeText(AddContent.this, getString(R.string.new_name_null),
              Toast.LENGTH_SHORT).show();
        } else
        {
          Date today = Calendar.getInstance().getTime();
          SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          cdate = df.format(today);

          loc_id = ReflectionListview.loc_id;

          // send URL
          uriAPI = "http://2.raywebstory.appspot.com/insert_reflections.jsp?loc_id="
              + loc_id
              + "&re_id="
              + re_id
              + "&title="
              + stitle
              + "&content="
              + scontent + "&date=" + cdate;
          
          HttpGet httpRequest = new HttpGet(uriAPI);
          
          try
          {
            HttpResponse httpResponse = new DefaultHttpClient()
                .execute(httpRequest);

            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
              String strResult = EntityUtils.toString(httpResponse.getEntity());
              error = 0;
              // strResult = eregi_replace("(\r\n|\r|\n|\n\r)","",strResult);
              // mTextView1.setText(strResult);
            } else
            {
              // mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString());
            }
          } catch (ClientProtocolException e)
          {
            // mTextView1.setText(e.getMessage().toString());
            e.printStackTrace();
            error = 1;
          } catch (IOException e)
          {
            // mTextView1.setText(e.getMessage().toString());
            e.printStackTrace();
            error = 1;
          } catch (Exception e)
          {
            // mTextView1.setText(e.getMessage().toString());
            e.printStackTrace();
            error = 1;
          }
        }
      }

      catch (Exception err)
      {
        Toast.makeText(AddContent.this, getString(R.string.new_fail),
            Toast.LENGTH_SHORT).show();

        error = 1;
      }

      if (error == 0)
      {
        openOptionsDialog("Insert Reflections OK");
      }
    }
  };

  private Button.OnClickListener pic_upload = new Button.OnClickListener()
  {
    public void onClick(View v)
    {
      mSubmit.setEnabled(true);
      Bundle bundle = new Bundle();
      bundle.putString("re_id", re_id);
      Intent intent = new Intent();
      intent.setClass(AddContent.this, PicReviewManager.class);
      intent.putExtras(bundle);
      startActivity(intent);
    }
  };

  // show message
  private void openOptionsDialog(String info)
  {
    new AlertDialog.Builder(this).setTitle("message").setMessage(info)
        .setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialoginterface, int i)
          {
          }
        }).show();
  }

}
