#Journey Builder for Apps Android SDK (Beta)

This is the git repository for the Salesforce Marketing Cloud Journey Builder for Apps Android SDK.<br>

Click the following to download the latest JB4A SDK:<br/>
<a href="https://github.com/ExactTarget/JB4A-SDK-Android/raw/master/JB4A-SDK/etsdk-3.5.0.jar" target="_blank">etsdk-3.5.0.jar</a>

## Release History ###

___
#### Version 4.0.0 ####
_Released June 22nd, 2015, correlating to the Salesforce Marketing Cloud 2015-04 Release_
 * MPUSH-3377 - Implement multi-threaded support so SDK will not block UI thread.<br/>
  _`Note:` Code changes required for ETPush.activityResumed() and ETPush.activityPaused() for apps targetting earlier than API level 14._ 
 * MPUSH-3379 - Ensure push and location (if turned on in readyAimFire()) is enabled by default and any updated registration data is sent each time readyAimFire() is called.<br/>
   _`Note:` Code should be changed to remove the call for enablePush() and startWatchingLocation() in your home launcher Activity._
 * MPUSH-3380 - Publish AAR to SDK's gh-pages branch.<br/>
   _`Note:` Change app/build.gradle to use com.exacttarget.etpushsdk:etsdk:4.0.0@aar_
 * MPUSH-3441 - Remove Access Token from payload and add to REST header.
 * MPUSH-3444 - Implement Builder Pattern for ETPush Initialization.<br/>
   _`Note:` Change call to readyAimFire() to use this builder pattern._ 
 * MPUSH-3445 - Add industry accepted standard getInstance() and deprecate getDefault().<br/>
   _`Note:` Change code to use getInstance()_
 * MPUSH-3411 - SDK will no longer set a subscriber key by default.  This change will ensure that any imported contacts will not be overridden by SDK.<br/>
   _`Note:` The subscriber key will be set by the Marketing Cloud instead.  This matches what happens with iOS devices now._
 * MPUSH-3396 - Create separate AndroidManifest.xml for Amazon devices.<br/>
   _`Note:` You can create an Amazon product flavor within Android Studio and include this manifest for Amazon builds.
 * MPUSH-3378 - Improve verification of incorrect AndroidManifest.xml setup for both Amazon and Google devices.
 * MPUSH-3580 - Change verification of incorrect AndroidManifest.xml setup to throw a RunTimeException to ensure critical errors are fixed early in development process.  
 * MPUSH-3390 - Post an Event to the EventBus when readyAimFire() finishes with success or failure status.<br/>
   _`Note:` You can implement the ReadyAimFireInitCompletedEvent in your code to execute code after readyAimFire() has completed in order to avoid UI blocks in your app._
 * MPUSH-3374 - Add information about the SDK, app version and other pertinent information to error logging in ADB logcat.
 * MPUSH-3356 - Send registration data when network becomes available including changes to tags/attributes that were made when network was unavailable.
 * MPUSH-3321 - Consolidate broadcast receivers and ensure proper use of Wakeful services.<br/>
  _`Note:` Code changes required to implement ETPushReceiver and ETPushService._<br/>
  _`Note:` Code changes may be required if you use location to use ETLocationReceiver and ETLocationService._<br/>
  _`Note:` In order to change how notifications are overridden, you must now use interfaces within ETNotifications class._
 * MPUSH-3330 - Obfuscate sensitive data such as App Id, Access Token, and GCM Sender Id from ADB logcat.
 * MPUSH-3423 - Add customer facing log capturing to assist with debugging when not connected to ADB.<br/>
  _`Note:` APIs available to start capture, stop capture, save log, and email log._ 
 * MPUSH-3160 - Remove Jackson third party library and use Android internal classes to reduce Dex count of SDK.<br/>
  _`Note:` SDK no longer depends on this JAR.  It may be removed if your application does not require it._ 
 * MPUSH-3218 - Remove Ormlite third party library and use Android internal classes to reduce Dex count of SDK.<br/>
  _`Note:` SDK no longer depends on this JAR.  It may be removed if your application does not require it._ 
 * MPUSH-3156 - Remove Joda third party library and use Android internal classes to reduce Dex count of SDK.<br/>
  _`Note:` SDK no longer depends on this JAR.  It may be removed if your application does not require it._ 
 * MPUSH-3333 - Change payload to properly send SDK Version and GCM Sender Id which are saved in pushAddressExtension table in the Marketing Cloud.
 * MPUSH-3322 - Ensure database used in SDK is initialized at beginning of readyAimFire().
 * MPUSH-3313 - Implement registerLifecycleCallbacks() to handle onResume() and onPause() detection for Activities.<br/>
  _`Note:` You can remove ETPush.activityResumed() and ETPush.activityPaused() for apps targetting API level 14 or later._ 
 * MPUSH-3256 - Encrypt all data within the SDK SharedPreferences file and all sensitive data in the SDK SQLite database.
 * MPUSH-3402 - Obfuscate non-public methods and attributes.
 * MPUSH-3233 - Support Google Play Services v6.5.87 and up. 
 * MPUSH-3150 - Update license for SDK to use Salesforce.com. 
 * MPUSH-3320 - Ensure that Location work within the SDK is not executed when location turned off in readyAimFire(). 
 * MPUSH-3293 - Build SDK with latest Android SDK, support, and app compat libraries.
 * MPUSH-3285 - Make sure CloudPage Inbox downloads occur whether Analytics are turned on or not.

