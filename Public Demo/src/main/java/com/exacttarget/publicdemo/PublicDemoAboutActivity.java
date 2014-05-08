package com.exacttarget.publicdemo;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLocationManager;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.util.EventBus;

/**
 * PublicDemoAboutActivity will display information about the PublicDemo App.
 *
 * This activity extends Activity to provide key information about the app that is running.
 * *
 * @author pvandyk
 */

public class PublicDemoAboutActivity extends Activity {

	private int currentPage = CONSTS.ABOUT_ACTIVITY;

	private static final String TAG = PublicDemoAboutActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_demo_about_layout);

		getActionBar().setTitle(R.string.public_demo_about_activity_title);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CONSTS.KEY_CURRENT_PAGE, currentPage);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			// Let ExactTarget know when each activity is resumed
			ETPush.pushManager().activityResumed(this);
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		prepareDisplay();
	}

	@Override
	protected void onPause() {
		super.onPause();

		try {
			// Let ExactTarget know when each activity paused
			ETPush.pushManager().activityPaused(this);
		}
		catch (Exception e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.global_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Utils.prepareMenu(currentPage, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Boolean result = Utils.selectMenuItem(this, currentPage, item);
		return result != null ? result : super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void prepareDisplay() {
		StringBuilder sb = new StringBuilder();

		// APP TITLE
		sb.append("<b>");
		sb.append(getString(R.string.app_name));
		sb.append("</b>");

		// APP VERSION
		try {
			PackageInfo packageInfo = PublicDemoApp.context().getPackageManager()
					.getPackageInfo(PublicDemoApp.context().getPackageName(), 0);
			sb.append("<br/>");
			sb.append("<i>App Version:</i> ");
			sb.append(packageInfo.versionName);
		}
		catch (Exception e) {
		}

		// SDK VERSION
		sb.append("<br/>");
		sb.append("<i>SDK Version:</i>  ");
		sb.append(ETPush.ETPushSDKVersionString);

		// LOG LEVEL
		sb.append("<br/>");
		sb.append("<i>Log Level:</i>  ");
		sb.append(getLoglevelText(ETPush.getLogLevel()));

		// PUSH SETTINGS
		sb.append("<br/>");
		sb.append("<br/>");
		sb.append("<b>Push Settings</b> ");

		ETPush pushManager = null;

		try {
			pushManager = ETPush.pushManager();
		}
		catch (Exception e) {
		}

		if (pushManager == null) {
			sb.append("<br/>");
			sb.append("PushManager not available.");
		}
		else {

			// PUSH ENABLED
			sb.append("<br/>");
			sb.append("<i>Push Enabled:</i> ");
			sb.append(pushManager.isPushEnabled());

			if (pushManager.isPushEnabled()) {

				// DEVICE TOKEN
				sb.append("<br/>");
				sb.append("<i>Device Token:</i> ");

				try {
					sb.append(pushManager.getDeviceToken());
				}
				catch (Exception e) {
					sb.append("None.");
				}

				// OPEN DEVICE RECIPIENT
				sb.append("<br/>");
				sb.append("<i>Open Direct Recipient:</i> ");

				try {
					sb.append(pushManager.getOpenDirectRecipient().getName());
				}
				catch (Exception e) {
					sb.append("None");
				}

				// NOTIFICATION RECIPIENT
				sb.append("<br/>");
				sb.append("<i>Notification Recipient:</i> ");

				try {
					sb.append(pushManager.getNotificationRecipientClass().getName());
				}
				catch (Exception e) {
					sb.append("None");
				}

				// CLOUD PAGE RECIPIENT
				sb.append("<br/>");
				sb.append("<i>Cloud Page Recipient:</i> ");

				try {
					sb.append(pushManager.getCloudPageRecipient().getName());
				}
				catch (Exception e) {
					sb.append("None");
				}
			}
		}

		// LOCATION SETTINGS
		sb.append("<br/>");
		sb.append("<br/>");
		sb.append("<b>Location Settings</b> ");

		ETLocationManager locationManager = null;

		try {
			locationManager = ETLocationManager.locationManager();
		}
		catch (Exception e) {
		}

		if (locationManager == null) {
			sb.append("<br/>");
			sb.append("Geo Fencing not available.");
		}
		else {
			// LOCATION MANAGEMENT ENABLED
			sb.append("<br/>");
			sb.append("<i>Geo Fencing Enabled:</i> ");
			sb.append(locationManager.isWatchingLocation());
		}

		Utils.setWebView (this, R.id.aboutWV, sb, true);

	}

	private String getLoglevelText(int loglevel) {
		switch (loglevel) {
			case Log.VERBOSE:
				return loglevel + " - Verbose";
			case Log.DEBUG:
				return loglevel + " - Debug";
			case Log.INFO:
				return loglevel + " - Info";
			case Log.WARN:
				return loglevel + " - Warning";
			case Log.ERROR:
				return loglevel + " - Error";
			default:
				return String.valueOf(loglevel);
		}
	}
}
