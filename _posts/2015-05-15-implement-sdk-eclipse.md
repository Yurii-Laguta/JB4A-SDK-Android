---
layout: page
title: "Eclipse Dependencies"
subtitle: "Eclipse Dependencies"
category: sdk-implementation
date: 2015-05-14 12:00:00
order: 3
---

Follow these Eclipse specific techniques to bootstrap the SDK to your Mobile app using the Eclipse IDE.

1. Copy the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/blob/master/JB4A-SDK/etsdk-4.1.1.jar?raw=true" target="_blank">etsdk-4.1.1.jar</a> into the **{project root}/libs** folder for your project.

1. Copy additional dependency jars from the SDK Explorer project found <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer/eclipse-build-google/libs" target="_blank">HERE</a>. Your final libs folder should resemble this sample where x.x.x is the version you use:<br/>
    <img class="img-responsive" src="{{ site.baseurl }}/assets/eclipse-libs.png" />

1.  Beacon Beta testers ONLY must copy the Android Beacon dependency from <a href="https://altbeacon.github.io/android-beacon-library/download.html" target="_blank">here</a> and follow <a href="https://altbeacon.github.io/android-beacon-library/configure.html" target="_blank">these</a> directions.  We tested and support version 2.5.1. 

1. For a Google Build, you need to add Google Play Services as a *Library Project* to your Android Project. Find instructions <a href="http://developer.android.com/google/play-services/setup.html" target="_blank">HERE</a>.<br/>