** Required Coding Changes ** - The following are changes that must be made in order to upgrade from previous releases of the SDK:
 * You must update the activityPaused() and activityResumed() calls as static calls in your activities to determine whether app is in foreground or background for CloudPage Inbox, Analytics, and Location messages.
   * [Analytics](doc/implement-android-analytics.md)
   * [Rich Push Inbox](doc/implement-android-inbox.md)
   * [Location](doc/implement-android-location.md)

** Documentation Updates **
 * What did we change and where

___
#### Version 3.5.0 ####
_Released March 9, 2015, correlating to Salesforce Marketing Cloud 2015-02 Release_
 * MPUSH-2948 - Development environment and examples converted to Android Studio
 * MPUSH-3076 - fix to prevent crash if meta-data missing from AndroidManifest.xml
 * MPUSH-2437 - Updates for improved Marketing Cloud interaction, including:
   * Implement PACKAGE_REPLACED receiver to ensure new Google Cloud Messaging (GCM) DeviceToken is requested and saved when new app version is installed
   * Automatically send updates to Marketing Cloud for addTag(), removeTag(), addAttribute(), removeAttribute() and setSubscriberKey()<br/>
   _`Note:` This means that the enablePush() call required after calling these methods in previous versions can be removed._
 * MPUSH-3064 - Add Done button to ETLandingPagePresenter class for Amazon devices
 * MPUSH-2933 - Add Web and Mobile Analytics<br/>
   _`NOTE:` A new optional parameter was added to readyAimFire().  You can set this parameter to true to send analytic data to the Web and Mobile Analytics pages within the Marketing Cloud._ See [Implement Analytics](../feature-implementation/analytics.html#android)
 * MPUSH-3071 - Updates for Android 5 (Lollipop) including:
   * Add new method called setNotificationResourceId() to accept an Android 5.0 compliant notification icon <br/>
   see <a href="http://developer.android.com/design/patterns/notifications.html" target="_blank">Android Notification Doc</a>

**Documentation Updates**
 * Ensure the enablePush() or disablePush() is called every time the launcher activity is started for an app: [Register Your Devices](register-your-devices.html#android-androidstudio)
 * Ensure the payload is added to custom Notification intent (if you have one): [Register Your Devices](register-your-devices.html#android-androidstudio)
 * Ensure that readyAimFire() uses an Application context: [Register Your Devices](register-your-devices.html#android-androidstudio)
 * Ensure that activityResumed() and activityPaused() are called in all your activities for Location Changes to detect changes between foreground and background: [Location Messages](../location-services/geolocation.html#android)
 * Ensure that activityResumed() and activityPaused() are called in all your activities for CloudPage Inbox to detect changes between foreground and background: [Rich Push Inbox](../rich-push/inbox.html#android)
 * Remove need to call enablePush() after calling setTag() or removeTag() [Tags](../feature-implementation/tags.html#android)
 * Remove need to call enablePush() after calling setAttribute() or removeAttribute() [Attributes](../feature-implementation/attributes.html#android)
 * Provide documentation to set subscriber key [Subscriber Key](../feature-implementation/subscriber-key.html#android)

___
#### Version 3.4.1 #### 
_Released December 11, 2014_
 * MPUSH-3153 - Fix registration error when Location is false in readyAimFire()

___
#### Version 3.4.0 ####
_Released November 17, 2014, correlating to Salesforce Marketing Cloud 2014-08 Release_
 * MPUSH-2913 - Rename SDK to Journey Builder for Apps SDK (primarily repo and comment changes)
 * MPUSH-2910 - Rename PracticeField to Journey Builder for Apps SDK Explorer (full rename of app)
 * MPUSH-2897 - Ensure SDK does not write to disk on main thread.<br/>
   _`NOTE:` A new parameter was added to readyAimFire() and setGcmSenderId() was removed._
 * MPUSH-2701 - Support interactive notifications (up to three buttons on a notification)
 * MPUSH-2701 - Support picture notifications (using custom key et_big_pic)
 * MPUSH-2702 - Support large text notifications on supported Android versions 
 * MPUSH-2727 - Add package name to intent-filter for ETOpenReceiver in AndroidManifest.xml<br/>
   _Changes required to add package name to intent-filter in AndroidManifest.xml_<br/>
   _`NOTE:` This ETOpenReceiver is now required to handle opening the application when a notification is tapped_

___

### Version 3.3.0
_Released August 25, 2014, correlating to ExactTarget's 2014-06 Release_<br/>
_(supported)_<br/>

#### Major Notes ####
* MPUSH-2053 - Add support for Amazon
* MPUSH-2371 - iBeacons support for Android 4.3 and later
* MPUSH-2532 - Event-based Analytics
* MPUSH-2668 - set default for locationType to geofence

### Version 3.2.0
_Released July 21, 2014, correlating to ExactTarget's 2014-05 Release_<br/>
<span class="alert">_This version no longer supported as of March 9, 2015._</span>

#### Major Notes ####
* MPUSH-2428 - Update PracticeField app to support cloud pages.
* MPUSH-2327 - Send GCM Sender ID to server for logging/issue resolution purposes
* MPUSH-2308 - Trigger re-register if GCM Sender ID changes.
* MPUSH-2306 - Send deviceName with Registration
* MPUSH-2238 - push_Enabled flag not sent with registration call
* MPUSH-2223 - Fix issue with calling enable/disable push too quickly
* MPUSH-2204 - Use consistent payload throughout workflow for opendirect handlers
* MPUSH-2193 - Add public getter methods for Attributes and Tags
* MPUSH-2158 - Add "debug" tag to any app running as a debug build
* MPUSH-2118 - Geofence re-downloading when device wakes up sometimes triggers 2nd fence message
* MPUSH-2079 - Fix some messages being sent multiple times to middle tier
* MPUSH-2041 - Log GPS Failure in LocationManager
* MPUSH-2166 - Show Notification when GooglePlayServices is out of date instead of dialog box.
* MPUSH-1859 - Create a modern and more realistic Public Demo app (PracticeField) that uses all SDK features. 
* MPUSH-2167 - PracticeField App Demo should show how to use proguard.cfg to be compatible with SDK and its jar dependencies.

### Version 3.1.2
_Released April 15, 2014, correlating to ExactTarget's 2014-04 Release_<br/>
<span class="alert">_This version no longer supported as of November 10, 2014._</span>

#### Major Notes ####
* MPUSH-1941 - Fix expectation of no message per period (geofences)
* MPUSH-1926 - Sometimes geofences aren't monitored after device reboot
* MPUSH-1858 - Don't use ORMLite's built-in reference counting so developers can still use it.
* MPUSH-1725 - VACUUM internal SQLite DB to keep size small.
* MPUSH-1697 - Validate AndroidManifest.xml on startup and throw ETException if missing required options.
* MPUSH-1556 - fenceID was not being sent with Stats.
* Add Interfaces for EventBus Listeners. Helps ensure that listener methods are implemented properly.
* Add checks to readyAimFire() to ensure the required permissions, receivers, and services exist in the ApplicationManifest.xml. Throws ETException if they don't.

### Version 3.1
_Released April 11, 2014, correlating to ExactTarget's 2014-03 Release_<br/>
<span class="alert">_This version no longer supported as of August 25, 2014._</span>

#### Major Notes ####
* Added Support for Geofence messaging
* Centralized support for how app backgrounding was handled.
* Add ability to turn on/off features in the call to ETPush.readyAimFire().

### Version 3.0
<span class="alert">_This version no longer supported as of July 21, 2014._</span>

#### Major Notes ####
* Code refactoring so you no longer need to extend any ExactTarget classes. Use ETPush.readyAimFire() to bootstrap.
* Support for Action/Uri type of Intents when notification is tapped.
* Ability to customize the notification and Intent by extending ET_GenericReceiver.
* ETAnalytics changes to use onPause() and onResume() for more accurate time tracking.
* Internal stability enhancements for returning data to ExactTarget.
* Removed deprecated GoogleCloudMessaging gcm.jar in favor of GoogleCloudMessaging included in Google Play Services

#### Deprecations
* **AnalyticsActivity** - Use ETAnalytics directly from each of your Activity's onPause() and onResume()
* **PushEnabledApplication** - Call ETPush.readyAimFire(this) from your Application's onCreate() to bootstrap instead of extending PushEnabledApplication.
* **gcm.jar** - Instead of gcm.jar, include GooglePlayServices support in your application. see: http://developer.android.com/google/play-services/setup.html

### Version 2.1
<span class="alert">_This version no longer supported as of July 21, 2014._</span>
* Providing a URL in the OpenDirect field through MobilePush will cause the provided URL to load when the notification is selected from the tray. This will *only* work if you have not specified a recipient for OpenDirect payloads. 

### Version 2.0
<span class="alert">_This version no longer supported as of July 21, 2014._</span>

#### Major Notes
* Support for Access Tokens in place of Client ID/Secret. Access Token is provided by Code@ExactTarget during app registrations. 
* Fixed a bug related delivery of payload to notification recipient.
* Added support for setting Subscriber ID to the record
* Reworked persistent store internally to better handle pushState
* Signficant rework of sample app to better demonstrate and comment code.

#### Deprecations
* **configureSDKWithAppIdAndClientIdAndClientSecret(String etAppId, String clientId, String clientSecret)** - Use **configureSDKWithAppIdAndAccessToken(String etAppId, String accessToken)** instead.
* **ETPush.pushEnabled** - Use **isPushEnabled()** in conjunction with **enablePush()** or **disablePush()** instead.

### Version 1.0
<span class="alert">_This version no longer supported as of July 21, 2014._</span>
* First public version
