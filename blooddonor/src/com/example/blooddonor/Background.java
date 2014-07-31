package com.example.blooddonor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpPostRequestHandler;
import com.example.blooddonor.interfaces.ResponseInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class Background extends Service implements
		android.location.LocationListener {

	LocationManager locationmanager;
	private static final String DELAY_TIME = "14400000";// milliseconds
	SharedprefClass pref;
	int i = 0;
	WakeLock wakeLock;
	boolean isDeamonrunning;

	public Background() {

	}

	/*
	 * Called befor service onStart method is called.All Initialization part
	 * goes here
	 */
	@Override
	public void onCreate() {
		locationmanager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates

		// Register the listener with the Location Manager to receive location
		// updates
		locationmanager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, Long.valueOf(DELAY_TIME), 0,
				this);
		pref = new SharedprefClass(getApplicationContext());
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MyWakelockTag");
		wakeLock.acquire();

	}

	/*
	 * You need to write the code to be executed on service start. Sometime due
	 * to memory congestion DVM kill the running service but it can be restarted
	 * when the memory is enough to run the service again.
	 */

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * Called when Sevice running in backgroung is stopped. Remove location
	 * upadate to stop receiving gps reading
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("info", "Service is destroyed");
		wakeLock.release();

		super.onDestroy();
	}

	@SuppressLint("NewApi")
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Intent myIntent = new Intent(this, Background.class);

		PendingIntent pendingIntent = PendingIntent.getService(
				getApplicationContext(), 0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.add(Calendar.SECOND, 10);

		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pendingIntent);

		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		System.out.println(location.getLatitude() + " "
				+ location.getLongitude());
		SharedprefClass pref = new SharedprefClass(getApplicationContext());
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("lat", location.getLatitude());
			jobj.put("long", location.getLongitude());
			jobj.put("phno", pref.get("phonenumber_me"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequestServce request = new HttpRequestServce(Constants.Url
				+ Constants.Latlng_Update, jobj, "update",
				this.getApplicationContext());
		request.execute();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public class HttpRequestServce extends AsyncTask<String, String, String> {
		String url;
		JSONObject params;
		String key;

		String test;
		Context context;

		public HttpRequestServce(String url, JSONObject params, String key,
				Context context) {
			// TODO Auto-generated constructor stub
			this.url = url;
			this.context = context;
			this.params = params;
			this.key = key;

			System.out.println("here" + params);
		}

		public void postData() {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = null;
			HttpPost httppost = new HttpPost(url);

			System.out.println("here1");
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair(key, params
						.toString()));
				System.out.println(params.toString());
				httppost.addHeader("Content-Type",
						"application/json; charset=utf-8");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				// Execute HTTP Post Request
				response = httpclient.execute(httppost);
				// System.out.println("Response" + response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			try {
				HttpEntity entity = response.getEntity();

				InputStream webs = null;
				webs = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(webs, "iso-8859-1"), 8);

				while ((test = reader.readLine()) != null) {
					System.out.println("test" + test);

				}

				webs.close();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				Log.e("Error in conversion: ", e.toString());
			}

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (Utils.isOnline(context))
				postData();
			return null;
		}

	}
}