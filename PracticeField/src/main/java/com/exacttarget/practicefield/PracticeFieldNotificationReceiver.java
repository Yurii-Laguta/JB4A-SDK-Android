package com.exacttarget.practicefield;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.ET_GenericReceiver;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * PracticeFieldNotificationReceiver to process custom notification sounds and vibration preferences set by the app user
 * <p/>
 * This receiver extends the ET_GenericReceiver to add notification settings set in the PracticeFieldSettingsActivity by the app user
 * <p/>
 * Replace the ET_GenericReceiver BroadcastReceiver in AndroidManifest.xml with this class.  For example:
 * <pre>
 * <code>
 * &lt;manifest ...&gt;
 *   ...
 *   &lt;application ...&gt;
 *     ...
 *     &lt;!-- ET Broadcast Receiver for handling Google/GCM push messages. You can use the ET_GenericReceiver directly, or extend it to customize notifications --&gt;
 *     &lt;receiver android:name="com.exacttarget.publicdemo.PracticeFieldNotificationReceiver" android:permission="com.google.android.c2dm.permission.SEND"&gt;
 *       &lt;intent-filter&gt;
 *         &lt;action android:name="com.google.android.c2dm.intent.RECEIVE"/&gt;
 *         &lt;action android:name="com.google.android.c2dm.intent.REGISTRATION"/&gt;
 *
 *         &lt;category android:name="com.exacttarget.demo.etsdkdemo"/&gt;
 *       &lt;/intent-filter&gt;
 *     &lt;/receiver&gt;
 *     &lt;!-- ET Broadcast Receiver for handling Amazon/ADM push messages. ADM requires it's own definition for this receiver.  You cannot combine this definition with the definition for Google/GCM. You can use the ET_GenericReceiver directly, or extend it to customize notifications --&gt;
 *     &lt;receiver android:name="com.exacttarget.publicdemo.PracticeFieldNotificationReceiver" android:permission="com.amazon.device.messaging.permission.SEND"&gt;
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
public class PracticeFieldNotificationReceiver extends ET_GenericReceiver {

	public static final String TAG = PracticeFieldNotificationReceiver.class.getName();

	public PracticeFieldNotificationReceiver() {
	}

	@Override
	public Intent setupLaunchIntent(Context context, Bundle payload) {
		//
		// save the push notification received info for later display
		//
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(PracticeFieldApp.context());
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putLong(CONSTS.KEY_PUSH_RECEIVED_DATE, Calendar.getInstance().getTimeInMillis());

		HashMap<String, String> map = new HashMap<String, String>();

		JSONObject jo = new JSONObject();
		try {
			for (String key : payload.keySet()) {
				jo.put(key, payload.get(key));
			}
		}
		catch (Exception e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		spEditor.putString(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, jo.toString());

		spEditor.commit();

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
}
