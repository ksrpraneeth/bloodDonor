package com.example.blooddonor;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Calculatorresult extends Activity {
	TextView bmiresult;
	TextView bmrresult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculatorresult);
		bmiresult = (TextView) findViewById(R.id.bmiresult);
		bmiresult.setText(

		Html.fromHtml("<b>Your BMI is: <font color='#d71920'>"
				+ getIntent().getExtras().getString("Bmiresult")
				+ "</font></b>.")

		);

		bmrresult = (TextView) findViewById(R.id.bmrresult);
		bmrresult.setText(Html
				.fromHtml("<b>Your BMR is: <font color='#d71920'>"
						+ getIntent().getExtras().getString("Bmrresult")
						+ "  Calories/day" + "</font></b>."));
	}
}
