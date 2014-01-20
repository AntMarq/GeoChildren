package com.example.mytouchimageview;


import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MapFragment extends Fragment
{

	private GlobalMethods application ;
	TouchImageView mImageView;
    int myChildPosition ;
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

		if(myChildPosition == 0)
	    {
			mImageView.setImageResource(R.drawable.allemagne_map);
		       
	    }
	    else if(myChildPosition == 1)
	    {
	    	mImageView.setImageResource(R.drawable.angleterre_map);
			
	    }
	    else if(myChildPosition ==  2)
	    {
	    	mImageView.setImageResource(R.drawable.espagne_map);
			
	    }
	    else if (myChildPosition ==  3)
	    {
	    	
	    	mImageView.setImageResource(R.drawable.france_map);
	    }
	    else if(myChildPosition ==  4)
	    {   	
	    	mImageView.setImageResource(R.drawable.italie_map);
			
	    }
	    else if(myChildPosition ==  5)
	    {
	    	mImageView.setImageResource(R.drawable.portugal_map);
			
	    }
	    else if(myChildPosition ==  6)
	    {
	    	mImageView.setImageResource(R.drawable.russie_map);
			
	    }
	    else if(myChildPosition ==  7)
	    {
	    	mImageView.setImageResource(R.drawable.suisse_map);
	    }
		
        return view;
	
	}
}
