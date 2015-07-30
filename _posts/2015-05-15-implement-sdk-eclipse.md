---
layout: page
title: "Eclipse Dependencies"
subtitle: "Eclipse Dependencies"
category: sdk-implementation
date: 2015-05-14 12:00:00
order: 3
---

Follow these Eclipse specific techniques to bootstrap the SDK to your Mobile app using the Eclipse IDE.

1.  Copy the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/blob/master/JB4A-SDK/etsdk-4.0.0.jar?raw=true" target="_blank">etsdk-4.0.0.jar</a> into the `{project root}/libs` folder for your project.

1.  Copy additional dependency jars from the SDK Explorer project found <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer/eclipse-build-google/libs" target="_blank">HERE</a>. Your final libs folder should resemble this sample where x.x.x is the version you are using:<br/>
    <img class="img-responsive" src="{{ site.baseurl }}/assets/eclipse-libs.png" />

1.  If you enable locations (as described [here]({{ site.baseurl }}/location/geolocation.html)) you must copy the Android Beacon dependency from the SDK Explorer project found <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer/eclipse-build-google/libs" target="_blank">HERE</a>. This dependency is required for GeoLocation and will be removed as a requirement in a future release. Your final libs folder should resemble this sample where x.x.x is the version you are using:<br/>
    <img class="img-responsive" src="{{ site.baseurl }}/assets/eclipse-libs-location.png" />

  > Failure to add this dependency if you enable location in readyAimFire() will result in a crash of your app: `java.lang.TypeNotPresentException: Type com/radiusnetworks/ibeacon/BleNotAvailableException not present`

1.  For a Google Build, you need to add Google Play Services as a *Library Project* to your Android Project.  Instructions can be found <a href="http://developer.android.com/google/play-services/setup.html" target="_blank">HERE</a>.<br/>

2.  For an Amazon Build, you should not include Google Play Services.  But you should make sure to adjust the Android Manifest as described here: [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html) or [Implement the SDK for Amazon]({{ site.baseurl }}/sdk-implementation/implement-sdk-amazon.html)