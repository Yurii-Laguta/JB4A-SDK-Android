---
layout: page
title: "Eclipse Dependencies"
subtitle: "Eclipse Dependencies"
category: sdk-implementation
date: 2015-05-14 12:00:00
order: 3
---

> NOTE: As of December 2015, Google has sunset support for their Android Developer Tools (ADT) plugin and ANT build system pluging for Eclipse.  Likewise, the JB4A Android Mobile Push SDK support for Eclipse is being sunset and will no longer be supported in an upcoming release.  Please upgrade to Android Studio as soon as possible.

### Adding Dependencies

1. Copy the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/blob/master/JB4A-SDK/etsdk-{{ site.currentVersion }}.jar?raw=true" target="_blank">etsdk-{{ site.currentVersion }}.jar</a> into the **{project root}/libs** folder for your project.

1. Copy additional dependency jars from the SDK Explorer project found <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer/eclipse-build-google/libs" target="_blank">HERE</a>. Your final libs folder should resemble this sample where x.x.x is the version you use:<br/>
    <img class="img-responsive" src="{{ site.baseurl }}/assets/eclipse-libs.png" />

1.  Beacon Beta testers ONLY must copy the Android Beacon dependency from <a href="https://altbeacon.github.io/android-beacon-library/download.html" target="_blank">here</a> and follow <a href="https://altbeacon.github.io/android-beacon-library/configure.html" target="_blank">these</a> directions.  We tested and support version 2.5.1. 

1. For a Google Build, you need to add Google Play Services as a *Library Project* to your Android Project. Find instructions <a href="http://developer.android.com/google/play-services/setup.html" target="_blank">HERE</a>.<br/>

### Update Your AndroidManifest.xml

Eclipse users cannot benefit from Android Studio's manifest merger process and must add the required permissions, activities, receivers and services to their project's manifest.
<script src="https://gist.github.com/sfmc-mobilepushsdk/e2b900bb655e09e8b67b.js"></script><br/>

### Bootstrap the SDK
[Follow the instructions found HERE]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html#bootstrap)
