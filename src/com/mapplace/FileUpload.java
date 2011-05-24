package com.mapplace;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FileUpload
{
  static final String BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";
  private String urlStr;
  private String path;
  private List<String[]> strParams;

  public FileUpload(String urlStr, List<String[]> strParams, String fileParams)
  {
    this.urlStr = urlStr;
    this.strParams = strParams;
    path = fileParams;
  }

  public byte[] sendPost() throws Exception
  {
    HttpURLConnection hc = null;
    ByteArrayOutputStream bos = null;
    InputStream is = null;
    byte[] res = null;

    try
    {
      URL url = new URL(urlStr);
      hc = (HttpURLConnection) url.openConnection();

      hc.setRequestProperty("Content-Type", "multipart/form-data; boundary="
          + BOUNDARY);
      hc.setRequestProperty("Charsert", "UTF-8");

      hc.setDoOutput(true);
      hc.setDoInput(true);
      hc.setUseCaches(false);
      hc.setRequestMethod("POST");

      OutputStream dout = hc.getOutputStream();

      String boundary = BOUNDARY;

      StringBuffer resSB = new StringBuffer("\r\n");

      String endBoundary = "\r\n--" + boundary + "--\r\n";

      if (strParams != null)
      {
        for (String[] parsm : strParams)
        {
          String key = parsm[0];
          String value = parsm[1];
          resSB.append("Content-Disposition: form-data; name=\"").append(key)
              .append("\"\r\n").append("\r\n").append(value).append("\r\n")
              .append("--").append(boundary).append("\r\n");
        }
      }

      String boundaryMessage = resSB.toString();

      dout.write(("--" + boundary + boundaryMessage).getBytes("utf-8"));

      resSB = new StringBuffer();

      File file = new File(path);

      resSB.append("Content-Disposition: form-data; name=\"")
          .append("theUploadFile").append("\"; filename=\"")
          .append(file.getName()).append("\"\r\n").append("Content-Type: ")
          .append("image/jpg").append("\r\n\r\n");

      dout.write(resSB.toString().getBytes("utf-8"));

      DataInputStream in = new DataInputStream(new FileInputStream(file));
      int bytes = 0;
      byte[] bufferOut = new byte[1024 * 5];
      while ((bytes = in.read(bufferOut)) != -1)
      {
        dout.write(bufferOut, 0, bytes);
      }

      dout.write(endBoundary.getBytes("utf-8"));

      in.close();
      dout.write(endBoundary.getBytes("utf-8"));

      dout.close();

      int ch;

      is = hc.getInputStream();

      bos = new ByteArrayOutputStream();
      while ((ch = is.read()) != -1)
      {
        bos.write(ch);
      }
      res = bos.toByteArray();
    } catch (Exception e)
    {
      e.printStackTrace();
    } finally
    {
      try
      {
        if (bos != null)
        {
          bos.close();
        }

        if (is != null)
        {
          is.close();
        }

      } catch (Exception e2)
      {
        e2.printStackTrace();
      }
    }
    return res;
  }
}
