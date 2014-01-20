package com.example.mytouchimageview;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class GlobalMethods extends Application
{
	private MapFragment mapFragment ;
	
	
	public int[] dimScreen()
	{
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		int height = d.getHeight();
		int width = d.getWidth();
		
		int[] size = {height,width};
		
		return size;
	}
	public MapFragment getMapFragment() {
		return mapFragment;
	}

	public void setMapFragment(MapFragment mapFragment) {
		this.mapFragment = mapFragment;
	}
}
