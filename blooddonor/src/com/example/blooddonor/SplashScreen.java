package com.example.blooddonor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	private static int SPLASH_TIME_OUT = 3000;
	SharedprefClass pref;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);
		pref = new SharedprefClass(getApplicationContext());

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				if (pref.get("inGotCode").equals("true")) {
					if (pref.get("inSignUp").equals("true")) {
						if (pref.get("inRegister").equals("true")) {
							if (pref.get("inMainActivity").equals("true")) {
								i = new Intent(SplashScreen.this,
										MainActivity.class);
								startActivity(i);
							} else {
								i = new Intent(SplashScreen.this,
										Register.class);
								startActivity(i);
							}
						} else {
							i = new Intent(SplashScreen.this, SignUp.class);
							startActivity(i);
						}
					} else {
						i = new Intent(SplashScreen.this, Gotyourcode.class);
						startActivity(i);
					}
				} else {
					i = new Intent(SplashScreen.this, Country.class);
					startActivity(i);

				}
				finish();
			}
		}, SPLASH_TIME_OUT);

	}
}
