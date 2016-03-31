---
layout: page
title: "Overview"
subtitle: "Implement Location Services"
category: location
date: 2015-05-14 08:44:12
order: 1
---
The Journey Builder for Apps SDK uses the location capabilities of your device to trigger location based notifications. The SDK caches Geofence messages and displays those messages when a mobile device crosses a Geofence boundary. You must receive user permission to implement location services.

Note that the Marketing Cloud must enable your account with access to MobilePush and Location Services in order to successfully use this functionality.

> NOTE: Versions 4.2 and newer of the JB4A Mobile Push Android SDK must be compiled with Google Play Services v8.x or newer. Older versions of the SDK must be compiled with Google Play Services v7.8 or older. Failing to follow these guidelines will result in an internal error and geolocation will not function. See the [troubleshooting steps]({{ site.baseurl }}/trouble-shooting/trouble-shooting-geolocation.html) for details and a resolution regarding this issue.<br/>
