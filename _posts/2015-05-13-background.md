---
layout: page
title: "Upgrading"
subtitle: "Upgrading From a Previous Release"
category: overview
date: 2015-05-14 12:00:00
order: 3
---
Below you will find the required changes to upgrade from an older SDK release to a newer SDK release.

### From v4.2 to v4.3
Full Documentation for v4.2 can be found [HERE](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android-v4.2.0/).

* Remove all manifest entries related to receivers and services.
* Remove any reference to `ETLandingPagePresenter`.
* Remove all permissions _*except*_ for `ACCESS_FINE_LOCATION` and `RECEIVE_BOOT_COMPLETED` if your application uses Geofences or Beacons.
* You must leave the `android:name` field in your `<application>` tag.

### From v4.1 to v4.2
Full Documentation for v4.1.x can be found [HERE](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android-v4.1.0/).

* Update Google Play Services dependencies to v8.x or higher.

### From v4.0 to v4.1
Full Documentation for v4.0.x can be found [HERE](http://salesforce-marketingcloud.github.io/JB4A-SDK-Android-v4.0.0/).

* Replace receiver's `CONNECTIVITY_CHANGE` intent-filter with `AIRPLANE_MODE`.
* Remove unnecessary `GET_ACCOUNTS` permission.

### Documentation for Older Versions
The Documentation for releases prior to 4.x can be found on Code@ [HERE](https://code.exacttarget.com/apis-sdks/journey-builder-for-apps)
