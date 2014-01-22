package com.example.mytouchimageview;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListGeoAdapter extends BaseExpandableListAdapter {
	 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<String>> _listChildMapSave;
   //categ title
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    ViewHolder holder = null;
 
    public ExpandableListGeoAdapter(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData,HashMap<String, List<String>> listMapSave) 
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listChildMapSave = listMapSave;
        this._listDataChild = listChildData;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) 
    {
    	if(groupPosition == 0)
    	{
    		 return this._listDataChild.get(this._listDataHeader.get(groupPosition))
    	                .get(childPosititon);
    	}
    	else
    	{
    		 return this._listChildMapSave.get(this._listDataHeader.get(groupPosition))
 	                .get(childPosititon);
    	}
       
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
 
        txtListChild.setText(childText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition)    
    {
    	if(this._listDataHeader.get(groupPosition).contentEquals("Cartes"))
    	{
    		
    		 return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    	}
    	else if (this._listDataHeader.get(groupPosition).contentEquals("Mes Cartes"))
    	{
    		return this._listChildMapSave.get(this._listDataHeader.get(groupPosition)).size();
    	}
		return 0;
    
       
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) 
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) 
        {
        	holder = new ViewHolder();
        	
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        holder.image = (ImageView) convertView.findViewById(R.id.item_icon);
        holder.lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        holder.lblListHeader.setTypeface(null, Typeface.BOLD);
        holder.lblListHeader.setText(headerTitle);
        
        if (holder.lblListHeader.getText().equals("Cartes"))
		{
        	holder.image.setImageResource(R.drawable.world_map);
		} 
		else if (holder.lblListHeader.getText().equals("Mes Cartes"))
		{
			holder.image.setImageResource(R.drawable.folder_open);
		} 
		else if (holder.lblListHeader.getText().equals("Gomme"))
		{
			holder.image.setImageResource(R.drawable.eraser);
		}
		else if (holder.lblListHeader.getText().equals("Enregistrer"))
		{
			holder.image.setImageResource(R.drawable.save);
		}
		else if (holder.lblListHeader.getText().equals("Importer"))
		{
			holder.image.setImageResource(R.drawable.map_add);
		}
		else
		{
			holder.image.setImageResource(R.drawable.map_delete);
		}
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    private class ViewHolder
	{
		TextView lblListHeader;		
		ImageView image;		

	}
}
