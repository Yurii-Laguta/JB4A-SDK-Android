---
layout: page
title: "Google"
subtitle: "Implement SDK on Google Devices"
category: sdk-implementation
date: 2015-05-14 12:00:00
order: 1
---
In order to use the SDK in your Mobile app, complete the steps required to register a device with the Salesforce Marketing Cloud. This process ultimately connects the device to the MobilePush app you created in the [App Center]({{ site.baseurl }}/create-apps/create-apps-overview.html).

This document provides examples using Android Studio. To review any Eclipse-specific coding required, see [Eclipse]({{ site.baseurl }}/sdk-implementation/implement-sdk-eclipse.html)

Follow the steps below to bootstrap the Journey Builder for Apps SDK into your mobile Android app. Example code comes from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>.

Use the JB4A Android SDK with Android API versions 10 (Gingerbread) or greater. Set your minimum SDK version to no less than 10.

1. Add the following repositories to your application's **build.gradle** file.

    ~~~
    allprojects {
        repositories {
            jcenter()
            maven {
                url "http://salesforce-marketingcloud.github.io/JB4A-SDK-Android/repository" 
            }
        }
    }
    ~~~
1. Add the following dependencies to your application **app\build.gradle** file.

    ~~~
    dependencies {
      // ET SDK
      compile 'com.exacttarget.etpushsdk:etsdk:4.1.1@aar'

      // Google Play Services for Location and Google Cloud Messaging
      compile 'com.google.android.gms:play-services-location:8.3.0'
      compile 'com.google.android.gms:play-services-gcm:8.3.0'

      // Google's Support v4 for Notification compatibility
      compile 'com.android.support:support-v4:22.2.0'

      // 3rd Party Libraries Required for SDK integration of Beacons (only for Beacon Beta Testers)
      // compile 'org.altbeacon:android-beacon-library:2.5.1@aar'
    }
    ~~~

1. In your **app\build.gradle** file, add an **applicationId** to the **defaultConfig{}** block to simplify integration.

    ~~~
    defaultConfig {
        // TODO Replace with your applicationId
        applicationId "com.example.package"
    }
    ~~~

1. In your **app\build.gradle** file, ensure that you request appropriate permissions if the user enabled Location features for builds targeting Android versions 23 and above.

    ~~~
    android {
        compileSdkVersion 23
    }
    ~~~

    ~~~
    onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults)
    ~~~

    If PERMISSION_GRANTED==TRUE, then put call to
    ~~~
      startWatchingLocation()
    ~~~

1. Developers using Android Studio with version 4.2 of the JB4A SDK do not need to modify the **AndroidManifest.xml** file unless using geolocation. In that case, add the following line:

    ~~~
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    ~~~

Developers using version 4.1 or earlier of the SDK in an application should include the following code in the **AndroidManifest.xml** file:

    ~~~
    <?xml version="1.0" encoding="utf-8"?>
       <manifest xmlns:android="http://schemas.android.com/apk/res/android">

       <!-- JB4A SDK Google Permissions -->
       <permission
           android:name="${applicationId}.permission.C2D_MESSAGE"
           android:protectionLevel="signature" />
       <uses-permission android:name="${applicationId}.permission.C2D_MESSAGE" />
       <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
       <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
       <!-- JB4A SDK Google Permissions -->

       <!-- ET SDK required permissions -->
       <uses-permission android:name="android.permission.INTERNET"/>
       <uses-permission android:name="android.permission.WAKE_LOCK"/>
       <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
       uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
       <!-- END ET SDK Required Permissions -->
          
       <application>
           <!-- ETPushReceiver and Service -->
            <receiver android:name="com.exacttarget.etpushsdk.ETPushReceiver"android:permission="com.google.android.c2dm.permission.SEND">
              <intent-filter>
                <action android:name="${applicationId}.MESSAGE_OPENED" />
                <action android:name="com.exacttarget.etpushsdk.SEND_REGISTRATION" />
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
                <action android:name="android.intent.action.ACTION_SHUTDOWN" />
                <action android:name="android.intent.action.AIRPLANE_MODE" />
                <category android:name="${applicationId}" />
              </intent-filter>
              <intent-filter>
                <action android:name="android.intent.action.PACKAGE_REPLACED" />
                <data android:scheme="package" />
                </intent-filter>
              </receiver>
   
           <service
               android:name="com.exacttarget.etpushsdk.ETPushService"
               android:enabled="true" />
           <!-- ETPushReceiver and Service -->

           <!-- JB4A SDK Activity required for Cloud Page or Open Direct URLs sent from Marketing Cloud -->
           <activity
              android:name="com.exacttarget.etpushsdk.ETLandingPagePresenter"
              android:label="Landing Page" />
           <!-- JB4A SDK Activity required for Cloud Page or Open Direct URLs sent from Marketing Cloud -->

           <!-- Google Play Services version.  Using the resource file will require your project contain the Google Play services project. -->
           <!-- See Google documentation for more information -->
           <meta-data
               android:name="com.google.android.gms.version"
               android:value="@integer/google_play_services_version" />
           <!-- Google Play Services version. -->
       </application>
       </manifest>
    ~~~
