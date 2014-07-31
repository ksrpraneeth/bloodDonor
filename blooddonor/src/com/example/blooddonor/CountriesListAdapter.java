package com.example.blooddonor;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class CountriesListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	GetCountryName getcountryname = new GetCountryName();
	ArrayList<String> test1 = null;


	public CountriesListAdapter(Context context, String[] values) {
		super(context, R.layout.country_list_item, values);
		
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.country_list_item, parent,
				false);
		TextView textView = (TextView) rowView
				.findViewById(R.id.txtViewCountryName);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.imgViewFlag);

		String[] g = values[position].split(",");
		textView.setTextColor(Color.BLACK);
		textView.setText(getcountryname.GetCountryZipCode(g[1]).trim()+ " "+"("
				+getcountryname.GetCountryZipCode(g[0]).trim()+")");

		String pngName = g[1].trim().toLowerCase();
		imageView.setImageResource(context.getResources().getIdentifier(
				"drawable/" + pngName, null, context.getPackageName()));
		return rowView;
	}

	@SuppressLint("DefaultLocale")
	public String[] filter(String charText, String[] recourseList) {
		charText = charText.toLowerCase();
		test1 = new ArrayList<String>();
		test1.clear();
		// System.out.println("here111");
		if (charText.length() == 0) {
			// System.out.println("here222");
			return recourseList;
		} else {
			// System.out.println("here333");
			for (String wp : recourseList) {

				String[] g = wp.split(",");
				// System.out.println(g[1]);
				Locale loc = new Locale("", g[1]);

				// System.out.println(loc.getDisplayCountry());

				if (loc.getDisplayCountry().trim().toLowerCase()
						.contains(charText)) {
					// System.out.println(loc.getDisplayCountry().trim()
					// .toLowerCase());
					test1.add(wp);
					//System.out.println(wp);
					// System.out.println(loc.getDisplayCountry().trim()
					// .toLowerCase());
				}
			}
			String[] stockArr = new String[test1.size()];
			stockArr = test1.toArray(stockArr);
			return stockArr;
		}
	}

}