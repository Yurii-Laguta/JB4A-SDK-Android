/**
 * Copyright (c) 2015 Salesforce Marketing Cloud.
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p/>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * <p/>
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.exacttarget.jb4a.sdkexplorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exacttarget.etpushsdk.adapter.CloudPageListAdapter;
import com.exacttarget.etpushsdk.data.Message;
import com.exacttarget.jb4a.sdkexplorer.utils.Utils;

/**
 * SDK_ExplorerCloudPageInboxActivity is an activity that will display CloudPages that have been downloaded
 * by the SDK.
 *
 * @author pvandyk
 */

public class SDK_ExplorerCloudPageInboxActivity extends FragmentActivity {
    private static final String TAG = Utils.formatTag(SDK_ExplorerCloudPageInboxActivity.class.getSimpleName());
    private AdapterView.OnItemClickListener cloudPageItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "Position Clicked: " + position);
            CloudPageListAdapter adapter = (CloudPageListAdapter) parent.getAdapter();
            Message message = (Message) adapter.getItem(position);

            adapter.setMessageRead(message);

            Intent intent = new Intent(SDK_ExplorerCloudPageInboxActivity.this, SDK_ExplorerCloudPageActivity.class);
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
    private int currentPage = CONSTS.CLOUDPAGE_INBOX_ACTIVITY;
    private MyCloudPageListAdapter cloudPageListAdapter;
    private RadioGroup.OnCheckedChangeListener radioChangedListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (R.id.filterAll == checkedId) {
                cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_ALL);
            } else if (R.id.filterRead == checkedId) {
                cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_READ);
            } else if (R.id.filterUnread == checkedId) {
                cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_UNREAD);
            }
        }

    };

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

    private void prepareDisplay() {
        Utils.setActivityTitle(this, R.string.cloudpage_inbox_activity_title);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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

            LayoutInflater mInflater = (LayoutInflater) SDK_ExplorerCloudPageInboxActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                view = mInflater.inflate(R.layout.cloudpage_list_item, parent, false);
            } else {
                view = convertView;
            }

            subject = (TextView) view.findViewById(R.id.cloudpageSubject);
            icon = (ImageView) view.findViewById(R.id.readUnreadIcon);
            time = (TextView) view.findViewById(R.id.timeTextView);

            Message message = (Message) getItem(position);

            if (message.getRead()) {
                icon.setImageResource(R.drawable.read);
            } else {
                icon.setImageResource(R.drawable.unread);
            }

            subject.setText(message.getSubject());

            time.setText(android.text.format.DateFormat.format("MMM dd yyyy - hh:mm a", message.getStartDate()));

            return view;
        }
    }
}
