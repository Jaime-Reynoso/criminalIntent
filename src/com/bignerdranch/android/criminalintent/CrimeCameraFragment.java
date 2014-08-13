package com.bignerdranch.android.criminalintent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {
	
	private static final String TAG = "CrimeCameraFragment";
	static final String EXTRA_PHOTO_FILENAME="com.bignerdranch.android.criminialintent.photo_filename";
	
	
	private View mProgressContainer;
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		
		public void onShutter() {
			mProgressContainer.setVisibility(View.VISIBLE);
			
		}
	};
	
	private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		
		public void onPictureTaken(byte[] data, Camera camera) {
			
			//create a filename
			String filename = UUID.randomUUID().toString()+".jpg";
			
			//Now I have to save this JPG
			FileOutputStream os = null;
			boolean success = true;
			
			try{
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			} catch(Exception e){
				Log.e(TAG, "error writing to file"+filename, e);
			} finally {
				try{
					if(os != null){
						os.close();
					}
				} catch(Exception e){
					Log.e(TAG, "error closing file "+ filename, e);
					success = false;
				}
			}
			
			if(success ){
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				getActivity().setResult(Activity.RESULT_OK,i);
				
			}
			else{
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
			
		}
	};
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	
	@Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);

        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);
        Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCamera != null) {
            	    mCamera.takePicture(mShutterCallback, null, mPictureCallback);
            	}
            } 
        });

        mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        // deprecated, but required for pre-3.0 devices
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {

            public void surfaceCreated(SurfaceHolder holder) {
                // tell the camera to use this surface as its preview area
                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException exception) {
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                // we can no longer display on this surface, so stop the preview.
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            	if (mCamera == null) return;
            	
                // the surface has changed size; update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(s.width, s.height);
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }
        });
        
        return v; 
    }
	/*
	 * Here we're opening the Camera when we resume or start the fragment
	 */
	
	@TargetApi(9)
	@Override
	public void onResume(){
		super.onResume();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
			mCamera = mCamera.open(0);
		} else{
			mCamera = mCamera.open();
		}
	}
	
	/*
	 *The camera is a resource that can only be used by one activity at a time, so you 
	 *have to make sure to release the resource once you're done using it
	 */
	
	@Override
	public void onPause(){
		super.onPause();
		
		if(mCamera != null){
			mCamera.release();
			mCamera = null;
		}
	}
	
	private Size getBestSupportedSize(List<Size> sizes, int width, int height){
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width*bestSize.height;
		for(Size c: sizes){
			int area = c.width*c.height;
			if(area > largestArea){
				bestSize = c;
				largestArea = area;
			}
		}
		return bestSize;
	}
	
}
