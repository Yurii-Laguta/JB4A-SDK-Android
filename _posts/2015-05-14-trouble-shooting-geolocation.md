---
layout: page
title: "Geolocation Troubleshooting"
subtitle: "Troubleshooting Location Services Errors"
category: trouble-shooting
date: 2015-05-14 08:44:12
order: 3
---
You may receive the following error when using location services with the JB4A SDK:

~~~
java.lang.reflect.InvocationTargetException
    at java.lang.reflect.Method.invoke(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:372)
    at com.exacttarget.etpushsdk.util.EventBus.post(SourceFile:179)
    at com.exacttarget.etpushsdk.util.EventBus.postSticky(SourceFile:202)
    at com.exacttarget.etpushsdk.ETPush$5.run(SourceFile:1174)
    at java.lang.Thread.run(Thread.java:818)
 Caused by: java.lang.IncompatibleClassChangeError: The method 'boolean com.google.android.gms.common.api.GoogleApiClient.isConnected()' was expected to be of type interface but instead was found to be of type virtual (declaration of 'java.lang.reflect.ArtMethod' appears in /system/framework/core-libart.jar)
    at com.exacttarget.etpushsdk.ETLocationManager.getNewLocationFromAndroid(SourceFile:459)
    at com.exacttarget.etpushsdk.ETLocationManager.foregroundLocationCheck(SourceFile:448)
~~~

To correct this error, ensure that you complile your app with version 7.8.0 or earlier of Google Play Services.