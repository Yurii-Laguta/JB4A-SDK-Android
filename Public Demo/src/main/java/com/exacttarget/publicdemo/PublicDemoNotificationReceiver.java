package com.exacttarget.publicdemo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.ET_GenericReceiver;

import java.lang.reflect.Field;

/**
 * PublicDemoNotificationReceiver to process custom notification sounds and vibration preferences set by the app user
 * <p/>
 * This receiver extends the ET_GenericReceiver to add notification settings set in the PublicDemoSettingsActivity by the app user
 * <p/>
 * Replace the ET_GenericReceiver BroadcastReceiver in AndroidManifest.xml with this class.  For example:
 * <pre>
 * <code>
 * &lt;manifest ...&gt;
 *   ...
 *   &lt;application ...&gt;
 *     ...
 *     &lt;!-- ET Broadcast Receiver for handling Google/GCM push messages. You can use the ET_GenericReceiver directly, or extend it to customize notifications --&gt;
 *     &lt;receiver android:name="com.exacttarget.publicdemo.PublicDemoNotificationReceiver" android:permission="com.google.android.c2dm.permission.SEND"&gt;
 *       &lt;intent-filter&gt;
 *         &lt;action android:name="com.google.android.c2dm.intent.RECEIVE"/&gt;
 *         &lt;action android:name="com.google.android.c2dm.intent.REGISTRATION"/&gt;
 *
 *         &lt;category android:name="com.exacttarget.demo.etsdkdemo"/&gt;
 *       &lt;/intent-filter&gt;
 *     &lt;/receiver&gt;
 *     &lt;!-- ET Broadcast Receiver for handling Amazon/ADM push messages. ADM requires it's own definition for this receiver.  You cannot combine this definition with the definition for Google/GCM. You can use the ET_GenericReceiver directly, or extend it to customize notifications --&gt;
 *     &lt;receiver android:name="com.exacttarget.publicdemo.PublicDemoNotificationReceiver" android:permission="com.amazon.device.messaging.permission.SEND"&gt;
 *       &lt;intent-filter&gt;
 *         &lt;action android:name="com.amazon.device.messaging.intent.RECEIVE"/&gt;
 *         &lt;action android:name="com.amazon.device.messaging.intent.REGISTRATION"/&gt;
 *
 *         &lt;category android:name="com.exacttarget.demo.etsdkdemo"/&gt;
 *       &lt;/intent-filter&gt;
 *     &lt;/receiver&gt;
 *     ...
 *   &lt;/application&gt;
 *     ...
 * &lt;/manifest&gt;
 * </code>
 * </pre>
 *
 * @author pvandyk
 */
public class PublicDemoNotificationReceiver extends ET_GenericReceiver {

	public static final String TAG = PublicDemoNotificationReceiver.class.getName();

	public PublicDemoNotificationReceiver() {
	}

	@Override
	public Intent setupLaunchIntent(Context context, Bundle payload) {
		//
		// This override will make sure that the launch intent is the only intent that is launched so only 1 is viewable at a time.
		// FLAG_ACTIVITY_CLEAR_TOP will close the current Intent (as well as any Activities stacked on top), and then show this new one
		// (if it is currently open).
		//
		// Remove .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) if you want to have a separate Intent be displayed for your notifications
		// if the app user receives a second notification message while viewing a previous message.
		//
		return super.setupLaunchIntent(context, payload).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	@Override
	public PendingIntent setupLaunchPendingIntent(Context context, Intent launchIntent) {
		return super.setupLaunchPendingIntent(context, launchIntent);
	}

	@Override
	public NotificationCompat.Builder setupNotificationBuilder(Context context, Bundle payload) {
		NotificationCompat.Builder builder = super.setupNotificationBuilder(context, payload);

		if (payload.getString("sound") != null) {
			//
			// only process sounds if sound has been requested.
			// if the payload does not contain indicate the sound type in the payload, then a silent alert was requested
			// This option is specified when creating the message in the Marketing Cloud
			//
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

			// Set the custom ringtone if the app user has requested to have vibrations a custom ringtone
			boolean customizeRingtone = sp.getBoolean(CONSTS.KEY_PREF_USE_CUSTOM_RINGTONE, false);
			if (customizeRingtone) {
				// the app user requested a custom sound to be used for their alerts

				if (ETPush.getLogLevel() <= Log.DEBUG) {
					Log.d(TAG, "setting custom sound...");
				}
				Uri ringtoneUri;
				String ringtone = sp.getString(CONSTS.KEY_PREF_CUSTOM_RINGTONE, null);
				if (ringtone == null) {
					ringtoneUri = Settings.System.DEFAULT_NOTIFICATION_URI;
				}
				else {
					ringtoneUri = Uri.parse(ringtone);
				}
				builder.setSound(ringtoneUri);
			}
			else {
				//
				// NOTE: This PUBLIC DEMO APP has a setting that indicates the user can choose their own notification sound.
				// If they don't choose one, then it says the sound for the notification alert will be a Whistle (custom.mp3).
				//
				// Since the SDK will only play the custom sound found in custom.mp3 if the message created in the Marketing Cloud
				// specifies to use the custom sound, then in order to provide a custom sound whenever the app user does not
				// select their own custom sound, this else block is required.

				// In your own app, only include this code if you want to use the custom sound ALL THE TIME the app user has not
				// selected a custom sound. Otherwise, leave out this else block so that you can specify whether to use the custom
				// sound or the sound selected by the user when you create the message in the Marketing Cloud.
				//
				try {
					String className = context.getPackageName() + ".R$raw";
					Class<?> raw = Class.forName(className);
					Field custom = raw.getDeclaredField("custom");
					Uri customSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + custom.getInt(null));
					builder.setSound(customSound);
				}
				catch (Exception e) {
					// this shouldn't happen if you put custom.mp3 in the raw folder or your res folder in your project
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.d(TAG, "Unable to find custom sound, so allow default device sound to ring (set in SDK)");
					}
				}
			}

			//
			// Set the vibration pattern if the app user has requested to have vibrations turned on
			//
			// NOTE: if an app user indicates that vibrations are not working when they have sound turned on, they have to make
			// sure that they change their device settings to indicate they want to be notified with vibrations as well.
			//
			boolean vibrate = sp.getBoolean(CONSTS.KEY_PREF_VIBRATE, false);
			if (vibrate) {
				if (ETPush.getLogLevel() <= Log.DEBUG) {
					Log.d(TAG, "setting vibration...");
				}
				builder.setVibrate(CONSTS.NOTIFICATION_CUSTOM_VIBRATE_PATTERN);
			}
		}

		return builder;
	}
}
