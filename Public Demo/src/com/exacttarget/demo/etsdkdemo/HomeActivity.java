package com.exacttarget.demo.etsdkdemo;

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

import com.exacttarget.etpushsdk.AnalyticActivity;
import com.exacttarget.etpushsdk.ETPush;

public class HomeActivity extends AnalyticActivity {
	private static final String TAG = "Home Activity";

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

		ETPush pushManager = ETPush.pushManager();

		pushManager.configureSDKWithAppIdAndAccessToken("", "");
		pushManager.setNotificationRecipientClass(HomeActivity.class);
		pushManager.setOpenDirectRecipient(OpenDirectDemo.class);
		pushManager.setGcmSenderID("");

		
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

		Button btnUpdateET = (Button) findViewById(R.id.btnUpdateET);
		btnUpdateET.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ETPush.pushManager().registerForRemoteNotifications();
				ETPush.pushManager().updateET();
			}
		});

		Button btnSecondActivity = (Button) findViewById(R.id.btnSecondActivity);
		btnSecondActivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(getBaseContext(),
						SecondActivity.class);
				startActivity(newIntent);
			}
		});
		
		tglPushEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Log.d(TAG, "Push enabled");
					ETPush.pushManager().enablePush();
				} else {
					Log.d(TAG, "Push disabled");
					ETPush.pushManager().disablePush();
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
	
	private void doRegistration() {
		ETPush.pushManager().registerForRemoteNotifications();
		ETPush.pushManager().enablePush();
	}
	
	private void doUnregistration() {
		ETPush.pushManager().unregisterForRemoteNotifications();
	}
}
