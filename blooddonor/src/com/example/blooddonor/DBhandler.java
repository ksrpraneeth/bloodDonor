package com.example.blooddonor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import beans.Profile;

public class DBhandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	Context context;
	private static final int DATABASE_VERSION = 63;

	// Database Name
	private static final String DATABASE_NAME = "blooddatabase.db";

	private static final String TABLE_PROFILE = "profiles";
	private static final String TABLE_ME_PROFILE = "userprofile";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phonenumber";
	private static final String KEY_ISDCODE = "isdcode";
	private static final String KEY_DOB = "dateofbirth";
	private static final String KEY_LDD = "lastdateofdonation";
	private static final String KEY_BLOOD_GROUP = "bloodgroup";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_GENDER = "gender";
	private static final String KEY_PHOTO = "photopath";

	public DBhandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + " ("
				+ KEY_NAME + " TEXT NOT NULL, " + "'" + KEY_PH_NO + "'"
				+ " TEXT PRIMARY KEY," + KEY_ISDCODE + " TEXT NOT NULL,"
				+ KEY_GENDER + " TEXT," + KEY_DOB + " TEXT," + KEY_LDD
				+ " TEXT," + KEY_BLOOD_GROUP + " TEXT," + KEY_EMAIL + " TEXT,"
				+ KEY_PHOTO + " TEXT" + ")";
		String CREATE_ME_PROFILE = "CREATE TABLE " + TABLE_ME_PROFILE + " ("
				+ KEY_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
				+ KEY_NAME + " TEXT," + KEY_BLOOD_GROUP + " TEXT," + KEY_DOB
				+ " TEXT," + KEY_LDD + " TEXT" + ") ";
		db.execSQL(CREATE_PROFILE_TABLE);
		db.execSQL(CREATE_ME_PROFILE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ME_PROFILE);
		// Create tables again
		onCreate(db);
	}

	public boolean addProfiles(List<Profile> profilelist) throws SQLException {
		SQLiteDatabase db = null;

		db = this.getWritableDatabase();
		for (int i = 0; i < profilelist.size(); i++) {
			Profile profile = profilelist.get(i);
			ContentValues values = new ContentValues();
			values.put(KEY_NAME, profile.getName());
			values.put(KEY_PH_NO, profile.getMobileno()); // Contact Name
			values.put(KEY_DOB, profile.getDob()); // Contact Phone
			values.put(KEY_LDD, profile.getLdd());
			values.put(KEY_BLOOD_GROUP, profile.getBloodgroup());
			values.put(KEY_EMAIL, profile.getEmail());
			values.put(KEY_ISDCODE, profile.getIsd());
			values.put(KEY_GENDER, profile.getGender());
			values.put(KEY_PHOTO, profile.getPhoto());
			// Contact Email
			// Inserting Row
			db.insert(TABLE_PROFILE, null, values);

		}

		db.close(); // Closing database connection
		return true;

	}

	public void dropProfiles() throws SQLException {
		SQLiteDatabase db = null;

		db = this.getWritableDatabase();
		String dropQuery = "DROP TABLE IF EXISTS " + TABLE_PROFILE;
		db.rawQuery(dropQuery, null);
		db.close();

	}

	public boolean addMeProfile(Profile profile) throws SQLException {
		SQLiteDatabase db = null;
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, profile.getName());
		values.put(KEY_BLOOD_GROUP, profile.getBloodgroup());
		values.put(KEY_DOB, profile.getDob());
		values.put(KEY_LDD, profile.getLdd());
		db.insert(TABLE_ME_PROFILE, null, values);
		return true;
	}

	public int updateMeProfile(Profile profile, String Id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_BLOOD_GROUP, profile.getBloodgroup());
		values.put(KEY_NAME, profile.getName());
		values.put(KEY_DOB, profile.getDob());
		values.put(KEY_LDD, profile.getLdd());
		// updating row
		return db.update(TABLE_ME_PROFILE, values, KEY_ID + " = ?",
				new String[] { Id });
	}

	// Getting All Contacts
	public ArrayList<Profile> getAllProfilesForphonno(String phonenumber)
			throws SQLException {
		ArrayList<Profile> profileList = new ArrayList<Profile>();
		// Select All Query
		System.out.println("phonenumber from selectquery: " + phonenumber);
		String selectQuery = "SELECT  * FROM " + TABLE_PROFILE + " WHERE "
				+ KEY_PH_NO + " = " + "'" + phonenumber + "'";
		System.out.println(phonenumber);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Profile profile = new Profile();
				profile.setName(cursor.getString(0).toString());
				profile.setMobileno(cursor.getString(1));
				profile.setDob(cursor.getString(4));
				profile.setLdd(cursor.getString(5));
				profile.setBloodgroup(cursor.getString(6));
				profile.setEmail(cursor.getString(7));
				profile.setGender(cursor.getString(3));
				profile.setIsd(cursor.getString(2));
				profile.setPhoto(cursor.getString(8));
				
				profileList.add(profile);

			} while (cursor.moveToNext());

		}
		db.close();
		// return contact list
		return profileList;

	}

	/* public ArrayList<Profile> */
	public List<Profile> getAllProfileNames() throws SQLException {
		List<Profile> profilelist = new ArrayList<Profile>();
		String selectQuery = "SELECT " + KEY_NAME + "," + KEY_PH_NO + ","
				+ KEY_BLOOD_GROUP + "," + KEY_PHOTO + " FROM " + TABLE_PROFILE
				+ " ORDER BY " + KEY_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Profile profile = new Profile();
				profile.setName(cursor.getString(0));
				profile.setMobileno(cursor.getString(1));
				profile.setBloodgroup(cursor.getString(2));
				profile.setPhoto(cursor.getString(3));
				profilelist.add(profile);
			} while (cursor.moveToNext());
		}
		return profilelist;

	}

	public List<Profile> getAllProfileNamesBG() throws SQLException {
		List<Profile> profilelist = new ArrayList<Profile>();
		String selectQuery = "SELECT " + KEY_NAME + "," + KEY_PH_NO + ","
				+ KEY_BLOOD_GROUP + "," + KEY_PHOTO + " FROM " + TABLE_PROFILE
				+ " ORDER BY " + KEY_BLOOD_GROUP;
		System.out.println(selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Profile profile = new Profile();
				profile.setName(cursor.getString(0));
				profile.setMobileno(cursor.getString(1));
				profile.setBloodgroup(cursor.getString(2));
				profile.setPhoto(cursor.getString(3));
				profilelist.add(profile);
			} while (cursor.moveToNext());
		}
		return profilelist;

	}

	public int getProfilesCount() throws SQLException {
		String countQuery = "SELECT * FROM " + TABLE_PROFILE;
		SQLiteDatabase db = DBhandler.this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return count;

	}

	public int getProfilesCountBG() throws SQLException {
		String countQuery = "SELECT * FROM " + TABLE_PROFILE + " ORDER BY "
				+ KEY_BLOOD_GROUP;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return count;

	}
	
	

	

}
