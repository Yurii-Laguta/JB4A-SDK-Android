package com.exacttarget.demo.etsdkdemo;

import com.exacttarget.etpushsdk.ETAnalytics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class OpenDirectDemo extends Activity {
	
	private static final String TAG = "OpenDirectDemo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_direct_demo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_open_direct_demo, menu);
		return true;
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
