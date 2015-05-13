1. Add the permissions, receivers, and services necessary for location capabilities and geofences in `ApplicationManifest.xml`.

	```java
	<!-- ExactTarget Permissions for location and region monitoring -->
	<uses-permission android:name="android.permission.ACCESS\_COARSE\_LOCATION"/>
	<uses-permission android:name="android.permission.ACCESS\_FINE\_LOCATION"/>
	<uses-permission android:name="android.permission.RECEIVE\_BOOT\_COMPLETED"/>
	<!-- END ExactTarget location and region monitoring Permissions -->
	<application>
	    <!-- ET Broadcast Receivers for handling location updates -->
	    <!-- Get location ourselves -->
	    <receiver android:name = "com.exacttarget.etpushsdk.location.receiver.LocationChangedReceiver"/>
	    <!-- piggyback and listen in on other apps that request location updates -->
	    <receiver android:name = "com.exacttarget.etpushsdk.location.receiver.PassiveLocationChangedReceiver"/>
	    <!-- Don't kill their battery using gps if it's low -->
	    <receiver android:name "com.exacttarget.etpushsdk.location.receiver.PowerStateChangedReceiver">
	        <intent-filter>
	            <action android:name="android.intent.action.BATTERY\_LOW"/>
	            <action android:name="android.intent.action.BATTERY\_OKAY"/>
	        </intent-filter>
	    </receiver>
	    <!-- Wake up the app up every so often to get user's location -->
	    <receiver android:name="com.exacttarget.etpushsdk.ETLocationTimeoutReceiver"/>
	    <receiver android:name="com.exacttarget.etpushsdk.ETLocationWakeupReceiver"/>
	    <receiverandroid:name="com.exacttarget.etpushsdk.ETLocationProviderChangeReceiver">
	        <intent-filter>
	            <actionandroid:name="android.location.PROVIDERS\_CHANGED"/>
	            <categoryandroid:name="android.intent.category.DEFAULT"/>
	        </intent-filter>
	    </receiver>
	    <receiver android:name="com.exacttarget.etpushsdk.ETGeofenceReceiver"/>
	    <!-- figure out where they are if they just turned on their phone (e.g. plane trip) -->
	    <receiver android:name="com.exacttarget.etpushsdk.location.receiver.BootReceiver">
	        <intent-filter>
	            <action android:name="android.intent.action.BOOT\_COMPLETED"/>
	        </intent-filter>
	    </receiver>
	    <!-- ET Service handlers for handling location updates -->
	    <service android:name="com.exacttarget.etpushsdk.ETLocationTimeoutService"/>
	    <service android:name="com.exacttarget.etpushsdk.ETLocationWakeupService"/>
	    <serviceandroid:name="com.exacttarget.etpushsdk.ETLocationProviderChangeService"/>
	    <service android:name="com.exacttarget.etpushsdk.ETGeofenceIntentService"/>
	</application>
	```
<br/>
1. Set the second Boolean parameter in the `readyAimFire()` method in the `onCreate()` method for your application to `true`.

	```java
    public static final boolean LOCATION\_ENABLED = true;
	```
<br/>
1. Call `startWatchingLocation()` to track a user location and enable the geofence.<br/>

	```java
	protected SharedPreferences sharedPreferences;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.yourlayout);
	    
	    try {
	        if(sharedPreferences.getBoolean(CONSTS.KEY\_PREF\_PUSH, true)) {
	            ETPush.pushManager().enablePush();
	        }
	        if(sharedPreferences.getBoolean(CONSTS.KEY\_PREF\_LOCATION, true)) {
	            if (ETLocationManager.locationManager().isWatchingLocation()) {
	                ETLocationManager.locationManager().startWatchingLocation();
	            }
	        }
	    }
	    catch (ETException e) {
	        Log.e(TAG, e.getMessage(), e);
	    }
	}
	```
	`startWatchingLocation()` must be called in the Home Activity `onCreate()` Method, as follows.  This example assumes your app keeps track of opt in for Push and Location in shared preferences.  Adjust accordingly if you do not save these preferences in shared preferences.
<br/><br/>
1. Call `stopWatchingLocation()` to stop tracking user location and disable the geofence functionality if the app user opts out of location services.
<br/><br/>
1. New Location messages are retrieved whenever your app comes into the foreground.<br/>  
`Note:` _For apps targetting **earlier than Android API 14**, you need to override onPause() and onResume() in each Activity class to notify the SDK when activities pause and resume so the SDK can determine when your app goes into the background._<br/><br/>
`Note:` _For apps targetting **Android API 14 or later**, the SDK will use registerLifecycleCallbacks() to determine when activities are paused or resumed._

    ```java
            @Override
            protected void onPause() {
                super.onPause();
                
                try {
                    // Let JB4A SDK know when each activity paused (4.0.0 release or later)
                    ETPush.activityPaused(this);
                    // Let JB4A SDK know when each activity paused (3.5.0 release or earlier)
                    // ETPush.pushManager().activityPaused(this);
                }
                catch (Exception e) {
                    if (ETPush.getLogLevel() <= Log.ERROR) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            @Override
            protected void onResume() {
                super.onResume();
                try {
                    // Let JB4A SDK know when each activity resumed( 4.0.0 release or later)
                    ETPush.activityResumed(this);
                    // Let JB4A SDK know when each activity resumed (3.5.0 release or earlier)
                    // ETPush.pushManager().activityResumed(this);
                }
                catch (ETException e) {
                    if (ETPush.getLogLevel() <= Log.ERROR) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }
    ```
<br/>
