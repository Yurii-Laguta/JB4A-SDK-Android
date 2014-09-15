/**
 * Copyright (c) 2014 ExactTarget, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.exacttarget.practicefield;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.*;
import android.util.Log;
import android.view.*;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLocationManager;
import com.exacttarget.etpushsdk.ETPush;
import com.radiusnetworks.ibeacon.BleNotAvailableException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * PracticeFieldSettingsActivity is the primary settings activity within the PracticeField App.
 * <p/>
 * This activity extends PreferenceActivity to provide the primary settings interface to collect user preferences.
 * <p/>
 * It handles settings that would normally be included within your customer facing app.  These settings that are sent to
 * the Marketing Cloud will take up to 15 minutes to take effect.  So, after setting or changing these settings, you
 * should wait at least 15 minutes before sending a message either from the Marketing Cloud or from the PracticeFieldSendMessagesDialog
 * found within this app.
 * <p/>
 * We have setup this demo app to require several settings before allowing the user to provide permission to receive notifications.
 * <p/>
 * Your app design may be different (for example, you may set notifications on by default in your Application class if you assume
 * permission was given by the user due to the permission settings set within the Google Play definition.
 * <p/>
 * Settings:
 * <p/>
 * 1) First and Last Name.
 * These are attributes saved in the Google Marketing Cloud.
 * <p/>
 * 2) Enable Push Preferences
 * This preference is the heart of the SDK. Without Push turned on, not much will happen!
 * <p/>
 * 3) Enable Location (for Geo Fencing and Beacons) Preference
 * This preference will test Geo Fencing and Beacon proximity.  For the PracticeField App, we have setup several fences around the
 * national parks.  A tool like Fake GPS can be used to test these location settings.
 * <p/>
 * <p>
 * To Test Beacons, we have setup messages using the GUID 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6.  If you assign this
 * GUID to beacons you purchase/configure, you will receive messages for the following major/minor combinations: 1,1; 1,2; 1,3; 1,4.
 * </p>
 * 4) Custom Ringtone and Custom Vibration
 * This app shows a way to completely customize the way notification sounds work.  Within the Marketing Cloud, you can
 * normally set to use the Default of a Custom Sound.  This demo, takes it a step further and will have it's own custom
 * sound (sports whistle) or allow the user to select their own sound.  The settings from the Marketing Cloud are
 * essentially ignored.
 * <p/>
 * All of this work is done locally and not through the SDK.
 * <p/>
 * 5) Team Tags
 * The Team Tags show examples of using Tags to allow your customers to select which type of notification they are interested in
 * receiving.  The example within this PracticeField App are Activities.  The tags are sent to the Marketing Cloud and can
 * be used to select customers to send the notification to.
 * <p/>
 * 6) Custom Keys
 * In the PracticeField App in the Marketing Cloud, we set up a custom key to drive processing within the app when the user
 * opens the notification.  This processing shows how to save the data sent.  As well as how to show different Activity or
 * at least different information in the Activity as dictated by the value of the custom key.
 *
 * @author pvandyk
 */

public class PracticeFieldSettingsActivity extends PreferenceActivity {

	private SharedPreferences sp;

	private ArrayList<String> pfConfigsListSummary = null;
	private ArrayList<String> pfConfigsListFiles = null;

	private static final int currentPage = CONSTS.SETTINGS_ACTIVITY;
	private static final String TAG = PracticeFieldSettingsActivity.class.getName();

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sp = PreferenceManager.getDefaultSharedPreferences(PracticeFieldApp.context());

		addPreferencesFromResource(R.xml.preferences);

