package com.exacttarget.publicdemo;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLocationManager;
import com.exacttarget.etpushsdk.ETPush;

/**
 * PublicDemoSettingsActivity is the primary settings activity within the PublicDemo App.
 *
 * This activity extends PreferenceActivity to provide the primary settings interface to collect user preferences.
 *
 * It handles settings that would normally be included within your customer facing app.  These settings that are sent to
 * the Marketing Cloud will take up to 15 minutes to take effect.  So, after setting or changing these settings, you
 * should wait at least 15 minutes before sending a message either from the Marketing Cloud or from the PublicDemoSendMessagesDialog
 * found within this app.
 *
 * We have setup this demo app to require several settings before allowing the user to provide permission to receive notifications.
 *
 * Your app design may be different (for example, you may set notifications on by default in your Application class if you assume
 * permission was given by the user due to the permission settings set within the Google Play definition.
 *
 * Settings:
 *
 * 		1) First and Last Name.
 * 		   These are attributes saved in the Google Marketing Cloud.
 *
 * 		2) Enable Push Preferences
 * 	       This preference is the heart of the SDK. Without Push turned on, not much will happen!
 *
 * 	    3) Enable Location (or Geo Fencing) Preference
 * 	       This preference will allow to test Geo Fencing.  For the PublicDemo App, we have setup several fences around the
 * 	       stadiums for the NFL and FC teams.  A tool like Fake GPS can be used to test these location settings.
 *
 * 	    4) Custom Ringtone and Custom Vibration
 * 	       This app shows a way to completely customize the way notification sounds work.  Within the Marketing Cloud, you can
 * 	       normally set to use the Default of a Custom Sound.  This demo, takes it a step further and will have it's own custom
 * 	       sound (sports whistle) or allow the user to select their own sound.  The settings from the Marketing Cloud are
 * 	       essentially ignored.
 *
 * 	       All of this work is done locally and not through the SDK.
 *
 * 		5) Subscriptions
 * 	       These show examples of using Tags to allow your customers to select which type of notification they are interested in
 * 	       receiving.  The example within this PublicDemo App are sports teams.  The tags are sent to the Marketing Cloud and can
 * 	       be used to select customers to send the notification to.
 *
 * 	    6) Custom Keys
 * 	       In the PublicDemo App in the Marketing Cloud, we set up a custom key to drive processing within the app when the user
 * 	       opens the notification.  This processing shows how to save the data sent.  As well as how to show different Activity or
 * 	       at least different information in the Activity as dictated by the value of the custom key.
 *
 * @author pvandyk
 */

public class PublicDemoSettingsActivity extends PreferenceActivity {

	private SharedPreferences sp;

	private static final int currentPage = CONSTS.SETTINGS_ACTIVITY;
	private static final String TAG = PublicDemoSettingsActivity.class.getName();

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			getActionBar().setTitle(R.string.public_demo_settings_activity_title);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		sp = PreferenceManager.getDefaultSharedPreferences(PublicDemoApp.context());

		addPreferencesFromResource(R.xml.preferences);

		prepareDisplay();

		// Warn about wait time.
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Delayed Update");
		builder.setMessage("Any updates to these Settings will take approximately 15 minutes to take effect.  Before sending a message, you should wait 15 minutes for the new values to take effect.");
		builder.setPositiveButton("OK", null);
		builder.create().show();

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

