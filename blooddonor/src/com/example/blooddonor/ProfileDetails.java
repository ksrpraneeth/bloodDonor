package com.example.blooddonor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.example.blooddonor.Utils.ChangeDateTime;
import com.example.blooddonor.Utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.ComponentName;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import beans.Profile;

public class ProfileDetails extends Activity implements OnClickListener {
	TextView name;
	TextView phonenotv;
	TextView dob;
	TextView lod;
	Button Call;
	Button Share;
	String ProfilePhonenumber;
	String ProfileName;
	String bloodgroup12;
	TextView bloodgroup;
	PackageManager pm;
	ImageView profilepic;
	boolean app_installed;
	boolean installed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profiledetails);

		name = (TextView) findViewById(R.id.det_name);
		profilepic = (ImageView) findViewById(R.id.profiledet_profilepic);
		dob = (TextView) findViewById(R.id.det_dob);
		lod = (TextView) findViewById(R.id.det_ldd);

		bloodgroup = (TextView) findViewById(R.id.det_bloodgroup);
		String phoneno = getIntent().getExtras().getString("phoneno");

		DBhandler db = new DBhandler(this);
		ArrayList<Profile> profilelistdet = new ArrayList<Profile>();
		profilelistdet = db.getAllProfilesForphonno(phoneno);

		db.close();

		name.setText(profilelistdet.get(0).getName());

		dob.setText(ChangeDateTime.parseDate(profilelistdet.get(0).getDob()));
		if (profilelistdet.get(0).getLdd().toString().equals("9999-12-31")) {
			lod.setText("Not Donated");
		} else {
			lod.setText(ChangeDateTime
					.parseDate(profilelistdet.get(0).getLdd()));
		}

		bloodgroup.setText(profilelistdet.get(0).getBloodgroup());

		ProfilePhonenumber = profilelistdet.get(0).getMobileno().toString();
		ProfileName = profilelistdet.get(0).getName().toString();
		bloodgroup12 = profilelistdet.get(0).getBloodgroup().toString();
		try {
			profilepic.setImageBitmap(Utils.getThumbnail(
					Uri.parse(profilelistdet.get(0).getPhoto()), this, 108));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("PROFILE PHONE NUMBER" + ProfilePhonenumber);
		Call = (Button) findViewById(R.id.det_call);
		Call.setOnClickListener(this);
		Share = (Button) findViewById(R.id.det_share);
		Share.setOnClickListener(this);
		System.out.println("NAME PHONENUMBER DOB LOD GENDER BLOODGROUP"
				+ profilelistdet.get(0).getName()
				+ profilelistdet.get(0).getMobileno()
				+ profilelistdet.get(0).getDob()
				+ profilelistdet.get(0).getLdd()
				+ profilelistdet.get(0).getGender()
				+ profilelistdet.get(0).getBloodgroup());

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.det_call) {
			Intent i = new Intent(Intent.ACTION_DIAL);
			String p = "tel:" + ProfilePhonenumber;
			i.setData(Uri.parse(p));
			startActivity(i);
		}

		if (v.getId() == R.id.det_share) {

			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = "Name:" + ProfileName + '\n' + "PhoneNumber:"
					+ ProfilePhonenumber + '\n' + "BloodGroup:" + bloodgroup12;
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Subject Here");
			sharingIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));

		}

	}

}
