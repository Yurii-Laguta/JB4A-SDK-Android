package com.exacttarget.demo.etsdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

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



}
