# ExactTarget MobilePush SDK for Android

This is the git repository for the ET MobilePush SDK for Android. 

For more information, please see [Code@ExactTarget](http://code.exacttarget.com).

## Release History

### Version 3.1

#### Major Notes
* Added Support for Geofence messaging
* Added Support for CloudPage messages. Both retrieved from the server (inbox) as well as a push message.
* Centralized support for how app backgrounding was handled.
* Add ability to turn on/off features in the call to ETPush.readyAimFire().

### Version 3.0

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
* Providing a URL in the OpenDirect field through MobilePush will cause the provided URL to load when the notification is selected from the tray. This will *only* work if you have not specified a recipient for OpenDirect payloads. 

### Version 2.0

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

* First public version
