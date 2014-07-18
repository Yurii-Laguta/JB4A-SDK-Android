package com.exacttarget.practicefield;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.adapter.CloudPageListAdapter;
import com.exacttarget.etpushsdk.data.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * PracticeFieldCloudPageInboxActivity is an activity that will display CloudPages that have been downloaded
 * by the SDK.
 *
 * @author pvandyk
 */

public class PracticeFieldCloudPageInboxActivity extends ActionBarActivity {
	private int currentPage = CONSTS.CLOUDPAGE_INBOX_ACTIVITY;
	private MyCloudPageListAdapter cloudPageListAdapter;

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM dd yyyy - hh:mm a");

	private static final String TAG = PracticeFieldCloudPageInboxActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloudpage_inbox_layout);

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
		Utils.setActivityTitle(this, R.string.cloudpage_inbox_activity_title);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		RadioGroup filterRadioGroup;
		ListView cloudPageListView;

		filterRadioGroup = (RadioGroup) findViewById(R.id.filterRadioGroup);
		cloudPageListView = (ListView) findViewById(R.id.cloudPageListView);

		filterRadioGroup.setOnCheckedChangeListener(radioChangedListener);

		cloudPageListView.setOnItemClickListener(cloudPageItemClickListener);
		cloudPageListView.setOnItemLongClickListener(cloudPageItemDeleteListener);

		cloudPageListAdapter = new MyCloudPageListAdapter(getApplicationContext());
		cloudPageListView.setAdapter(cloudPageListAdapter);

	}

	private AdapterView.OnItemClickListener cloudPageItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.i(TAG, "Position Clicked: " + position);
			CloudPageListAdapter adapter = (CloudPageListAdapter) parent.getAdapter();
			Message message = (Message) adapter.getItem(position);

			adapter.setMessageRead(message);

			Intent intent = new Intent(PracticeFieldCloudPageInboxActivity.this, PracticeFieldCloudPageActivity.class);
			intent.putExtra("_x", message.getUrl());
			startActivity(intent);
		}

	};

	private AdapterView.OnItemLongClickListener cloudPageItemDeleteListener = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			Log.i(TAG, "Position Deleted: " + position);
			CloudPageListAdapter adapter = (CloudPageListAdapter) parent.getAdapter();
			Message message = (Message) adapter.getItem(position);

			adapter.deleteMessage(message);

			return true;
		}

	};

	private RadioGroup.OnCheckedChangeListener radioChangedListener = new RadioGroup.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(R.id.filterAll == checkedId) {
				cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_ALL);
			}
			else if(R.id.filterRead == checkedId) {
				cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_READ);
			}
			else if(R.id.filterUnread == checkedId) {
				cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_UNREAD);
			}
		}

	};

	private class MyCloudPageListAdapter extends CloudPageListAdapter {

		public MyCloudPageListAdapter(Context appContext) {
			super(appContext);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ImageView icon;
			TextView subject;
			TextView time;

			LayoutInflater	mInflater = (LayoutInflater) PracticeFieldCloudPageInboxActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				view = mInflater.inflate(R.layout.cloudpage_list_item, parent, false);
			}
			else {
				view = convertView;
			}

			subject = (TextView) view.findViewById(R.id.cloudpageSubject);
			icon = (ImageView) view.findViewById(R.id.readUnreadIcon);
			time = (TextView) view.findViewById(R.id.timeTextView);

			Message message = (Message) getItem(position);

			if(message.getRead()) {
				icon.setImageResource(R.drawable.read);
			}
			else {
				icon.setImageResource(R.drawable.unread);
			}

			subject.setText(message.getSubject());

			DateTime eventTime = new DateTime(message.getStartDate());
			time.setText(dateTimeFormatter.print(eventTime));

			return view;
		}
	}
}
