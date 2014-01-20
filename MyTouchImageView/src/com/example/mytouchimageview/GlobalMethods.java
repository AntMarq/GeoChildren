package com.example.mytouchimageview;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class GlobalMethods extends Application
{
	private MapFragment mapFragment ;
	
	
	
	private BaseDeDonnees	myBaseDeDonnee;
	
	
	@Override
	public void onCreate ()
	{
		
		myBaseDeDonnee = new BaseDeDonnees (getBaseContext ());

		try
		{
			myBaseDeDonnee.createDataBase ();
		}
		catch (IOException ioe)
		{
			throw new Error ("Unable to create database");
		}	
	}

	public BaseDeDonnees getMyBaseDeDonnee ()
	{
		return myBaseDeDonnee;
	}

	public void setMyBaseDeDonnee (BaseDeDonnees myBaseDeDonnee)
	{
		this.myBaseDeDonnee = myBaseDeDonnee;
	}
	
	
	
	
	
	
	
	
	
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
