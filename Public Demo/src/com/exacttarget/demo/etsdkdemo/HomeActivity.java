package com.exacttarget.demo.etsdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

import com.exacttarget.etpushsdk.ETAnalytics;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

public class HomeActivity extends Activity {
	private static final String TAG = "HomeActivity";

	private static String FirstNameKey = "FirstName";
	private static String LastNameKey = "LastName";
	private static String EmailAddressKey = "EmailAddress";
	private static String PreferencesKey = "ETDemoPreferences";

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		final EditText txtFirstName = (EditText) findViewById(R.id.txtFirstName);
		final EditText txtLastName = (EditText) findViewById(R.id.txtLastName);
		final EditText txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
		
		final ToggleButton tglPushEnabled = (ToggleButton) findViewById(R.id.togglePushEnabled);		

		try {
			ETPush pushManager = ETPush.pushManager();
	
			pushManager.configureSDKWithAppIdAndAccessToken("1b83b32a-ea05-48ec-a1c7-69afa81afab9", "n4twywndzq3yrxk5ku4t4zsp");
			pushManager.setNotificationRecipientClass(HomeActivity.class);
	//		pushManager.setOpenDirectRecipient(OpenDirectDemo.class);
			pushManager.setGcmSenderID("1072910018575");
	
			
			pushManager.addTag("Android");
			pushManager.addTag("6.0");
	
			tglPushEnabled.setChecked(pushManager.isPushEnabled());
			
			if (getPreferenceForKey(FirstNameKey) != null) {
				pushManager.addAtributeNamedValue("FirstName",
						getPreferenceForKey(FirstNameKey));
				txtFirstName.setText(getPreferenceForKey(FirstNameKey));
			}
	  
			if (getPreferenceForKey(LastNameKey) != null) {
				pushManager.addAtributeNamedValue("LastName",
						getPreferenceForKey(LastNameKey));
				txtLastName.setText(getPreferenceForKey(LastNameKey));
			}
	
			if (getPreferenceForKey(EmailAddressKey) != null) {
				// pushManager.addAtributeNamedValue("FirstName",
				// getPreferenceForKey(FirstNameKey));
				txtEmailAddress.setText(getPreferenceForKey(EmailAddressKey));
			}
		}
		catch(ETException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		txtEmailAddress.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				updatePreferencesForKey(EmailAddressKey, txtEmailAddress.getText().toString());
			}
		});
		
		txtEmailAddress.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_NEXT) {
		        	updatePreferencesForKey(EmailAddressKey, txtEmailAddress.getText().toString());
		            return true;
		        }
		        else {
		            return false;
		        }
			}
		});

		txtFirstName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				updatePreferencesForKey(FirstNameKey, txtFirstName.getText()
						.toString());
			}
		});

		txtLastName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				updatePreferencesForKey(LastNameKey, txtLastName.getText()
						.toString());
			}
		});

		/**
		 * End developers don't need to call updateET. This is just useful for internal testing by
		 * the ET development team.
		 */
		Button btnUpdateET = (Button) findViewById(R.id.btnUpdateET);
		btnUpdateET.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ETPush.pushManager().registerForRemoteNotifications();
					ETPush.pushManager().updateET();
				}
				catch(ETException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		});
	
		Button btnUnregister = (Button) findViewById(R.id.btnUnregister);
		btnUnregister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ETPush.pushManager().unregisterForRemoteNotifications();
				}
				catch(ETException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		});

		Button btnSecondActivity = (Button) findViewById(R.id.btnSecondActivity);
		btnSecondActivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(getBaseContext(), SecondActivity.class);
				startActivity(newIntent);
			}
		});
		
		tglPushEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					if (isChecked) {
						Log.d(TAG, "Push enabled");
						ETPush.pushManager().enablePush();
					} else {
						Log.d(TAG, "Push disabled");
						ETPush.pushManager().disablePush();
					}	
				}
				catch(ETException e) {
					Log.e(TAG, e.getMessage(), e);
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updatePreferencesForKey(String key, String value) {
		Log.d(TAG, "Setting " + key + " for " + value);
		SharedPreferences preferences = this.getSharedPreferences(
				PreferencesKey, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();

	}

	private String getPreferenceForKey(String key) {
		SharedPreferences preferences = this.getSharedPreferences(
				PreferencesKey, MODE_PRIVATE);
		return preferences.getString(key, null);
	}
	
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume()");
		super.onResume();

		// To use ETAnalytics, you should override onResume and call this method
		// It ensures proper tracking of time-in-app analytics.
		ETAnalytics.engine().activityResumed(this);
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause()");
		super.onPause();

		// To use ETAnalytics, you should override onPause and call this method
		// It ensures proper tracking of time-in-app analytics.
		ETAnalytics.engine().activityPaused(this);
	}

}
