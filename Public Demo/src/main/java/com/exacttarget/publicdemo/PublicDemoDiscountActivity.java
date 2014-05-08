package com.exacttarget.publicdemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

import java.io.InputStream;
import java.util.Calendar;

public class PublicDemoDiscountActivity extends Activity {
	private int currentPage = CONSTS.DISCOUNT_ACTIVITY;

	private SharedPreferences sp;

	private static final String TAG = PublicDemoDiscountActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_demo_discount_layout);

		sp = PreferenceManager.getDefaultSharedPreferences(PublicDemoApp.context());

		getActionBar().setTitle(R.string.public_demo_discount_activity_title);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CONSTS.KEY_CURRENT_PAGE, currentPage);
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

		if (sp.contains(CONSTS.KEY_PREF_DISCOUNT_END_DATE)) {
			Calendar currDate = Calendar.getInstance();
			Calendar discountEndDate = Calendar.getInstance();
			discountEndDate.setTimeInMillis(sp.getLong(CONSTS.KEY_PREF_DISCOUNT_END_DATE, currDate.getTimeInMillis()));

			Utils.setToMidnight(currDate);
			Utils.setToMidnight(discountEndDate);

			if (discountEndDate.before(currDate)) {
				showNoDiscounts();
			}
			else {
				String message = sp.getString(CONSTS.KEY_PREF_DISCOUNT_MESSAGE, "");
				String imageFile = sp.getString(CONSTS.KEY_PREF_DISCOUNT_IMAGE_FILE, "");

				if (message.isEmpty() | imageFile.isEmpty()) {
					showNoDiscounts();
				}
				else {
					TextView messageTV = (TextView) findViewById((R.id.messageTV));
					messageTV.setText(message);

					TextView expiresTV = (TextView) findViewById((R.id.expiresTV));
					expiresTV.setText(("Expires TODAY!"));

					try {
						InputStream ims = getAssets().open(imageFile);
						// load image as Drawable
						Drawable d = Drawable.createFromStream(ims, null);

						ImageView qrIV = (ImageView) findViewById(R.id.QRcodeIV);
						qrIV.setImageDrawable(d);
					}
					catch (Exception e) {
						if (ETPush.getLogLevel() <= Log.ERROR) {
							Log.e(TAG, e.getMessage(), e);
						}
					}
				}
			}
		}
		else {
			showNoDiscounts();
		}


	}

	private void showNoDiscounts() {
		TextView messageTV = (TextView) findViewById((R.id.messageTV));
		messageTV.setText("No discounts at this time.  Stay tuned!");
		TextView expiresTV = (TextView) findViewById((R.id.expiresTV));
		expiresTV.setText("");
	}
}
