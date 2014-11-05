# ExactTarget Journey Builder for Apps Android SDK (formerly MobilePush Android SDK)

This is the git repository for the ExactTarget Journey Builder for Apps Android SDK (formerly MobilePush Android SDK). 

For more information, please see [Code@ExactTarget](http://code.exacttarget.com), or visit the online documentation [here](http://exacttarget.github.com/JB4A-SDK-Android).

## Release History
As of July 2014, we have clarified the version numbering of the SDK and versions that will be supported.  For more information see [here](https://code.exacttarget.com/apis-sdks/mobilepush-sdks/mobile-push-sdk.html).

### Version 3.4.0
_Released November 10, 2014, correlating to ExactTarget's 2014-08 Release_
_(supported)_

#### Major Notes
* MPUSH-2913 - Rename SDK to Journey Builder for Apps SDK (primarily repo and comment changes)
* MPUSH-2910 - Rename PracticeField to Journey Builder for Apps SDK Explorer (full rename of app)
* MPUSH-2897 - Ensure SDK does not write to disk on main thread.<br/>
_Changes required due to a new parameter in readyAimFire() and removal of setGcmSenderId()._
* MPUSH-2701 - Support interactive notifications (up to three buttons on a notification)
* MPUSH-2701 - Support picture notifications (using custom key et_big_pic)
* MPUSH-2702 - Support large text notifications on supported Android versions 
* MPUSH-2727 - Add package name to intent-filter for ETOpenReceiver in AndroidManifest.xml<br/>
_Changes required to add package name to intent-filter in AndroidManifest.xml_

### Version 3.3.0
_Released August 25, 2014, correlating to ExactTarget's 2014-06 Release_
_(supported)_

#### Major Notes
* MPUSH-2053 - Add support for Amazon
* MPUSH-2371 - iBeacons support for Android 4.3 and later
* MPUSH-2532 - Event-based Analytics
* MPUSH-2668 - set default for locationType to geofence

### Version 3.2.0
_Released July 21, 2014, correlating to ExactTarget's 2014-05 Release_
_(supported)_

#### Major Notes
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
_Released April 15, 2014, correlating to ExactTarget's 2014-04 Release_
<span class="alert">_This version no longer supported as of November 10, 2014._</span>

#### Major Notes
* MPUSH-1941 - Fix expectation of no message per period (geofences)
* MPUSH-1926 - Sometimes geofences aren't monitored after device reboot
* MPUSH-1858 - Don't use ORMLite's built-in reference counting so developers can still use it.
* MPUSH-1725 - VACUUM internal SQLite DB to keep size small.
* MPUSH-1697 - Validate AndroidManifest.xml on startup and throw ETException if missing required options.
* MPUSH-1556 - fenceID was not being sent with Stats.
* Add Interfaces for EventBus Listeners. Helps ensure that listener methods are implemented properly.
* Add checks to readyAimFire() to ensure the required permissions, receivers, and services exist in the ApplicationManifest.xml. Throws ETException if they don't.

### Version 3.1
Released April 11, 2014, correlating to ExactTarget's 2014-03 Release
<span class="alert">_This version no longer supported as of August 25, 2014._</span>

#### Major Notes
* Added Support for Geofence messaging
* Centralized support for how app backgrounding was handled.
* Add ability to turn on/off features in the call to ETPush.readyAimFire().

### Version 3.0
<span class="alert">_This version no longer supported as of July 21, 2014._</span>

#### Major Notes
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
