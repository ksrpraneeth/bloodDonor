package com.example.blooddonor.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.EditText;

public class Utils {
	static boolean selection;

	public static String getPathForImageUri(Context context, Uri uri) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(uri, filePathColumn,
				null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;

	}

	public static boolean ShowAlertDialogue(Context context, String title,
			String message) {

		return selection;
	}

	public static Bitmap getFacebookPhoto(Context context, String phoneNumber) {
		Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Uri photoUri = null;
		ContentResolver cr = context.getContentResolver();
		Cursor contact = cr.query(phoneUri,
				new String[] { ContactsContract.Contacts._ID }, null, null,
				null);

		if (contact.moveToFirst()) {
			long userId = contact.getLong(contact
					.getColumnIndex(ContactsContract.Contacts._ID));
			photoUri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, userId);

		} else {
			Bitmap defaultPhoto = BitmapFactory.decodeResource(
					context.getResources(),
					android.R.drawable.ic_menu_report_image);
			return defaultPhoto;
		}
		if (photoUri != null) {
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(cr, photoUri);
			if (input != null) {
				return BitmapFactory.decodeStream(input);
			}
		} else {
			Bitmap defaultPhoto = BitmapFactory.decodeResource(
					context.getResources(),
					android.R.drawable.ic_menu_report_image);
			return defaultPhoto;
		}
		Bitmap defaultPhoto = BitmapFactory
				.decodeResource(context.getResources(),
						android.R.drawable.ic_menu_report_image);
		return defaultPhoto;
	}

	public static void showError(EditText edittext, String message) {
		edittext.setError(message);
	}

	public static void removeErrorListener(final EditText edittext) {
		edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				edittext.setError(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static boolean isOnline(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static String convert(String photopath) {
		Bitmap bm = BitmapFactory.decodeFile(photopath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap
															// object
		byte[] b = baos.toByteArray();
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

		return encodedImage;
	}

	public static Bitmap convertStringToBitmap(String encodedString)
			throws IOException {
		byte[] base64arr = Base64.decode(encodedString, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(base64arr, 0, base64arr.length);
	}

	public static Uri saveToInternalSorage(Bitmap bitmapImage,
			String photoname, Context context) {
		ContextWrapper cw = new ContextWrapper(context);
		// path to /data/data/yourapp/app_data/imageDir
		Uri mSaveUri = null;
		File directory = cw.getDir("blooddonor", Context.MODE_PRIVATE);
		// Create imageDir
		File mypath = new File(directory, photoname);

		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);

			// Use the compress method on the BitMap object to write image to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
			mSaveUri = Uri.fromFile(new File(mypath.getAbsolutePath()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mSaveUri;
	}

	public static Bitmap getThumbnail(Uri uri, Context context,
			int thumbnailSize) throws FileNotFoundException, IOException {
		InputStream input = context.getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1)
				|| (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;

		double ratio = (originalSize > thumbnailSize ? (originalSize / thumbnailSize)
				: 1.0);

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}
}
