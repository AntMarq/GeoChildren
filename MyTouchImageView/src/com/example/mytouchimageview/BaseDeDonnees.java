package com.example.mytouchimageview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class BaseDeDonnees extends SQLiteOpenHelper
{
	
	private static final String	tag	= "BDD";
	private static String		DB_PATH	= "/data/data/com.example.mytouchimageview/databases/";
	private static String		DB_NAME	= "BDDGeoChildren";
	private String src;
	private SQLiteDatabase		myDataBase;
	private final Context		myContext;
	
	
	
	
	public BaseDeDonnees (Context context)
	{
		super (context, DB_NAME, null, 1);
		this.myContext = context;
	}

	@Override
	public void onCreate (SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onCreate (db);
	}

	public void createDataBase () throws IOException
	{
		boolean dbExist = checkDataBase ();

		if (dbExist)
		{
			// do nothing - database already exist
		}
		else
		{
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getWritableDatabase ();

			try
			{
				
				copyDataBase ();	
				Log.v (tag, "BDD copié");
			}
			catch (IOException e)
			{

				throw new Error ("Error copying database");
			}
		}
	}

	private boolean checkDataBase ()
	{

		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	private void copyDataBase () throws IOException
	{

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets ().open (DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream (outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte [] buffer = new byte [1024];
		int length;
		while ( (length = myInput.read (buffer)) > 0)
		{
			myOutput.write (buffer, 0, length);
		}

		// Close the streams
		myOutput.flush ();
		myOutput.close ();
		myInput.close ();
	}

	public SQLiteDatabase getDatabase ()
	{
		String myPath = DB_PATH + DB_NAME;
		return SQLiteDatabase.openDatabase (myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	public void openDataBase () throws SQLException
	{
		// Open the database
		
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase (myPath, null, SQLiteDatabase.OPEN_READWRITE);
		Log.v(tag, "openDataBase" + myDataBase);
	}

	@Override
	public synchronized void close ()
	{
		if (myDataBase != null)
		{
			myDataBase.close ();
			super.close ();
		}
	}

	/* MAP_ORIGINE */

	public void insertMapOrigine (Map map_origine) 
	{
		ContentValues cvMapOrigine = new ContentValues ();
		cvMapOrigine.put ("_id", map_origine.getId ());
		cvMapOrigine.put ("id_type", map_origine.getId_type());
		cvMapOrigine.put ("title", map_origine.getTitle());
		
		//convert Bitmap to blob
		if(map_origine.getPicture()!=null) 
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			map_origine.getPicture().compress(Bitmap.CompressFormat.PNG, 100, bos);
			byte[] bArray = bos.toByteArray();
			cvMapOrigine.put ("picture",bArray);
		} 
		else 
		{
			cvMapOrigine.put ("picture","");
		}
	
		if ( ! myDataBase.isOpen ())
		{
			Log.v (tag, "Ouverture BDDGeoChildren");
			openDataBase ();
		}
		
		if(checkMapOrigine(map_origine.getId ())) {
			try {
				myDataBase.insert ("MAP_ORIGINE", null, cvMapOrigine);
			} catch (SQLiteException e) {
				throw new Error ("RecentDbManager Exception in inserting data" + e.getMessage ());
			}
		}
		myDataBase.close ();
	}

	public void deleteMapOrigine (long id ) {
		if ( ! myDataBase.isOpen ()) {
			openDataBase ();
		}
			try {
				myDataBase.delete ("MAP_ORIGINE", "_id="+id, null) ;
			} catch (SQLiteException e) {
				throw new Error ("RecentDbManager Exception in deleting data" + e.getMessage ());
			}
		
		myDataBase.close ();
	}

	private boolean checkMapOrigine(int id) {
		Cursor retour = myDataBase.query ("MAP_ORIGINE", null, "_id="+id, null, null, null, null);
		if(retour.getCount ()!=0) {
			retour.close();
			return false;
		}
		retour.close();
		return true;
	}

	public Cursor getMapOrigine () {
		String[] columns = new String [] {"_id", "id_type", "title", "picture"};
		Cursor cursorMapOrigine = myDataBase.query ("MAP_ORIGINE", columns, null, null, null, null, null, null);
		return cursorMapOrigine;
	}
	
	/* MAP_SAVE */

	public void insertMapSave (Map map_save) 
	{
		Log.v (tag, "insertMapSave" + myDataBase);
		ContentValues cvMapSave = new ContentValues ();
		cvMapSave.put ("_id", map_save.getId());
		cvMapSave.put ("id_type", map_save.getId_type());
		cvMapSave.put ("id_map", map_save.getId_map());
		cvMapSave.put ("title", map_save.getTitle());
	
		//convert Bitmap to blob
		if(map_save.getPicture()!=null) 
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			map_save.getPicture().compress(Bitmap.CompressFormat.PNG, 100, bos);
			byte[] bArray = bos.toByteArray();
			cvMapSave.put ("picture",bArray);
		} 
		else 
		{
			cvMapSave.put ("picture","");
		}
	
		if ( ! myDataBase.isOpen ()) {
			
			openDataBase ();
		}
		
		if(checkMapSave(map_save.getId ())) {
			try {
				myDataBase.insert ("MAP_SAVE", null, cvMapSave);
				Toast.makeText(myContext, "Carte sauvegardée", Toast.LENGTH_SHORT).show();;
			} catch (SQLiteException e) {
				throw new Error ("RecentDbManager Exception in inserting data" + e.getMessage ());
			}
		}
		myDataBase.close ();
	}

	public void updateMapSave(Map map_save) {
		ContentValues cvMapSave = new ContentValues ();
		cvMapSave.put ("_id", map_save.getId ());
		cvMapSave.put ("id_type", map_save.getId_type());
		cvMapSave.put ("id_map", map_save.getId_map());
		cvMapSave.put ("title", map_save.getTitle());
		
		//convert Bitmap to blob
		if(map_save.getPicture()!=null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			map_save.getPicture().compress(Bitmap.CompressFormat.PNG, 100, bos);
			byte[] bArray = bos.toByteArray();
			cvMapSave.put ("picture",bArray);
		} else {
			cvMapSave.put ("picture","");
		}
		
		if( ! myDataBase.isOpen()) {
			openDataBase();
		}
		try {
			myDataBase.update("MAP_SAVE", cvMapSave, "_id="+map_save.getId(), null);
		} catch (SQLiteException e) {
			throw new Error ("RecentDbManager Exception in updating data" + e.getMessage ());
		}
		
		myDataBase.close ();
	}
	
	public void deleteMapSave(long id ) {
		if ( ! myDataBase.isOpen()) {
			openDataBase ();
		}
			try {
				myDataBase.delete ("MAP_SAVE", "_id="+id, null) ;
			} catch (SQLiteException e) {
				throw new Error ("RecentDbManager Exception in deleting data" + e.getMessage ());
			}
		
		myDataBase.close ();
	}

	private boolean checkMapSave(int id) {
		Cursor retour = myDataBase.query ("MAP_SAVE", null, "_id="+id, null, null, null, null);
		if(retour.getCount ()!=0) {
			retour.close();
			return false;
		}
		retour.close();
		return true;
	}

	public Cursor getMapSave() {
		String[] columns = new String [] {"_id", "id_type", "id_map", "title", "picture"};
		Cursor cursorMapSave = myDataBase.query ("MAP_SAVE", columns, null, null, null, null, null, null);
		return cursorMapSave;
	}
	
	/* TYPE_MAP */

	private boolean checkMapType(int id) {
		Cursor retour = myDataBase.query ("TYPE_MAP", null, "_id="+id, null, null, null, null);
		if(retour.getCount ()!=0) {
			retour.close();
			return false;
		}
		retour.close();
		return true;
	}

	public Cursor getMapType() {
		String[] columns = new String [] {"_id", "name"};
		Cursor cursorMapOrigine = myDataBase.query ("TYPE_MAP", columns, null, null, null, null, null, null);
		return cursorMapOrigine;
	}
}


