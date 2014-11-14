package com.exacttarget.jb4a.sdkexplorer;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

/**
 * Created by pvandyk on 10/23/14.
 */
public class BaseActivity extends ActionBarActivity {

	private static final String TAG = BaseActivity.class.getName();

	@Override
	protected void onPause() {
		super.onPause();

		try {
			// Let JB4A SDK know when each activity paused
			ETPush.pushManager().activityPaused(this);
		}
		catch (Exception e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			// Let JB4A SDK know when each activity is resumed
			ETPush.pushManager().activityResumed(this);
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}
}
