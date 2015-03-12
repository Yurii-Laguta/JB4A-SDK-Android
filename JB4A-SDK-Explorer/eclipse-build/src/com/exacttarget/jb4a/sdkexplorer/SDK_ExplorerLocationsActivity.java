/**
 * Copyright (c) 2014 ExactTarget, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
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

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.exacttarget.etpushsdk.util.EventBus;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.CirclePageIndicator;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.PageIndicator;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.ScrollPagesAdapter;

/**
 * SDK_ExplorerBeaconsActivity will provide an overview of how to receive messages for Beacons.
 *
 * @author pvandyk
 */

public class SDK_ExplorerLocationsActivity extends BaseActivity {

	private int currentPage = CONSTS.BEACONS_ACTIVITY;

	private static final String TAG = SDK_ExplorerLocationsActivity.class.getName();

	ScrollPagesAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	String[] pages = new String[] {"0", "1", "2", "3", "4", "5"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll_pages);
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

		Utils.setActivityTitle(this, R.string.locations_activity_title);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		prepareDisplay();
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

		StringBuilder sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Geo-fences - Overview</b><br/>");
		sb.append("<p>");
		sb.append("The JB4A SDK Explorer has been set with Entry and Exit geo-fence messages for the following three locations:");
		sb.append("</p>");
		sb.append("<ul>");
		sb.append("<li>Grand Canyon National Park (Coordinates: 36.106965, -112.112997)</li><br/>");
		sb.append("<li>Yellowstone National Park (Coordinates: 44.598884, -110.499898)</li><br/>");
		sb.append("<li>Yosemite Valley (Coordinates: 37.74856, -119.588113)</li>");
		sb.append("</ul>");
		sb.append("<p>");
		sb.append("In order to see how Geo-fences work within this app, you can use a Mock GPS tool to set your location on your device to one of the these locations.");
		sb.append("</p>");
		pages[0] = sb.toString();

		sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Beacons - Overview</b><br/>");
		sb.append("<p>");
		sb.append("The JB4A SDK Explorer has been set with several Beacon messages. In order to see how Beacons work, you will need to purchase several Beacons and configure them to work with the JB4A SDK Explorer.");
		sb.append("</p>");
		sb.append("<p>");
		sb.append("Each Beacon can be assigned a unique GUID as well as a Major and Minor number. For any Beacons you purchase, make sure that the Beacons can be configured.  You must be able to edit the GUID as well as the Major and Minor number.");
		sb.append("</p>");
		sb.append("<p>");
		sb.append("Messages are fired when you are in proximity of a Beacon.  Different messages can be fired when you are in the following ranges:");
		sb.append("</p>");
		sb.append("<ul>");
		sb.append("<li>Immediate (2-3 inches)</li>");
		sb.append("<li>Near (2-3 feet)</li>");
		sb.append("<li>Far (Up to 50 feet)</li>");
		sb.append("</ul>");
		sb.append("<p>");
		sb.append("To see how Beacons work, we have setup 4 Beacons with a GUID of 2f234454-cf6d-4a0f-adf2-f4911ba9ffa6.  The following pages describe the messages associated with each Beacon.");
		sb.append("</p>");
		pages[1] = sb.toString();

		sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Beacon 1</b><br/>");
		sb.append("This Beacon is setup with a GUID of 2f234454-cf6d-4a0f-adf2-f4911ba9ffa6 and a Major number of 1 and Minor number of 1.<br/>");
		sb.append("<br/>");
		sb.append("The following messages have been setup that are unlimited:<br/>");
		sb.append("<ul>");
		sb.append("<li>IMMEDIATE 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 1</li><br/>");
		sb.append("<li>NEAR 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 1</li><br/>");
		sb.append("<li>FAR 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 1</li><br/>");
		sb.append("</ul>");
		pages[2] = sb.toString();

		sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Beacon 2</b><br/>");
		sb.append("This Beacon is setup with a GUID of 2f234454-cf6d-4a0f-adf2-f4911ba9ffa6 and a Major number of 1 and Minor number of 2.<br/>");
		sb.append("<br/>");
		sb.append("The following messages have been setup to fire at most once per hour:<br/>");
		sb.append("<ul>");
		sb.append("<li>IMMEDIATE 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 2</li><br/>");
		sb.append("</ul>");

		pages[3] = sb.toString();

		sb = new StringBuilder();
		sb.append(CONSTS.PAGE_TITLE);
		sb.append("<b>Beacon 3</b><br/>");
		sb.append("This Beacon is setup with a GUID of 2f234454-cf6d-4a0f-adf2-f4911ba9ffa6 and a Major number of 1 and Minor number of 3.<br/>");
		sb.append("<br/>");
		sb.append("The following messages have been setup to fire at most once per day:<br/>");
		sb.append("<ul>");
		sb.append("<li>IMMEDIATE 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 3</li><br/>");
		sb.append("<li>NEAR 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 3</li><br/>");
		sb.append("</ul>");
		pages[4] = sb.toString();

		sb = new StringBuilder();
		sb.append("<b>Beacon 4</b><br/>");
		sb.append("This Beacon is setup with a GUID of 2f234454-cf6d-4a0f-adf2-f4911ba9ffa6 and a Major number of 1 and Minor number of 4.<br/>");
		sb.append("<br/>");
		sb.append("The following messages have been setup to fire once ever:<br/>");
		sb.append("<ul>");
		sb.append("<li>IMMEDIATE 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 4</li><br/>");
		sb.append("<li>FAR 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 Major 1 Minor 4</li><br/>");
		sb.append("</ul>");
		pages[5] = sb.toString();

		mAdapter = new ScrollPagesAdapter(getSupportFragmentManager(), pages, false);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}
}
