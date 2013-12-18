package com.exacttarget.demo.etsdkdemo;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLandingPagePresenter;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.adapter.CloudPageListAdapter;
import com.exacttarget.etpushsdk.data.CloudPagesResponse;
import com.exacttarget.etpushsdk.data.Message;
import com.exacttarget.etpushsdk.util.EventBus;

public class CloudPageActivity extends Activity {

	private static final String TAG = "CloudPageActivity";
	
	private RadioGroup filterRadioGroup;
	private Button addCloudPageButton;
	private ListView cloudPageListView;
	private MyCloudPageListAdapter cloudPageListAdapter;
	
	private static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
															.appendHours().minimumPrintedDigits(0)
															.appendSuffix(" hr", " hrs")
															.appendSeparatorIfFieldsBefore(", ")
															.appendMinutes().minimumPrintedDigits(1).printZeroAlways()
															.appendSuffix(" min", " mins")
															.appendLiteral(" ago")
															.toFormatter();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudpage);

    	filterRadioGroup = (RadioGroup) findViewById(R.id.filterRadioGroup);
    	addCloudPageButton = (Button) findViewById(R.id.addCloudPage);
    	cloudPageListView = (ListView) findViewById(R.id.cloudPageListView);
    	
    	filterRadioGroup.setOnCheckedChangeListener(radioChangedListener);
    	
    	addCloudPageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//let's pretend a cloudpage message got downloaded
				CloudPagesResponse response = new CloudPagesResponse();
				ArrayList<Message> messages = new ArrayList<Message>();
				Message msg = new Message();
				msg.setId("kdkdkdk");
				msg.setMessageType(Message.MESSAGE_TYPE_BASIC);
				msg.setContentType(Message.CONTENT_TYPE_PAGE);
				msg.setStartDate(new Date());
				msg.setSubject("CloudPage Message");
				msg.setUrl("http://www.google.com");
				msg.setRead(Boolean.FALSE);
				messages.add(msg);
				response.setMessages(messages);
				EventBus.getDefault().post(response);
			}
		});
    	
    	cloudPageListView.setOnItemClickListener(cloudPageItemClickListener);
    	cloudPageListView.setOnItemLongClickListener(cloudPageItemDeleteListener);
    	
    	cloudPageListAdapter = new MyCloudPageListAdapter(getApplicationContext());
    	cloudPageListView.setAdapter(cloudPageListAdapter);
    }
    
    private OnItemClickListener cloudPageItemClickListener = new OnItemClickListener() {

    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		Log.i(TAG, "Position Clicked: " + position);
    		CloudPageListAdapter adapter = (CloudPageListAdapter) parent.getAdapter();
    		Message message = (Message) adapter.getItem(position);

    		adapter.setMessageRead(message);
    		
    		Intent intent = new Intent(CloudPageActivity.this, ETLandingPagePresenter.class);
    		intent.putExtra("loadURL", message.getUrl());
    		startActivity(intent);
    	}
    	
	};
	
	private OnItemLongClickListener cloudPageItemDeleteListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    		Log.i(TAG, "Position Deleted: " + position);
    		CloudPageListAdapter adapter = (CloudPageListAdapter) parent.getAdapter();
    		Message message = (Message) adapter.getItem(position);

    		adapter.deleteMessage(message);
			
			return true;
		}
		
	};
    
    private OnCheckedChangeListener radioChangedListener = new OnCheckedChangeListener(){

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
			LayoutInflater mInflater = null;
			View view;
			ImageView icon;
			TextView subject;
			TextView time;
			boolean inflated = false;
			
			if(mInflater == null) {
				 mInflater = (LayoutInflater) CloudPageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}

			if (convertView == null) {
				view = mInflater.inflate(R.layout.cloudpage_list_item, parent, false);
				inflated = true;
			}
			else {
				view = convertView;
			}

			subject = (TextView) view.findViewById(R.id.cloudpageSubject);
			icon = (ImageView) view.findViewById(R.id.readUnreadIcon);
			time = (TextView) view.findViewById(R.id.timeTextView);

			if (inflated) {
				//one-time setup stuff for the view here
			}

			Message message = (Message) getItem(position);
			
			if(message.getRead()) {
				icon.setImageResource(R.drawable.read);
			}
			else {
				icon.setImageResource(R.drawable.unread);
			}
			
			subject.setText(message.getSubject());
			DateTime eventTime = new DateTime(message.getStartDate());
			Period period = new Period(eventTime, DateTime.now());
			String timeString = periodFormatter.print(period);
			if(" ago".equals(timeString)) {
				timeString = "just now";
			}
			time.setText(timeString);
			
			return view;
		}
    	
    };
    
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
