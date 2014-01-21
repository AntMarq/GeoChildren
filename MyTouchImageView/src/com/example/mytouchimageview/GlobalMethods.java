package com.example.mytouchimageview;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.view.Display;
import android.view.WindowManager;

public class GlobalMethods extends Application
{
	private MapFragment mapFragment ;
	private BaseDeDonnees	myBaseDeDonnee;
	private ArrayList<Map> mapOrigineList ;
	
	
	

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
		
		fillMapOrigineList();
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
	
	public ArrayList<Map> getMapOrigineList() {
		return mapOrigineList;
	}

	public void setMapOrigineList(ArrayList<Map> mapOrigineList) {
		this.mapOrigineList = mapOrigineList;
	}
	
	public void fillMapOrigineList() {
		  mapOrigineList = new ArrayList<Map>();
		  myBaseDeDonnee.openDataBase();
		    
		  Cursor cursor = myBaseDeDonnee.getMapOrigine();
		    int nbMap = cursor.getCount();
		    if (nbMap != 0) {
		     for (int i = 0; i < nbMap; i ++ ) {
		      cursor.moveToPosition (i);
		      Map map = new Map(cursor);
		      mapOrigineList.add(map); 
		     }
		    } 
		    cursor.close ();
		    myBaseDeDonnee.close();
		 }
	
	
	public MapFragment getMapFragment() {
		return mapFragment;
	}

	public void setMapFragment(MapFragment mapFragment) {
		this.mapFragment = mapFragment;
	}
}
