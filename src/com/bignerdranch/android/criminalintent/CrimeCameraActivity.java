package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
/*
 * After creating the fragment, you need to create an activity that returns an instance
 * of that fragment class
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		/*
		 * These have to happen before calling the super because of the fact that 
		 * these are OS-Level features
		 */
		//hide the window tile
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//hide the status bar and other OS-LEVEL devices
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}
	/*
	 * The activity is called to create a Fragment
	 */
	@Override
	protected Fragment createFragment() {
		return new CrimeCameraFragment();
	}

}
