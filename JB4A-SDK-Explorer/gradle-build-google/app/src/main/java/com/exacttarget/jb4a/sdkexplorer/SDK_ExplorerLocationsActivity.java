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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.exacttarget.etpushsdk.util.EventBus;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.CirclePageIndicator;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.PageIndicator;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.ScrollPagesAdapter;
import com.exacttarget.jb4a.sdkexplorer.utils.Utils;

/**
 * SDK_ExplorerBeaconsActivity will provide an overview of how to receive messages for Beacons.
 *
 * @author pvandyk
 */

public class SDK_ExplorerLocationsActivity extends FragmentActivity {

    private static final String TAG = Utils.formatTag(SDK_ExplorerLocationsActivity.class.getSimpleName());
    ScrollPagesAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    String[] pages = new String[]{"0"};
    private int currentPage = CONSTS.LOCATION_ACTIVITY;

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
        EventBus.getInstance().unregister(this);
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

        mAdapter = new ScrollPagesAdapter(getSupportFragmentManager(), pages, false);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
