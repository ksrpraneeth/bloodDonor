package com.example.blooddonor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import beans.Contact;

import com.example.blooddonor.interfaces.ContactInterface;

class ContactsFetch extends AsyncTask<String, String, String> {
	String phone;
	String name;
	Context context;
	Boolean completed = false;
	List<Contact> contactslist = new ArrayList<Contact>();
	ContactInterface contactInterface;

	public ContactsFetch(Context context, ContactInterface activity) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.contactInterface = activity;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		completed = false;
	}

	@Override
	protected String doInBackground(String... params) {

		readContacts();
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		ContactInterface c = contactInterface;
		c.getContacts(context, contactslist);
	}

	public void readContacts() throws SQLiteConstraintException {
		ContentResolver cr = context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		if (cur.getCount() > 0) {

			while (cur.moveToNext()) {
				Contact c = new Contact();
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					// System.out.println("name : " + name + ", ID : " + id);
					c.set_name(name);
					// get the phone number
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						phone = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						// System.out.println("phone" + phone);
						c.set_phone_number(phone);
					}
					contactslist.add(c);
					pCur.close();

				}

			}

		}
	}

}