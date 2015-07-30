---
layout: page
title: "GeoLocation"
subtitle: "Adding GeoLocation"
category: location
date: 2015-05-14 08:43:35
order: 2
---

1.  Add the following dependencies to your application's `app\build.gradle` file.  This dependency is required for GeoLocation and will be removed as a requirement in a future release.

    ~~~
    dependencies {
      
       // 3rd Party Libraries Required for SDK integration
       compile 'com.radiusnetworks:AndroidIBeaconLibrary:0.7.6'
    }
    ~~~

  > Failure to add this dependency if you enable location in readyAimFire() will result in a crash of your app: `java.lang.TypeNotPresentException: Type com/radiusnetworks/ibeacon/BleNotAvailableException not present`

1.  Add the permissions, receivers, and services necessary for location capabilities and geofences in `ApplicationManifest.xml`.

    ~~~
    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android">
      
        <!-- JB4A SDK Permissions for location and region monitoring -->
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
        <!-- END JB4A SDK location and region monitoring Permissions -->
    
        <application>
            <!-- ETLocationReceiver and Service -->
                <receiver android:name="com.exacttarget.etpushsdk.ETLocationReceiver">
                    <intent-filter>
                        <action android:name="android.location.PROVIDERS_CHANGED" />
                        <category android:name="android.intent.category.DEFAULT" />
                    </intent-filter>
               </receiver>
        
                <service
                    android:name="com.exacttarget.etpushsdk.ETLocationService"
                    android:enabled="true" />
            <!-- ETLocationReceiver and Service -->
    
        </application>
    
    </manifest>
    ~~~ 
1.  Set the Boolean parameter in the `readyAimFire()` method in the `onCreate()` method for of your Application class to `true`.

    ~~~ 
    ETPush.readyAimFire(this, 
                        CONSTS_API.getEtAppId(), 
                        CONSTS_API.getAccessToken(), 
                        CONSTS_API.getGcmSenderId(), 
                        true,     // enable ET Analytics 
                        true,     // enable Location Manager, if you purchased this feature
                        true);    // enable Cloud Page, if you purchased this feature
    ~~~ 
1.  If you enable locations you must add the following dependency to your application's `app\build.gradle` file.

    ~~~
    compile 'com.radiusnetworks:AndroidIBeaconLibrary:0.7.6’
    ~~~

   > Failure to add this dependency if enable location in readyAimFire() will result in a crash of your app: `java.lang.TypeNotPresentException: Type com/radiusnetworks/ibeacon/BleNotAvailableException not present`

1.  If you enable locations readyAimFire(), the SDK will automatically start watching locations.  Good practice would be to call `stopWatchingLocation()` to stop tracking user location and disable the geofence functionality in a Settings screen to allow the user to opt out of location services.  You can then call `startWatchingLocation()` to allow them to opt back into geofence messages.

1.  Notify the SDK when the app comes into the foreground because the SDK will retrieve new Location messages whenever your app comes into the foreground.<br/>  

    > The following is required only if you are targetting **earlier than Android API 14**.  For apps targetting **Android 14 or later**, the SDK will implement these calls using registerActivityLifecycleCallbacks().

    ~~~ 
    @Override
    protected void onPause() {
        super.onPause();
        
        try {
            // Let JB4A SDK know when each activity paused
            ETPush.activityPaused(this);
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
            // Let JB4A SDK know when each activity resumed(
            ETPush.activityResumed(this);
        }
        catch (ETException e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }
    ~~~ 