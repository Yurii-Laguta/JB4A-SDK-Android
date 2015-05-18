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

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.exacttarget.etpushsdk.ETNotificationBuilder;
import com.exacttarget.etpushsdk.ETNotificationLaunchIntent;
import com.exacttarget.etpushsdk.ETNotifications;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.ETPushConfig;
import com.exacttarget.etpushsdk.data.Attribute;
import com.exacttarget.etpushsdk.event.CloudPagesResponse;
import com.exacttarget.etpushsdk.event.ReadyAimFireInitCompletedEvent;
import com.exacttarget.etpushsdk.event.RegistrationEvent;
import com.exacttarget.etpushsdk.util.ETLogger;
import com.exacttarget.etpushsdk.util.EventBus;
import com.exacttarget.jb4a.sdkexplorer.utils.Utils;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * SDK_ExplorerApp is the primary application class.
 * <p/>
 * This class extends Application to provide global activities for the Journey Builder for Apps (JB4A) SDK Explorer.
 * <p/>
 * It calls several methods in order to link to the JB4A Android SDK:
 * <p/>
 * 1) To set the logging level, call ETPush.setLogLevel().  An example is provided that
 * checks if this application is a test or production version.	As well, we have provided
 * an example of storing the application keys in a separate class in order to provide
 * centralized access to these keys and to ensure you use the appropriate keys when
 * compiling the test and production versions.  This is not part of the SDK, but is shown
 * as a way to assist in managing these keys (CONSTS_API.setDevelopment()).
 * <p/>
 * 2) ETPush.readyAimFire() should be the first call within this app after setting the LogLevel().
 * Here you pass in the AppId and AccessToken.  These values are taken from the Marketing Cloud
 * definition for your app.  Here you also set whether you enable LocationManager, CloudPages, and
 * Analytics.
 * <p/>
 * When ReadyAimFire() is called for the first time for a device, it will get a device token from
 * Google or Amazon and send to the MarketingCloud.
 * <p/>
 * As well, the ETPackageReplacedReceiver will ensure that a new device token is retrieved from
 * Google or Amazon when a new version of your app is installed.  However, it will only initiate
 * the send when the user opens the app and your app calls readyAimFire()
 * <p/>
 * 3) Several optional methods are called as well, depending on the design of your app.
 *
 * @author pvandyk
 */

public class SDK_ExplorerApp extends Application {

    public static final String ITEM_SPOTLIGHT = "item_spotlight";
    public static final String ONE_DAY_SALE = "one_day_sale";
    private static final String TAG = Utils.formatTag(SDK_ExplorerApp.class.getSimpleName());
    private static Context appContext;
    private static boolean quitAppNow = false;

    private static String deviceId = null;

    public static Context context() {
        return appContext;
    }

    public static void setQuitAppNow() {
        quitAppNow = true;
    }

    public static boolean getQuitAppNow() {
        return quitAppNow;
    }

    public static String getDeviceId() {
        return deviceId;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;

        //only needed until beacons is turned back on
        SharedPreferences prefs = getSharedPreferences("ETPush", Context.MODE_PRIVATE);
        if (prefs.contains("et_proximity_enabled_key")) {
            boolean oldPEK = prefs.getBoolean("et_proximity_enabled_key", false);

            if (oldPEK) {
                prefs.edit().putBoolean("et_proximity_enabled_key", false).commit();
            }
        }

        // EventBus.getInstance()
        //
        //		Register this Application to process events from the SDK.
        EventBus.getInstance().register(this);

        // Initialize Salesforce Journey Builder for Apps SDK
        JB4A_SDK_init();

        if (ETPush.getLogLevel() <= Log.DEBUG) {
            // use log.i to differentiate from SDK
            Log.i(TAG, "onCreate() end.");
        }
    }

