package com.example.mytouchimageview;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class GlobalMethods extends Application
{
	private MapFragment 	mapFragment ;
	private BaseDeDonnees	myBaseDeDonnee;
	
	private ArrayList<Map> 	mapOriginList ;
	private Map				currentMap;
	private ArrayList<Map> mapSaveList ;
	private ArrayList<City> cityMapList = null;

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
		

		fillMapOriginList();
		fillMapSave();

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

	public ArrayList<Map> getMapSaveList() {
		return mapSaveList;
	}

	public void setMapSaveList(ArrayList<Map> mapSaveList) {
		this.mapSaveList = mapSaveList;
	}
	
	public ArrayList<Map> getMapOriginList() {
		return mapOriginList;
	}

	public void setMapOriginList(ArrayList<Map> mapOrigineList) {
		this.mapOriginList = mapOrigineList;
	}

	public void fillMapOriginList() {
		mapOriginList = new ArrayList<Map>();
		myBaseDeDonnee.openDataBase();
		    
		Cursor cursor = myBaseDeDonnee.getMapOrigin();
		int nbMap = cursor.getCount();
		if (nbMap != 0) {
			for (int i = 0; i < nbMap; i ++ ) {
				cursor.moveToPosition (i);
				Map map = new Map(cursor);
				mapOriginList.add(map); 
			}
		} 
		cursor.close();
		myBaseDeDonnee.close();
	}
	public void fillMapSave() {
		  mapSaveList = new ArrayList<Map>();
		  myBaseDeDonnee.openDataBase();
		    
		  Cursor cursor = myBaseDeDonnee.getMapSave();
		    int nbMap = cursor.getCount();
		    if (nbMap != 0) {
		     for (int i = 0; i < nbMap; i ++ ) {
		      cursor.moveToPosition (i);
		      Map map = new Map(cursor);
		      mapSaveList.add(map); 
		     }
		    } 
		    cursor.close ();
		    myBaseDeDonnee.close();
		 }

	public Map getCurrentMap() {
		return currentMap;
	}

	public void setCurrentMap(Map currentMap) {
		this.currentMap = currentMap;
	}

	public ArrayList<City> getCityMapList() {
		return cityMapList;
	}

	public void setCityMapList() {
		this.cityMapList = myBaseDeDonnee.getCity(currentMap.getId());
	}
	
	public void setCityVisibilityToFalse(String cityName) {
		Log.v("city", "pin : " + cityName);

		for (City city : this.cityMapList) {
			if(city.getName().equals(cityName))
				city.setDisplayed(false);
		}
	}
	
	public boolean allCitiesDisplayed() {
		int nbCity = this.cityMapList.size();
		int nbCityDisplayed = 0;
		
		for (City city : this.cityMapList) {
			if(city.isDisplayed()) {
				nbCityDisplayed++;
			}
		}
		
		if(nbCityDisplayed == nbCity)
			return true;
		
		return false;
	}
}
