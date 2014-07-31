package com.example.blooddonor;

import java.io.File;
import com.example.blooddonor.Utils.ChangeDateTime;
import java.io.FileOutputStream;

import com.droid4you.util.cropimage.CropImage;
import com.example.blooddonor.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class Me extends Fragment {
	TextView name, dateofbirth, bloodgroup, lastdate;
	SharedprefClass sharedpref;
	ImageView profilepic;
	private static final int PIC_CROP = 2;
	private static final int REQUEST_CODE_CROP_IMAGE = 1;
	private static int RESULT_LOAD_IMAGE = 1;
	String ProfilePhotoPath;
	ImageView imageView;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int REQUEST_CAMERA = 0;
	final CharSequence[] itemsDialog = { "Take Photo", "Choose from Library",
			"Cancel" };
	boolean startedFirst;
	View v;
	Uri fileUri = null;

	public Me() {

		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_me, container, false);
		setHasOptionsMenu(true);
		System.out.println("On create");
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		menu.clear();
		inflater.inflate(R.menu.menu_me, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.edit:
			Intent starteditactivity = new Intent(getActivity(), Me_edit.class);
			starteditactivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(starteditactivity);
			return true;

		case R.id.editpic:
			Toast.makeText(getActivity(),
					"click on the profile pic to change it", Toast.LENGTH_SHORT)
					.show();
			profilepic.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					AlertDialog.Builder builder = new AlertDialog.Builder(arg0
							.getContext());

					builder.setTitle("Add Photo!");
					builder.setItems(itemsDialog,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int item) {
									// TODO Auto-generated method stub
									if (itemsDialog[item].equals("Take Photo")) {
										Intent intent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);

										intent.putExtra("return-data", true);
										startActivityForResult(intent,
												REQUEST_CAMERA);

									} else if (itemsDialog[item]
											.equals("Choose from Library")) {
										Intent i = new Intent(
												Intent.ACTION_PICK,
												android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

										startActivityForResult(i,
												RESULT_LOAD_IMAGE);
									} else if (itemsDialog[item]
											.equals("Cancel")) {
										dialog.dismiss();
									}
								}

							});
					builder.show();

				}

			});

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public Uri saveToInternalSorage(Bitmap bitmapImage) {
		ContextWrapper cw = new ContextWrapper(getActivity());
		// path to /data/data/yourapp/app_data/imageDir
		Uri mSaveUri = null;
		File directory = cw.getDir("blooddonor", Context.MODE_PRIVATE);
		// Create imageDir
		File mypath = new File(directory, "camcap.jpg");

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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		name = (TextView) v.findViewById(R.id.name1);
		dateofbirth = (TextView) v.findViewById(R.id.dob1);
		lastdate = (TextView) v.findViewById(R.id.lod1);
		bloodgroup = (TextView) v.findViewById(R.id.bloodgroup1);
		profilepic = (ImageView) v.findViewById(R.id.me_profilepic);
		System.out.println("On activity created");
		sharedpref = new SharedprefClass(getActivity().getApplicationContext());
		String ussername = sharedpref.get("username");
		String dob = sharedpref.get("dateofbirth");
		String lastdate1 = sharedpref.get("lastdonate");
		String bloodgroup1 = sharedpref.get("bloodgroup");
		System.out.println("bloodgroup" + bloodgroup1);
		System.out.println(" user" + ussername + dob + lastdate1 + bloodgroup);
		profilepic.setImageBitmap(BitmapFactory.decodeFile(sharedpref
				.get("profilephotopath")));
		name.setText(ussername);
		bloodgroup.setText(bloodgroup1);
		dateofbirth.setText(ChangeDateTime.parseDate(dob));
		System.out.println("Last date1" + '\n' + lastdate1);
		
		
		if (lastdate1.toString().equals("9999-12-31")) {
			System.out.println("in not donated");
			lastdate.setText("Not Donated");
		}
		
		else {
			System.out.println("IN Donated");
			lastdate.setText(ChangeDateTime.parseDate(lastdate1));
		}
		
		

	}

	// Inflate the layout for this fragment

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE
				&& resultCode == Activity.RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			System.out.println("selsected image" + selectedImage.toString());

			performCrop(selectedImage);

		}

		else if (requestCode == REQUEST_CAMERA) {
			try {
				System.out.println("entered onactivity result");
				// ProfilePhotoPath = fileUri.getPath();
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				fileUri = saveToInternalSorage(thumbnail);
				performCrop(fileUri);
			} catch (Exception e) {
				Toast.makeText(getActivity().getApplicationContext(),
						"kindly take a picture", Toast.LENGTH_SHORT).show();
			}

		}

		else if (requestCode == PIC_CROP && resultCode == Activity.RESULT_OK
				&& null != data) {
			Bundle extras = data.getExtras();
			// get the cropped bitmap
			Uri thePic = extras.getParcelable("uri");
			ProfilePhotoPath = thePic.getPath();

			System.out.println("uri" + thePic);

			ImageView imageView = (ImageView) v
					.findViewById(R.id.me_profilepic);

			imageView.setImageDrawable(null);

			imageView.setImageURI(thePic);

		}

	}

	private void performCrop(Uri selectedImage) {

		Intent intent = new Intent(getActivity(), CropImage.class);
		System.out.println(selectedImage);
		intent.putExtra("image-path", selectedImage);
		intent.putExtra("image-path-string", selectedImage.toString());
		intent.setDataAndType(selectedImage, "image/*");
		intent.putExtra("scale", true);
		// intent.putExtra("circleCrop", "true"); // for circular crop
		intent.putExtra("return-data", false);

		startActivityForResult(intent, PIC_CROP);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("Me started");
		String ussername = sharedpref.get("username");
		String dob = sharedpref.get("dateofbirth");
		String lastdate1 = sharedpref.get("lastdonate");
		String bloodgroup1 = sharedpref.get("bloodgroup");
		System.out.println("bloodgroup" + bloodgroup1);
		System.out.println(" user" + ussername + dob + lastdate1 + bloodgroup);
		profilepic.setImageBitmap(BitmapFactory.decodeFile(sharedpref
				.get("profilephotopath")));
		name.setText(ussername);
		bloodgroup.setText(bloodgroup1);
		dateofbirth.setText(ChangeDateTime.parseDate(dob));
		if (lastdate1.toString().equals("9999-12-31")) {
			System.out.println("in not donated");
			lastdate.setText("Not Donated");
		}
		
		else {
			System.out.println("IN Donated");
			lastdate.setText(ChangeDateTime.parseDate(lastdate1));
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("Me resumed");
	}

}
