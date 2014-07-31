package com.example.blooddonor.httprequests;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.interfaces.ResponseInterface;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpGetRequestHandler extends AsyncTask<String, String, String> {
	ResponseInterface responseactivity;
	Context context;
	String url;

	public HttpGetRequestHandler(Context context,
			ResponseInterface responseactivity, String url) {
		this.context = context;
		this.url = url;
		this.responseactivity = responseactivity;
	}

	public void connect(String url) {

		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status
			Log.i("Blood donor", response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream, "iso-8859-1"), 8);

				String test;
				while ((test = reader.readLine()) != null) {
					System.out.println("test" + test);
					responseactivity.getResponse(context, test.toString());

				}
			}

		} catch (Exception e) {
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (Utils.isOnline(context))
			connect(url);
		return null;
	}

}
