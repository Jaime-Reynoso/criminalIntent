package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	
	/*
	 * These values are made static so that another class can have direct access to them
	 * This will help with matching everything up later. 
	 */
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE="title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_SUSPECT = "suspect";
	private static final String JSON_PHOTO="photo";
	
	private Photo mPhoto;
	private String mSuspect;
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }
    
    public Crime(JSONObject json) throws JSONException{
    	mId =UUID.fromString(json.getString(JSON_ID));
    	if(json.has(JSON_TITLE)){
    		mTitle = json.getString(JSON_TITLE);
    	}
    	if(json.has(JSON_PHOTO)){
    		mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
    	}
    	if(json.has(JSON_SUSPECT)){
    		mSuspect = json.getString(JSON_SUSPECT);
    	}
    	mSolved = json.getBoolean(JSON_SOLVED);
    	mDate = new Date(json.getLong(JSON_DATE));
    	
    }

    @Override
    public String toString() {
        return mTitle;
    }
    
    
    /*
     * This method is pretty straightforward because the class returns a JSONObject
     * The steps were to add all the aspect of the crime into a JSON Object
     */
    public JSONObject toJSON() throws JSONException
    {
    	JSONObject object = new JSONObject();
    	object.put(JSON_ID, mId.toString());
    	object.put(JSON_TITLE, mTitle);
    	object.put(JSON_SOLVED, mSolved);
    	object.put(JSON_DATE, mDate.getTime());
    	object.put(JSON_SUSPECT, mSuspect);
    	
    	if(mPhoto != null){
    		object.put(JSON_PHOTO, mPhoto.toJSON());
    	}
    	return object;
    }
    
    public String getSuspect(){
    	return mSuspect;
    }
    
    public void setSuspect(String s){
    	mSuspect = s;
    }
    
    public Photo getPhoto(){
    	return mPhoto;
    }
    
    public void setPhoto(Photo p){
    	mPhoto = p;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


}
