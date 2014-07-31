package com.example.blooddonor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import beans.Contact;
import beans.Profile;

import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpPostRequestHandler;
import com.example.blooddonor.interfaces.ContactInterface;
import com.example.blooddonor.interfaces.ResponseInterface;

public class Friends_Loading extends Activity implements ContactInterface,
		ResponseInterface {
	Context context;
	ContactsFetch ct;
	List<Profile> profilelist = new ArrayList<Profile>();
	JSONArray obj1 = new JSONArray();
	ProgressDialog friendsLoadingProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends__loading);
		context = this;
		friendsLoadingProgress = new ProgressDialog(Friends_Loading.this);
		friendsLoadingProgress.setCancelable(false);
		friendsLoadingProgress.setMessage("Please wait....");
		friendsLoadingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		DBhandler db3 = new DBhandler(context);
		db3.dropProfiles();
		db3.close();
		friendsLoadingProgress.show();
		ct = new ContactsFetch(context, Friends_Loading.this);
		ct.execute();
		friendsLoadingProgress
				.setMessage("Searching for Blood Donor in your friends...");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends__loading, menu);
		return true;
	}

	@Override
	public void getResponse(Context context, String json) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		try {
			System.out.println("Response :" + json);
			JSONArray array1 = new JSONArray(json);
			if (array1.length() > 0) {
				parseJson(json);
			} else {
				Intent i = new Intent(this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJson(String json) {
		// TODO Auto-generated method stub

		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = new JSONObject();
				object = array.getJSONObject(i);
				Profile profile = new Profile(object.getString("phonenumber"),
						object.getString("countrycode"),
						object.getString("firstname") + " "
								+ object.getString("lastname"),
						object.getString("DOB"), object.getString("LDO"),
						object.getString("bloodgroup"),
						object.getString("email"), object.getString("gender"),
						Utils.saveToInternalSorage(
								Utils.convertStringToBitmap(object
										.getString("photo")),
								object.getString("phonenumber") + ".jpg",
								context).toString());
				profilelist.add(profile);

			}
			System.out.println("Entering Db:" + profilelist.get(0).getEmail());
			submitProfilestoDb(profilelist);
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(i);
			// this.finish();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void submitProfilestoDb(List<Profile> profilelist2) {
		try {
			System.out.println("Entering Db:" + profilelist2.get(0).getEmail());
			// TODO Auto-generated method stub
			DBhandler db = new DBhandler(context);
			System.out.println("here");
			db.addProfiles(profilelist2);

			db.close();
			friendsLoadingProgress.dismiss();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void getContacts(Context context, List<Contact> contact) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		System.out.println("After executing contacts");
		List<Contact> contacts = contact;
		// System.out.println(contacts.get(0).get_name());
		for (int i = 0; i < contacts.size(); i++) {
			Contact c = contacts.get(i);
			// System.out.println(c.get_name());
			obj1.put(c.get_phone_number());
			// System.out.println(c.get_phone_number());

		}
		System.out.println("JSON ARRAY" + obj1);
		try {
			System.out.println("Size of array :"
					+ obj1.toString().getBytes("UTF-8").length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject obj2 = new JSONObject();
		try {
			obj2.put("Contacts", obj1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpPostRequestHandler handler = new HttpPostRequestHandler(Constants.Url
				+ Constants.Contact_url, obj2, "Contacts", this, context);
		handler.execute();
	}

}
