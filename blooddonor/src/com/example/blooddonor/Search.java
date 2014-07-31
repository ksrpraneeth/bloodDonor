package com.example.blooddonor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.blooddonor.LocationServices.AppLocationService;
import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpPostRequestHandler;
import com.example.blooddonor.interfaces.ResponseInterface;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class Search extends Fragment implements OnClickListener,
		ResponseInterface {
	Spinner units;
	Spinner bloodgroup;
	EditText area;
	TextView people;
	CheckBox postonfb;
	List<String> bloodgrouplist;
	List<String> unitslist;
	AutoCompleteTextView atvPlaces;
	PlacesTask placesTask;
	ParserTask parserTask;
	Button send;
	View v;
	Context context;
	SharedprefClass pref;
	ProgressDialog searchProgress;
	TextView success;

	public Search() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		v = inflater.inflate(R.layout.fragment_search, container, false);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		people = (TextView) v.findViewById(R.id.onnetwork);
		area = (EditText) v.findViewById(R.id.area);
		bloodgroup = (Spinner) v.findViewById(R.id.entergroup);
		units = (Spinner) v.findViewById(R.id.enterunits);
		send = (Button) v.findViewById(R.id.Sendtoserver);
		send.setOnClickListener(this);
		success = (TextView) v.findViewById(R.id.succesfullSearch);
		context = getActivity();
		pref = new SharedprefClass(context);

		ArrayAdapter<CharSequence> bloodadapter = ArrayAdapter
				.createFromResource(getActivity().getApplicationContext(),
						R.array.Bloodgroups, R.layout.simple_spinner);
		bloodadapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		bloodgroup.setAdapter(new NothingSelectedSpinnerAdapter(bloodadapter,
				R.layout.contact_spinner_row_nothing_selected,
				// / R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				getActivity().getApplicationContext()));

		ArrayAdapter<CharSequence> unitsadapater = ArrayAdapter
				.createFromResource(getActivity().getApplicationContext(),
						R.array.Units, R.layout.simple_spinner);
		unitsadapater
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		units.setAdapter(new NothingSelectedSpinnerAdapter(unitsadapater,
				R.layout.spinnerrownothing_selected, getActivity()
						.getApplicationContext()));

		atvPlaces = (AutoCompleteTextView) v.findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);
		Utils.removeErrorListener(area);
		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				atvPlaces.setError(null);
				placesTask = new PlacesTask();
				placesTask.execute(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyC-FerPGzC5jRVkRLgQ5fFnBM-alS7Js_k";

			String input = "";

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=false";

			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&" + key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			try {
				// Fetching the data from web service in background
				data = downloadUrl(url);
				// System.out.println("data" + data);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			if (Utils.isOnline(getActivity())) {
				parserTask.execute(result);
			}

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);
				// System.out.println("JSON OBJECT" + jsonData[0]);
				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { android.R.id.text1 };

			// Creating a SimpleAdapter for the AutoCompleteTextView
			SimpleAdapter adapter = new SimpleAdapter(getActivity()
					.getBaseContext(), result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			atvPlaces.setAdapter(adapter);
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.Sendtoserver) {
			System.out.println("BUTTON CLICKED"
					+ bloodgroup.getSelectedItemPosition());
			if (bloodgroup.getSelectedItemPosition() == 0) {
				Toast.makeText(getActivity(), "Select Blood Group.",
						Toast.LENGTH_SHORT).show();
			} else if (units.getSelectedItemPosition() == 0) {
				Toast.makeText(getActivity(), "Select Number of Units.",
						Toast.LENGTH_SHORT).show();
			} else if (area.getText().length() < 1) {
				Utils.showError(area, "Enter Hospital name.");
			} else if (atvPlaces.getText().length() < 1) {
				Utils.showError(atvPlaces, "Enter Area of Hospital");
			} else {

				if (Utils.isOnline(context)) {

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					alertDialogBuilder.setTitle("Search");

					alertDialogBuilder
							.setMessage("Sends Notification to the people with in 5kms from hospital range ");

					// set positive button: Yes message

					alertDialogBuilder.setPositiveButton("Search",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									sendToServer();

								}

							}

					);

					// set negative button: No message

					alertDialogBuilder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {

									// cancel the alert box and put a Toast to
									// the
									// user

									dialog.cancel();

								}

							});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.setCancelable(false);

					// show alert

					alertDialog.show();
				}

				else {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					alertDialogBuilder.setTitle("Internet Settings");

					alertDialogBuilder
							.setMessage("You are not connected to Internet");

					// set positive button: Yes message

					alertDialogBuilder.setPositiveButton("Settings",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {

									Intent intent = new Intent(
											Settings.ACTION_WIRELESS_SETTINGS);
									startActivity(intent);
								}

							});

					// set negative button: No message

					alertDialogBuilder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {

									// cancel the alert box and put a Toast to
									// the
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

	}

	private void sendToServer() {
		// TODO Auto-generated method stub
		if (Geocoder.isPresent()) {
			try {
				searchProgress = new ProgressDialog(getActivity());
				searchProgress.setCancelable(false);
				searchProgress.setMessage("Please wait....");
				searchProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				searchProgress.show();
				List<LatLng> ll = null;
				String location = atvPlaces.getText().toString();
				System.out.println("IN GEO CODER" + location);
				Geocoder gc = new Geocoder(getActivity()
						.getApplicationContext());
				System.out.println("ADDRESSSSS");
				List<Address> addresses = gc.getFromLocationName(
						location.trim(), 1);
				System.out.println("BANDI");
				System.out.println("ADDRESS" + addresses);
				Address a = addresses.get(0);

				ll = new ArrayList<LatLng>(addresses.size());

				if (a.hasLatitude() && a.hasLongitude()) {
					ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
				}

				System.out.println("LIST" + ll);
				JSONObject gcmobj = new JSONObject();
				gcmobj.put("bloodgroup", bloodgroup.getSelectedItem()
						.toString());
				gcmobj.put("units",
						Integer.valueOf(units.getSelectedItem().toString()));
				gcmobj.put("hospital", area.getText().toString());
				gcmobj.put("location", atvPlaces.getText().toString());
				gcmobj.put("distance", 10);
				gcmobj.put("lat", Double.valueOf(ll.get(0).latitude).toString());
				gcmobj.put("long", Double.valueOf(ll.get(0).longitude)
						.toString());
				gcmobj.put("regID", pref.get("regID"));
				HttpPostRequestHandler handler = new HttpPostRequestHandler(
						Constants.Url + Constants.Gcm_url, gcmobj, "details",
						this, context);
				System.out.println("here" + Constants.Url + Constants.Gcm_url);
				handler.execute();
			} catch (IOException e) {
				// handle the exception
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("BUTTON CLICKED in else");

		}
	}

	@Override
	public void getResponse(Context context, String json) {
		// TODO Auto-generated method stub
		if (searchProgress.isShowing()) {
			searchProgress.dismiss();
		}

		String count = null;
		try {
			JSONObject jobj = new JSONObject(json);
			count = jobj.getString("count");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Search.this.success
				.setText(Html
						.fromHtml("<b>Notification successfully sent to <font color='#EE0000'>"
								+ count + "</font></b>."));
	}
}
