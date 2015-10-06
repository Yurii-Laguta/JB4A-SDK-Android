---
layout: page
title: "Overview"
subtitle: "Implement Location Services"
category: location
date: 2015-05-14 08:44:12
order: 1
---
The Journey Builder for Apps SDK uses location capabilities to store end user location data in the ExactTarget database. You can use this information to target messages to a segmented group of contacts. The app pre-downloads geofence messages and triggers those messages when a mobile device crosses a geofence boundary.

Note that the account using this functionality must have access to both MobilePush and Location Services in order to successfully receive location alerts.

> At this time, ensure that you use version 7.8 of Google Play Services to enable geolocation for your app. If you complile your app using Google Play Services version 8 or later, you will receive an error and geolocation will fail to function. Unless you must use one of the features outlined in the [September 2015 section of the Google APIs](https://developers.google.com/android/guides/releases), follow the [troubleshooting steps]({{ site.baseurl }}/location/geolocation-troubleshooting.html) to deal with the error.