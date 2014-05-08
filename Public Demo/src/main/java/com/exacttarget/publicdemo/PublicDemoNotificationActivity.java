package com.exacttarget.publicdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;
import java.util.Calendar;

public class PublicDemoNotificationActivity extends Activity {
	private int currentPage = CONSTS.NOTIFICATION_ACTIVITY;

	private static final String TAG = PublicDemoNotificationActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_demo_notification_layout);

		getActionBar().setTitle(R.string.public_demo_notification_activity_title);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		prepareDisplay(savedInstanceState == null);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CONSTS.KEY_CURRENT_PAGE, currentPage);
	}

	@Override
	protected void onDestroy() {
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
	protected void onStart() {
		super.onStart();
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

	private void prepareDisplay(boolean firstOpen) {

		StringBuilder sb = new StringBuilder();

		Bundle payload = getIntent().getBundleExtra("payload");

		// PAYLOAD
		sb.append("<b>Payload Sent with Message</b>  ");


		if (payload == null) {
			sb.append("<br/>");
			sb.append("Payload is null");
		}
		else {
			sb.append("<br/><br/>");
			sb.append("<i>Key/Value pairs:</i>  ");
			for (String key : payload.keySet()) {
				Object value = payload.get(key);
				sb.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("<u>");
				sb.append(key);
				sb.append("</u>");
				sb.append(" : ");
				sb.append(value);
			}

			sb.append("<br/><br/>");
			sb.append("<i>Discount Code:</i>  ");
			if (payload.containsKey(CONSTS.KEY_PAYLOAD_DISCOUNT)) {
				// CUSTOM KEYS
				sb.append(payload.get(CONSTS.KEY_PAYLOAD_DISCOUNT));

				if (firstOpen) {
					// if the Activity was refreshed, then don't flow to the discount screen.
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(PublicDemoApp.context());
					SharedPreferences.Editor spEditor = sp.edit();

					spEditor.putLong(CONSTS.KEY_PREF_DISCOUNT_END_DATE, Calendar.getInstance().getTimeInMillis());
					spEditor.putString(CONSTS.KEY_PREF_DISCOUNT_MESSAGE, payload.getString(CONSTS.KEY_PAYLOAD_ALERT));

					try {
						int discount = Integer.valueOf(payload.getString(CONSTS.KEY_PAYLOAD_DISCOUNT));

						switch (discount) {
							case 10:
								spEditor.putString(CONSTS.KEY_PREF_DISCOUNT_IMAGE_FILE, "10percentoffQR.png");
								break;
							case 15:
								spEditor.putString(CONSTS.KEY_PREF_DISCOUNT_IMAGE_FILE, "15percentoffQR.png");
								break;
							case 20:
								spEditor.putString(CONSTS.KEY_PREF_DISCOUNT_IMAGE_FILE, "20percentoffQR.png");
								break;
							default:
								spEditor.putString(CONSTS.KEY_PREF_DISCOUNT_IMAGE_FILE, "10percentoffQR.png");
								break;
						}

						spEditor.commit();

						Intent intent = new Intent(PublicDemoNotificationActivity.this, PublicDemoDiscountActivity.class);
						startActivity(intent);

					}
					catch (Exception e) {
						sb.append("Problem displaying discount code.  Check logcat.");
						if (ETPush.getLogLevel() <= Log.ERROR) {
							Log.e(TAG, e.getMessage(), e);
						}
					}
				}
			}
			else {
				sb.append("n/a");
				sb.append("<br/>");
				sb.append("NOTE: No discount_code key was sent with this message.");
			}
		}

		Utils.setWebView(this, R.id.notificationWV, sb, false);
	}
}
