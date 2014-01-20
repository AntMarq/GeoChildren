package com.example.mytouchimageview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDeDonnees extends SQLiteOpenHelper
{
	
	private static final String	tag	= "BDD";
	private static String		DB_PATH	= "/data/data/com.exemple.mytouchimageview/databases/";
	private static String		DB_NAME	= "BDDGeoChildren";
	private String src;
	private SQLiteDatabase		myDataBase;
	private final Context		myContext;
	private int typeActu = 0;
	private int typeShop = 1;
	private int typeRep = 2;
	private int typeAlbum= 3;
	private GlobalMethods application;
	
	
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
				Log.v (tag, "BDD copié");
				copyDataBase ();			
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
}
