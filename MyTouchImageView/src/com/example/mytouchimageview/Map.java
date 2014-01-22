package com.example.mytouchimageview;

import java.io.ByteArrayInputStream;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Map {

	private static final String	tag	= "MAP";
	private int		id;
	private int		id_map; //si map_save
	private int		id_type;
	private String	title;
	private Bitmap  picture;
	
	// constructors
	
	public Map() {
		super();
	}
	
	public Map(Cursor cursor) {
		super();
		this.id 	 	 = cursor.getInt(cursor.getColumnIndex("_id"));
		if(cursor.getColumnIndex("id_map") != -1)
			this.id_map  = cursor.getInt(cursor.getColumnIndex("id_map"));
		this.id_type 	 = cursor.getInt(cursor.getColumnIndex("id_type"));
		this.title 	 	 = cursor.getString(cursor.getColumnIndex("title"));

		ByteArrayInputStream inputStream = new ByteArrayInputStream(cursor.getBlob(cursor.getColumnIndex("picture")));
		this.picture = BitmapFactory.decodeStream(inputStream);
	}

	//getter and setter
	
	public int getId() {
		return id;
	}

	public int getId_map() {
		return id_map;
	}

	public int getId_type() {
		return id_type;
	}

	public String getTitle() {
		return title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setId_map(int id_map) {
		this.id_map = id_map;
	}

	public void setId_type(int id_type) {
		this.id_type = id_type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}
	
	//toString
	
	@Override
	public String toString() {
		return "Map [id=" + id + ", id_map=" + id_map + ", id_type=" + id_type
				+ ", title=" + title + "]";
	}
}
