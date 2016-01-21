---
layout: page
title: "Overview"
subtitle: "Implement Location Services"
category: location
date: 2015-05-14 08:44:12
order: 1
---
The Journey Builder for Apps SDK uses location capabilities to store end-user location data in the Salesforce Marketing Cloud database. You can use this information to target messages to a segmented group of contacts. The app pre-downloads geofence messages and triggers those messages when a mobile device crosses a geofence boundary. You must receive user permission to implement location services.

Note that the Salesforce Marketing Cloud must enable your account with access to MobilePush and Location Services in order to successfully use this functionality.

> Google Play Services Note - At this time, ensure that you use version 7.8.0 or earlier of Google Play Services to enable geolocation for your app. If you compile your app using Google Play Services version 8 or later, you will receive an error and geolocation will fail to function. Unless you must use one of the features outlined in the [September 2015 section of the Google APIs](https://developers.google.com/android/guides/releases){:target="_blank"}, follow the [troubleshooting steps]({{ site.baseurl }}/trouble-shooting/trouble-shooting-geolocation.html) to deal with the error. For versions 4.2 and later, use version 8.1 or later of Google Play Services.