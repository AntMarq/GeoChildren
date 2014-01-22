package com.example.mytouchimageview;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ZoomablePinView extends ImageView{
	private GlobalMethods 	    application;
	private float 		  	    posX=0, posY=0;
	private float 		    	posXInPixels=0, posYInPixels=0;
	private float 		 	    width=0, height=0;
	private static final Random rgenerator = new Random();
	private String 				text;
	Paint 						paint;
	Paint 						paintRect = new Paint();
	Bitmap 						bitmap;

	
	public ZoomablePinView(Context context) 
	{
		super(context);

		//setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.map_marker_32dp));
		this.text = randomText(context);
		
		Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
		
        Rect bounds = new Rect();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(resources.getColor(R.color.blue));
        paint.setTextSize((int) (18 * scale));
        paint.getTextBounds(text, 0 , text.length(), bounds);
     
		 //draw Rectangle		
        Bitmap bm = Bitmap.createBitmap((bounds.width()+12),  (bounds.height()+10), Config.ARGB_8888);
	
        Canvas canvas = new Canvas(bm);
        canvas.drawARGB(150, 245, 245, 245);
	
	 
        int x = (bm.getWidth() - bounds.width())/3;
        int y = (bm.getHeight() + bounds.height())/3;
        Log.v("ZoomablePinView", "bm = " + x + " / " + y);
        canvas.drawText(text,x*scale, y*scale, paint);
        setImageBitmap(bm);
	}


	@Override
	public void setImageBitmap(Bitmap bm)
	{ 
		super.setImageBitmap(bm);
		this.width = bm.getWidth();
		this.height = bm.getHeight();
	}

	public void setPosition (float posX, float posY, PointF centerPoint, PointF centerFocus, float saveScale) {
		this.posX = posX;
		this.posY = posY;
		setRealPosition(centerPoint, centerFocus, saveScale);
		setMargins();
	}

	public void moveOnZoom (float focusX, float focusY, float scale) {
		posX = (scale * (posX - focusX)) + focusX;
		posY = (scale * (posY - focusY)) + focusY;
		setMargins();
	}

	public void moveOnDrag (float dx, float dy) {
		posX += dx;
		posY += dy;
		setMargins();
	}

	private void setMargins() {
		int leftMargin = (int) (posX - width/2);
		int topMargin = (int) (posY - height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getLayoutParams());
		layoutParams.setMargins( leftMargin, topMargin, 0, 0);
		setLayoutParams(layoutParams);
	}

	public void drag (float dx, float dy, PointF centerPoint, PointF centerFocus, float saveScale) {
		moveOnDrag(dx, dy);
		setRealPosition(centerPoint, centerFocus, saveScale);
	}

	private void setRealPosition (PointF centerPoint, PointF centerFocus, float saveScale) {
		float deltaX = (posX - centerPoint.x) / saveScale;
		float deltaY = (posY - centerPoint.y) / saveScale;
		this.posXInPixels = centerFocus.x + deltaX;
		this.posYInPixels = centerFocus.y + deltaY;
	}

	public PointF getPositionInPixels() {
		PointF pinPos = new PointF(posXInPixels, posYInPixels);
		return pinPos;
	}

	public float getCenterPointViewX() {
		return posX;
	}

	public float getCenterPointViewY() {
		return posY - height/2;
	}
	
	public String getText() {
		return text;
	}

	public String randomText (Context context)
	{
		application = (GlobalMethods) context.getApplicationContext();		
		int rnb;
		
		do {
			rnb = rgenerator.nextInt(application.getCityMapList().size());
		} while(application.getCityMapList().get(rnb).isDisplayed());
		
		String q = application.getCityMapList().get(rnb).getName();
		application.getCityMapList().get(rnb).setDisplayed(true);
		
		return q;
	}
}
