package com.example.mytouchimageview;

import android.database.Cursor;

public class City {
	private String name;
	private boolean isDisplayed;
	
	// constructors
	
	public City() {
		super();
	}
	
	public City(Cursor cursor) {
		this.name = cursor.getString(cursor.getColumnIndex("name"));
		this.isDisplayed = false;
	}

	// getter and setter
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDisplayed() {
		return isDisplayed;
	}
	
	public void setDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}
}