1. Update your <a href="http://developer.android.com/reference/android/app/Application.html" target="_blank">Application Class</a> to connect your Mobile App with the correct [App Center App]({{ site.baseurl }}/create-apps/create-apps-provisioning.html).

    1. Create a Class that extends <a href="http://developer.android.com/reference/android/app/Application.html" target="_blank">android.app.Application</a>. <b>You must complete this mandatory step.</b>

    1. Initialize the ETSDK by calling `readyAimFire()` using an Application Context and from within your Application Class.

        ~~~
        try {
	        ETPush.readyAimFire(new ETPushConfig.Builder(this)
	        		.setEtAppId("etAppId")
	        		.setAccessToken("accessToken")
	        		.setGcmSenderId("gcmSenderId")
	        		.setAnalyticsEnabled(true|false)
	        		.setPiAnalyticsEnabled(true|false)
	        		.setLocationEnabled(true|false)      // set to true ONLY if you purchased Location as it requires additional overhead
	        		.setCloudPagesEnabled(true|false)    // set to true ONLY if you purchased RichPush as it requires additional overhead
	        		.setLogLevel(BuildConfig.DEBUG ? android.util.Log.VERBOSE : android.util.Log.ERROR)
	        		/* Builder methods to override SDK behavior */
	        		//.setCloudPageRecipientClass(SomeActivity.class) // Override ETLandingPagePresenter
	        		//.setOpenDirectRecipientClass(SomeActivity.class) // Override ETLandingPagePresenter
	        		//.setNotificationRecipientClass(SomeActivity.class) // Override Notification Handling
	        		//.setNotificationAction("some_string")
	        		//.setNotificationActionUri(Uri.parse("some_uri"))
	        		//.setNotificationResourceId(R.drawable.some_drawable)
	        		.build()
            );
        catch (ETException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        ~~~

        > readyAimFire() must be called from your Application class to ensure that background receivers and services can be initialized properly.  Failing to do so will result in 1) your application failing to receive background push notifications, location updates, etc. and 2) potentially crashes.

    1. Register the ET **EventBus** to receive notification of completed registration. See [Event Bus]({{ site.baseurl }}/features/eventbus.html).

    1. Add an event callback to process any code after readyAimFire() completes.  

        ~~~
    /**
     * EventBus callback listening for a ReadyAimFireInitCompletedEvent.  After we receive this
     * event, we can safely use our ETPush instance.
     *
     * @param event the type of event we're listening for.
     */
    public void onEvent(final ReadyAimFireInitCompletedEvent event) {
        ETPush etPush = null;
        try {
            etPush = event.getEtPush();

            /*
                Good practice - add the application version name as a tag you can later use to target push notifications to specific application versions.
             */
            etPush.addTag(HelloWorldApplication.VERSION_NAME);

        } catch (ETException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
        ~~~
    
    > 4.0.0 of the SDK eliminates the need to add enablePush() in your launcher activity. The SDK will enable push by default. If you wish to disable push by default, call **etPush.disablePush()** in the event callback described above.

    > Changes, including your initial registration from a device, propagate from the server every 15 minutes.  Ensure you wait an appropriate amount of time before expecting to receive push notifications or for changes to take affect.

1. Create a Preference Screen that allows a user to opt out of push messages. Review example code in the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>. Call **disablePush()** to allow a user to opt out of push services. You can then call **enablePush()** to allow user to opt back into push services.

Once you complete this process, you can build your application and send it push notifications from your Marketing Cloud account.