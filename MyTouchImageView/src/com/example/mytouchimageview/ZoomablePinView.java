package com.example.mytouchimageview;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ZoomablePinView extends ImageView{
	private GlobalMethods 	    application;
	private float 		  	    posX=0, posY=0;
	private float 		    	posXInPixels=0, posYInPixels=0;
	private float 		 	    width=0, height=0;
	private static final Random rgenerator = new Random();
	
	public ZoomablePinView(Context context) 
	{
		super(context);
		//setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.map_marker_32dp));
		String text = randomText(context);
	 	Bitmap bmp = drawTextToBitmap(context,R.drawable.bubble,text);
	 	setImageBitmap(bmp);
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
	
	public Bitmap drawTextToBitmap(Context mContext,  int resourceId,  String mText) {
	    try {
	         Resources resources = mContext.getResources();
	            float scale = resources.getDisplayMetrics().density;
	            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

	            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
	            // set default bitmap config if none
	            if(bitmapConfig == null) {
	              bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
	            }
	            // resource bitmaps are imutable,
	            // so we need to convert it to mutable one
	            bitmap = bitmap.copy(bitmapConfig, true);

	            Canvas canvas = new Canvas(bitmap);
	            // new antialised Paint
	            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	            // text color - #3D3D3D
	            paint.setColor(Color.rgb(110,110, 110));
	            // text size in pixels
	            paint.setTextSize((int) (16 * scale));
	            // text shadow
	            paint.setShadowLayer(1f, 0f, 1f, Color.MAGENTA);

	            // draw text to the Canvas center
	            Rect bounds = new Rect();
	            paint.measureText (mText);
	            paint.getTextBounds(mText, 0 , mText.length(), bounds);
	            int x = (bitmap.getWidth() - bounds.width())/6;
	            int y = (bitmap.getHeight() + bounds.height())/5;

	          canvas.drawText(mText, 0, y * scale, paint);
	           
	            return bitmap;
	    } catch (Exception e) {
	        // TODO: handle exception
	    	return null;
	    }

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
