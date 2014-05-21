package com.exacttarget.publicdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.*;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

import java.io.File;

/**
 * PublicDemoDebugSettingsActivity is a settings activity to manage debug options within the PublicDemo App.
 *
 * This activity extends PreferenceActivity to provide the debug settings to debug issues or trace code execution.
 *
 * Settings:
 *
 * 		1) Turn Debug On.
 * 		   In the production version of the PublicDemo App, the default logging level is WARN.  This setting will turn
 * 		   logging to DEBUG.
 *
 * 		2) Capture Logcat
 * 	       After running some tests, you may want to see how to see the debug info is produced in the logcat.  This will
 * 	       be especially useful if you send to a customer to test.  This setting requires the following permissions in the Manifest:
 *				<uses-permission android:name="android.permission.READ_LOGS" />  // to create Logcat file
 *				<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> // to create logCat file
 *
 * @author pvandyk
 */

public class PublicDemoDebugSettingsActivity extends PreferenceActivity {

	private SharedPreferences sp;

	private static final int currentPage = CONSTS.DEBUG_SETTINGS_ACTIVITY;
	private static final String TAG = PublicDemoDebugSettingsActivity.class.getName();

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setTitle(R.string.public_demo_settings_activity_title);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		sp = PreferenceManager.getDefaultSharedPreferences(PublicDemoApp.context());

		addPreferencesFromResource(R.xml.debug_prefs);

		prepareDisplay();
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
	protected void onStop() {
		super.onStop();
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

	//
	// Prepare the display of the settings
	//
	@SuppressWarnings("deprecation")
	private void prepareDisplay() {

		//
		// ENABLE DEBUG ON PREFERENCE
		//
		CheckBoxPreference edPref = (CheckBoxPreference) findPreference(CONSTS.KEY_DEBUG_PREF_ENABLE_DEBUG);
		if (ETPush.getLogLevel() <= Log.DEBUG) {
			edPref.setChecked(true);
		}
		else {
			edPref.setChecked(false);
		}

		edPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference pref, Object newValue) {
				Boolean new_edPref = (Boolean) newValue;

				try {
					if (new_edPref) {
						ETPush.setLogLevel(Log.DEBUG);
					}
					else {
						ETPush.setLogLevel(Log.WARN);
					}
					updatePreferencesForKey(CONSTS.KEY_DEBUG_PREF_ENABLE_DEBUG, new_edPref);
				}
				catch (ETException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, e.getMessage(), e);
					}
				}

				return true;
			}
		});

		//
		// COLLECT LOGCAT PREFERENCE
		//
		Preference clPref = findPreference(CONSTS.KEY_DEBUG_PREF_COLLECT_LOGCAT);

		clPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override public boolean onPreferenceClick(Preference preference) {

				try {

					// create attachments:
					//		1) logcat
					//		2) ET Database
					//
					//	copy to temp location so that the email program can find them.
					File logcat = Utils.createLogcatFile();
					File[] attachments = new File[] {Utils.copyFileToTemp(logcat), Utils.copyFileToTemp(PublicDemoApp.context().getDatabasePath("etdb.db"))};
					Utils.sendEmailToEmailAddress(PublicDemoDebugSettingsActivity.this, "Android Public Demo Debug Info", Html.fromHtml(Utils.formatAboutPage().toString()), attachments);

					// delete file as it has been copied to temp location
					logcat.delete();

				}
				catch (Exception e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				return true;
			}
		});

	} //prepareDisplay()

	//
	// updatePreferencesForKey
	//
	// Update the Shared Preferences file for the given key.
	//
	private void updatePreferencesForKey(String key, Boolean value) {
		sp.edit().putBoolean(key, value).apply();
	}

}