		if (getIntent().getAction() != null && getIntent().getAction().contains(Intent.ACTION_VIEW)) {
			// check if new config file passed in
			Uri data = getIntent().getData();
			if (data != null) {
				getIntent().setData(null);
				try {
					importPFConfigFile(data);
				}
				catch (Exception e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, "Failed to save new PFConfig File.", e);
					}
				}
			}
		}

		if (pfConfigsListFiles == null | pfConfigsListSummary == null) {
			getConfigsList();
		}

		prepareDisplay();

		// Warn about wait time.
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(getString(R.string.alert_settings1_title));
		builder.setMessage(R.string.alert_settings1_text);
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

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			Utils.setPrefActivityTitle(this, getString(R.string.settings_activity_title));
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

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

		//re-register with ET to send the tags and attributes that changed.
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

		addConfigChange();

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
		// ENABLE LOCATION (GEO and BEACONS) PREFERENCE
		//

		final CheckBoxPreference locationsPref = (CheckBoxPreference) findPreference(CONSTS.KEY_PREF_LOCATION);

		if (sp.getString(CONSTS.KEY_PREF_FIRST_NAME, "").isEmpty() | sp.getString(CONSTS.KEY_PREF_LAST_NAME, "").isEmpty()) {
			locationsPref.setEnabled(false);
		}
		else {
			locationsPref.setEnabled(true);
			try {
				locationsPref.setChecked(ETLocationManager.locationManager().isWatchingLocation());
			}
			catch (Exception e) {
				if (ETPush.getLogLevel() <= Log.ERROR) {
					Log.e(TAG, e.getMessage(), e);
				}
				locationsPref.setChecked(false);
			}
		}
		locationsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference pref, Object newValue) {
				Boolean newPrefLocation = (Boolean) newValue;

				try {
					if (newPrefLocation) {
						ETLocationManager.locationManager().startWatchingLocation();
						try {
							if (!ETLocationManager.locationManager().startWatchingProximity()) {
								// startWatchingProximity will return false if BlueTooth is not turned on.
								promptForBluetoothSettings();
							}
						}
						catch (BleNotAvailableException e) {
							// Bluetooth LE only available on 4.3 and later
							// https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
							Log.w(TAG, "BLE is not available on this device");
							ETLocationManager.locationManager().stopWatchingProximity();
						}
					}
					else {
						ETLocationManager.locationManager().stopWatchingLocation();
						ETLocationManager.locationManager().stopWatchingProximity();
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
		// SPORTS SUBSCRIPTIONS
		//
		PreferenceScreen ps = getPreferenceScreen();
		PreferenceCategory activities_prefCat = new PreferenceCategory(this);
		activities_prefCat.setTitle("Activity Tags");
		activities_prefCat.setKey(CONSTS.KEY_PREF_CAT_SPORTS);
		ps.addPreference(activities_prefCat);

		String[] activityNames = getResources().getStringArray(R.array.activity_names);
		String[] activityKeys = getResources().getStringArray(R.array.activity_keys);
		for (int i = 0; i < activityNames.length; i++) {
			setSubCheckBoxPref(activities_prefCat, activityNames[i], activityKeys[i]);
		}

		//
		// enable/disable any settings dependent on push (either messages or locations)
		//
		enablePushDependentPrefs();

	} //prepareDisplay()

	//
	// set_pfcl
	//
	private int set_pfcl(ListPreference pfclPref) {
		SharedPreferences sp = PracticeFieldApp.context().getSharedPreferences(CONSTS.PREFS_CONFIG, Context.MODE_PRIVATE);
		String currFilePath = sp.getString(CONSTS.KEY_PREF_CONFIG_FILE, "");
		int index = pfConfigsListFiles.indexOf(currFilePath);

		if (currFilePath.equals("") | index == -1) {
			// default to prod
			index = 0;
		}

		if (pfclPref != null) {
			pfclPref.setValueIndex(index);
			pfclPref.setSummary(pfConfigsListSummary.get(index));
		}

		return index;
	}

	//
	// setSubCheckBoxPref
	//
	// Create the CheckBoxPreference controls to subscribe to an Activity notification.
	//
	private void setSubCheckBoxPref(PreferenceCategory prefCat, final String activityName, final String activityKey) {
		CheckBoxPreference cbp = new CheckBoxPreference(this);
		cbp.setKey(activityKey);
		cbp.setTitle(activityName);
		cbp.setSummary("Receive notifications for " + activityName);
		cbp.setDefaultValue(Boolean.FALSE);

		cbp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference pref, Object newValue) {
				Boolean enabled = (Boolean) newValue;
				try {
					if (enabled) {
						ETPush.pushManager().addTag(activityKey);
					}
					else {
						ETPush.pushManager().removeTag(activityKey);
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

	//**************************************************************
	//
	// Remove before adding to staging
	//
	//**************************************************************

	//
	// addConfigChange
	//
	// add Config change preference
	//
	private void addConfigChange() {
		//
		// PFConfigs PREFERENCE
		//
		final ListPreference pfclPref = (ListPreference) findPreference(CONSTS.KEY_PREF_CONFIGS_LIST);

		boolean forInternalUseOnly = false;
		try {
			ApplicationInfo ai = PracticeFieldApp.context().getPackageManager().getApplicationInfo(PracticeFieldApp.context().getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			forInternalUseOnly = bundle.getBoolean("internalUseOnly");
		}
		catch (Exception e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage());
			}
		}

		if (forInternalUseOnly) {

			String[] strArray = new String[pfConfigsListSummary.size()];
			strArray = pfConfigsListSummary.toArray(strArray);
			pfclPref.setEntries(strArray);
			strArray = new String[pfConfigsListFiles.size()];
			strArray = pfConfigsListFiles.toArray(strArray);
			pfclPref.setEntryValues(strArray);

			set_pfcl(pfclPref);

			pfclPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference pref, Object newValue) {
					final int newIndex = pfConfigsListFiles.indexOf((String) newValue);

					SharedPreferences sp = PracticeFieldApp.context().getSharedPreferences(CONSTS.PREFS_CONFIG, Context.MODE_PRIVATE);
					sp.edit().putString(CONSTS.KEY_PREF_CONFIG_SUMMARY, pfConfigsListSummary.get(newIndex)).commit();
					sp.edit().putString(CONSTS.KEY_PREF_CONFIG_FILE, pfConfigsListFiles.get(newIndex)).commit();
					pfclPref.setSummary(pfConfigsListSummary.get(newIndex));

					switchConfig(pfclPref, newIndex);

					return true;
				}
			});
		}
		else {
			PreferenceScreen allPrefs = getPreferenceScreen();
			PreferenceCategory configCat = (PreferenceCategory) findPreference("pref_configs_settings");
			allPrefs.removePreference(configCat);
		}

	}

	//
	// getConfigsList
	//
	// Get the ConfigsList that have been imported.
	//
	private void getConfigsList() {

		this.pfConfigsListSummary = new ArrayList<String>();
		this.pfConfigsListFiles = new ArrayList<String>();

		this.pfConfigsListSummary.add("PROD");
		this.pfConfigsListFiles.add(CONSTS.PROD_CONFIG);
		this.pfConfigsListSummary.add("QA");
		this.pfConfigsListFiles.add(CONSTS.QA_CONFIG);
		this.pfConfigsListSummary.add("Development");
		this.pfConfigsListFiles.add(CONSTS.DEV_CONFIG);

		File fileList = new File(getFilesDir() + "/PFConfigFiles/");
		File[] fileNames = fileList.listFiles();
		if (fileNames != null) {
			for (File tf : fileNames) {
				JSONObject json = Utils.getConfig(tf.getAbsolutePath());

				if (json != null) {
					try {
						json = json.getJSONObject("PFConfig");
						this.pfConfigsListSummary.add(json.getString("configurationName"));
						this.pfConfigsListFiles.add(tf.getAbsolutePath());
						if (ETPush.getLogLevel() <= Log.DEBUG) {
							Log.d(TAG, "===> Loaded a config: " + json.getString("configurationName"));
						}

					}
					catch (JSONException e) {
						if (ETPush.getLogLevel() <= Log.ERROR) {
							Log.e(TAG, "JSON Get error: " + e.getLocalizedMessage());
						}
					}
				}
			}
		}
	}

	//
	// importPFConfigFile
	//
	// Import a new PracticeField Config File
	//

	private void importPFConfigFile(Uri data) {

		if (CONSTS_API.getBuildType() != CONSTS_API.BuildType.PRODUCTION) {
			final String scheme = data.getScheme();
			JSONObject json;

			if (ContentResolver.SCHEME_CONTENT.equals(scheme) || ContentResolver.SCHEME_FILE.equals(scheme)) {
				try {
					ContentResolver cr = getApplicationContext().getContentResolver();
					InputStream is = cr.openInputStream(data);
					if (is == null)
						return;

					StringBuilder buf = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					String str;
					while ((str = reader.readLine()) != null) {
						buf.append(str);
						buf.append("\n");
					}
					is.close();

					json = new JSONObject(buf.toString());
					Log.d(TAG, json.toString(4));

					try {
						File PFConfigFile = new File(getFilesDir() + "/PFConfigFiles/" + json.getJSONObject("PFConfig").getString("configurationName") + ".pfconfig");

						if (PFConfigFile.getParentFile().exists() || PFConfigFile.getParentFile().mkdirs()) {

							if (PFConfigFile.exists()) {
								if (!PFConfigFile.delete()) {
									if (ETPush.getLogLevel() <= Log.ERROR) {
										throw new Exception("deleting existing PFConfig file failed.");
									}
								}
							}

							if (!PFConfigFile.createNewFile()) {
								if (ETPush.getLogLevel() <= Log.ERROR) {
									throw new Exception("creating new PFConfig file failed.");
								}
							}
							FileOutputStream fos = new FileOutputStream(PFConfigFile);

							fos.write(json.toString(4).getBytes());
							fos.flush();
							fos.close();

							getConfigsList();

							int newIndex = pfConfigsListFiles.indexOf(PFConfigFile.getAbsolutePath());
							switchConfig(null, newIndex);

							Toast.makeText(PracticeFieldApp.context(), "Config import successful.  Switching to new config.", Toast.LENGTH_LONG).show();

						}
					}
					catch (Exception e) {
						if (ETPush.getLogLevel() <= Log.ERROR) {
							Log.e(TAG, "Saving PFConfig file failed.", e);
						}
					}

				}
				catch (FileNotFoundException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, "Unable to open PFConfig file.", e);
					}
				}
				catch (IOException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, "IO Exception saving PFConfig failed.", e);
					}
				}
				catch (JSONException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, "Error parsing JSON response from PFConfig.", e);
					}
				}
			}
		}
		else {
			finish();
		}
	}

	//
	// switchConfig
	//
	// Switch to new imported or selected config
	//
	private void switchConfig(final ListPreference pfclPref, final int newIndex) {

		final ProgressDialog progressDialog = ProgressDialog.show(PracticeFieldSettingsActivity.this, "Reset Config", "Please wait...", true);
		new Thread(new Runnable() {
			public void run() {
				try {

					// clear shared prefs
					sp.edit().clear().apply();

					// set new config type
					CONSTS_API.setConfigType(pfConfigsListSummary.get(newIndex), pfConfigsListFiles.get(newIndex));

					// it's possible there is a problem with the config, if so, set to new number
					final int index = set_pfcl(pfclPref);
					if (index != newIndex) {
						if (ETPush.getLogLevel() <= Log.DEBUG) {
							Log.d(TAG, "Unable to set to new config.  Switched to: " + pfConfigsListSummary.get(index));
						}
						CONSTS_API.setConfigType(pfConfigsListSummary.get(index), pfConfigsListFiles.get(index));
					}

					ETPush.resetPush(PracticeFieldApp.context(), CONSTS_API.getEtAppId(), CONSTS_API.getAccessToken());
					ETPush.pushManager().setGcmSenderID(CONSTS_API.getGcmSenderId());

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!PracticeFieldSettingsActivity.this.isFinishing()) {
								progressDialog.dismiss();
							}
							if (index == newIndex)
								Toast.makeText(PracticeFieldApp.context(), "Reset to new config successful: " + pfConfigsListSummary.get(index), Toast.LENGTH_LONG).show();
							else
								Toast.makeText(PracticeFieldApp.context(), "Reset to new config not successful.  Reset to PRODUCTION", Toast.LENGTH_LONG).show();

							finish();

						}
					});
				}
				catch (Exception e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!PracticeFieldSettingsActivity.this.isFinishing()) {
								progressDialog.dismiss();
							}
							Toast.makeText(PracticeFieldApp.context(), "Problems resetting to new config.  Check logcat.", Toast.LENGTH_LONG).show();
						}
					});
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, "Problems resetting to new config.", e);
					}
				}
			}
		}).start();
	}

	//
	// promptForBluetoothSettings
	//
	// Bluetooth is not turned on, so ask user to turn it on.
	//
	private void promptForBluetoothSettings() {
		new AlertDialog.Builder(PracticeFieldSettingsActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Enable Bluetooth?")
				.setMessage("Beacons require that you have Bluetooth enabled. Enable it now?")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Enable Bluetooth", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
						if (!mBluetoothAdapter.isEnabled()) {
							mBluetoothAdapter.enable();
						}

						try {
							ETLocationManager.locationManager().startWatchingProximity();
						}
						catch (BleNotAvailableException e) {
							if (ETPush.getLogLevel() <= Log.ERROR) {
								Log.e(TAG, e.getMessage(), e);
							}
						}
						catch (ETException e) {
							if (ETPush.getLogLevel() <= Log.ERROR) {
								Log.e(TAG, e.getMessage(), e);
							}
						}
					}
				})
				.show();
	}
}
