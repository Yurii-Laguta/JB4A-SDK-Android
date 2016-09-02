---
layout: page
title: "Beacons"
subtitle: "Add Beacons"
category: location
date: 2016-09-01 12:00:00
order: 3
---
1. Add geolocation. Follow the steps for [Add Geolocation]({{ site.baseurl }}/location/geolocation.html).
1. Add the <a href="http://altbeacon.org/" target="_blank">AltBeacon Library</a> dependency to the `app/build.gradle` file:
<script src="https://gist.github.com/sfmc-mobilepushsdk/82638adbc0cd4677ee04feb6d19c7681.js"></script>
1. When SDK initialization has completed, you must call `startWatchingProximity()` in order for the user to receive beacon notifications:
<script src="https://gist.github.com/sfmc-mobilepushsdk/84ef64ac6f3d0c029306f0e1683a8be1.js"></script>

> To understand how beacons behave in different situations, see Beacon Behavior.
