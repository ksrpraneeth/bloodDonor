package com.example.blooddonor.httprequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.interfaces.ResponseInterface;

public class HttpPostRequestHandler extends AsyncTask<String, String, String> {
	String url;
	JSONObject params;
	String key;
	ResponseInterface responseActivity;
	String test;
	Context context;

	public HttpPostRequestHandler(String url, JSONObject params, String key,
			ResponseInterface Activity, Context context) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.context = context;
		this.params = params;
		this.key = key;
		this.responseActivity = Activity;
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
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair(key, params.toString()));
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					webs, "iso-8859-1"), 8);

			while ((test = reader.readLine()) != null) {
				System.out.println("test" + test);
				responseActivity.getResponse(context, test.toString());

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
