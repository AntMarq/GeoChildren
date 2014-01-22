package com.example.mytouchimageview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity 
{
	private static final int SELECT_PICTURE = 1;
	private static final String tag = "MainActivity";
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	List<String> listMapTitleSave;
	List<String> listDataHeader;
	List<String> listMapTitle;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listChildMapSave;
    private Fragment fragment;
    private String tagMap = "Map";
    private ExpandableListGeoAdapter mExpandableListGeoAdapter;
    final static String ARG_POSITION = "position";
    FragmentManager manager;
    MapFragment frag;
    private Bitmap bitmap;
    FragmentTransaction ft;
    GlobalMethods application;
    Context mContext = this;
    Map mp ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		application = (GlobalMethods) this.getApplicationContext();
		prepareListDataHeader();
		prepareListDataChildSave();

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
		
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

	//	dataDrawer = new ArrayDrawerData(this);
	//	mDrawerList.setAdapter(new CustomArrayAdapter(this.getBaseContext(), dataDrawer));
		mExpandableListGeoAdapter = new ExpandableListGeoAdapter(this.getBaseContext(), listDataHeader, listDataChild,listChildMapSave); 
		mDrawerList.setAdapter(mExpandableListGeoAdapter);
		mDrawerList.setOnGroupClickListener(new DrawerItemClickListener());
		mDrawerList.setOnChildClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		)
		{
			public void onDrawerClosed(View view)
			{
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();// creates call to
										// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null)
		{
			 mDrawerLayout.postDelayed(new Runnable() 
			    {
			        @Override
			        public void run() 
			        {
			        	mDrawerLayout.openDrawer(Gravity.LEFT);
			        }
			    }, 1000);
		}
	}
	
	@Override
	protected void onResume() 
	{
	    super.onResume(); 
	    Log.v(tag, "onResume");
	    
	}
	
		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main, menu);
			return super.onCreateOptionsMenu(menu);
		}

		/* Called whenever we call invalidateOptionsMenu() */
		@Override
		public boolean onPrepareOptionsMenu(Menu menu)
		{
			return super.onPrepareOptionsMenu(menu);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			// Only handle with DrawerToggle if the drawer indicator is enabled.
			if (mDrawerToggle.isDrawerIndicatorEnabled() && mDrawerToggle.onOptionsItemSelected(item))
			{

				return true;
			}
			return super.onOptionsItemSelected(item);
		}

		/* The click listner for ListView in the navigation drawer */
		private class DrawerItemClickListener implements ExpandableListView.OnGroupClickListener,ExpandableListView.OnChildClickListener
		{

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,int groupPosition, long id) 
			{ 
				Log.v(tag, "OnGroupClick");
				
				manager = getSupportFragmentManager();
				frag = (MapFragment) manager.findFragmentByTag(tagMap);
				
				if(mExpandableListGeoAdapter.getChildrenCount(groupPosition) > 0)
				{
					
					Log.v(tag, "onGroupClickChildisPresent" + parent.getChildCount());
				}
				else
				{
					if(frag != null)
					{
						if(groupPosition == 2)
						{
							frag.mImageView.removeAllPin();
						}

						if(groupPosition == 3)
						{

							bitmap = frag.mImageView.saveScreen();
							Log.v(tag, "bitmap " + bitmap);
							Builder builder = new Builder(mContext);
					        final EditText input = new EditText(mContext);
					        builder
					            .setTitle("Saisissez votre titre")
					            .setMessage("Confirmer la sauvegarde ? ")
					            .setView(input)
					            .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
					            {

					                public void onClick(DialogInterface dialog, int which) 
					                {
					                    String value = input.getText().toString();
					                    if (input.getText().toString().trim().length() == 0) 
					                    {
					                        Toast.makeText(application,"Titre non saisi", Toast.LENGTH_SHORT).show();
					                    } 
					                    else 
					                    {
					                    	application.getMyBaseDeDonnee ().openDataBase();
					                    	if(bitmap != null)
					                		{
					                    		Log.v(tag, "bitmap not null");
					                			mp = new Map();
					                			mp.setTitle(value);
					                			mp.setId_map(application.getCurrentMap().getId());
					                			mp.setId_type(2);
					                			mp.setPicture(bitmap);
					                			application.getMyBaseDeDonnee().insertMapSave(mp);

					                		}
					                		
					                		application.getMyBaseDeDonnee().close();
					                		application.fillMapSave();
					                		mExpandableListGeoAdapter.notifyDataSetChanged();
					                		//getData();
					                    }
					                }
					            })
					        	.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					            public void onClick(DialogInterface dialog, int which) {
					                InputMethodManager imm = (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);
					                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
					            }

					        });

					        builder.show();
						}
					}
					
						if(groupPosition == 4)
						{
							Intent intent = new Intent();
	                        intent.setType("image/*");
	                        intent.setAction(Intent.ACTION_GET_CONTENT);
	                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
							
						}
					
				}
				
				return false;
			}

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) 
			{
				Log.v(tag, "childPosition = " + childPosition + "groupPosition " + groupPosition);
				
					if(groupPosition == 0 )
					{
						manager = getSupportFragmentManager();
						FragmentTransaction ft = manager.beginTransaction();
						fragment = new MapFragment();
					
						Bundle args = new Bundle();												
						
						args.putInt("position", childPosition);

						fragment.setArguments(args);
					
						ft.replace(R.id.content_frame, fragment, tagMap).commit();
					}
					else if(groupPosition == 1 && mExpandableListGeoAdapter.getChildrenCount(groupPosition) > 0)
					{
						//todo mes cartes
					}
						
						mDrawerList.setItemChecked(childPosition, true);
						String dataString = listMapTitle.get(childPosition).toString();
						setTitle(dataString);
						Log.v(tag, "title =" + dataString );
						mDrawerLayout.closeDrawer(mDrawerList);
				
				return false;
			}
		}
		
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) 
	            {
	            	try {
	                    // We need to recyle unused bitmaps
	                    if (bitmap != null) {
	                        bitmap.recycle();
	                    }
	                    InputStream stream = getContentResolver().openInputStream(data.getData());
	                    bitmap = BitmapFactory.decodeStream(stream);
	                    stream.close();
	                    Log.v(tag, "frag = " + frag);
	                  
	                    frag.mImageView.setImageBitmap(bitmap);
	                } catch (FileNotFoundException e) {
	                    e.printStackTrace();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            super.onActivityResult(requestCode, resultCode, data);
	            }
	        }
	    }
		
		
		private void prepareListDataHeader() {

	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<String>>();
	        listChildMapSave = new HashMap<String, List<String>>();
	 
	        // Adding child data
	        listDataHeader.add("Cartes");
	        listDataHeader.add("Mes Cartes");	     
	        listDataHeader.add("Gomme");
	        listDataHeader.add("Enregistrer");
	        listDataHeader.add("Importer");	        
	        listDataHeader.add("Supprimer");

	        // Adding child data
	        listMapTitle = new ArrayList<String>();
	        
	         application = (GlobalMethods) this.getApplicationContext();
	         List<Map> listMapOrigine = application.getMapOriginList();
	        // Adding child data 
	         listMapTitle = new ArrayList<String>();
	         

	         
	         for (Map map : listMapOrigine) 
	         {
	        	 listMapTitle.add(map.getTitle());
	         }
	     
	        listDataChild.put(listDataHeader.get(0), listMapTitle); // Header, Child data
		}
		
		private void prepareListDataChildSave()
		{
			//Adding child data for saveMap
	         listMapTitleSave = new ArrayList<String>();
	         List<Map> listMapSave = application.getMapSaveList();
	         
	         for (Map map : listMapSave) 
	         {
	        	 listMapTitleSave.add(map.getTitle());
	         }
	     
	         listChildMapSave.put(listDataHeader.get(1), listMapTitleSave);
		}
}
