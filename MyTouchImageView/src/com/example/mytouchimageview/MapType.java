package com.example.mytouchimageview;

import android.database.Cursor;

public class MapType {
	
	private static final String	tag	= "MAP_TYPE";
	private int		id;
	private String	name;
	
	//constructors 
	
	public MapType() {
		super();
	}

	public MapType(Cursor cursor) {
		super();
		this.id = cursor.getInt(cursor.getColumnIndex("_id"));
		this.name = cursor.getString(cursor.getColumnIndex("name"));
	}
	
	//getter and setter
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	// toString
	
	@Override
	public String toString() {
		return "MapType [id=" + id + ", name=" + name + "]";
	}
	
}
