package com.example.blooddonor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedprefClass {
	Context context;

	SharedprefClass(Context context) {
		this.context = context;
	}

	void put(String key, String data) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, data);
		editor.commit();
	}

	String get(String key) {
		String result = "";
		SharedPreferences preferences2 = PreferenceManager
				.getDefaultSharedPreferences(context);
		result = preferences2.getString(key, "");
		return result;
	}
}
