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

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

/**
 * SDK_ExplorerApp is the primary application class.
 *
 * This class extends Application to provide global activities for the Journey Builder for Apps (JB4A) SDK Explorer.
 *
 * It calls several methods in order to link to the JB4A Android SDK:
 *
 * 		1) To set the logging level, call ETPush.setLogLevel().  An example is provided that
 * 	       checks if this application is a test or production version.	As well, we have provided
 * 	       an example of storing the application keys in a separate class in order to provide
 * 	       centralized access to these keys and to ensure you use the appropriate keys when
 * 	       compiling the test and production versions.  This is not part of the SDK, but is shown
 * 	       as a way to assist in managing these keys (CONSTS_API.setDevelopment()).
 *
 * 	    2) ETPush.readyAimFire() should be the first call within this app after setting the LogLevel().
 * 	       Here you pass in the AppId and AccessToken.  These values are taken from the Marketing Cloud
 * 	       definition for your app.  Here you also set whether you enable LocationManager, CloudPages, and
 * 	       Analytics.
 *
 * 	    3) To connect this app with Google Cloud Messaging, you must call ETPush.pushManager().pushManager.setGcmSenderID()
 *
 * 	    4) Several optional methods are called as well, depending on the design of your app.
 *
 * @author pvandyk
 */

public class SDK_ExplorerApp extends Application {

	private static Context appContext;

	private static final String TAG = SDK_ExplorerApp.class.getName();

	@Override
	public void onCreate() {
		super.onCreate();

        appContext = this;

		try {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

			// A production build, which normally doesn't have debugging turned on.
            // However, SDK_ExplorerDebugSettingsActivity allows you to override the debug level.  Set it now.
            boolean enableDebug = sp.getBoolean(CONSTS.KEY_DEBUG_PREF_ENABLE_DEBUG, false);

            if (enableDebug) {
                ETPush.setLogLevel(Log.DEBUG);
            }

			// ETPush.readyAimFire
			//
			//		This call should be the first call in your default activity for your app.
			//
			//			enableAnalytics is set to true to show how analytics will save statistics for how your customers use the app
			//			enableLocationManager is set to true to show how geo fencing and beacons works within the SDK
			//			enableCloudPages is set to true to test how notifications can send your app customers to different web pages
			//
			//			Your app will have these choices set based on how you want your app to work.
			//
			ETPush.readyAimFire(this, CONSTS_API.getEtAppId(), CONSTS_API.getAccessToken(), CONSTS_API.getGcmSenderId(), true, true, true);
			ETPush pushManager = ETPush.pushManager();

			// ETPush.pushManager().setNotificationRecipientClass
			//
			//		This call is used to specify which activity is displayed when your customers click on the alert.
			//		This call is optional.  By default, the default launch intent for your app will be displayed.
			pushManager.setNotificationRecipientClass(SDK_ExplorerDisplayMessageActivity.class);

			// ETPush.pushManager().setOpenDirectRecipient
			//
			//		This call is used to specify which activity is used to process an Open Direct URL sent in the payload of a push message from the Marketing Cloud.
			//
			//		If you don't specify this class, but do specify a URL in the OpenDirect field when creating the Message in the Marketing
			//      cloud, then the JB4A SDK will use the ETLandingPagePresenter class to display the URL.
			//
			//		Instead, we will use the SDK_ExplorerOpenDirectActivity which extends ETLandingPagePresenter in order to provide control over the
			//      ActionBar and what happens when the user selects back.
			pushManager.setOpenDirectRecipient(SDK_ExplorerOpenDirectActivity.class);

		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	public static Context context() {
		return appContext;
	}
}
