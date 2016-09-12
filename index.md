---
layout: default
title: "README"
---
# Journey Builder for Apps Android SDK

This documentation includes information for the Marketing Cloud Journey Builder for Apps Android SDK:<br/>
<a href="https://github.com/ExactTarget/JB4A-SDK-Android" target="_blank">JB4A Android SDK GitHub Repository</a><br>

Review the Java docs for the SDK:<br/>
<a href="{{ site.baseurl }}/javadocs/index.html" target="_blank">Android SDK Java Docs</a>

Learn how to use the SDK with the Marketing Cloud Learning App:<br/>
<a href="https://github.com/salesforce-marketingcloud/LearningAppAndroid" target="_blank">Android Learning App</a>

Download the latest JB4A SDK aar:<br/>
<a href="https://github.com/ExactTarget/JB4A-SDK-Android/blob/master/JB4A-SDK/etsdk-{{ site.currentVersion }}.aar?raw=true" target="_blank">etsdk-{{ site.currentVersion }}.aar</a>

> NOTE: Versions 4.2 and newer of the JB4A Mobile Push Android SDK must be compiled with Google Play Services v8.x or newer. Older versions of the SDK must be compiled with Google Play Services v7.8 or older. Failing to follow these guidelines will result in an internal error and geolocation will not function. See the [troubleshooting steps]({{ site.baseurl }}/trouble-shooting/trouble-shooting-geolocation.html) for details and a resolution regarding this issue.<br/><br/>

## SDK Compatibility

The JB4A Android SDK is compatible with Android API versions 15 (aka _Ice Cream Sandwich_ or _Android 4.0.3_) or newer and has dependencies on Google Play Services and the Android Support v4 library.<br/><br/>

## Release History
For releases prior to {{ site.currentVersion }}, see: <a href="http://salesforce-marketingcloud.github.io/JB4A-SDK-Android-v{{ site.previousVersion }}/" target="_blank">Prior Release Documentation</a><br/><br/>

#### Version 4.6.0

_Released September 12, 2016, correlating with the Marketing Cloud 2016-05 release._

