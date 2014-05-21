package com.exacttarget.publicdemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

/**
 * PublicDemoApp is the primary application class.
 *
 * This class extends Application to provide global activities for the PublicDemo App.
 *
 * It calls several methods in order to link to the ET Android SDK:
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

public class PublicDemoApp extends Application {

	private static Context appContext;

	private static final String TAG = PublicDemoApp.class.getName();

	@Override
	public void onCreate() {
		appContext = this;
		super.onCreate();

		try {

			boolean isDebuggable =  ( 0 != ( getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );

			if (isDebuggable) {
				// ETPush.setLogLevel
				//
				//		This call should be the first call in app Application class to ensure you get all the debug info in logcat from all the SDK calls.
				//		In production you can remove this call as the default is Log.WARN
				ETPush.setLogLevel(Log.DEBUG);

				// CONSTS_API.setDevelopment()
				//
				// This class needs to change to use your API keys you have setup for your Mobile Push App as well as your Server to Server App.
				//
				// Production values are used by default.  So, only call setDevelopment() for your testing.  To Test Your Production app settings, you can comment this line out.
				//
				CONSTS_API.setDevelopment();
			}
			else {
				// A production build, which normally doesn't have debugging turned on.
				// However, PublicDemoDebugSettingsActivity allows you to override the debug level.  Set it now.
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(PublicDemoApp.context());
				boolean enableDebug = sp.getBoolean(CONSTS.KEY_DEBUG_PREF_ENABLE_DEBUG, false);

				if (enableDebug) {
					ETPush.setLogLevel(Log.DEBUG);
				}
			}

			// ETPush.readyAimFire
			//
			//		This call should be the first call in your default activity for your app.
			//
			//			enableAnalytics is set to true to show how analytics will save statistics for how your customers use the app
			//			enableLocationManager is set to true to show how geo fencing works within the SDK
			//			enableCloudPages is set to true to test how notifications can send your app customers to different web pages
			//
			//			Your app will have these choices set based on how you want your app to work.
			//
			ETPush.readyAimFire(this, CONSTS_API.getEtAppId(), CONSTS_API.getAccessToken(), true, true, true);

			// ETPush.pushManager().setGcmSenderID
			//
			//		This call is used to specify which Google Cloud Messaging account/project to use to send messages.
			//
			ETPush pushManager = ETPush.pushManager();
			pushManager.setGcmSenderID(CONSTS_API.getGcmSenderId());  //

			// ETPush.pushManager().setNotificationRecipientClass
			//
			//		This call is used to specify which activity is displayed when your customers click on the alert.
			//		This call is optional.  By default, the default launch intent for your app will be displayed.
			pushManager.setNotificationRecipientClass(PublicDemoNotificationActivity.class);

			// ETPush.pushManager().setOpenDirectRecipient
			//
			//		This call is used to specify which activity is used to process a URL sent in the payload of a push message from the Marketing Cloud.
			//
			//		If you don't specify this class, but do specify a URL in the OpeDirect field when creating the Message in the Marketing
			//      cloud, then the SDK will use the ETLandingPagePresenter class to display the URL.
			//
			//		Instead, we will use the PublicDemoOpenDirectActivity which extends ETLandingPagePresenter in order to provide control over the
			//      ActionBar and what happens when the user selects back.
			pushManager.setOpenDirectRecipient(PublicDemoOpenDirectActivity.class);
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
