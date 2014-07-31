package com.example.blooddonor.interfaces;

import java.util.List;

import android.content.Context;
import beans.Contact;

public interface ContactInterface {
	public void getContacts(Context context, List<Contact> contactslist);
}
