---
layout: page
title: "Beacons"
subtitle: "Add Beacons"
category: location
date: 2016-09-01 12:00:00
order: 3
---
1. Add geolocation. Beacons messages require a geolocation fix, so you must implement geolocation in your SDK. Follow the steps for [Add Geolocation]({{ site.baseurl }}/location/geolocation.html) before continuing.<br/>

1. Set the Boolean parameter for `setProximityEnabled()` in your `ETPushConfig.Builder`:
<script src="https://gist.github.com/sfmc-mobilepushsdk/06f47c53aca02c3dad2ef1d750c6f4ac.js"></script>
1. Add the <a href="http://altbeacon.org/" target="_blank">AltBeacon Library</a> dependency to the `app/build.gradle` file:
<script src="https://gist.github.com/sfmc-mobilepushsdk/82638adbc0cd4677ee04feb6d19c7681.js"></script>
1. When SDK initialization has completed, you must call `startWatchingProximity()` in order for the user to receive beacon notifications:
<script src="https://gist.github.com/sfmc-mobilepushsdk/84ef64ac6f3d0c029306f0e1683a8be1.js"></script>

> MobilePush prevents the app from displaying a beacon message with an empty alert. If you include AMPscript in your message that returns no content or an empty string, the mobile app will not display that message. 

> To understand how beacons behave in different situations, see the MobilePush beacons help documentation.