		//re-register with ET to send the subscription tags and attributes that changed.
		try {
			if (ETPush.pushManager().isPushEnabled()) {
				ETPush.pushManager().enablePush();
			}
		}
		catch (ETException e) {
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

	private void addAttributes() {
		try {
			ETPush.pushManager().addAttribute(CONSTS.KEY_ATTRIB_FIRST_NAME, sp.getString(CONSTS.KEY_PREF_FIRST_NAME, ""));
			ETPush.pushManager().addAttribute(CONSTS.KEY_ATTRIB_LAST_NAME, sp.getString(CONSTS.KEY_PREF_LAST_NAME, ""));
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	//
	// Prepare the display of the settings
	//
	@SuppressWarnings("deprecation")
	private void prepareDisplay() {

		//
		// FIRST NAME PREFERENCE
		//
		final Preference fnPref = findPreference(CONSTS.KEY_PREF_FIRST_NAME);
		if (!sp.getString(CONSTS.KEY_PREF_FIRST_NAME, "").isEmpty()) {
			fnPref.setSummary(sp.getString(CONSTS.KEY_PREF_FIRST_NAME, ""));
		}

		fnPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				PreferenceScreen prefSet = getPreferenceScreen();

				final EditTextPreference fnETP = (EditTextPreference) prefSet.findPreference(CONSTS.KEY_PREF_FIRST_NAME);

				final AlertDialog d = (AlertDialog) fnETP.getDialog();
				final EditText fnET = fnETP.getEditText();
				fnET.setText(sp.getString(CONSTS.KEY_PREF_FIRST_NAME, ""));

				Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(android.view.View v) {
						String newFirstName = fnET.getText().toString().trim();
						if (newFirstName.isEmpty()) {
							Utils.flashError(fnET, getString(R.string.error_cannot_be_blank));
							return;
						}
						else {

							// save the preference to Shared Preferences
							updatePreferencesForKey(CONSTS.KEY_PREF_FIRST_NAME, newFirstName);
							fnPref.setSummary(newFirstName);

							try {
								String spLastName = sp.getString(CONSTS.KEY_PREF_LAST_NAME, "");
								if (!spLastName.isEmpty()) {
									CheckBoxPreference pushPref = (CheckBoxPreference) findPreference(CONSTS.KEY_PREF_PUSH);
									pushPref.setEnabled(true);
									if (ETPush.pushManager().isPushEnabled()) {
										ETPush.pushManager().addAttribute(CONSTS.KEY_ATTRIB_FIRST_NAME, newFirstName);
										ETPush.pushManager().addAttribute(CONSTS.KEY_ATTRIB_LAST_NAME, spLastName);
									}
								}
							}
							catch (ETException e) {
								if (ETPush.getLogLevel() <= Log.ERROR) {
									Log.e(TAG, e.getMessage(), e);
								}
							}
						}

						d.dismiss();
					}
				});

				return true;
			}
		});

		//
		// LAST NAME PREFERENCE
		//
		final Preference lnPref = findPreference(CONSTS.KEY_PREF_LAST_NAME);
		if (!sp.getString(CONSTS.KEY_PREF_LAST_NAME, "").isEmpty()) {
			lnPref.setSummary(sp.getString(CONSTS.KEY_PREF_LAST_NAME, ""));
		}

		lnPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				PreferenceScreen prefSet = getPreferenceScreen();

				final EditTextPreference lnETP = (EditTextPreference) prefSet.findPreference(CONSTS.KEY_PREF_LAST_NAME);

				final AlertDialog d = (AlertDialog) lnETP.getDialog();
				final EditText lnET = lnETP.getEditText();
				lnET.setText(sp.getString(CONSTS.KEY_PREF_LAST_NAME, ""));

				Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(android.view.View v) {
						String newLastName = lnET.getText().toString().trim();
						if (newLastName.isEmpty()) {
							Utils.flashError(lnET, getString(R.string.error_cannot_be_blank));
							return;
						}
						else {
							// save the preference to Shared Preferences
							updatePreferencesForKey(CONSTS.KEY_PREF_LAST_NAME, newLastName);
							lnPref.setSummary(newLastName);

							try {
								String spFirstName = sp.getString(CONSTS.KEY_PREF_FIRST_NAME, "");
								if (!spFirstName.isEmpty()) {
									CheckBoxPreference pushPref = (CheckBoxPreference) findPreference(CONSTS.KEY_PREF_PUSH);
									pushPref.setEnabled(true);
									if (ETPush.pushManager().isPushEnabled()) {
										ETPush.pushManager().addAttribute(CONSTS.KEY_ATTRIB_FIRST_NAME, spFirstName);
										ETPush.pushManager().addAttribute(CONSTS.KEY_ATTRIB_LAST_NAME, newLastName);
									}
								}
							}
							catch (ETException e) {
								if (ETPush.getLogLevel() <= Log.ERROR) {
									Log.e(TAG, e.getMessage(), e);
								}
							}
						}

						d.dismiss();
					}
				});

				return true;
			}
		});

		//
		// ENABLE PUSH PREFERENCE
		//

		CheckBoxPreference pushPref = (CheckBoxPreference) findPreference(CONSTS.KEY_PREF_PUSH);
		if (sp.getString(CONSTS.KEY_PREF_FIRST_NAME, "").isEmpty() | sp.getString(CONSTS.KEY_PREF_LAST_NAME, "").isEmpty()) {
			pushPref.setEnabled(false);
		}
		else {
			pushPref.setEnabled(true);

			try {
				pushPref.setChecked(ETPush.pushManager().isPushEnabled());
			}
			catch (Exception e) {
				if (ETPush.getLogLevel() <= Log.ERROR) {
					Log.e(TAG, e.getMessage(), e);
				}
				pushPref.setChecked(false);
			}
		}

		pushPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference pref, Object newValue) {
				Boolean newPrefPush = (Boolean) newValue;

				try {
					if (newPrefPush) {
						addAttributes();
						ETPush.pushManager().enablePush();
					}
					else {
						ETPush.pushManager().disablePush();
					}

					enablePushDependentPrefs();
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
		// ENABLE LOCATION (GEO) PREFERENCE
		//

		final CheckBoxPreference geoPref = (CheckBoxPreference) findPreference(CONSTS.KEY_PREF_GEO);

		if (sp.getString(CONSTS.KEY_PREF_FIRST_NAME, "").isEmpty() | sp.getString(CONSTS.KEY_PREF_LAST_NAME, "").isEmpty()) {
			geoPref.setEnabled(false);
		}
		else {
			geoPref.setEnabled(true);
			try {
				geoPref.setChecked(ETLocationManager.locationManager().isWatchingLocation());
			}
			catch (Exception e) {
				if (ETPush.getLogLevel() <= Log.ERROR) {
					Log.e(TAG, e.getMessage(), e);
				}
				geoPref.setChecked(false);
			}
		}
		geoPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference pref, Object newValue) {
				Boolean newPrefGeo = (Boolean) newValue;

				try {
					if (newPrefGeo) {
						ETLocationManager.locationManager().startWatchingLocation();
					}
					else {
						ETLocationManager.locationManager().stopWatchingLocation();
					}

					enablePushDependentPrefs();
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
		// NFL TEAMS SUBSCRIPTIONS
		//
		PreferenceScreen ps = getPreferenceScreen();
		PreferenceCategory nfl_prefCat = new PreferenceCategory(this);
		nfl_prefCat.setTitle("NFL Subscriptions");
		nfl_prefCat.setKey(CONSTS.KEY_PREF_CAT_NFL);
		ps.addPreference(nfl_prefCat);

		String[] nflTeamNames = getResources().getStringArray(R.array.nfl_teamNames);
		String[] nflTeamKeys = getResources().getStringArray(R.array.nfl_teamKeys);
		for (int i = 0; i < nflTeamNames.length; i++) {
			setSubCheckBoxPref(nfl_prefCat, nflTeamNames[i], nflTeamKeys[i]);
		}

		//
		// SOCCER TEAMS PREFERENCE
		//
		PreferenceCategory fc_prefCat = new PreferenceCategory(this);
		fc_prefCat.setTitle("FC Subscriptions");
		fc_prefCat.setKey(CONSTS.KEY_PREF_CAT_FC);
		ps.addPreference(fc_prefCat);

		String[] fcTeamNames = getResources().getStringArray(R.array.fc_teamNames);
		String[] fcTeamKeys = getResources().getStringArray(R.array.fc_teamKeys);
		for (int i = 0; i < fcTeamNames.length; i++) {
			setSubCheckBoxPref(fc_prefCat, fcTeamNames[i], fcTeamKeys[i]);
		}

		//
		// enable/disable any settings dependent on push (either messages or locations)
		//
		enablePushDependentPrefs();

	} //prepareDisplay()

	//
	// setSubCheckBoxPref
	//
	// Create the CheckBoxPreference controls to subscribe to a team notification.
	//
	private void setSubCheckBoxPref(PreferenceCategory prefCat, final String teamName, final String teamKey) {
		CheckBoxPreference cbp = new CheckBoxPreference(this);
		cbp.setKey(teamKey);
		cbp.setTitle(teamName);
		cbp.setSummary("Receive notifications for " + teamName);
		cbp.setDefaultValue(Boolean.FALSE);

		cbp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference pref, Object newValue) {
				Boolean enabled = (Boolean) newValue;
				try {
					if (enabled) {
						ETPush.pushManager().addTag(teamKey);
					}
					else {
						ETPush.pushManager().removeTag(teamKey);
					}
				}
				catch (ETException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				return true;
			}
		});

		prefCat.addPreference(cbp);
	}

	//
	// enablePushDependentPrefs
	//
	// Determine whether to enable or disable prefs depending on whether Push or Location services are enabled.
	//
	private void enablePushDependentPrefs() {
		boolean pushEnabled = false;

		try {
			pushEnabled = ETPush.pushManager().isPushEnabled();
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		if (pushEnabled) {
			enablePushDependentPrefs(true);
		}
		else {
			enablePushDependentPrefs(false);
		}
	}

	//
	// enablePushDependentPrefs
	//
	// Enable or disable all the prefs that are dependant on whether Push or Location services are enabled.
	//
	@SuppressWarnings("deprecation")
	private void enablePushDependentPrefs(boolean enable) {

		for (String prefId : CONSTS.KEY_PREF_PUSH_DEPENDENT) {
			Preference pref = findPreference(prefId);
			if (pref != null) {
				pref.setEnabled(enable);
			}
		}
	}

	//
	// updatePreferencesForKey
	//
	// Update the Shared Preferences file for the given key.
	//
	private void updatePreferencesForKey(String key, String value) {
		sp.edit().putString(key, value).apply();
	}

}