* **MobilePush Beacons Support** - The SDK supports [MobilePush Beacons]({{ site.baseurl }}/location/add-beacons.html).
* **Adjust Beacons Logging** - You can control the amount of logging for MobilePush beacons by [setting the log level]({{ site.baseurl }}/location/add-beacons.html#log_level).
* **Use Multiple Push Providers Cautiously** - If you use multiple push proviers, there are several things you should be aware of: [Troubleshooting]({{ site.baseurl }}/trouble-shooting/multiple-push-sdks.html).

#### Version 4.5.0

_Released July 18 2016, correlating to the Marketing Cloud 2016-04_<br/>

* MOBILESDK-907 - Add PI Analytics [track_cart]({{ site.baseurl }}/features/analytics.html#track_cart) and [track_conversion]({{ site.baseurl }}/features/analytics.html#track_conversion) convenience methods.
* MOBILESDK-494 - Force an immediate Marketing Cloud Registration call.

#### Version 4.4.1

_Released June 1 2016, correlating to the Marketing Cloud 2016-03.1_<br/>

* MOBILESDK-938 - Gracefully handle devices without Google Play Services installed.

#### Version 4.4.0

_Released May 23 2016, correlating to the Marketing Cloud 2016-03_<br/>

> As of the current SDK release (2016-03, Version 4.4.0 of the JB4A SDK), we no longer support apps built using Eclipse. This end-of-life activity includes removal of JAR files, documentation, and all other forms of support. Ensure you can continue your development activities by switching to the Google development platform. For additional information on Google’s support of Android Eclipse tools, please view the [Google official announcement](http://android-developers.blogspot.com/2015/06/an-update-on-eclipse-android-developer.html).

* MOBILESDK-707 - Add developer convenience method for `getDeviceId()`.
* MOBILESDK-712 - Ensure proper expiry of CloudPage messages.
* MOBILESDK-713 - Replace `readyAimFire()` with `configureSdk()`.
* MOBILESDK-826 - Do not show a notification for a message with an empty alert.
* MOBILESDK-833 - Send Subscriber Key in PI Analytics Payload.
* MOBILESDK-839 - Allow Application Label to be set in PushConfig.Builder().
* MOBILESDK-845 - Retry for failed GET REST requests.
* MOBILESDK-894 - Provide Google Play Services status in `configureSdk()` callback.

#### Version 4.3.3

_Released June 1 2016, correlating to the Marketing Cloud 2016-02.3_<br/>

* MOBILESDK-938 - Gracefully handle devices without Google Play Services installed.

#### Version 4.3.2

_Released May 9 2016, correlating to the Marketing Cloud 2016-02 MR2_<br/>

* MOBILESDK-893 - [Google Play Services compatibility alert suppression]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html#configure).

#### Version 4.3.1

_Released April 6 2016, correlating to the Marketing Cloud 2016-02 MR1_<br/>

* MOBILESDK-797 - Fix app transfer to new Android devices.  Unable to decrypt Android Device ID.
* MOBILESDK-794 - Validate "Location" and "Proximity" are enabled before calling state variables in `getSdkState()`.
* MOBILESDK-761 - Ensure the data adapter for SDK Cloud Pages only returns active Marketing Cloud messages

#### Version 4.3.0

_Released March 21 2016, correlating to the Marketing Cloud 2016-02_<br/>

* MOBILESDK-721 - Deprecate `activityPaused()` and `activityResumed()` as those are only required by Android Gingerbread which is no longer supported.
* MOBILESDK-719 - Remove `BATTERY_LOW` and `BATTERY_OKAY` intent-filters from our receivers and let Android handle the location services on/off in those states.
* MOBILESDK-669 - Do not send null Tags to the Marketing Cloud
* MOBILESDK-666 - Resolved NPE and IllegalState errors in `ETLocationManager`.
* MOBILESDK-659 - Improve timeliness of downloading of Geofence and Beacon messages.
* MOBILESDK-642 - Failed GCM Registrations now implement an exponential back-off retry.
* MOBILESDK-639 - Location messages with OpenDirect URLs now correctly launch the `ETLandingPagePresenter` Class.
* MOBILESDK-634 - [Added a Logging interface]({{ site.baseurl }}/features/loginterface.html).
* MOBILESDK-632 - Remove unnecessary `ACCESS_WIFI_STATE` permission.
* MOBILESDK-601 - For those in the Beacon Beta Test Group: Beacon messages, if enabled, will update at least every 24 hours while the application is in the background.
* MOBILESDK-594 - Updated Google Play Services dependency to version 8.4, Support-v4 to version 23.1.1 and the Android Beacon Library to version 2.7.
* MOBILESDK-580 - Implement [`getSdkState()`]({{ site.baseurl }}/trouble-shooting/trouble-shooting-push-setup.html#getsdkstate) developer convenience method.
* MOBILESDK-559 - Ensure inter-platform consistency and predictability for location analytics.
* MOBILESDK-531 - Remove Amazon's ADM support.
* MOBILESDK-509 - Never send a null Subscriber Key to the Marketing Cloud.
* MOBILESDK-481 - Restrict [Reserved Words]({{ site.baseurl }}/features/attributes.html#reservedwords) from being used as Attribute Keys.
* MOBILESDK-437 - Verify Android Beacon Library, if required, during SDK configure.
* MOBILESDK-397 - Replace legacy `GCM.register()` with `InstanceId.getToken()` for GCM registration.
* MOBILESDK-330 - Verify Google Play Services Location library, if required, during SDK configure.

#### Version 4.2.0

_Released February 2 2016, correlating to the Marketing Cloud 2016-01_<br/>

* MOBILESDK-501 - Resolved "Format Conversion Error" in ETLocation Logging
* MOBILESDK-487 - Display Google Play Services Version in the Logs
* MOBILESDK-484 - Implemented [trackPageView()]({{ site.baseurl }}/features/analytics.html){:target="_blank"}
* MOBILESDK-480 - Update Google Play Services references in Documentation
* MOBILESDK-479 - Completed [Manifest Updates]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html){:target="_blank"}
* MOBILESDK-478 - Updated [getAttributes()]({{ site.baseurl }}/features/attributes.html){:target="_blank"} and [getTags()]({{ site.baseurl }}/features/tags.html){:target="_blank"}
* MOBILESDK-477 - Implement [permissions check for location when using Marshmallow versions 23 or greater]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html){:target="_blank"}
* MOBILESDK-465 - Resolved Lint warning regarding unchecked type casts
* MOBILESDK-456 - Resolved NPE in RegistrationSQL Attributes getKey()
* MOBILESDK-432 - Review and update Javadocs for all other packages within the SDK
* MOBILESDK-408 - ANDROID SDK Integration Simplification 
* MOBILESDK-364 - Compile & Test SDK w/Google Play Services v8.1
* MOBILESDK-361 - Resolved ANDROID SDK Incompatible w/Google Play Services v8.1
* MOBILESDK-329 - Resolved Permission Issue w/Marshmallow
* MOBILESDK-257 - Replace deprecated org.apache.Http with UrlConnection

#### Version 4.1.1

_Released January 8 2016, correlating to the Marketing Cloud 2015-07.2-HF1 Release_<br/>

* MSDK-501 - Fix for format conversion error in ETLocationManager

#### Version 4.1.0
_Released November 20 2015, correlating to the Marketing Cloud 2015-07.2 Release_<br/>

* MSDK-238 - Make sure latitude/longitude values send with proper formatting regardless of the device language.
* MSDK-248 - Add Retry logic for all data POSTed to the Marketing Cloud including Registrations and Analytics.  If the initial POST fails, then the POST REST call will be retried in a logarithmic fashion.
* MSDK-292 - Make sure app monitors Location regions after a device reboot.
* MSDK-295 - Remove unnecessary GET_ACCOUNTS permission.  You may remove this permission from your AndroidManifest.xml file.
* MSDK-297 - Replace CONNECTIVITY_CHANGE Receiver with AIRPLANE_MODE Receiver. Update your [AndroidManifest.xml]({{ site.baseurl}}/sdk-implementation/implement-sdk-google.html) file.
* MSDK-298 - Return an instance of ETPush in ReadyAimFireCompletedEvent
* MSDK-299 - Make sure that if any problems in POSTing analytics data occur, that the local database saves at most 1000 entries so as not to add excessive database content.
* MSDK-307 - Remove ET Logger
* MSDK-312 - Throw RuntimeException for configuration errors in debug builds to highlight errors before releasing.
* MSDK-406 - Consolidate Receiver Intent Filters. Update your [AndroidManifest.xml]({{ site.baseurl}}/sdk-implementation/implement-sdk-google.html) file.
* MSDK-407 - Simplify Geolocation Implementation. Update your [AndroidManifest.xml]({{ site.baseurl}}/location/geolocation.html) file.
* MSDK-431 - Deprecate URL and Action Notification method. If your app uses these methods, [move to the indicated alternates]({{ site.baseurl}}/features/override-notifications.html).
* MSDK-189 - Update SDK with newest Beacon library.  Companies testing as part of a Beacons Beta test should switch to using this library in your Gradle dependency: org.altbeacon:android-beacon-library:2.5.1@aar.

  Remove requirement for Beacons library when Location is turned on. If you're not part of the Beacons Beta Test and you use Locations, you can remove the beacons library from your build.

#### Version 4.0.9
_Released November 20 2015_<br/>

* MSDK-453  - Fix for Java ConcurrentModificationException when modifying Tags & Attributes

#### Version 4.0.7
Released October 2 2015, correlating to the Marketing Cloud 2015-06 Release_<br/>

* MSDK-82  - Only check classes for the SDK's enabled features
* MSDK-237 - Display Page Title or URL when showing a CloudPage
* MSDK-239 - Only require the beacon library if Location is enabled
* MSDK-243 - Fix for Java ConcurrentModificationException when modifying Attributes
* MSDK-252 - Apply PRNG encryption fix only if it hasn't previously been applied
* MSDK-253 - Correctly transmit UTF-8 characters in Registration POST REST calls
* MSDK-272 - Replace StringBuilder() with thread-safe StringBuffer() to resolve IndexOutOfBounds Exception
* MSDK-322 - Do not send Location History
* MSDK-324 - Remove GET_ACCOUNTS permission
* MSDK-328 - Never send "null" as a String in a Registration payload
* MSDK-332 - Disable Proximity (beacons) for API < 18
* MSDK-334 - Validate PRNG fix required Proguard statements during SDK configure
* MSDK-336 - Fix for NPE during WakefulBroadcastReceiver.completeWakefulIntent()

> Implementing the PRNG fix requires reflection.  Please ensure your Proguard config file contains the correct statements found [here]({{ site.baseurl }}/sdk-implementation/proguard.html).

#### Version 4.0.6
_Released September 9th 2015_<br/>

* MSDK-305 - Replace RuntimeException thrown during manifest inspection with LogCat Log.ERROR output<br/>

#### Version 4.0.5
_Released August 25th 2015_<br/>

* MSDK-243 - Fix for Java ConcurrentModificationException<br/>

#### Version 4.0.4
_Released July 22nd 2015_<br/>

* MSDK-11 - Group Registrations that are sent to the Marketing Cloud when updating Tags, Attributes, and other contact data to reduce traffic and improve throughput.<br/>

#### Version 4.0.3
_Released July 9th 2015, correlating to the Marketing Cloud 2015-04.3 Release_<br/>

* MPUSH-3809 - Wipe data from device if encryption fails.<br/>
  _`Note:` Encryption of data was added in 4.0.0.  This change will prevent a device from integrating with the SDK if encryption fails._ See [Encryption Trouble Shooting]({{ site.baseurl }}/trouble-shooting/trouble-shooting-encryption.html) for more information.<br/>
* MPUSH-3824 - Throw a RunTimeException if [ProGuard]({{ site.baseurl }}/sdk-implementation/proguard.html) statements obfuscated required classes within the SDK.<br/>
* MPUSH-3841 - Pushes without an `alert` key in the payload will not result in a notification being shown to the user.<br/>
* MPUSH-3822 - Make sure SDK respects a `sound` key value of `none` which results in a notification without a sound.<br/>
* MPUSH-3828 - SDK was reporting `locationEnabled` as `true` to the Marketing Cloud even when it was disabled.<br/>
* MPUSH-3782 - Allow for [Background Push]({{ site.baseurl }}/features/background-push.html) messages via `content-available` flag.<br/>

#### Version 4.0.0
_Released June 24th 2015, correlating to the Marketing Cloud 2015-04 Release_<br/>

* MPUSH-3377 - Implement multi-threaded support so SDK will not block UI thread.<br/>
  _`Note:` Code changes required for ETPush.activityResumed() and ETPush.activityPaused() for apps targeting earlier than API level 14._ <br/>
* MPUSH-3379 - Ensure push and location (if turned on in readyAimFire()) is enabled by default and any updated registration data is sent each time readyAimFire() is called.<br/>
   _`Note:` Code should be changed to remove the call for enablePush() and startWatchingLocation() in your home launcher Activity._
* MPUSH-3380 - Publish AAR to SDK's gh-pages branch.<br/>
  _`Note:` Change app/build.gradle to use com.exacttarget.etpushsdk:etsdk:4.0.0@aar._ See [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).<br/>
* MPUSH-3441 - Remove Access Token from payload and add to REST header.<br/>
* MPUSH-3444 - Implement Builder Pattern for ETPush Initialization.<br/>
  _`Note:` Change call to readyAimFire() to use this builder pattern._ See [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).<br/>
* MPUSH-3445 - Add industry accepted standard getInstance() and deprecate getDefault().<br/>
  _`Note:` Change code to use getInstance()_<br/>
* MPUSH-3411 - SDK will no longer set a subscriber key by default.  This change will ensure that any imported contacts will not be overridden by SDK.<br/>
  _`Note:` The subscriber key will be set by the Marketing Cloud instead.  This matches what happens with iOS devices now._ See [Subscriber Key]({{ site.baseurl }}/features/subscriber-key.html).<br/>
* MPUSH-3396 - Create separate AndroidManifest.xml for Amazon devices.<br/>
  _`Note:` You can create an Amazon product flavor within Android Studio and include this manifest for Amazon builds._ See [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).<br/>
* MPUSH-3378 - Improve verification of incorrect AndroidManifest.xml setup for both Amazon and Google devices.<br/>
* MPUSH-3580 - Change verification of incorrect AndroidManifest.xml setup to throw a RunTimeException to ensure critical errors are fixed early in development process. <br/> 
* MPUSH-3390 - Post an Event to the EventBus when readyAimFire() finishes with success or failure status.<br/>
  _`Note:` You can implement the ReadyAimFireInitCompletedEvent in your code to execute code after readyAimFire() has completed in order to avoid UI blocks in your app._ [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).<br/>
* MPUSH-3374 - Add information about the SDK, app version and other pertinent information to error logging in ADB logcat.<br/>
* MPUSH-3356 - Send registration data when network becomes available including changes to tags/attributes that were made when network was unavailable.<br/>
* MPUSH-3321 - Consolidate broadcast receivers and ensure proper use of Wakeful services.<br/>
 _`Note:` Code changes required to implement ETPushReceiver and ETPushService._ See [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).<br/>
 _`Note:` Code changes may be required if you use location to use ETLocationReceiver and ETLocationService._ See [Location Services]({{ site.baseurl }}/location/geolocation-overview.html)<br/>
 _`Note:` In order to change how notifications are overridden, you must now use interfaces within ETNotifications class._ See [Override Notifications]({{ site.baseurl }}/features/override-notifications.html)<br/>
* MPUSH-3330 - Obfuscate sensitive data such as App Id, Access Token, and GCM Sender Id from ADB logcat.<br/>
* MPUSH-3423 - Add customer facing log capturing to assist with debugging when not connected to ADB.<br/>
 _`Note:` APIs available to start capture, stop capture, save log, and email log._  See [ET Logger]({{ site.baseurl }}/features/et-logger.html) <br/>
* MPUSH-3160 - Remove Jackson third party library and use Android internal classes to reduce Dex count of SDK.<br/>
 _`Note:` SDK no longer depends on this JAR.  It may be removed if your application does not require it._ <br/>
* MPUSH-3218 - Remove Ormlite third party library and use Android internal classes to reduce Dex count of SDK.<br/>
 _`Note:` SDK no longer depends on this JAR.  It may be removed if your application does not require it._ <br/>
* MPUSH-3156 - Remove Joda third party library and use Android internal classes to reduce Dex count of SDK.<br/>
 _`Note:` SDK no longer depends on this JAR.  It may be removed if your application does not require it._ <br/>
* MPUSH-3333 - Change payload to properly send SDK Version and GCM Sender Id which are saved in pushAddressExtension table in the Marketing Cloud.<br/>
* MPUSH-3322 - Ensure database used in SDK is initialized at beginning of readyAimFire().<br/>
* MPUSH-3313 - Implement registerActivityLifecycleCallbacks() to handle onResume() and onPause() detection for Activities.<br/>
 _`Note:` You can remove ETPush.activityResumed() and ETPush.activityPaused() for apps targeting API level 14 or later._ <br/>
* MPUSH-3256 - Encrypt all data within the SDK SharedPreferences file and all sensitive data in the SDK SQLite database.<br/>
 _`Note:` If you use ProGuard in your production app, you must not obfuscate the SDK._ See [ProGuard Implementation]({{ site.baseurl }}/sdk-implementation/proguard.html)<br/>
* MPUSH-3402 - Obfuscate non-public methods and attributes.<br/>
* MPUSH-3233 - Support Google Play Services v6.5.87 and up. <br/>
* MPUSH-3150 - Update license for SDK to use Salesforce.com. <br/>
* MPUSH-3320 - Ensure that Location work within the SDK is not executed when location turned off in readyAimFire(). <br/>
* MPUSH-3293 - Build SDK with latest Android SDK, support, and appcompat libraries.<br/>
* MPUSH-3285 - Make sure CloudPage Inbox downloads occur whether Analytics are turned on or not.<br/>
* MPUSH-3485 - Fix crash in serializing/deserializing blank Attributes.
