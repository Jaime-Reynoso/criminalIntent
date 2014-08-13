package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

public class pictureUtils {
	/*
	 * The whole point of this class is to get a smaller image from a larger one
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getScaledDrawable(Activity a, String path){
		Display display = a.getWindowManager().getDefaultDisplay();
		float destWidth= display.getWidth();
		float destHeight = display.getHeight();
		
		//read in the dimensions of the image on the disk
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds= true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		
		int inSampleSize = 1;
		if(srcHeight > destHeight || srcWidth > destWidth){
			if(srcWidth > srcHeight){
				inSampleSize = Math.round(srcHeight/destHeight);
			} else{
				inSampleSize = Math.round(srcWidth/destWidth);
			}
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		
		return new BitmapDrawable(a.getResources(), bitmap);
	}
	/*
	 * This method basically clears an ImageView from the memory
	 */
	public static void cleanImageView(ImageView imageView){
		if(!(imageView.getDrawable() instanceof BitmapDrawable))
		{
			return;
		}
		BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
		/*
		 * Calling b.recycle() clears the native storage of the bitmap in question, 
		 * which is important on a phone because most phones have a limited amount of
		 * resources
		 */
		b.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}
}
