package com.example.blooddonor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpPostRequestHandler;

public class Gotyourcode extends Activity {
	Button verify;
	SharedprefClass pref;
	EditText gotcode;
	TextView resend;
	TextView reenterphno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gotyourcode);
		verify = (Button) findViewById(R.id.donecode);
		gotcode = (EditText) findViewById(R.id.verifycode);
		resend = (TextView) findViewById(R.id.reSendVerify);
		reenterphno = (TextView) findViewById(R.id.reEnterNumber);
		String reentercode = "<u><i><b>Reenter Phone number?</b></i></u>";
		String resendcode = "<u><i><b>Resend verify code?</b></i></u>";
		reenterphno.setText(Html.fromHtml(reentercode));
		resend.setText(Html.fromHtml(resendcode));
		pref = new SharedprefClass(getApplicationContext());
		Utils.removeErrorListener(gotcode);
		pref.put("inGotCode", "true");
		Toast.makeText(getApplicationContext(), pref.get("verificationcode"),
				Toast.LENGTH_LONG).show();
		reenterphno.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Gotyourcode.this, Country.class);
				startActivity(i);
				Gotyourcode.this.finish();
			}
		});
		
		
		resend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getApplicationContext());

				alertDialogBuilder.setTitle("Resend Code");

				alertDialogBuilder.setMessage("Do you want to send code again");

				// set positive button: Yes message

				alertDialogBuilder.setPositiveButton("Send",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								
//								HttpRequestHandler handler = new HttpRequestHandler(
//								Constants.Url + Constants.PhoneVerification_url, obj,
//								"number", this, this);
//						handler.execute();

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
		});
		
		
		verify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (gotcode.getText().toString()
						.equals(pref.get("verificationcode"))) {
					finish();
					Intent i = new Intent(Gotyourcode.this, SignUp.class);
					startActivity(i);
				} else {
					Utils.showError(gotcode,
							"Entered verification code is wrong");
				}

			}
		});
	}
}
