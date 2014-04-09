package com.exacttarget.demo.etsdkdemo;

import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PushEnabledApplication extends Application {

	private static final String TAG = "[ET] PushEnabledApplication";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		try {
			//This method sets up the ExactTarget mobile push system
			//we're enabling both the ETAnalytics and ETLocationManager modules
			//ETPush.readyAimFire(this, "1b83b32a-ea05-48ec-a1c7-69afa81afab9", "n4twywndzq3yrxk5ku4t4zsp", true, true);
			
			//1mcollins_s1qa1_Core app
			//ETPush.readyAimFire(this, "373466d1-4fe3-48bf-a57b-6ceec7a5aed5", "b6tdbz9rm6jbyq7yydk3wrrw", true, true);
			
			//Automated Testing app
			ETPush.setLogLevel(Log.WARN);
			ETPush.readyAimFire(this, "02f60bbe-ccbb-4043-b4f7-144c9ddbba54", "qqsu6htzqw6yh36rqgwskmk5", true, true, true);
			ETPush pushManager = ETPush.pushManager();
			pushManager.setGcmSenderID("1072910018575");

			//A good practice is to add the versionName of your app from the manifest as a tag
			//so you can target specific app versions with a push message later if necessary.
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			pushManager.addTag(versionName);
		} 
		catch (ETException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

}
