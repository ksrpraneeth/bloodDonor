package com.example.blooddonor;

import java.util.ArrayList;

import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpGetRequestHandler;
import com.example.blooddonor.interfaces.ResponseInterface;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class Home extends ListFragment implements OnItemClickListener,
		ResponseInterface {
	ArrayList<String> array = new ArrayList<String>();
	ListView list;
	PackageManager pm;
	Context context;
	boolean app_installed;
	boolean installed;
	ProgressDialog progressBar;

	public Home(Context context) {
		// Required empty public constructor
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		inflater.inflate(R.menu.home_menu, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.home_share:
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = "BloodDonor is going to be released soon";
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Subject Here");
			sharingIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			startActivity(Intent.createChooser(sharingIntent, "Share via"));
			return true;
		case R.id.home_settings:
			Intent settingsIntent = new Intent(getActivity(),
					settingActivity.class);
			startActivity(settingsIntent);
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		list = getListView();

		String[] prgmNameList = { "Requirements", "Question of the Day",
				"Calculator", "About us" };

		list.setAdapter(new CustomList(getActivity().getApplicationContext(),
				prgmNameList));
		list.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:

			break;
		case 1:
			if (Utils.isOnline(context)) {

				HttpGetRequestHandler getrequest = new HttpGetRequestHandler(
						getActivity(), this, Constants.Url
								+ Constants.Questions);
				getrequest.execute();
				progressBar = new ProgressDialog(getActivity());
				progressBar.setCancelable(true);
				progressBar.setMessage("Please wait getting question...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				progressBar.show();
			} else {
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

			break;
		case 2:
			Intent ibmi = new Intent(getActivity(), Bmicalc.class);
			startActivity(ibmi);
		default:
			break;
		}
	}

	@Override
	public void getResponse(Context context, String json) {
		// TODO Auto-generated method stub
		progressBar.dismiss();
		Intent i = new Intent(getActivity(), Question_of_the_day.class);
		i.putExtra("question", json);
		startActivity(i);
	}
}

class CustomList extends BaseAdapter {

	Context context;
	String[] prgmlist;

	private static LayoutInflater inflater = null;

	public CustomList(Context context, String[] prgmlist) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.prgmlist = prgmlist;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		TextView tv;
		ImageView img;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Holder holder = new Holder();
		View rowView = convertView;
		rowView = inflater.inflate(R.layout.homemenuitem, null);
		holder.tv = (TextView) rowView.findViewById(R.id.textView1);
		holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
		holder.tv.setText(prgmlist[position]);

		return rowView;
	}

}
