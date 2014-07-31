package com.example.blooddonor;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GCMMessageView extends Activity implements OnClickListener {
	String message;
	TextView txtmsg;
	JSONObject gcmmessage;
	Button accept;
	Button decline;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.blooddonor.R.layout.messageview);

		// Retrive the data from GCMIntentService.java
		Intent i = getIntent();
		accept = (Button) findViewById(R.id.acceptblood);
		decline = (Button) findViewById(R.id.rejectblood);
		message = i.getStringExtra("message");
		accept.setOnClickListener(this);
		decline.setOnClickListener(this);

		try {
			gcmmessage = new JSONObject(message);
			message = gcmmessage.getString("gcmmess");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Locate the TextView
		txtmsg = (TextView) findViewById(com.example.blooddonor.R.id.message);

		// Set the data into TextView
		txtmsg.setText(Html.fromHtml(message));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.acceptblood:
			try {
				txtmsg.setText("Please call "
						+ gcmmessage.getString("phoneno").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.rejectblood:
			this.finish();
			break;
		default:
			break;
		}
	}
}