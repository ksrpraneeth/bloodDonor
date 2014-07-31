package com.example.blooddonor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class CountryCode extends Activity implements OnItemClickListener {
	ListView listview;
	GetCountryName getcountryname = new GetCountryName();
	EditText editsearch;
	String[] recourseList;
	CountriesListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.country_code);

		editsearch = (EditText) findViewById(R.id.filtersearch);

		recourseList = this.getResources().getStringArray(R.array.CountryCodes);

		listview = (ListView) findViewById(R.id.listView);
		adapter = new CountriesListAdapter(this, recourseList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);

		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text1 = editsearch.getText().toString();
				String text = text1.toLowerCase();
				// System.out.println(text);
				String[] recourselist1 = adapter.filter(text, recourseList);

				CountriesListAdapter adapter1 = new CountriesListAdapter(
						getApplicationContext(), recourselist1);
				listview.setAdapter(adapter1);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

						String itemValue = (String) listview
								.getItemAtPosition(position);
						String[] g = itemValue.split(",");
						

						String code = "+"
								+ getcountryname.GetCountryZipCode(g[0]).trim();
						Intent returnIntent = new Intent();
						returnIntent.putExtra("result",
								getcountryname.GetCountryZipCode(g[1]));
						returnIntent.putExtra("code1", code);
						setResult(RESULT_OK, returnIntent);
						finish();

					}

				});

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.country_code, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		String itemValue = (String) listview.getItemAtPosition(position);
		String[] g = itemValue.split(",");
		String display = getcountryname.GetCountryZipCode(g[1]).trim() + ""
				+ "(+" + getcountryname.GetCountryZipCode(g[0]).trim() + ")";

		String code = "+" + getcountryname.GetCountryZipCode(g[0]).trim();
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", display);
		returnIntent.putExtra("code1", code);
		setResult(RESULT_OK, returnIntent);
		finish();

	}

}