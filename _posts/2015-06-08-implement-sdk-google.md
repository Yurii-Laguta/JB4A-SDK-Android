---
layout: page
title: "Google"
subtitle: "Integrating the SDK"
category: sdk-implementation
date: 2015-05-14 12:00:00
order: 1
---

To integrate the Salesforce Marketing Cloud Mobile Push Android SDK with your Mobile Android App, you will need to register a device with the Salesforce Marketing Cloud. To do this you must add the SDK and its dependencies to your application.  The following steps will guide you through the process.

This process connects the device to the MobilePush app you created previously in the [APP CENTER]({{ site.baseurl }}/create-apps/create-apps-overview.html). If you have not completed these steps please do so now.<br/>

> NOTE: The Salesforce Marketing Cloud Mobile Push Android SDK requires Android API 15 (aka _Ice Cream Sandwich or Android v4.0.3_) or greater and has dependencies on the Android Support v4 and Google Play Services libraries.  Android API 23 (aka _Marshmallow or Android v6.0_) and the new Android Permissions model is supported.<br/>

> NOTE: Eclipse support has been discontinued by Google and is being deprecated by the Sales Force Marketing Cloud.  Support for Eclipse will soon be discontinued by the Marketing Cloud, but until then the documentation for Eclipse implementations can be found [HERE]({{ site.baseurl }}/sdk-implementation/implement-sdk-eclipse.html). Please move to Android Studio as soon as possible to ensure compatibility with future releases.<br/>

## Integrating via Android Studio<br/><br/>

### Update Your Project's `build.gradle`
Add the following repositories to your application's `build.gradle` file.
<script src="https://gist.github.com/sfmc-mobilepushsdk/83bd7b645aeaf4c586cd.js"></script><br/>

### Update Your Project's `app\build.gradle`

#### Add an Application ID
Add an `applicationId` to the `defaultConfig{ }` section.  This is required for the manifest merger process to work and have the appropriate permissions and intent-filters assigned to your application.
<script src="https://gist.github.com/sfmc-mobilepushsdk/f67cb31c44328870f6e1.js"></script><br/>

#### Add the SDK's Dependencies
Add the following dependencies to your application `app\build.gradle` file.
<script src="https://gist.github.com/sfmc-mobilepushsdk/086bd8b65afc8d99c222.js"></script><br/>

### Update Your Project's `AndroidManifest.xml`
Your manifest must contain a named application and have a class that extends Android Application.  This is accomplished by adding an `android:name` field to the `<application>` tag.
<script src="https://gist.github.com/sfmc-mobilepushsdk/8b3d059b5382f40c92a8.js"></script>

> NOTE: As of v4.2 of the Salesforce Marketing Cloud Mobile Push Android SDK, you no longer have to explicitely declare the permissions, activities, receivers and services required by the SDK.  A manifest is provided in the AAR and Android's build tools will automatically merge the manifests. You should remove any previously included statements in your manifest to avoid conflicts.<br/><br/>

### <a name="configure"></a>Configure the SDK
In your Android Application Class' `onCreate()` method you will need to configure the SDK with a call to `readyAimFire()`:
<script src="https://gist.github.com/sfmc-mobilepushsdk/a1f32591efa5fcfb6943.js"></script>

> NOTE: `readyAimFire()` must be called from your Application Class to ensure that background receivers and services can be initialized properly.  Failing to do so will result in 1) your application failing to receive background push notifications, location updates, etc. and 2) potentially crashes.<br/>

> NOTE: Changes, including your initial registration from a device, propagate from the server every 5 minutes.  Ensure you wait an appropriate amount of time before expecting to receive push notifications or for changes to take affect.<br/>

## Success!
You should now be able to send a push notification from the Salesforce Marketing Cloud to your application!<br/><br/>

---

## Upgrading From Previous Versions

Please see the Upgrading section [HERE]({{ site.baseurl }}/overview/upgrading.html)