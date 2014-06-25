package com.exacttarget.practicefield;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

/**
 * PracticeFieldOpenDirectActivity is an activity that will display the URL sent with the payload
 * of the message sent from the Marketing Cloud.
 *
 * This activity extends ETLandingPagePresenter to provide some additional functions that the
 * ETLandingPagePresenter does not do such as creating a Home button in the Action bar as well
 * as providing analytics in the onResume() and onPause().
 *
 * @author pvandyk
 */

public class PracticeFieldOpenDirectActivity extends ActionBarActivity {
	private int currentPage = CONSTS.OPENDIRECT_ACTIVITY;
	private static final String TAG = PracticeFieldOpenDirectActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		prepareDisplay();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CONSTS.KEY_CURRENT_PAGE, currentPage);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Boolean result = Utils.selectMenuItem(this, currentPage, item);
		return result != null ? result : super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
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

	private void prepareDisplay() {
		Utils.setActivityTitle(this, "Loading...");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		ll.setGravity(Gravity.CENTER);

		WebView webView = new WebView(this);
		webView.loadUrl(this.getIntent().getExtras().getString("_od"));
		webView.getSettings().setJavaScriptEnabled(true);
		ll.addView(webView);

		webView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				Utils.setActivityTitle(PracticeFieldOpenDirectActivity.this, view.getTitle());
			}
		});

		setContentView(ll);

	}
}
