package com.exacttarget.publicdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLocationManager;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.data.Attribute;
import com.exacttarget.etpushsdk.event.RegistrationEvent;
import com.exacttarget.etpushsdk.util.EventBus;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * PublicDemoHomeActivity is the primary activity in the PublicDemo App.
 *
 * This activity extends Activity to provide the primary interface for user interaction.
 *
 * It calls several methods in order to link to the ET Android SDK:
 *
 * 		1) To get notified of events that occur within the SDK, call
 * 	       EventBus.getDefault().register() in onCreate() and
 * 	       EventBus.getDefault().unregister(); in onDestroy()
 *
 * 	    2) To ensure that registrations stay current with Google Cloud Messaging,
 * 	       call ETPush.pushManager().enablePush() if push is enabled for this
 * 	       device.  You would call ETPush.pushManager().isPushEnabled() to determine
 * 	       if push is enabled.
 *
 * 	    3) To provide analytics about the usage of your app, call ETPush.pushManager().activityResumed();
 * 	       in onResume() and ETPush.pushManager().activityPaused() in onPause().
 *
 * @author pvandyk
 */

public class PublicDemoHomeActivity extends ActionBarActivity {

	private static final int currentPage = CONSTS.HOME_ACTIVITY;
	private static final String TAG = PublicDemoHomeActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_demo_home_layout);

		getSupportActionBar().setTitle(R.string.public_demo_home_activity_title);

		// EventBus.getDefault()
		//
		//		Register this Activity to process events from the SDK.
		//		Don't forget to unregister this in onDestroy()
		EventBus.getDefault().register(this);

		try {
			if (ETPush.pushManager().isPushEnabled()) {

				// ETPush.pushManager().enablePush()
				//
				//		GCM requires each app to opt in when a version changes.
				//		This will be handled by the SDK, so calling enablePush at the start of this activity ensures no one drops off without intentionally requesting to be removed
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
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CONSTS.KEY_CURRENT_PAGE, currentPage);
	}

	@Override
	protected void onDestroy() {
		// EventBus.getDefault().unregister()
		//
		//		Unregister this Activity from processing events from the SDK.
		//		Only called since EventBus.getDefault().register() is called in onCreate()
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

	private void prepareDisplay() {

		StringBuilder sb = new StringBuilder();

		Button sendMessages = (Button) findViewById(R.id.sendMessagesButton);
		TextView sep = (TextView) findViewById(R.id.separator);

		boolean pushEnabled = false;
		try {
			pushEnabled = ETPush.pushManager().isPushEnabled();
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		// PRODUCTION OR DEVELOPMENT??
		if (CONSTS_API.isDevelopment()) {
			sb.append("<b>Using Development App Keys</b>");
		}
		else {
			sb.append("<b>Using Production App Keys</b>");
		}
		sb.append("<br/><br/>");

		// get the Attributes saved with ExactTarget registration for this device
		ArrayList<Attribute> attributes;
		try {
			attributes = ETPush.pushManager().getAttributes();
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
			attributes = new ArrayList<Attribute>();
		}

		Attribute firstNameAttrib = getAttribute(attributes, CONSTS.KEY_ATTRIB_FIRST_NAME);
		Attribute lastNameAttrib = getAttribute(attributes, CONSTS.KEY_ATTRIB_LAST_NAME);

		if (firstNameAttrib != null && lastNameAttrib != null) {
			// have settings!
			// the settings activity ensures that no other settings can be set until the first and last name are set.

			// show the settings that have been set

			// PERSONAL SETTINGS
			sb.append("<b>Attributes</b>");

			// FIRST NAME
			sb.append("<br/>");
			sb.append("<i>First Name:</i>  ");
			sb.append(firstNameAttrib.getValue());

			// LAST NAME
			sb.append("<br/>");
			sb.append("<i>Last Name:</i>  ");
			sb.append(lastNameAttrib.getValue());

			// NOTIFICATION SETTINGS
			sb.append("<br/><br/>");
			sb.append("<b>Notification Settings</b>");

			// PUSH ENABLED
			sb.append("<br/>");
			sb.append("<i>Push Enabled:</i>  ");
			sb.append(pushEnabled);

			// LOCATION ENABLED
			sb.append("<br/>");
			sb.append("<i>Location (Geo Fencing) Enabled:</i>  ");

			boolean locationEnabled = false;
			try {
				locationEnabled = ETLocationManager.locationManager().isWatchingLocation();
				sb.append(locationEnabled);
			}
			catch (ETException e) {
				if (ETPush.getLogLevel() <= Log.ERROR) {
					Log.e(TAG, e.getMessage(), e);
				}
				sb.append("Error determining if Location (Geo Fencing) is enabled.");
			}

			// get the tags that have been saved with ExactTarget registration for this device
			HashSet<String> tags;
			try {
				tags = ETPush.pushManager().getTags();
			}
			catch (ETException e) {
				if (ETPush.getLogLevel() <= Log.ERROR) {
					Log.e(TAG, e.getMessage(), e);
				}
				tags = new HashSet<String>();
			}

			if (pushEnabled | locationEnabled) {
				// NFL SUBSCRIPTIONS
				sb.append("<br/><br/>");
				sb.append("<b>NFL Subscriptions (Tags)</b>");

				String[] nflTeamNames = getResources().getStringArray(R.array.nfl_teamNames);
				String[] nflTeamKeys = getResources().getStringArray(R.array.nfl_teamKeys);

				int num_NFL_subs = 0;
				for (int i = 0; i < nflTeamNames.length; i++) {
					if (tags.contains(nflTeamKeys[i])) {
						setSubLine(sb, nflTeamNames[i]);
						num_NFL_subs++;
					}
				}

				if (num_NFL_subs == 0) {
					sb.append("<br/>");
					sb.append("No NFL subscriptions.");
				}

				// SOCCER SUBSCRIPTIONS
				sb.append("<br/><br/>");
				sb.append("<b>FC Subscriptions (Tags)</b>");

				String[] fcTeamNames = getResources().getStringArray(R.array.fc_teamNames);
				String[] fcTeamKeys = getResources().getStringArray(R.array.fc_teamKeys);

				int numSoccerSubs = 0;
				for (int i = 0; i < fcTeamNames.length; i++) {
					if (tags.contains(fcTeamKeys[i])) {
						setSubLine(sb, fcTeamNames[i]);
						numSoccerSubs++;
					}
				}

				if (numSoccerSubs == 0) {
					sb.append("<br/>");
					sb.append("No FC subscriptions.");
				}
			}

			if (pushEnabled) {
				sendMessages.setVisibility(View.VISIBLE);
				sep.setVisibility(View.VISIBLE);

				sendMessages.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						PublicDemoSendMessagesDialog smDialog = new PublicDemoSendMessagesDialog(PublicDemoHomeActivity.this);
						smDialog.setCancelable(true);
						smDialog.show();
					}
				});
			}
			else {
				sendMessages.setVisibility(View.GONE);
				sep.setVisibility(View.GONE);
			}
		}
		else {

			sendMessages.setVisibility(View.GONE);
			sep.setVisibility(View.GONE);

			// show information about this demo app
			sb.append("<b>ET Public Demo App for MobilePush SDK</b><br/>");
			sb.append("<br/>");
			sb.append("This is the PublicDemo App for the MobilePush SDK a key component of ");
			sb.append("<a href=\"http://www.exacttarget.com/products/mobile-marketing\">Mobile Marketing</a> for your company.<br/>");
			sb.append("<br/>");
			sb.append("This App serves the following purposes:<br/>");
			sb.append("<ul>");
			sb.append("<li>Provides an example or template for creating Android apps that use the ExactTarget MobilePush SDK.</li><br/>");
			sb.append("<li>Provides a UI for testing various features of the MobilePush SDK.</li><br/>");
			sb.append("<li>Provides a mechanism to collect and send debugging information.</li>");
			sb.append("</ul>");
			sb.append("For more information about the MobilePush SDK, see ");
			sb.append("<a href=\"https://code.exacttarget.com/api/mobilepush-sdks\">Code@</a><br/>");
			sb.append("<br/>");
			sb.append("To see the code for this Public Demo, please see the gitHub repository for the Android MobilePush SDK found ");
			sb.append("<a href=\"https://github.com/ExactTarget/MobilePushSDK-Android\">here</a>");
			sb.append(" and then open the Public Demo folder.<br/>");
			sb.append("<br/>");
			sb.append("In order to use this app, you will need to open the Settings screen.  Add your name and then enable Push and Location Settings.  Then add several teams to your subscription list.<br/>");
			sb.append("<br/>");
			sb.append("It will take approximately 15 minutes for your Push registration to take effect.  Once you have waited at least 15 minutes, you can click on the Send Messages button which will appear on the bottom of this screen once Settings are completed.<br/>");
			sb.append("<br/>");
			sb.append("Normally, sending messages to devices is done from the Marketing Cloud.  However, in order to provide a fully functional demo, you are able to send a message to yourself (as if you had sent it from the Marketing Cloud).<br/>");
			sb.append("<br/>");
			sb.append("You may also want to be notified about entering or exiting specific locations.  See the Public Demo README file on ");
			sb.append("<a href=\"https://github.com/ExactTarget/MobilePushSDK-Android\">gitHub</a> for more information.<br/>");

			// no settings have been set, show that message and ask if they want to set them.
			// Show Settings set so far

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("Add Settings");
			String message = "This is the ET Public Demo App for the MobilePush SDK.\n\nPress Cancel for further information.\n\nOtherwise, press Settings to add attributes, enable Push and Location Notifications, and add teams to your subscription list.";
			builder.setMessage(message);
			builder.setNegativeButton("Cancel", null);
			builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent settingsActivity = new Intent(PublicDemoHomeActivity.this, PublicDemoSettingsActivity.class);
					startActivity(settingsActivity);
				}
			});
			builder.create().show();
		}

		Utils.setWebView(this, R.id.homeWV, sb, false);
	}

	private void setSubLine(StringBuilder sb, String teamName) {
		sb.append("<br/>");
		sb.append(teamName);
	}

	private Attribute getAttribute(ArrayList<Attribute> attributes, String key) {
		for (Attribute attribute : attributes) {
			if (attribute.getKey().equals(key)) {
				return attribute;
			}
		}
		return null;
	}

	// onEvent(RegistrationEvent event)
	//
	//		This method is one of several methods for getting notified when an event
	//      occurs in the SDK.
	//
	//		They are all called onEvent(), but will have a different parameter to indicate
	//		the event that has occurred.
	//
	//		RegistrationEvent will be triggered when the SDK receives the response from the
	// 		registration as triggered by the com.google.android.c2dm.intent.REGISTRATION intent.
	//
	//		These events are only called if EventBus.getDefault().register() is called in onCreate()
	//		for this activity.
	//
	public void onEvent(final RegistrationEvent event) {
		Log.i(TAG, "Registration occurred.  You could now save registration details in your own data stores...");
		Log.i(TAG, "Device ID:" + event.getDeviceId());
		Log.i(TAG, "Device Token:" + event.getDeviceToken());
		Log.i(TAG, "Subscriber key:" + event.getSubscriberKey());
	}

}
