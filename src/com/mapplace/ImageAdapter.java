package com.mapplace;

import java.io.BufferedReader;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter
{
  private String[] imagefile;
  private Context mContext;
  private String Path;
  private int UseStyle;
  private PicReviewManager prm;
  private String TAG = "toDo";
  public final static int UseGridView = 0x01;
  public final static int UseGallery = 0x02;

  public ImageAdapter(Context context, int SetUseStyle, PicReviewManager p)
  {
    UseStyle = SetUseStyle;
    mContext = context;
    prm = p;
  }

  public int LoadImageFile(String iPath)
  {
    File f = new File(iPath);
    int len = 0;
    if (f != null && f.isDirectory())
    {
      Path = iPath;
      imagefile = f.list(new ImageFilter());
      len = imagefile.length;
    }

    if (len == 0)
    {
      Toast showmesg = Toast.makeText(mContext,
          "Cann't find any image in the path!", Toast.LENGTH_SHORT);
      showmesg.setGravity(Gravity.CENTER, 0, 0);
      showmesg.show();
    }
    return len;
  }

  public int getCount()
  {
    // TODO Auto-generated method stub
    if (imagefile == null)
      return 0;
    return imagefile.length;
  }

  public Object getItem(int arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public long getItemId(int position)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  public View getView(int position, View convertView, ViewGroup parent)
  {
    // TODO Auto-generated method stub
    ImageView iv = null;

    if (convertView == null)
      iv = new ImageView(mContext);
    else
      iv = (ImageView) convertView;

    int DefaultSize;
    String fullpath = Path + "/" + imagefile[position];
    Bitmap bitmap = BitmapFactory.decodeFile(fullpath, null);

    if (bitmap == null)
      throw new IllegalStateException("Incorrect error image formate.");

    if (UseStyle == UseGridView)
    {
      iv.setLayoutParams(new GridView.LayoutParams(85, 85));
      iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
      iv.setPadding(8, 8, 8, 8);
      DefaultSize = 50;
    } else
    {
      iv.setPadding(20, 20, 20, 20);
      iv.setScaleType(ImageView.ScaleType.CENTER);
      DefaultSize = 200;
    }
    bitmap = Bitmap.createScaledBitmap(bitmap, DefaultSize, DefaultSize, true);
    iv.setImageBitmap(bitmap);
    iv.setOnTouchListener(new OnDoubleClickListener(fullpath));
    return iv;
  }

  public byte[] getBytesFromFile(File file) throws IOException
  {
    InputStream is = new FileInputStream(file);
    long length = file.length();

    if (length > Integer.MAX_VALUE)
    {
      // File is too large
    }

    byte[] bytes = new byte[(int) length];
    int offset = 0;
    int numRead = 0;

    while (offset < bytes.length
        && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
    {
      offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < bytes.length)
    {
      throw new IOException("Could not completely read file " + file.getName());
    }

    // Close the input stream and return bytes
    is.close();
    return bytes;
  }

  public void upload(String URLs, String nPATH)
  {

    List<String> list = new ArrayList<String>(); // 要上傳的文件名,如：d:\haha.doc.你要實現自己的業務。我這裡就是一個空list.

    try
    {

      String BOUNDARY = "---------7d4a6d158c9";
      URL url = new URL(URLs);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      // conn.setInstanceFollowRedirects(true);

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent",
          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
      conn.setRequestProperty("Charsert", "UTF-8");
      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="
          + BOUNDARY);

      OutputStream out = new DataOutputStream(conn.getOutputStream());
      byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();

      File file = new File(nPATH);
      StringBuilder sb = new StringBuilder();
      sb.append("--");
      sb.append(BOUNDARY);
      sb.append("\r\n");
      sb.append("Content-Disposition: form-data;name=\"theUploadFile"
          + "\";filename=\"" + file.getName() + "\"\r\n");
      sb.append("Content-Type:application/octet-stream\r\n\r\n");
      byte[] data = sb.toString().getBytes();
      prm.openOptionsDialog(sb.toString());
      out.write(data);

      DataInputStream in = new DataInputStream(new FileInputStream(file));
      int bytes = 0;
      byte[] bufferOut = new byte[1024];

      while ((bytes = in.read(bufferOut)) != -1)
      {
        out.write(bufferOut, 0, bytes);
      }

      in.close();
      out.write(end_data);
      out.flush();
      out.close();

      int responseCode = conn.getResponseCode();
      if (responseCode == 200)
      {
        // 定義BufferedReader輸入流來讀取URL的響應
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            conn.getInputStream()));
        String line = null;

        while ((line = reader.readLine()) != null)
        {
          prm.openOptionsDialog(line);
        }
      } else
      {
        prm.openOptionsDialog(Integer.toString(responseCode));
        prm.openOptionsDialog(conn.getHeaderField("location"));
      }

    } catch (Exception e)
    {

      prm.openOptionsDialog("發送POST請求出現異常！" + e);

      e.printStackTrace();

    }
  }

  private void uploadFile(String actionUrl, String nPATH, String nNAME)
  {
    String end = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    // String boundary = "==================================";
    try
    {
      URL url = new URL(actionUrl);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setDoInput(true);
      con.setDoOutput(true);
      con.setUseCaches(false);
      con.setRequestMethod("POST");
      /* setRequestProperty */
      con.setRequestProperty("Connection", "Keep-Alive");
      con.setRequestProperty("Charset", "UTF-8");
      con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
          + boundary);
      DataOutputStream ds = new DataOutputStream(con.getOutputStream());
      ds.writeBytes(twoHyphens + boundary + end);
      ds.writeBytes("Content-Disposition: form-data; "
          + "name=\"theUploadFile\";filename=\"" + nNAME + "\"" + end);
      ds.writeBytes(end);

      FileInputStream fStream = new FileInputStream(nPATH);
      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];

      int length = -1;
      while ((length = fStream.read(buffer)) != -1)
      {
        ds.write(buffer, 0, length);
      }
      ds.writeBytes(end);
      ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

      /* close streams */
      fStream.close();
      ds.flush();

      int responseCode = con.getResponseCode();
      if (responseCode == 200)
      {
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1)
        {
          b.append((char) ch);
        }
        prm.openOptionsDialog(b.toString().trim());
      } else
        prm.openOptionsDialog(Integer.toString(responseCode));

      ds.close();
    } catch (Exception e)
    {
      prm.openOptionsDialog("" + e);
    }
  }
  
  private String re_id = "";
  
  public void setReId(String re_id){
    this.re_id = re_id;
  }
  
  public String getReId(){
    return re_id;
  }

  public void put(String targetURL, File file, String newName) throws Exception
  {

    String BOUNDRY = "==================================";
    HttpURLConnection conn = null;

    try
    {

      // These strings are sent in the request body. They provide information
      // about the file being uploaded
      String contentDisposition = "Content-Disposition: form-data; name=\"theUploadFile\"; filename=\""
          + newName + "\"";
      // String contentType = "Content-Type: application/octet-stream";

      // This is the standard format for a multipart request
      StringBuffer requestBody = new StringBuffer();
      requestBody.append("--");
      requestBody.append(BOUNDRY);
      requestBody.append('\n');
      requestBody.append(contentDisposition);
      requestBody.append('\n');
      // requestBody.append(contentType);
      // requestBody.append('\n');
      requestBody.append('\n');
      requestBody.append(new String(getBytesFromFile(file)));
      requestBody.append("--");
      requestBody.append(BOUNDRY);
      requestBody.append("--");

      // Make a connect to the server
      URL url = new URL(targetURL);
      conn = (HttpURLConnection) url.openConnection();

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="
          + BOUNDRY);

      // Send the body
      DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());
      dataOS.writeBytes(requestBody.toString());
      dataOS.flush();
      dataOS.close();

      // Ensure we got the HTTP 200 response code
      int responseCode = conn.getResponseCode();
      if (responseCode != 200)
      {
        throw new Exception(String.format(
            "Received the response code %d from the URL %s", responseCode, url));
      }

      // Read the response
      InputStream is = conn.getInputStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] bytes = new byte[1024];
      int bytesRead;
      while ((bytesRead = is.read(bytes)) != -1)
      {
        baos.write(bytes, 0, bytesRead);
      }
      byte[] bytesReceived = baos.toByteArray();
      baos.close();

      is.close();
      String response = new String(bytesReceived);
      prm.openOptionsDialog(response);

      // TODO: Do something here to handle the 'response' string
      prm.openOptionsDialog("upload OK");

    } finally
    {
      if (conn != null)
      {
        conn.disconnect();
      }
    }

  }

  public class OnDoubleClickListener implements OnTouchListener
  {
    long firstclick = 0;
    String ImagePath;

    public OnDoubleClickListener(String Path)
    {
      ImagePath = Path;
    }

    public boolean onTouch(View arg0, MotionEvent arg1)
    {
      // TODO Auto-generated method stub
      List<String[]> stringParams = new ArrayList<String[]>();
      stringParams.add(new String[]{"re_id", getReId()});
      if (System.currentTimeMillis() - firstclick < 1000)
      {
        Log.i(TAG, "call upload file");
        StringTokenizer Tok = new StringTokenizer(ImagePath, "/");

        String filename = "";
        while (Tok.hasMoreElements())
        {
          // System.out.println("" + ++n +": "+Tok.nextElement());
          filename = (String) Tok.nextElement();
        }

        String strResult = "";
        // TODO Auto-generated method stub
        String uriAPI = "http://2.raywebstory.appspot.com/upload_link.jsp";
        HttpGet httpRequest = new HttpGet(uriAPI);
        try
        {
          HttpResponse httpResponse = new DefaultHttpClient()
              .execute(httpRequest);
          if (httpResponse.getStatusLine().getStatusCode() == 200)
          {
            strResult = EntityUtils.toString(httpResponse.getEntity());
          }
        } catch (ClientProtocolException e)
        {
          e.printStackTrace();
        } catch (IOException e)
        {
          e.printStackTrace();
        } catch (Exception e)
        {
          e.printStackTrace();
        }

        try
        {
          FileUpload up = new FileUpload(strResult.trim(),stringParams, ImagePath);
          try
          {
            byte[] response = up.sendPost();
            prm.openOptionsDialog("upload OK" + ": "+ new String(response));
            
            AddContent.re_id = new String(response).trim();
            //System.out.println(new String("response "+response));
          } catch (Exception e)
          {
            prm.openOptionsDialog("upload false");
            e.printStackTrace();
          }
        } catch (Exception e)
        {
          // TODO Auto-generated catch block
          prm.openOptionsDialog("upload false");
          e.printStackTrace();
        }

        firstclick = 0;
        return true;
      } else
      {
        firstclick = System.currentTimeMillis();
      }
      return false;
    }
  }

  public class ImageFilter implements FilenameFilter
  {
    private boolean IsJPG(String file)
    {
      if (file.toLowerCase().endsWith(".jpg"))
        return true;
      return false;
    }

    private boolean IsBMP(String file)
    {
      if (file.toLowerCase().endsWith(".bmp"))
        return true;
      return false;
    }

    private boolean IsPNG(String file)
    {
      if (file.toLowerCase().endsWith(".png"))
        return true;

      return false;
    }

    public boolean accept(File dir, String filename)
    {
      // TODO Auto-generated method stub
      return (IsPNG(filename) | IsBMP(filename) | IsJPG(filename));
    }
  }

}
