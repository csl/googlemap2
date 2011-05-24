package com.mapplace; 

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

public class PicReviewManager extends Activity{
	 /** Called when the activity is first created. */
	GridView gridview;
	PictureInfo PInfo;
	private String re_id = "";
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview);
        
        Bundle bundle = this.getIntent().getExtras();
        re_id = bundle.getString("re_id");
        
        
        gridview  = (GridView) this.findViewById(R.id.GridView01);
        PInfo = new PictureInfo(this);
        
        ImageAdapter ad = new ImageAdapter(this,ImageAdapter.UseGridView, this);
        ad.setReId(re_id);
        if (ad.LoadImageFile(PInfo.GetImagePath() + "/pic") >0)        
        	gridview.setAdapter(ad);           
    }	
	
    private static final int REQUEST_CODE =0;
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		if (requestCode == REQUEST_CODE)
		{
			if (resultCode == RESULT_CANCELED)
			{
				Toast.makeText(this,"Not find any image file!", Toast.LENGTH_SHORT);
			}else if (resultCode == RESULT_OK)
			{
				String ImagePath = null;
				Bundle extras = data.getExtras();
				if (extras !=null)
				{					
					if ((ImagePath = extras.getString("ImagePath")) != null)
					{						
						ImageAdapter ad = new ImageAdapter(this, PictureInfo.UseGridView, this);
						PInfo.SetImagePath(ImagePath);
						if (ad.LoadImageFile(ImagePath)>0)
					    	gridview.setAdapter(ad);
					}
				}
			}
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
         }
         }
        )
    .show();
}
	
}
