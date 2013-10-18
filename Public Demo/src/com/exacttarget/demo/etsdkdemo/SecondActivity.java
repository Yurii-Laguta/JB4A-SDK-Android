package com.exacttarget.demo.etsdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.exacttarget.etpushsdk.ETAnalytics;

public class SecondActivity extends Activity {

	private static final String TAG = "SecondActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
