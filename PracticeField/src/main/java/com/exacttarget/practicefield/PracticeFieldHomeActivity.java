package com.exacttarget.practicefield;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.event.RegistrationEvent;
import com.exacttarget.etpushsdk.util.EventBus;
import com.exacttarget.practicefield.scrollpages.CirclePageIndicator;
import com.exacttarget.practicefield.scrollpages.PageIndicator;
import com.exacttarget.practicefield.scrollpages.ScrollPagesAdapter;

/**
 * PracticeFieldHomeActivity is the primary activity in the PracticeField App.
 * <p/>
 * This activity extends Activity to provide the primary interface for user interaction.
 * <p/>
 * It calls several methods in order to link to the ET Android SDK:
 * <p/>
 * 1) To get notified of events that occur within the SDK, call
 * EventBus.getDefault().register() in onCreate() and
 * EventBus.getDefault().unregister(); in onDestroy()
 * <p/>
 * 2) To ensure that registrations stay current with Google Cloud Messaging,
 * call ETPush.pushManager().enablePush() if push is enabled for this
 * device.  You would call ETPush.pushManager().isPushEnabled() to determine
 * if push is enabled.
 * <p/>
 * 3) To provide analytics about the usage of your app, call ETPush.pushManager().activityResumed();
 * in onResume() and ETPush.pushManager().activityPaused() in onPause().
 *
 * @author pvandyk
 */

public class PracticeFieldHomeActivity extends ActionBarActivity {

	private static final int currentPage = CONSTS.HOME_ACTIVITY;
	private static final String TAG = PracticeFieldHomeActivity.class.getName();

	ScrollPagesAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	String[] pages = new String[] {"0", "1", "2", "3"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll_pages);

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
		Utils.setActivityTitle(this, R.string.home_activity_title);
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
		// show information about this demo app
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Overview</b><br/>");
		sb.append("<hr>");
		sb.append("This is an app where you can practice using the ExactTarget MobilePush SDK.");
		sb.append("<br/><br/>");
		sb.append("The ExactTarget MobilePush SDK is a key component of ");
		sb.append("<a href=\"http://www.exacttarget.com/products/mobile-marketing\">Mobile Marketing</a> for your company.<br/>");
		pages[0] = sb.toString();

		sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Purpose</b><br/>");
		sb.append("<hr>");
		sb.append("<ul>");
		sb.append("<li>Provides a UI for practicing with various features of the ET MobilePush SDK.</li><br/>");
		sb.append("<li>Provides an example or template for creating an Android app that use the ExactTarget MobilePush SDK.</li><br/>");
		sb.append("<li>Allows you to review the SDK components by collecting debugging information and sharing via email.</li><br/>");
		sb.append("</ul>");
		pages[1] = sb.toString();

		sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Additional Resources</b><br/>");
		sb.append("<hr>");
		sb.append("The following resources are available to learn more about using the ET MobilePush SDK.  They are not required to run this app, but are available to assist you in developing an app using the ET MobilePush SDK.<br/>");
		sb.append("<br/>");
		sb.append("<b>Code@</b><br/>");
		sb.append("For more information about the ET MobilePush SDK, see ");
		sb.append("<a href=\"https://code.exacttarget.com/api/mobilepush-sdks\">Code@</a><br/>");
		sb.append("<br/>");
		sb.append("<b>gitHub</b><br/>");
		sb.append("To view or use the code for this Practice Field app, please see the gitHub repository for the ET MobilePush Android SDK found ");
		sb.append("<a href=\"https://github.com/ExactTarget/MobilePushSDK-Android\">here</a>");
		sb.append(" and then open the PracticeField folder.<br/>");
		pages[2] = sb.toString();

		sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Using this App</b><br/>");
		sb.append("<hr>");
		sb.append("<ul>");
		sb.append("<li>Open Preferences to add your name and then enable Push Notifications.</li><br/>");
		sb.append("<li>Wait 15 minutes to ensure your settings have been registered.</li><br/>");
		sb.append("<li>Open Send Message to send messages to this device.</li><br/>");
		sb.append("<li>After receiving the notification, you can view the payload using Last Message.</li><br/>");
		sb.append("<li>If you would like to see debugging statements in the Android logcat, turn on debugging in Debug Settings.</li><br/>");
		sb.append("<li>You also send the database and the logcat to an email address in Debug Settings.</li><br/>");
		sb.append("</ul>");
		pages[3] = sb.toString();

		mAdapter = new ScrollPagesAdapter(getSupportFragmentManager(), pages, false);

		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
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
