package com.example.blooddonor;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.blooddonor.interfaces.GcmInterface;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCM Tutorial::Service";
	NotificationCompat.Builder mBuilder;
	NotificationManager mNotificationManager;

	// Use your PROJECT ID from Google API into SENDER_ID
	public static final String SENDER_ID = "1024949539137";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		GcmInterface gcm = new Register();
		Log.i(TAG, "onRegistered: registrationId=" + registrationId);
		gcm.onReceiveResponse(registrationId, context);

	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {

		Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent data) {
		String message;
		// Message from PHP server
		message = data.getStringExtra("message");
		// Open a new activity called GCMMessageView
		Intent intent = new Intent(this, GCMMessageView.class);
		// Pass data to the new activity
		System.out.println("Gcm message " + message);
		intent.putExtra("message", message);
		// Starts the activity on notification click
		PendingIntent pIntent = PendingIntent
				.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
						| PendingIntent.FLAG_ONE_SHOT);
		// Create the notification with a notification builder
		Notification notification = new Notification.Builder(this)
				.setSmallIcon(R.drawable.logo)
				.setWhen(System.currentTimeMillis())
				.setContentTitle("Blood Donor")
				.setContentText("Blood donor is needed!")
				.setContentIntent(pIntent).getNotification();

		// Remove the notification on click
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		if (sharedPrefs.getBoolean("prefSendReport", false))
			notification.defaults = Notification.DEFAULT_SOUND;
		if (sharedPrefs.getBoolean("prefSettings", false)) {

			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			manager.notify(R.string.app_name, notification);
		}

		{
			// Wake Android Device when notification received
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			final PowerManager.WakeLock mWakelock = pm.newWakeLock(
					PowerManager.FULL_WAKE_LOCK
							| PowerManager.ACQUIRE_CAUSES_WAKEUP, "GCM_PUSH");
			mWakelock.acquire();

			// Timer before putting Android Device to sleep mode.
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					mWakelock.release();
				}
			};
			timer.schedule(task, 5000);
		}

	}

	@Override
	protected void onError(Context arg0, String errorId) {

		Log.e(TAG, "onError: errorId=" + errorId);
	}

}