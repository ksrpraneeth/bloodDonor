package com.example.blooddonor;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class settingActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
	}
}
