package com.exacttarget.demo.etsdkdemo;

import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

import android.app.Application;
import android.util.Log;

public class PushEnabledApplication extends Application {

	private static final String TAG = "[ET] PushEnabledApplication";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		try {
			//This method sets up the ExactTarget mobile push system
			ETPush.readyAimFire(this);
			
		} catch (ETException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

}
