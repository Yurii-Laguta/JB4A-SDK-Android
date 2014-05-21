package com.exacttarget.publicdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.util.EventBus;

/**
 * PublicDemoAboutActivity will display information about the PublicDemo App.
 *
 * This activity extends Activity to provide key information about the app that is running.
 * *
 * @author pvandyk
 */

public class PublicDemoAboutActivity extends ActionBarActivity {

	private int currentPage = CONSTS.ABOUT_ACTIVITY;

	private static final String TAG = PublicDemoAboutActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_demo_about_layout);

		getSupportActionBar().setTitle(R.string.public_demo_about_activity_title);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CONSTS.KEY_CURRENT_PAGE, currentPage);
	}

	@Override
	protected void onDestroy() {
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
		Utils.setWebView (this, R.id.aboutWV, Utils.formatAboutPage(), true);
	}
}
