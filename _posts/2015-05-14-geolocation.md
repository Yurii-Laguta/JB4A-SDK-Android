---
layout: page
title: "GeoLocation"
subtitle: "Adding GeoLocation"
category: location
date: 2015-05-14 08:43:35
order: 2
---

1. Add the following permissions to **AndroidManifest.xml**:
<script src="https://gist.github.com/sfmc-mobilepushsdk/68477bb9c521a550d7af.js"></script>

1. Set the Boolean parameter for `setLocationEnabled()` in your `ETPushConfig.Builder()`:
<script src="https://gist.github.com/sfmc-mobilepushsdk/472545d620983be6d8d7.js"></script>

1. When SDK initialization has completed you must call `startWatchingLocation()` in order for the user to receive Geofence notifications:
<script src="https://gist.github.com/sfmc-mobilepushsdk/9102e0af94a15ceb7efa.js"></script>

	> NOTE: You may disable Geofence notifications by calling `stopWatchingLocation()`:

	<script src="https://gist.github.com/sfmc-mobilepushsdk/ea08a3981609479ffc7c.js"></script>

> MobilePush prevents the app from displaying a geofence message with an empty alert. If you include AMPscript in your message that returns no content or an empty string, the mobile app will not display that message. 