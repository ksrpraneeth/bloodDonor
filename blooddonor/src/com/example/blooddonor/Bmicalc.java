package com.example.blooddonor;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bmicalc extends Activity implements OnClickListener,
		OnItemSelectedListener {

	EditText age;
	EditText height;
	EditText weight;
	EditText waist;
	EditText hip;
	EditText heightfeet;
	EditText heightinches;
	Button calculate;
	RadioButton male;
	RadioButton female;
	Spinner heightcm;
	LinearLayout heightcms, heightfts;
	double height1;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bmicalc);
		age = (EditText) findViewById(R.id.age);
		height = (EditText) findViewById(R.id.height);
		weight = (EditText) findViewById(R.id.weight);
		waist = (EditText) findViewById(R.id.waist);
		hip = (EditText) findViewById(R.id.hip);
		calculate = (Button) findViewById(R.id.calculate);
		heightfeet = (EditText) findViewById(R.id.heightfeet);
		heightinches = (EditText) findViewById(R.id.heightinch);
		heightcm = (Spinner) findViewById(R.id.spinnerft);
		heightcms = (LinearLayout) findViewById(R.id.heightlinear);
		heightfts = (LinearLayout) findViewById(R.id.heightlinear1);
		heightcms.setVisibility(LinearLayout.INVISIBLE);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);

		ArrayAdapter<CharSequence> heightcmadapter = ArrayAdapter
				.createFromResource(getApplicationContext(),
						R.array.HeightSpinner, R.layout.simple_spinner);

		heightcmadapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		heightcm.setAdapter(heightcmadapter);
		heightcm.setOnItemSelectedListener(this);

		male.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				female.setChecked(false);

				type = 1;

			}
		});

		female.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				male.setChecked(false);
				type = 2;

			}
		});

		calculate.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.calculate) {
			if (age.getText().toString().trim().length() < 1) {
				Toast.makeText(this, "enter age", Toast.LENGTH_SHORT).show();
			} else if (weight.getText().toString().trim().length() < 1) {
				Toast.makeText(this, "Enter weight", Toast.LENGTH_SHORT).show();
			} else {
				if (heightcms.isShown()) {
					if (height.getText().toString().trim().length() < 1) {
						Toast.makeText(this, "enter height", Toast.LENGTH_SHORT)
								.show();
					} else {
						System.out.println("cm taken");
						height1 = Integer.valueOf(height.getText().toString());
					}
				} else if (heightfts.isShown()) {
					if (heightfeet.getText().toString().trim().length() < 1
							&& heightinches.getText().toString().trim()
									.length() < 1) {
						Toast.makeText(this, "enter height", Toast.LENGTH_SHORT)
								.show();
					} else {
						System.out.println("ft taken");
						int ft = Integer.valueOf(heightfeet.getText()
								.toString());
						int inches = Integer.valueOf(heightinches.getText()
								.toString());
						height1 = ((ft * 12) + inches) * 2.54;
					}
				}

				System.out.println("height in cm " + height1);
				String age1 = age.getText().toString();
				int age2 = Integer.valueOf(age1);
				System.out.println("AGE " + age2);
				Double height2 = (double) height1 / 100;

				System.out.println("height in float" + height2);
				int weight1 = Integer.valueOf(weight.getText().toString());
				System.out.println("weight" + weight1);
				Float bmi = (float) (weight1 / (height2 * height2));
				System.out.println("BMI" + bmi);

				Float bmr;

				if (type == 1) {

					bmr = (float) ((10 * weight1) + (6.25 * height1)
							- (5 * age2) + 5);

					System.out.println("BMR MEN" + bmr);
				}

				else {
					bmr = (float) ((10 * weight1) + (6.25 * height1)
							- (5 * age2) - 161);

					System.out.println("BMR WOMEN" + bmr);
				}
				Intent calcresult = new Intent(this, Calculatorresult.class);
				calcresult.putExtra("Bmiresult", bmi.toString());

				calcresult.putExtra("Bmrresult", bmr.toString());

				startActivity(calcresult);

			}

		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		System.out.println(parent.getItemAtPosition(position).toString());
		if (parent.getItemAtPosition(position).toString()
				.equalsIgnoreCase("cms")) {
			heightcms.setVisibility(LinearLayout.VISIBLE);
			heightfts.setVisibility(LinearLayout.INVISIBLE);
		} else {
			heightcms.setVisibility(LinearLayout.INVISIBLE);
			heightfts.setVisibility(LinearLayout.VISIBLE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

		heightcms.setVisibility(LinearLayout.VISIBLE);
		heightfts.setVisibility(LinearLayout.INVISIBLE);

	}

}
