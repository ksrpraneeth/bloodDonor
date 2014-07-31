package com.example.blooddonor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import beans.Profile;

import com.droid4you.util.cropimage.CropImage;

import com.example.blooddonor.Utils.Utils;
import com.example.blooddonor.httprequests.Constants;
import com.example.blooddonor.httprequests.HttpPostRequestHandler;
import com.example.blooddonor.interfaces.GcmInterface;
import com.example.blooddonor.interfaces.ResponseInterface;
import com.google.android.gcm.GCMRegistrar;

/**
 * @author KP
 * 
 */
@SuppressLint("SimpleDateFormat")
public class Register extends Activity implements OnClickListener,
		ResponseInterface, GcmInterface {
	private static final int REQUEST_CAMERA = 0;
	EditText name;
	TextView dob, lastdate;
	Spinner bloodgroup;
	Button register;
	ImageView lastcal;
	ImageView dobcal;
	String test1;

	ImageView selectimage;
	SharedprefClass sharedpref;
	private static int RESULT_LOAD_IMAGE = 1;
	final int PIC_CROP = 2;
	String ProfilePhotoPath;
	CheckBox notdonated;
	String dob1;
	ProgressDialog registrationProgress;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	Uri fileUri = null;
	final CharSequence[] itemsDialog = { "Take Photo", "Choose from Library",
			"Cancel" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_register);
		sharedpref = new SharedprefClass(getApplicationContext());
		name = (EditText) findViewById(R.id.name);
		dob = (TextView) findViewById(R.id.dob);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		lastdate = (TextView) findViewById(R.id.lastdate);
		bloodgroup = (Spinner) findViewById(R.id.bloodgroup);
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(this);
		dobcal = (ImageView) findViewById(R.id.dobcal);
		dobcal.setOnClickListener(this);
		lastcal = (ImageView) findViewById(R.id.lastcal);
		lastcal.setOnClickListener(this);
		notdonated = (CheckBox) findViewById(R.id.notdonated);
		notdonated.setOnClickListener(this);
		ArrayAdapter<CharSequence> bloodadapter = ArrayAdapter
				.createFromResource(getApplicationContext(),
						R.array.Bloodgroups, R.layout.simple_spinner);
		bloodadapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

		bloodgroup.setAdapter(new NothingSelectedSpinnerAdapter(bloodadapter,
				R.layout.contact_spinner_row_nothing_selected,
				// / R.layout.contact_spinner_nothing_selected_dropdown, //
				// Optional
				getApplicationContext()));

		name.setText(sharedpref.get("name"));
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		sharedpref.put("inRegister", "true");
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-genemethod stub
				if (s.toString().trim().length() > 1) {
					name.setError(null);
				}

			}
		});

		selectimage = (ImageView) findViewById(R.id.selectimage);
		new LoadProfileImage().execute(sharedpref.get("photo"));
		selectimage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View arg0) {

				AlertDialog.Builder builder = new AlertDialog.Builder(arg0
						.getContext());

				builder.setTitle("Add Photo!");
				builder.setItems(itemsDialog,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int item) {
								// TODO Auto-generated method stub
								if (itemsDialog[item].equals("Take Photo")) {
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									// fileUri = getOutputMediaFileUri(arg0,1);
									// intent.putExtra(MediaStore.EXTRA_OUTPUT,
									// fileUri);
									// System.out.println("uri"+fileUri);
									intent.putExtra("return-data", true);
									startActivityForResult(intent,
											REQUEST_CAMERA);
									// Toast.makeText(getApplicationContext(),"camera clicked",Toast.LENGTH_SHORT).show();
								} else if (itemsDialog[item]
										.equals("Choose from Library")) {
									Intent i = new Intent(
											Intent.ACTION_PICK,
											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

									startActivityForResult(i, RESULT_LOAD_IMAGE);
								} else if (itemsDialog[item].equals("Cancel")) {
									dialog.dismiss();
								}
							}

						});
				builder.show();
			}
		});

	}

	/**
	 * Background Async task to load user profile picture from url
	 * */
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

		ProgressDialog progressBar;

		public LoadProfileImage() {

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar = new ProgressDialog(Register.this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Loading photo from G+");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			progressBar.show();
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			System.out.println("url :" + urldisplay);
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			try {
				System.out.println(result.toString());
				fileUri = Utils.saveToInternalSorage(result, "ProfilePic.jpg",
						getApplicationContext());
				performCrop(fileUri);
				progressBar.dismiss();
			} catch (Exception e) {

				progressBar.dismiss();
				// e.printStackTrace();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	/*
	 * private static Uri getOutputMediaFileUri(View arg0, int type){ return
	 * Uri.fromFile(getOutputMediaFile(arg0,type)); }
	 *//**
	 * Create a File for saving an image or video
	 * 
	 * @param arg0
	 */
	/*
	 * private static File getOutputMediaFile(View arg0, int type){ // To be
	 * safe, you should check that the SDCard is mounted // using
	 * Environment.getExternalStorageState() before doing this. //File file =
	 * new File( arg0.getContext().getCacheDir(), "picture" ); File
	 * mediaStorageDir = new File(arg0.getContext().getCacheDir(),
	 * "MyCameraApp"); // This location works best if you want the created
	 * images to be shared // between applications and persist after your app
	 * has been uninstalled.
	 * 
	 * // Create the storage directory if it does not exist if (!
	 * mediaStorageDir.exists()){ if (! mediaStorageDir.mkdirs()){
	 * Log.d("MyCameraApp", "failed to create directory"); return null; } }
	 * 
	 * // Create a media file name String timeStamp = new
	 * SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); File mediaFile;
	 * if (type == MEDIA_TYPE_IMAGE){ mediaFile = new
	 * File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp +
	 * ".jpg"); } else if(type == MEDIA_TYPE_VIDEO) { mediaFile = new
	 * File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp +
	 * ".mp4"); } else { return null; }
	 * 
	 * return mediaFile; }
	 */

	// ///////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			System.out.println("selsected image" + selectedImage.toString());

			// ProfilePhotoPath = Utils.getPathForImageUri(
			// getApplicationContext(), selectedImage);

			performCrop(selectedImage);

			// imageView
			// .setImageBitmap(BitmapFactory.decodeFile(ProfilePhotoPath));

		}

		else if (requestCode == REQUEST_CAMERA) {
			System.out.println("entered onactivity result");
			try {
				// ProfilePhotoPath = fileUri.getPath();
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				fileUri = Utils.saveToInternalSorage(thumbnail,
						"ProfilePic.jpg", getApplicationContext());
				performCrop(fileUri);
			} catch (Exception e) {
				Toast.makeText(this, "Kindly take a picture",
						Toast.LENGTH_SHORT).show();
			}

		}

		else if (requestCode == PIC_CROP && resultCode == RESULT_OK
				&& null != data) {
			Bundle extras = data.getExtras();
			// get the cropped bitmap
			Uri thePic = extras.getParcelable("uri");
			ProfilePhotoPath = thePic.getPath();

			System.out.println("uri" + thePic);
			// retrieve a reference to the ImageView
			// ImageView picView = (ImageView) findViewById(R.id.picture);
			// display the returned cropped image
			// GraphicsUtil graphicUtil = new GraphicsUtil();
			// picView.setImageBitmap(graphicUtil.getRoundedShape(thePic,(float)1.5,92));
			ImageView imageView = (ImageView) findViewById(R.id.selectimage);
			imageView.setImageDrawable(null);
			// imageView.setImageBitmap(graphicUtil.getRoundedShape(thePic));
			imageView.setImageURI(thePic);
			// File removefile=new File(fileUri.getPath());
			// removefile.delete();

		}

	}

	private void performCrop(Uri selectedImage) {
		// take care of exceptions
		// System.out.println("performCrop");
		// try {
		// call the standard crop action intent (the user device may not
		// support it)
		/*
		 * Intent cropIntent = new Intent("com.android.camera.action.CROP"); //
		 * indicate image type and Uri cropIntent.setDataAndType(selectedImage,
		 * "image/*"); // set crop properties cropIntent.putExtra("crop",
		 * "true"); // indicate aspect of desired crop
		 * cropIntent.putExtra("aspectX", 1); cropIntent.putExtra("aspectY", 1);
		 * // indicate output X and Y cropIntent.putExtra("outputX", 256);
		 * cropIntent.putExtra("outputY", 256); // retrieve data on return
		 * cropIntent.putExtra("return-data", true); // start the activity - we
		 * handle returning in onActivityResult
		 * startActivityForResult(cropIntent, PIC_CROP);
		 */

		Intent intent = new Intent(this, CropImage.class);
		System.out.println(selectedImage);
		intent.putExtra("image-path", selectedImage);
		intent.putExtra("image-path-string", selectedImage.toString());
		intent.setDataAndType(selectedImage, "image/*");
		intent.putExtra("scale", true);
		// intent.putExtra("circleCrop", "true"); // for circular crop
		intent.putExtra("return-data", false);
		// System.out.println("here1");
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
		startActivityForResult(intent, PIC_CROP);

		// }
		// respond to users whose devices do not support the crop action
		/*
		 * catch (ActivityNotFoundException anfe) { // display an error message
		 * String errorMessage =
		 * "Whoops - your device doesn't support the crop action!"; Toast toast
		 * = Toast .makeText(this, errorMessage, Toast.LENGTH_SHORT);
		 * toast.show(); }
		 */
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.register) {
			if (name.getText().toString().trim().length() < 1) {

				name.setError("Please Enter Name.");
			} else if (dob.getText().toString().trim().length() < 1) {
				Toast.makeText(this, "Enter Date of Birth", Toast.LENGTH_SHORT)
						.show();

			} else if (lastdate.getText().toString().trim().length() < 1) {
				Toast.makeText(this, "Enter Last date of donation",
						Toast.LENGTH_SHORT).show();
			} else if (bloodgroup.getSelectedItemPosition() == 0) {
				Toast.makeText(this, "Enter Bloodgroup", Toast.LENGTH_SHORT)
						.show();
			}

			else {

				sharedpref.put("username", name.getText().toString());
				sharedpref.put("dateofbirth", dob.getText().toString());
				System.out.println("lastdate" + lastdate.getText().toString());
				if (lastdate.getText().toString().equals("Not Donated")) {
					sharedpref.put("lastdonate", "9999-12-31");
				}

				else {

					sharedpref.put("lastdonate", lastdate.getText().toString());
				}
				sharedpref.put("bloodgroup", bloodgroup.getSelectedItem()
						.toString());
				DBhandler dbofme = new DBhandler(this);
				Profile profiledet = new Profile();
				profiledet.setName(name.getText().toString());
				profiledet.setDob(dob.getText().toString());
				if (lastdate.getText().toString().equals("Not Donated")) {
					profiledet.setLdd("9999-12-31");
				} else {
					profiledet.setLdd(lastdate.getText().toString());
				}

				profiledet.setBloodgroup(bloodgroup.getSelectedItem()
						.toString());
				dbofme.addMeProfile(profiledet);
				dbofme.close();
				System.out.println("profilepic path" + ProfilePhotoPath);
				if (ProfilePhotoPath != null)
					sharedpref.put("profilephotopath", ProfilePhotoPath);
				sharedpref.put("splash", "1");
				if (lastdate.getText().toString().equals("Not Donated")) {
					sharedpref.put("ldd", "9999-12-31");
				} else {
					sharedpref.put("ldd", lastdate.getText().toString());
				}
				sharedpref.put("dob", dob.getText().toString());
				GCMRegistrar.register(getApplicationContext(),
						com.example.blooddonor.GCMIntentService.SENDER_ID);
				registrationProgress = new ProgressDialog(Register.this);
				registrationProgress.setCancelable(false);
				registrationProgress.setMessage("Please wait....");
				registrationProgress
						.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				registrationProgress.show();

			}

		} else if (v.getId() == R.id.dobcal) {

			calendarfrom(v);

		} else if (v.getId() == R.id.lastcal) {
			if (dob.getText().toString().trim().length() < 1) {
				Toast.makeText(this, "First Enter Date of Birth",
						Toast.LENGTH_SHORT).show();
			} else {
				calendarlast(v);
			}
		} else if (v.getId() == R.id.notdonated) {
			lastdate.setText("Not Donated");
		}

	}

	public void calendarfrom(View v) {
		Calendar c;

		// Process to get Current Date
		c = Calendar.getInstance();
		int mYear, mMonth, mDay;
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						dob.setText(year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth);

					}
				}, mYear, mMonth, mDay);
		long valid = (long) (System.currentTimeMillis() - 568024668000.0);
		dpd.getDatePicker().setMaxDate(valid);
		dpd.show();

	}

	public void calendarlast(View v) {
		Calendar c;

		// Process to get Current Date
		c = Calendar.getInstance();
		int mYear, mMonth, mDay;
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						lastdate.setText(year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth);

					}
				}, mYear, mMonth, mDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		try {
			Date date;

			dob1 = dob.getText().toString();
			date = sdf.parse(dob1);
			System.out.println(date.getTime());
			long valid = (long) (Long.valueOf(date.getTime()) + 568024668000.0);
			dpd.getDatePicker().setMinDate(valid);
			dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dpd.show();

	}

	@Override
	public void getResponse(Context context, String json) {
		// TODO Auto-generated method stub

		System.out.println("Register response :" + json);
		System.out.println("context " + this);
		if (json.contains("OK") || json.contains("Registered")) {
			try {

				Intent intent = new Intent(context, Friends_Loading.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
			}

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry Unable to Register your account!",
					Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onReceiveResponse(String Response, Context context) {
		// // TODO Auto-generated method stub

		JSONObject obj = new JSONObject();
		SharedprefClass pref = new SharedprefClass(context);
		pref.put("regID", Response);
		try {
			obj.put("firstname", pref.get("username"));
			obj.put("phonenumber", pref.get("phonenumber_me"));
			obj.put("lastname", "");
			obj.put("gender", pref.get("gender"));
			obj.put("DOB", pref.get("dob"));
			System.out.println("dob" + dob1);
			obj.put("LOD", pref.get("ldd"));
			obj.put("email", pref.get("email"));
			obj.put("bloodgroup", pref.get("bloodgroup"));
			obj.put("countrycode", pref.get("code1"));
			obj.put("regID", pref.get("regID"));
			obj.put("lat", pref.get("lat"));
			obj.put("long", pref.get("long"));

			if (pref.get("profilephotopath").length() > 1) {
				System.out
						.println("Photo path:" + pref.get("profilephotopath"));
				String ecodedimage = Utils
						.convert(pref.get("profilephotopath"));

				obj.put("photo", ecodedimage);
				byte[] b;
				try {
					b = ecodedimage.getBytes("UTF-8");
					System.out.println("STRING SIZE" + b.length);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				obj.put("photo", "");
			}

			System.out.println("Response from onreceive" + obj);
			HttpPostRequestHandler handler = new HttpPostRequestHandler(
					Constants.Url + Constants.Registration_url, obj,
					"registration", this, context);
			handler.execute();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
