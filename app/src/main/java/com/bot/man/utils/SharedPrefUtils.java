package com.bot.man.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bot.man.model.Constants;

public class SharedPrefUtils {

	private static SharedPrefUtils INSTANCE;
	private static SharedPreferences sSharedPreferences;

	private SharedPrefUtils(Context context) {
		sSharedPreferences = context.getSharedPreferences(
				Constants.APPLICATION_PREFERENCE_NAME, Context.MODE_PRIVATE);
	}

	public static SharedPrefUtils getInstance() {
		return INSTANCE;
	}

	public static void init(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new SharedPrefUtils(context);
		}
	}

	/**
	 * Method  used to set the string value of SharedPreferences.
	 *
	 * @param key   Key of the string to be set.
	 * @param value Value of the string to be set.
	 */
	public void setStringValue(String key, String value) {
		SharedPreferences.Editor editor = sSharedPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	/**
	 * Method to get the string value from SharedPreferences.
	 *
	 * @param key Key of the string to be get.
	 * @return Key of te string.
	 */
	public String getStringValue(String key) {
		return sSharedPreferences
				.getString(key, Constants.DEFAULT_SHARED_STRING_VALUE);
	}

	/**
	 * Method  used to set the b boolean value of SharedPreferences.
	 *
	 * @param key   Key of the string to be set.
	 * @param value Value of the boolean to be set.
	 */
	public void setBooleanValue(String key, boolean value) {
		SharedPreferences.Editor editor = sSharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	/**
	 * Method to get the boolean value from SharedPreferences.
	 *
	 * @param key Key of the string to be get.
	 * @return value of te boolean.
	 */
	public boolean getBooleanValue(String key) {
		return sSharedPreferences
				.getBoolean(key, Constants.DEFAULT_SHARED_BOOLEAN_VALUE);
	}
}
