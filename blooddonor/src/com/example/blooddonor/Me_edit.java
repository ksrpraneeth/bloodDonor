package com.example.blooddonor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpPostRequestHandler;
import com.example.blooddonor.interfaces.ResponseInterface;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Me_edit extends Activity implements OnClickListener,
		ResponseInterface {
	EditText name;
	Spinner bloodgroup;
	Button sendeditToserver;
	SharedprefClass pref;
	ImageView lastcal;
	TextView dob, lastdate;
	ImageView dobcal;
	String dob1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_edit);
		bloodgroup = (Spinner) findViewById(R.id.bloodedit);
		name = (EditText) findViewById(R.id.nameedit);
		dob = (TextView) findViewById(R.id.dob);
		lastdate = (TextView) findViewById(R.id.lastdate);
		dobcal = (ImageView) findViewById(R.id.dobcalendar);
		dobcal.setOnClickListener(this);
		lastcal = (ImageView) findViewById(R.id.lastcalendar);
		lastcal.setOnClickListener(this);
		sendeditToserver = (Button) findViewById(R.id.sendEditToserver);
		sendeditToserver.setOnClickListener(this);

		ArrayAdapter<CharSequence> bloodadapter = ArrayAdapter
				.createFromResource(this, R.array.Bloodgroups,
						R.layout.simple_spinner);
		bloodadapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		bloodgroup.setAdapter(new NothingSelectedSpinnerAdapter(bloodadapter,
				R.layout.contact_spinner_row_nothing_selected,
				// R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				this));

		System.out.println("SPINNWE" + bloodgroup.getSelectedItem());

		pref = new SharedprefClass(this);
		// sendeditToserver.setOnClickListener(new OnClickListener() {

		// // @Override
		// public void onClick(View v) {
		//
		//
		// if (bloodgroup.getSelectedItem().toString().equals() {
		// pref.put("bloodgroup",
		// bloodgroup.getSelectedItem().toString()); }
		// if (name.getText().length() > 0) {
		// pref.put("username", name.getText().toString());
		// } else if (dob.getText().length() > 0) {
		// pref.put("dateofbirth", dob.getText().toString());
		// } else if (ldd.getText().length() > 0) {
		// pref.put("lastdonate", ldd.getText().toString());
		// }
		// finish();
		// }
		// });
		// }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.dobcalendar) {
			System.out.println("clicekd");
			calendarfrom(v);
		}

		else if (v.getId() == R.id.lastcalendar) {
			System.out.println("clicekd");
			calendarlast(v);
		} else if (v.getId() == R.id.sendEditToserver) {
			// if (name.getText().length() > 0) {
			// pref.put("username", name.getText().toString());
			// } else if (bloodgroup.getSelectedItemPosition() > 0) {
			// pref.put("bloodgroup", bloodgroup.getSelectedItem().toString());
			// } else if (dob.getText().length() > 0) {
			// pref.put("dateofbirth", dob.getText().toString());
			// } else if (lastdate.getText().length() > 0) {
			// pref.put("lastdonate", lastdate.getText().toString());
			// }
			pref.put("username",
					(name.getText().toString().length() > 0) ? name.getText()
							.toString() : pref.get("username"));
			pref.put(
					"bloodgroup",
					(bloodgroup.getSelectedItemPosition() > 0) ? bloodgroup
							.getSelectedItem().toString() : pref
							.get("bloodgroup"));
			pref.put("lastdonate", (lastdate.getText().length() > 0) ? lastdate
					.getText().toString() : pref.get("lastdonate"));
			pref.put("dateofbirth", (dob.getText().length() > 0) ? dob
					.getText().toString() : pref.get("dateofbirth"));

			JSONObject jsonstring = new JSONObject();
			try {
				jsonstring.put("bloodgroup", pref.get("bloodgroup"));
				jsonstring.put("DOB", pref.get("dateofbirth"));
				jsonstring.put("LDD", pref.get("lastdonate"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpPostRequestHandler handler = new HttpPostRequestHandler(
					Constants.Url + Constants.UpdateProfile, jsonstring,
					"registration", this, this);
			handler.execute();
			finish();
		}
	}

	public void calendarfrom(View v) {
		Calendar c;

		// Process to get Current Date
		c = Calendar.getInstance();
		int mYear, mMonth, mDay;
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						dob.setText(year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth);

					}
				}, mYear, mMonth, mDay);
		long valid = (long) (System.currentTimeMillis() - 568024668000.0);
		dpd.getDatePicker().setMaxDate(valid);
		dpd.show();

	}

	public void calendarlast(View v) {
		Calendar c;

		// Process to get Current Date
		c = Calendar.getInstance();
		int mYear, mMonth, mDay;
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						lastdate.setText(year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth);

					}
				}, mYear, mMonth, mDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		try {
			Date date;

			dob1 = dob.getText().toString();
			date = sdf.parse(dob1);
			System.out.println(date.getTime());
			long valid = (long) (Long.valueOf(date.getTime()) + 568024668000.0);
			dpd.getDatePicker().setMinDate(valid);
			dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dpd.show();

	}

	@Override
	public void getResponse(Context context, String json) {
		// TODO Auto-generated method stub
		System.out.println(json);
	}

}
