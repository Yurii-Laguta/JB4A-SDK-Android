---
layout: page
title: "GeoLocation"
subtitle: "Adding GeoLocation"
category: location
date: 2015-05-14 08:43:35
order: 2
---

1. Add the following permission to **AndroidManifest.xml**.

    ~~~
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    ~~~

1. Add the following intent filters to **AndroidManifest.xml**.

    ~~~
    <action android:name="android.intent.action.BATTERY_LOW" />
    <action android:name="android.intent.action.BATTERY_OKAY" />
    ~~~

1. Add the following entries to **AndroidManifest.xml**.

    ~~~
    <receiver android:name="com.exacttarget.etpushsdk.ETLocationReceiver" />
    <service android:name="com.exacttarget.etpushsdk.ETLocationService" android:enabled="true" />
    ~~~

1. Set the Boolean parameter in the **readyAimFire()** method in the **onCreate()** method for of your Application class to **true**.

    ~~~ 
    ETPush.readyAimFire(this, 
                        CONSTS_API.getEtAppId(), 
                        CONSTS_API.getAccessToken(), 
                        CONSTS_API.getGcmSenderId(), 
                        true,     // enable ET Analytics 
                        true,     // enable Location Manager, if you purchased this feature
                        true);    // enable Cloud Page, if you purchased this feature
    ~~~ 

1. If you enable locations readyAimFire(), the SDK will automatically start watching locations. Call `stopWatchingLocation()` to stop tracking user location and disable the geofence functionality in a Settings screen to allow the user to opt out of location services.  You can then call `startWatchingLocation()` to allow them to opt back into geofence messages.

1. Notify the SDK when the app comes into the foreground because the SDK will retrieve new Location messages whenever your app comes into the foreground.<br/>  

    > The SDK requires the following code only if your app targets API versions **earlier than Android API 14**.  For apps targeting **Android 14 or later**, the SDK will implement these calls using  registerActivityLifecycleCallbacks().

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