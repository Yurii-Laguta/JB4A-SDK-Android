---
layout: page
title: "Google"
subtitle: "Implement SDK on Google Devices"
category: sdk-implementation
date: 2015-05-14 12:00:00
order: 1
---
In order to use the SDK in your Mobile app, there are several steps required to register a Google device with the Salesforce Marketing Cloud which ultimately connects it to the MobilePush app you created in the [App Center]({{ site.baseurl }}/create-apps/create-apps-overview.html).  To connect an Amazon device refer to [this]({{ site.baseurl }}/sdk-implementation/implement-sdk-amazon.html) document.

We have provided examples assuming you are using Android Studio.  To see any Eclipse specific coding required, see [Eclipse]({{ site.baseurl }}/sdk-implementation/implement-sdk-eclipse.html)

Follow the steps below to bootstrap the Journey Builder for Apps SDK into your mobile Android app. Example code taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>.

The JB4A Android SDK is compatible with Android API versions 10 (Gingerbread) or greater.  Your minimum SDK version must be set no less than 10.

1.  Add the following repositories to your application's `build.gradle` file.

    ~~~
    allprojects {
        repositories {
            jcenter()
            maven {
                url "http://exacttarget.github.io/JB4A-SDK-Android/repository" 
            }
        }
    }
    ~~~
1.  Add the following dependencies to your application's `app\build.gradle` file.

    ~~~
    dependencies {
      // ET SDK
      compile 'com.exacttarget.etpushsdk:etsdk:4.0.0@aar'

      // Google Play Services for Location and Google Cloud Messaging
      compile 'com.google.android.gms:play-services-location:7.5.0'
      compile 'com.google.android.gms:play-services-gcm:7.5.0'

      // Google's Support v4 for Notification compatibility
      compile 'com.android.support:support-v4:22.2.0'
    }
    ~~~
1.  Also in your application's `app\build.gradle` file it is recommended that you add an `applicationId` to the `defaultConfig{}` block as that will greatly simplify integration.

    ~~~
    defaultConfig {
        // TODO Replace with your applicationId
        applicationId "com.example.package"
    }
    ~~~

1.  In your application's AndroidManifest.xml file, include the following:

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
          
       <application>
           <!-- ETPushReceiver and Service -->
           <receiver
               android:exported="true"
               android:name="com.exacttarget.etpushsdk.ETPushReceiver"
               android:permission="com.google.android.c2dm.permission.SEND">
               <intent-filter>
                   <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                   <action android:name="com.google.android.c2dm.intent.REGISTRATION" />
                   <action android:name="android.intent.action.ACTION_SHUTDOWN" />

                   <category android:name="${applicationId}" />
               </intent-filter>
               <intent-filter>
                   <action android:name="android.intent.action.BOOT_COMPLETED" />
                   <action android:name="android.intent.action.BATTERY_LOW" />
                   <action android:name="android.intent.action.BATTERY_OKAY" />
               </intent-filter>
               <intent-filter>
                   <action android:name="android.intent.action.PACKAGE_REPLACED" />
                   <data android:scheme="package" />
               </intent-filter>
               <intent-filter>
                   <action android:name="${applicationId}.MESSAGE_OPENED" />
                   <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
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
1.  Update your <a href="http://developer.android.com/reference/android/app/Application.html" target="_blank">Application Class</a> to connect your Mobile App with the correct [App Center App]({{ site.baseurl }}/create-apps/create-apps-provisioning.html).

    1.  Create a Class that extends <a href="http://developer.android.com/reference/android/app/Application.html" target="_blank">android.app.Application</a>. <b>This is not optional.</b>

    1.  Initialize the ETSDK by calling `readyAimFire()` using an Application Context and from within your Application Class.

        ~~~
        try {
	        ETPush.readyAimFire(new ETPushConfig.Builder(this)
	        		.setEtAppId("etAppId")
	        		.setAccessToken("accessToken")
	        		.setGcmSenderId("gcmSenderId")
	        		.setAnalyticsEnabled(true|false)
	        		.setPiAnalyticsEnabled(true|false)
	        		.setLocationEnabled(true|false)
	        		.setCloudPagesEnabled(true|false)
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

    1.  Register the ET `EventBus` to receive notification of completed registration.  See [Event Bus]({{ site.baseurl }}/features/eventbus.html).

    1.  Add an event callback to process any code after readyAimFire() completes.  

        ~~~
        public void onEvent(ReadyAimFireInitCompletedEvent event) {
           if (ETPush.getLogLevel() <= Log.DEBUG) {
               Log.i(TAG, "ReadyAimFireInitCompletedEvent started.");
           }
        
           if (event.isReadyAimFireReady()) {
             // successful bootstrap with SDK  
        
        
           } else {
              // unsuccessful bootstrap with SDK 
                if (event.getCode() == ETException.RAF_INITIALIZE_ENCRYPTION_FAILURE) {
                     message = "ETPush readyAimFire() did not initialize due to an Encryption failure.";
                 } else if (event.getCode() == ETException.RAF_INITIALIZE_ENCRYPTION_OPTOUT_FAILURE) {
                     message = "ETPush readyAimFire() did not initialize encryption failure and unable to opt-out.";
                 } else if (event.getCode() == ETException.RAF_INITIALIZE_EXCEPTION) {
                     message = "ETPush readyAimFire() did not initialize due to an Exception.";
                 } else {
                     message = "ETPush readyAimFire() did not initialize due to an Exception.";
                 }
                 Log.e(TAG, String.format("ETPush readyAimFire() did not initialize due to an Exception with message: %s and code: %d", event.getMessage(), event.getCode()), event.getException());
                 throw new RuntimeException(message);
           }
        }
        ~~~
    
    > 4.0.0 of the SDK eliminates the need to add enablePush() in your launcher activity.  Push will be enabled by default.  If you wish to have Push disabled by default, then you should call `etPush.disablePush()` in the event callback described above.

    > Changes, including your initial registration from a device, are propagated by the server every 15 minutes.  Please ensure you have waited an appropriate amount of time before expecting to receive push notifications or for changes to take affect.

1.  Good practice would be to create a Preference Screen that allows a user to opt out of Push messages.   Example code can be found in the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>. Call `disablePush()` to allow a user to opt out of Push services.  You can then call `enablePush()` to allow them to opt back into Push services.

### CONGRATULATIONS!!!

#### At this point you should be able to build your application and send it push notifications from your Marketing Cloud account!