package com.example.blooddonor.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeDateTime {
	
	public static String parseDateToddMMyyyy(String time) {
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "dd-MMM-yyyy h:mm a";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		Date date = null;
		String str = null;
		try {
			date = inputFormat.parse(time);
			str = outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static String parseDate(String date) {
		String inputPattern = "yyyy-MM-dd";
		String outputPattern = "dd-MMM-yyyy";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		Date date1 = null;
		String str = null;
		try {
			date1 = inputFormat.parse(date);
			str = outputFormat.format(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
}
