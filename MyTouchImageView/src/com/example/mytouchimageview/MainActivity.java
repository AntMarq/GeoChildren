package com.example.mytouchimageview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
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
import android.widget.ExpandableListView;

public class MainActivity extends FragmentActivity 
{
	private static final int SELECT_PICTURE = 1;
	private static final String tag = "MainActivity";
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	List<String> listDataCateg;
	List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Fragment fragment;
    private String tagMap = "Map";
    private ExpandableListGeoAdapter mExpandableListGeoAdapter;
    final static String ARG_POSITION = "position";
    List<String> listMapTitle;
    FragmentManager manager;
    MapFragment frag;
    private Bitmap bitmap;
    FragmentTransaction ft;
    GlobalMethods application;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prepareListData();

		
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
		
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

	//	dataDrawer = new ArrayDrawerData(this);
	//	mDrawerList.setAdapter(new CustomArrayAdapter(this.getBaseContext(), dataDrawer));
		mExpandableListGeoAdapter = new ExpandableListGeoAdapter(this.getBaseContext(), listDataHeader, listDataChild); 
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
						if(groupPosition == 3)
						{
							Log.v(tag, "groupPosition = 3");
							
							frag.mImageView.removeAllPin();
						
							Log.v(tag, "onGroupClickNoChild" + frag.toString());
						}

						if(groupPosition == 4)
						{
							frag.mImageView.saveScreen();						
						}
					}
					
						if(groupPosition == 5)
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
				
						
						manager = getSupportFragmentManager();
						FragmentTransaction ft = manager.beginTransaction();
						fragment = new MapFragment();
					
						Bundle args = new Bundle();												
						
						args.putInt("position", childPosition);

						fragment.setArguments(args);
					
						ft.replace(R.id.content_frame, fragment, tagMap).commit();
						
						Log.v(tag, "onGroupClickNoChild" + fragment.toString());
						
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
	                    InputStream stream = getContentResolver().openInputStream(
	                            data.getData());
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
		
		private void selectItem(int position)
		{			
			
			Log.v(tag, "selectItem" + position);
			
		//	mDrawerList.setItemChecked(position, true);
			//String dataString = listDataHeader.get(position).toString();
		//	setTitle(dataString);
		//	mDrawerLayout.closeDrawer(mDrawerList);
		}
		
		private void prepareListData() {

	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<String>>();
	        
	 
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
	         List<Map> listMapOrigine = application.getMapOrigineList();
	         
	         for (Map map : listMapOrigine) 
	         {
	        	 listMapTitle.add(map.getTitle());
	         }
	     
	        listDataChild.put(listDataHeader.get(0), listMapTitle); // Header, Child data

	    }
		
}
