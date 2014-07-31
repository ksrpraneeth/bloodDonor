package com.example.blooddonor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.example.blooddonor.Utils.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import beans.Profile;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class Friends extends ListFragment {
	ListView list;
	ArrayList<String> ContactNames = new ArrayList<String>();
	ArrayList<String> PhoneNumber = new ArrayList<String>();
	JSONArray obj1 = new JSONArray();
	ContactsFetch ct;
	List<Profile> profilelist = new ArrayList<Profile>();
	Context context;
	List<Profile> listprofile = new ArrayList<Profile>();
	CustomListAdapter adapter;

	public Friends(Context context) {
		// Required empty public constructor
		this.context = context;
	}

	public Friends() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_friends, container, false);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		menu.clear();
		inflater.inflate(R.menu.friends, menu);

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.sbb:
			list = this.getListView();
			DBhandler db = new DBhandler(context);
			if (db.getProfilesCountBG() > 0) {
				populateLisViewBG();

			}
			db.close();

			return true;

		case R.id.sbn:
			list = this.getListView();
			DBhandler db1 = new DBhandler(context);
			if (db1.getProfilesCount() > 0) {
				populateLisView();

			}
			db1.close();
			return true;
		case R.id.refresh:

			if (Utils.isOnline(context)) {

				Intent intent = new Intent(context, Friends_Loading.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
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

							public void onClick(DialogInterface dialog, int id) {

								Intent intent = new Intent(
										Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
							}

						});

				// set negative button: No message

				alertDialogBuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {

								// cancel the alert box and put a Toast to the
								// user

								dialog.cancel();

							}

						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.setCancelable(false);

				// show alert

				alertDialog.show();
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		list = this.getListView();
		DBhandler db = new DBhandler(context);
		if (db.getProfilesCount() > 0) {
			populateLisView();

		}
		db.close();

	}

	private void populateLisView() {
		// TODO Auto-generated method stub
		DBhandler db = new DBhandler(context);

		int profcount = db.getProfilesCount();
		System.out.println("db contact count" + profcount);
		if (profcount > 0) {
			listprofile = db.getAllProfileNames();
			adapter = new CustomListAdapter(context, R.layout.contactlistitem,
					listprofile);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		db.close();
	}

	private void populateLisViewBG() {
		// TODO Auto-generated method stub
		DBhandler db = new DBhandler(context);

		int profcount = db.getProfilesCountBG();
		System.out.println("db contact count" + profcount);
		if (profcount > 0) {
			listprofile = db.getAllProfileNamesBG();

			adapter = new CustomListAdapter(context, R.layout.contactlistitem,
					listprofile);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		db.close();
	}

}

class CustomListAdapter extends ArrayAdapter<String> {

	Context contex1t;
	// ArrayList<String> phoneNumber = new ArrayList<String>();
	private static LayoutInflater inflater = null;
	List<Profile> listprofile = new ArrayList<Profile>();

	/*
	 * public CustomListAdapter(Context context, ArrayList<Profile>
	 * contactNames) { // TODO
	 * 
	 * }
	 */
	public CustomListAdapter(Context context, int resource,
			List<Profile> listprofile) {
		super(context, resource);
		// TODO Auto-generated constructor stub

		this.contex1t = context;
		inflater = (LayoutInflater) contex1t
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.listprofile = listprofile;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listprofile.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		TextView tv;
		ImageView img;
		ImageView bloodgroup;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final Holder holder = new Holder();
		View rowView = convertView;
		rowView = inflater.inflate(R.layout.contactlistitem, null);
		holder.tv = (TextView) rowView.findViewById(R.id.contactname);
		holder.img = (ImageView) rowView.findViewById(R.id.contactimage);
		holder.tv.setText(listprofile.get(position).getName());
		try {
			holder.img.setImageBitmap(Utils.getThumbnail(
					Uri.parse(listprofile.get(position).getPhoto()), contex1t,
					48));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.bloodgroup = (ImageView) rowView
				.findViewById(R.id.bloodgroupimg);
		if (listprofile.get(position).getBloodgroup().equals("A+")) {
			holder.bloodgroup.setImageResource(R.drawable.apositive);
		} else if (listprofile.get(position).getBloodgroup().equals("A-")) {
			holder.bloodgroup.setImageResource(R.drawable.anegative);
		} else if (listprofile.get(position).getBloodgroup().equals("B+")) {
			holder.bloodgroup.setImageResource(R.drawable.bpositive);
		} else if (listprofile.get(position).getBloodgroup().equals("B-")) {
			holder.bloodgroup.setImageResource(R.drawable.bnegative);
		} else if (listprofile.get(position).getBloodgroup().equals("O+")) {
			holder.bloodgroup.setImageResource(R.drawable.opositive);
		} else if (listprofile.get(position).getBloodgroup().equals("O-")) {
			holder.bloodgroup.setImageResource(R.drawable.onegative);
		} else if (listprofile.get(position).getBloodgroup().equals("AB+")) {
			holder.bloodgroup.setImageResource(R.drawable.abpostive);
		} else if (listprofile.get(position).getBloodgroup().equals("AB-")) {
			holder.bloodgroup.setImageResource(R.drawable.abnegative);
		}

		rowView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String phonnumber = listprofile.get(position).getMobileno()
						.toString();

				Intent intent = new Intent(contex1t, ProfileDetails.class);
				intent.putExtra("phoneno", phonnumber);
				contex1t.startActivity(intent);

			}
		});
		return rowView;
	}
}