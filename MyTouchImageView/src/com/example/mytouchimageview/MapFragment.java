package com.example.mytouchimageview;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MapFragment extends Fragment
{

	private GlobalMethods application ;
	private String tag = "MapFragment";
	TouchImageView mImageView;
    int myChildPosition,clearAllPin ;
    private Button buttonShowPin, buttonRemovePin ; 


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.mainfragment, parent, false);
		
		application = (GlobalMethods) getActivity().getApplicationContext();
		
		mImageView = (TouchImageView)view.findViewById(R.id.touchImageView1);
		buttonRemovePin = (Button)view.findViewById(R.id.removepin);
		buttonShowPin = (Button)view.findViewById(R.id.showpin);
		
		buttonRemovePin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mImageView.removePin();	
			}
		});
		
		buttonShowPin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZoomablePinView pin = mImageView.getPin();
				if (pin != null) {
					PointF pinPos = pin.getPositionInPixels();
					Toast.makeText(application, "pin position: " + pinPos.x + ", " + pinPos.y, Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(application, "no pin selected", Toast.LENGTH_SHORT).show();
				}	
			}
		});
		
        Bundle bundle=getArguments(); 
      
       
		myChildPosition =bundle.getInt("position"); 
		
		application.setCurrentMap(application.getMapOriginList().get(myChildPosition));
		application.setCityMapList();
		
		mImageView.setImageBitmap(application.getCurrentMap().getPicture());

        return view;
	
	}
}
