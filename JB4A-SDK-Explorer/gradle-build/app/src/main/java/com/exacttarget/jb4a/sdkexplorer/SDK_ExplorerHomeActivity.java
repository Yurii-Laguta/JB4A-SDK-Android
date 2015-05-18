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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.CirclePageIndicator;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.PageIndicator;
import com.exacttarget.jb4a.sdkexplorer.scrollpages.ScrollPagesAdapter;
import com.exacttarget.jb4a.sdkexplorer.utils.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * SDK_ExplorerHomeActivity is the primary activity in the JB4A SDK Explorer.
 * <p/>
 * This activity extends Activity to provide the primary interface for user interaction.
 * <p/>
 * It calls several methods in order to link to the JB4A Android SDK:
 * <p/>
 * 1) To get notified of events that occur within the SDK, call
 * EventBus.getInstance().register() in onCreate() and
 * EventBus.getInstance().unregister(); in onDestroy()
 * <p/>
 * 2) To ensure that registrations stay current with Google Cloud Messaging,
 * call ETPush.getInstance().enablePush() if push is enabled for this
 * device.  You would call ETPush.getInstance().isPushEnabled() to determine
 * if push is enabled.
 * <p/>
 * 3) To provide analytics about the usage of your app, call ETPush.getInstance().activityResumed();
 * in onResume() and ETPush.getInstance().activityPaused() in onPause().
 *
 * @author pvandyk
 */

public class SDK_ExplorerHomeActivity extends BaseActivity {

    private static final int currentPage = CONSTS.HOME_ACTIVITY;
    private static final String TAG = Utils.formatTag(SDK_ExplorerHomeActivity.class.getSimpleName());

    ScrollPagesAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    String[] pages = new String[]{"0", "1", "2", "3"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_pages);

        if (ETPush.getLogLevel() <= Log.DEBUG) {
            Log.i(TAG, "start onCreate()");
        }
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

        Utils.setActivityTitle(this, R.string.home_activity_title);

        prepareDisplay();
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
        return Utils.selectMenuItem(this, currentPage, item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void prepareDisplay() {
        StringBuilder sb = new StringBuilder();
        // show information about the JB4A SDK Explorer
        sb.append(CONSTS.PAGE_TITLE);
        sb.append("<b>Overview</b><br/>");
        sb.append("<hr>");
        sb.append("The SDK Explorer allows you to explore how to use the Journey Builder for Apps (JB4A) SDK.");
        sb.append("<br/><br/>");
        sb.append("The JB4A SDK is a key component of ");
        sb.append("<a href=\"http://www.exacttarget.com/products/mobile-marketing\">Mobile Marketing</a> for your company.<br/>");
        pages[0] = sb.toString();

        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);
        sb.append("<b>Purpose</b><br/>");
        sb.append("<hr>");
        sb.append("<ul>");
        sb.append("<li>Provides a way to explore the features of the Journey Builder for Apps (JB4A) SDK.</li><br/>");
        sb.append("<li>Provides an example or template for creating an Android app that uses the JB4A SDK.</li><br/>");
        sb.append("<li>Allows you to review the JB4A SDK components by collecting debugging information and sharing via email.</li><br/>");
        sb.append("</ul>");
        pages[1] = sb.toString();

        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);
        sb.append("<b>Additional Resources</b><br/>");
        sb.append("<hr>");
        sb.append("The following resources are available to learn more about using the Journey Builder for Apps SDK.  They are not required to run this app, but are available to assist you in developing an app using the JB4A SDK.<br/>");
        sb.append("<br/>");
        sb.append("<b>Code@</b><br/>");
        sb.append("For more information about the JB4A SDK, see ");
        sb.append("<a href=\"https://code.exacttarget.com/api/mobilepush-sdks\">Code@</a><br/>");
        sb.append("<br/>");
        sb.append("<b>gitHub</b><br/>");
        sb.append("To view or use the code for this JB4A SDK Explorer, please see the gitHub repository for the Journey Builder for Apps SDK found ");
        sb.append("<a href=\"https://github.com/ExactTarget/MobilePushSDK-Android\">here</a>");
        sb.append(" and then open the JB4A_SDK_Explorer folder.<br/>");
        pages[2] = sb.toString();

        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);
        sb.append("<b>Using this App</b><br/>");
        sb.append("<hr>");
        sb.append("<ul>");
        sb.append("<li>Open Preferences to add your name and then enable Push Notifications.</li><br/>");
        sb.append("<li>Wait 15 minutes to ensure your settings have been registered.</li><br/>");
        sb.append("<li>Open Send Message to send messages to this device.</li><br/>");
        sb.append("<li>After receiving the notification, you can view the payload using Last Message.</li><br/>");
        sb.append("<li>If you would like to see debugging statements in the Android logcat, turn on debugging in Debug Settings.</li><br/>");
        sb.append("<li>You also send the database and the logcat to an email address in Debug Settings.</li><br/>");
        sb.append("</ul>");
        pages[3] = sb.toString();

        mAdapter = new ScrollPagesAdapter(getSupportFragmentManager(), pages, false);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
