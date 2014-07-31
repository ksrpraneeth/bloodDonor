package com.example.blooddonor;

import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.blooddonor.LocationServices.AppLocationService;
import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpPostRequestHandler;
import com.example.blooddonor.interfaces.LocationInterface;
import com.example.blooddonor.interfaces.ResponseInterface;

public class Country extends Activity implements
		android.view.View.OnClickListener, LocationInterface, ResponseInterface {
	TextView countrycode;
	String result;
	String code;
	Button send;
	EditText mobilenumber;
	EditText isdcode;
	SharedprefClass pref = new SharedprefClass(this);
	ProgressDialog progressBar;
	Boolean GotCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entercountry);
		countrycode = (TextView) findViewById(R.id.entercountry);
		countrycode.setOnClickListener(this);
		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(this);
		mobilenumber = (EditText) findViewById(R.id.mobilenumber);
		isdcode = (EditText) findViewById(R.id.isdcode);
		mobilenumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-genemethod stub
				if (s.toString().trim().length() > 1) {
					mobilenumber.setError(null);
				}

			}
		});
		pref.put("GotCode", "false");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.country, menu);
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				result = data.getStringExtra("result");
				code = data.getStringExtra("code1");
				/*
				 * System.out.println("code" + code);
				 * System.out.println(result);
				 */
				isdcode.setText(code);
				isdcode.setClickable(false);
				countrycode.setText(result);
				countrycode.setTextColor(Color.BLACK);

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.entercountry) {
			Intent i = new Intent(this, CountryCode.class);
			startActivityForResult(i, 1);
		}

		if (v.getId() == R.id.send) {
			if ((countrycode.getText().toString().trim().length() < 1)) {

				countrycode.setError("Choose Country.");

			}

			else if ((mobilenumber.getText().toString().trim().length() < 1)
					|| !(mobilenumber.getText().toString().trim()
							.matches("^[0-9]{10}$"))) {
				mobilenumber.setError("Enter 10 digit Mobile Number");

			}

			else {
				pref.put("code1", code);
				System.out.println("Code " + code);
				System.out.println("Mobile Number"
						+ mobilenumber.getText().toString());
				pref.put("phonenumber_me", mobilenumber.getText().toString());

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);

				alertDialogBuilder.setTitle("Phone Number");

				alertDialogBuilder.setMessage("Is "
						+ pref.get("phonenumber_me") + " your phone number?");

				// set positive button: Yes message

				alertDialogBuilder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								GotCode = true;
								AppLocationService appLocationService = new AppLocationService(
										Country.this, Country.this);
								Location nwLocation = appLocationService
										.getLocation(LocationManager.NETWORK_PROVIDER);
								if (appLocationService.islocAvalable()) {
									progressBar = new ProgressDialog(
											Country.this);
									progressBar.setCancelable(false);
									progressBar
											.setMessage("Sending Verification please wait....");
									progressBar
											.setProgressStyle(ProgressDialog.STYLE_SPINNER);

									progressBar.show();
								} else {
									appLocationService
											.showSettingsAlert("NETWORK");
								}

							}

						});

				// set negative button: No message

				alertDialogBuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {

								// cancel the alert box and put a Toast to the
								// user

								dialog.cancel();

							}

						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.setCancelable(false);

				// show alert

				alertDialog.show();

			}
		}

	}

	@Override
	public void LocationReceived(Location location) {
		// TODO Auto-generated method stub
		if (GotCode) {
			if (progressBar != null)
				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}

			pref.put("lat", Double.toString(location.getLatitude()));
			pref.put("long", Double.toString(location.getLongitude()));
			System.out.println(location.getLatitude() + " "
					+ location.getLongitude());
			try {
				Sha1Hex hexcode = new Sha1Hex();
				pref.put("verificationcode",
						hexcode.makeSHA1Hash(pref.get("phonenumber_me"))
								.toString());
				JSONObject obj = new JSONObject();

				obj.put("phoneNo",
						pref.get("code1")
								+ pref.get("phonenumber_me").toString());
				obj.put("verifyNo", pref.get("verificationcode"));
				System.out.println(obj.toString());
				// HttpRequestHandler handler = new HttpRequestHandler(
				// Constants.Url + Constants.PhoneVerification_url, obj,
				// "number", this, this);
				// handler.execute();
				GotCode = false;
				Intent i = new Intent(Country.this, Gotyourcode.class);
				Country.this.startActivity(i);
				this.finish();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void getResponse(Context context, String json) {
		// TODO Auto-generated method stub
		System.out.println("Received verification : " + json);
	}

}
