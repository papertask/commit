package com.interview.iso.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DataPreferenceManager {
	private static DataPreferenceManager _instance;
	/**
	 * Holder for Preference database
	 */
	public static final String PREFS_NAME = "_pref_iso_";
	
	/**
	 * Holder data for item
	 */
	// TODO Define here
	
	private static SharedPreferences settings;

	/**
	 * No need to setting SharedPreferences to store data
	 * @author Atula
	 * @param context 
	 * @return instance of DataPreferenceManager
	 */
	public static DataPreferenceManager getInstance(Context context) {
		settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		if (_instance == null) {
			_instance = new DataPreferenceManager();
		}
		return _instance;
	}

	/**
	 * store boolean data
	 * @author Atula
	 * @param holderKey key to holder boolean data
	 * @param value data
	 */
	public void writeIntData(String holderKey, int value) {
		try {
			SharedPreferences.Editor ed = settings.edit();
			ed.putInt(holderKey, value);
			ed.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * get boolean data from stored, its hold by key
	 * @author Atula
	 * @param hold int key to holder data
	 * @return if error occur or default return true, else return data
	 */
	public int  getDataIntFromHolder(String holderLong){
		try {
			return settings.getInt(holderLong, 0);
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}



	/**
	 * store boolean data
	 * @author Atula
	 * @param holderKey key to holder boolean data
	 * @param holderData data
	 */
	public void writeBooleanData(String holderKey, boolean holderData) {
		try {
			SharedPreferences.Editor ed = settings.edit();
			ed.putBoolean(holderKey, holderData);
			ed.commit();				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get boolean data from stored, its hold by key
	 * @author Atula
	 * @param holderString key to holder data
	 * @return if error occur or default return true, else return data
	 */
	public boolean getDataBooleanFromHolder(String holderString){
		try {
			return settings.getBoolean(holderString, false);
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}