    // onEvent(RegistrationEvent event)
    //
    //		This method is one of several methods for getting notified when an event
    //      occurs in the SDK.
    //
    //		They are all called onEvent(), but will have a different parameter to indicate
    //		the event that has occurred.
    //
    //		RegistrationEvent will be triggered when the SDK receives the response from the
    // 		registration as triggered by the com.google.android.c2dm.intent.REGISTRATION intent.
    //
    //		These events are only called if EventBus.getInstance().register() is called
    //
    @SuppressWarnings({"unused", "unchecked"})
    public void onEvent(final RegistrationEvent event) {
        if (ETPush.getLogLevel() <= Log.DEBUG) {
            Log.i(TAG, "Marketing Cloud update occurred.  You could now save Marketing Cloud details in your own data stores...");
            Log.i(TAG, "Device ID:" + event.getDeviceId());
            Log.i(TAG, "System Token:" + event.getSystemToken());
            Log.i(TAG, "Subscriber key:" + event.getSubscriberKey());

            for (Attribute attribute : (ArrayList<Attribute>) event.getAttributes()) {
                Log.i(TAG, "Attribute " + attribute.getKey() + ": [" + attribute.getValue() + "]");
            }
            Log.i(TAG, "Tags: " + event.getTags());
            Log.i(TAG, "Language: " + event.getLocale());

            // save DeviceId for use in Info screen
            SDK_ExplorerApp.deviceId = event.getDeviceId();
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(final CloudPagesResponse event) {
        Log.i(TAG, "CloudPages returned...");
        Log.i(TAG, "Num messages: " + event.getMessages().size());
    }


    @SuppressLint("CommitPrefEdits")
    private void JB4A_SDK_init() {
        try {
            // ETPush.readyAimFire
            //
            //		This call should be completed in your Application Class
            //
            //			enableETAnalytics is set to true to show how Salesforce analytics will save statistics for how your customers use the app
            //			enablePIAnalytics is set to true to show how Predictive Intelligence analytics will save statistics for how your customers use the app (by invitation at this point)
            //			enableLocationManager is set to true to show how geo fencing works within the SDK
            //			enableCloudPages is set to true to test how notifications can send your app customers to different web pages
            //
            //			Your app will have these choices set based on how you want your app to work.
            //
            if (ETPush.getLogLevel() <= Log.DEBUG) {
                Log.i(TAG, "Calling readyAimFire()");
            }

            // checkReceiverExistsInManifest();
            // checkServicesExistInManifest();

            ETPushConfig.Builder pushConfigBuilder = new ETPushConfig.Builder(this);
            pushConfigBuilder
                    .setEtAppId(CONSTS_API.getEtAppId())
                    .setAccessToken(CONSTS_API.getAccessToken())
                    .setGcmSenderId(CONSTS_API.getGcmSenderId())
                    .setAnalyticsEnabled(true)
                    .setPiAnalyticsEnabled(true)
                    .setCloudPagesEnabled(true);

            if (getString(R.string.companyName).equalsIgnoreCase("google")) {
                pushConfigBuilder.setLocationEnabled(true);
            } else {
                pushConfigBuilder.setLocationEnabled(false);
            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            // A production build, which normally doesn't have debugging turned on.
            // However, SDK_ExplorerDebugSettingsActivity allows you to override the debug level.  Set it now.

            boolean enableDebug = sp.getBoolean(CONSTS.KEY_DEBUG_PREF_ENABLE_DEBUG, true);
            if (enableDebug || Utils.isDebugApp(this)) {
                pushConfigBuilder.setLogLevel(Log.DEBUG);

                // ETLogger.startCapture(maxMemorySize, maxFileSize)
                //
                //		Since we are in debug mode, make sure to capture the log to a file
                //      We are choosing not to clear the log when we start so that we can get a
                //      continuous log of our testing.
                //
                ETLogger.getInstance().startCapture(this, 100000l, 1000000l, false);
            }

            ETPush.readyAimFire(pushConfigBuilder.build());

            if (ETPush.getLogLevel() <= Log.DEBUG) {
                Log.i(TAG, "readyAimFire() has been called.");
            }

            ETNotifications.setNotificationLaunchIntent(new ETNotificationLaunchIntent() {
                @Override
                public Intent setupLaunchIntent(Context context, Bundle payload) {
                    //
                    // save the push notification received info for later display
                    //
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SDK_ExplorerApp.context());
                    SharedPreferences.Editor spEditor = sp.edit();
                    long currTime = Calendar.getInstance().getTimeInMillis();
                    spEditor.putLong(CONSTS.KEY_PUSH_RECEIVED_DATE, currTime);

                    JSONObject jo = new JSONObject();
                    try {
                        for (String key : payload.keySet()) {
                            jo.put(key, payload.get(key));
                        }
                    } catch (Exception e) {
                        if (ETPush.getLogLevel() <= Log.ERROR) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }

                    spEditor.putString(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, jo.toString());

                    spEditor.commit();

                    // Since the SDK_ExplorerDisplayMessageActivity will show either the last or current message
                    // pass a similar bundle to what is saved in Shared Preferences
                    payload.putLong(CONSTS.KEY_PUSH_RECEIVED_DATE, currTime);
                    payload.putString(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, jo.toString());

                    //
                    // This override will make sure that the launch intent is the only intent that is launched so only 1 is viewable at a time.
                    // FLAG_ACTIVITY_CLEAR_TOP will close the current Intent (as well as any Activities stacked on top), and then show this new one
                    // (if it is currently open).
                    //
                    // Remove .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) if you want to have a separate Intent be displayed for your notifications
                    // if the app user receives a second notification message while viewing a previous message.
                    //
                    return ETNotifications.setupLaunchIntent(context, payload).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            });

            ETNotifications.setNotificationBuilder(new ETNotificationBuilder() {
                @Override
                public NotificationCompat.Builder setupNotificationBuilder(Context context, Bundle payload) {
                    NotificationCompat.Builder builder = ETNotifications.setupNotificationBuilder(context, payload);

                    String category = payload.getString("category");
                    if (category != null && !category.isEmpty()) {
                        if (ITEM_SPOTLIGHT.equalsIgnoreCase(category)) {
                            //we need to add the 3 item_spotlight buttons to the notification. Android allows
                            //a max of 3 action buttons on the BigStyle notifications.
                            Intent similarIntent = new Intent(context, SDK_ExplorerViewSimilarActivity.class);
                            similarIntent.putExtras(payload);
                            PendingIntent similarPendingIntent = ETNotifications.createPendingIntentWithOpenAnalytics(context, similarIntent, true);
                            builder.addAction(R.drawable.ic_action_labels, "Similar", similarPendingIntent);

                            Intent favoritesIntent = new Intent(context, SDK_ExplorerViewFavoritesActivity.class);
                            favoritesIntent.putExtras(payload);
                            PendingIntent favoritesPendingIntent = ETNotifications.createPendingIntentWithOpenAnalytics(context, favoritesIntent, true);
                            builder.addAction(R.drawable.ic_action_favorite, "Fav", favoritesPendingIntent);

                            Intent reviewsIntent = new Intent(context, SDK_ExplorerViewReviewsActivity.class);
                            reviewsIntent.putExtras(payload);
                            PendingIntent reviewsPendingIntent = ETNotifications.createPendingIntentWithOpenAnalytics(context, reviewsIntent, true);
                            builder.addAction(R.drawable.ic_action_important, "Review", reviewsPendingIntent);
                        } else if (ONE_DAY_SALE.equalsIgnoreCase(category)) {
                            //get custom key for the sale date.
                            String saleDateString = payload.getString("sale_date");
                            if (saleDateString != null && !saleDateString.isEmpty()) {
                                try {
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    df.setTimeZone(TimeZone.getDefault());
                                    Date saleDate = df.parse(saleDateString);

                                    Intent intent = new Intent(Intent.ACTION_INSERT)
                                            .setData(CalendarContract.Events.CONTENT_URI)
                                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, saleDate.getTime())
                                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, saleDate.getTime())
                                            .putExtra(CalendarContract.Events.TITLE, payload.getString("event_title"))
                                            .putExtra(CalendarContract.Events.DESCRIPTION, payload.getString("alert"))
                                            .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                                            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                                    PendingIntent reminderPendingIntent = PendingIntent.getActivity(context, 38456, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                    builder.addAction(R.drawable.ic_action_add_alarm, "Add Reminder", reminderPendingIntent);
                                } catch (ParseException e) {
                                    Log.e(TAG, e.getMessage(), e);
                                }
                            }
                        }
                    }

                    return builder;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(ReadyAimFireInitCompletedEvent event) {
        try {

            if (ETPush.getLogLevel() <= Log.DEBUG) {
                Log.i(TAG, "ReadyAimFireInitCompletedEvent started.");
            }

            if (event.isReadyAimFireReady()) {
                ETPush pushManager = ETPush.getInstance();
                // ETPush.getInstance().setNotificationRecipientClass
                //
                //		This call is used to specify which activity is displayed when your customers click on the alert.
                //		This call is optional.  By default, the default launch intent for your app will be displayed.
                pushManager.setNotificationRecipientClass(SDK_ExplorerDisplayMessageActivity.class);

                // ETPush.getInstance().setNotificationResourceId
                //
                //		This call is used to specify the notification icon to use for the app.
                //
                //		If this resource ID is not set, then the launcher icon will be used.  However, the launcher
                //      icon is not valid for a notification icon, per these specs:
                //      http://developer.android.com/design/style/iconography.html#notification
                //
                pushManager.setNotificationResourceId(R.drawable.ic_stat_sdk_explorer);

                // ETPush.getInstance().setOpenDirectRecipient
                //
                //		This call is used to specify which activity is used to process an Open Direct URL sent in the payload of a push message from the Marketing Cloud.
                //
                //		If you don't specify this class, but do specify a URL in the OpenDirect field when creating the Message in the Marketing
                //      cloud, then the JB4A SDK will use the ETLandingPagePresenter class to display the URL.
                //
                //		Instead, we will use the SDK_ExplorerWebContentActivity which extends ETLandingPagePresenter in order to provide control over the
                //      ActionBar and what happens when the user selects back.
                pushManager.setOpenDirectRecipient(SDK_ExplorerWebContentActivity.class);

                // ETPush.getInstance().setCloudPageRecipient
                //
                //		This call is used to specify which activity is used to process an CloudPage URL sent in the payload of a push message from the Marketing Cloud.
                //
                //		If you don't specify this class, but do send a CloudPage Alert when creating the Message in the Marketing
                //      cloud, then the JB4A SDK will use the ETLandingPagePresenter class to display the URL.
                //
                //		Instead, we will use the SDK_ExplorerWebContentActivity which extends ETLandingPagePresenter in order to provide control over the
                //      ActionBar and what happens when the user selects back.
                pushManager.setCloudPageRecipient(SDK_ExplorerWebContentActivity.class);
            } else {
                Log.e(TAG, "ETPush readyAimFire() did not initialize due to an Exception.", event.getException());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
