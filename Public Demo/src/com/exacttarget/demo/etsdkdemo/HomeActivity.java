package com.exacttarget.demo.etsdkdemo;

import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLocationManager;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.event.LastKnownLocationEvent;
import com.exacttarget.etpushsdk.event.LastKnownLocationEventListener;
import com.exacttarget.etpushsdk.util.EventBus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends FragmentActivity implements LastKnownLocationEventListener {
	private static final String TAG = "HomeActivity";

	private static final String FirstNameKey = "FirstName";
	private static final String LastNameKey = "LastName";
	private static final String EmailAddressKey = "EmailAddress";
	private static final String PreferencesKey = "ETDemoPreferences";
	
	private SharedPreferences prefs;
	
	private TextView locationText;
	private GoogleMap map;

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		prefs = getSharedPreferences(PreferencesKey, MODE_PRIVATE);

		final EditText txtFirstName = (EditText) findViewById(R.id.txtFirstName);
		final EditText txtLastName = (EditText) findViewById(R.id.txtLastName);
		final EditText txtEmailAddress = (EditText) findViewById(R.id.txtEmailAddress);
		
		final ToggleButton tglPushEnabled = (ToggleButton) findViewById(R.id.togglePushEnabled);
		final ToggleButton tglGeoEnabled = (ToggleButton) findViewById(R.id.toggleGeoEnabled);
		
		locationText = (TextView) findViewById(R.id.locationText); 
		
		FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        map = supportmapfragment.getMap();
		
		EventBus.getDefault().register(this);

		try {
			ETPush.pushManager().setNotificationRecipientClass(HomeActivity.class);
	//		ETPush.pushManager().setOpenDirectRecipient(OpenDirectDemo.class);
			
			//restore saved user preference attributes to the screen and pushmanager
			String firstName = getPreferenceForKey(FirstNameKey);
			if (firstName != null && firstName.length() > 0) {
				ETPush.pushManager().addAttribute("FirstName",	firstName);
				txtFirstName.setText(firstName);
			}
	  
			String lastName = getPreferenceForKey(LastNameKey);
			if (lastName != null && lastName.length() > 0) {
				ETPush.pushManager().addAttribute("LastName", lastName);
				txtLastName.setText(lastName);
			}
	
			String emailAddress = getPreferenceForKey(EmailAddressKey);
			if (emailAddress != null && emailAddress.length() > 0) {
				ETPush.pushManager().setSubscriberKey(emailAddress);
				txtEmailAddress.setText(emailAddress);
			}
			
			//set buttons to their proper state
			tglPushEnabled.setChecked(ETPush.pushManager().isPushEnabled());
			tglGeoEnabled.setChecked(ETLocationManager.locationManager().isWatchingLocation());

		}
		catch(ETException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		tglPushEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					if (isChecked) {
						Log.d(TAG, "Push enabled");
						if(txtFirstName.getText() != null && txtFirstName.getText().toString().length() > 0) {
							String firstName = txtFirstName.getText().toString();
							updatePreferencesForKey(FirstNameKey, firstName);
							ETPush.pushManager().addAttribute("FirstName", firstName);
						}
						if(txtLastName.getText() != null && txtLastName.getText().toString().length() > 0) {
							String lastName = txtLastName.getText().toString();
							updatePreferencesForKey(LastNameKey, lastName);
							ETPush.pushManager().addAttribute("LastName", lastName);
						}
						if(txtEmailAddress.getText() != null && txtEmailAddress.getText().toString().length() > 0) {
							String emailAddress = txtEmailAddress.getText().toString();
							updatePreferencesForKey(EmailAddressKey, emailAddress);
							ETPush.pushManager().setSubscriberKey(emailAddress);
						}

						ETPush.pushManager().enablePush(HomeActivity.this);
					} else {
						Log.d(TAG, "Push disabled");
						ETPush.pushManager().disablePush(HomeActivity.this);
					}	
				}
				catch(ETException e) {
					Log.e(TAG, e.getMessage(), e);
				}

			}
		});
		
		tglGeoEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					if(isChecked) {
						Log.d(TAG, "Geo enabled");
						ETLocationManager.locationManager().startWatchingLocation();
					}
					else {
						Log.d(TAG, "Geo disabled");
						ETLocationManager.locationManager().stopWatchingLocation();
					}
				}
				catch(ETException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		});

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
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
		case R.id.cloudpages:
			Intent intent = new Intent(this, CloudPageActivity.class);
			startActivity(intent);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void updatePreferencesForKey(String key, String value) {
		Log.i(TAG, "Setting " + key + " for " + value);
		prefs.edit().putString(key, value).apply();
	}

	private String getPreferenceForKey(String key) {
		return prefs.getString(key, null);
	}
	
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume()");
		super.onResume();

		try {
			// Let ExactTarget know when each activity resumed
			ETPush.pushManager().activityResumed(this);
		}
		catch (ETException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause()");
		super.onPause();

		try {
			// Let ExactTarget know when each activity paused
			ETPush.pushManager().activityPaused(this);
		}
		catch (ETException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	/**
	 * Example of registering with the EventBus to send us events we might care about. In this case, we get notified each time ETLocationManager
	 * finds a new current location (about every 15 minutes)
	 * @param event
	 */
	private Marker me = null;
	@Override
	public void onEvent(final LastKnownLocationEvent event) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//update text
				locationText.setText("Provider: "+event.getLocation().getProvider()+", Lat: "+event.getLocation().getLatitude()+", Lon: "+event.getLocation().getLongitude()+", Accuracy: "+event.getLocation().getAccuracy()+", Timestamp: "+new Date(event.getLocation().getTime()));
			}
		});
		
		updateMapMarker(event.getLocation().getLatitude(), event.getLocation().getLongitude());
	}

	public void updateMapMarker(final double latitude, final double longitude) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//show where we are on the map
				if(me != null) {
					me.remove();
				}
				LatLng location = new LatLng(latitude, longitude);
				me = map.addMarker(new MarkerOptions().position(location));
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f), 5000, null);
			}
		});
	}
}
