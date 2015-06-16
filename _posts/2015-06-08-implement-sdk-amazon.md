---
layout: page
title: "Amazon"
subtitle: "Implement SDK on Amazon Devices"
category: sdk-implementation
date: 2015-05-14 12:00:00
order: 2
---
In order to use the SDK in your Mobile app on Amazon devices, you should follow the steps found in [this]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html) document.  Then create a build variant as shown in the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>.

1. You must create a text file named `api_key.txt`. The file should contain the key you obtained during [Amazon Provisioning]({{ site.baseurl }}/provisioning/amazon.html) step. Place the file in the `{project root}/app/src/main/assets` folder.

1.  Create an Amazon specific build file for your Amazon build.  
 
    1.  In the Amazon AndroidManifest.xml file, include the following:
        ~~~
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:amazon="http://schemas.amazon.com/apk/res/android">
        
            <!-- JB4A SDK Amazon Permissions -->
            <permission
                android:name="${applicationId}.permission.RECEIVE_ADM_MESSAGE"
                android:protectionLevel="signature" />
        
            <uses-permission android:name="${applicationId}.permission.RECEIVE_ADM_MESSAGE" />
            <uses-permission android:name="com.amazon.device.messaging.permission.RECEIVE" />
            <!-- END JB4A SDK Amazon Permissions -->
        
            <application>
                <!-- Required to send notifications to Kindle devices. -->
                <amazon:enable-feature
                    android:name="com.amazon.device.messaging"/>
        
                <!-- ETPushReceiver and Service -->
                <receiver
                    android:exported="true"
                    android:name="com.exacttarget.etpushsdk.ETPushReceiver"
                    android:permission="com.amazon.device.messaging.permission.SEND">
                    <intent-filter>
                        <action android:name="com.amazon.device.messaging.intent.RECEIVE" />
                        <action android:name="com.amazon.device.messaging.intent.REGISTRATION" />
        
                        <category android:name="${applicationId}" />
                    </intent-filter>
                    <intent-filter>
                        <action android:name="android.intent.action.PACKAGE_REPLACED" />
                        <data android:scheme="package" />
                    </intent-filter>
                    <intent-filter>
                        <action android:name="${applicationId}.MESSAGE_OPENED" />
                        <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                    </intent-filter>
                </receiver>
        
                <service
                    android:name="com.exacttarget.etpushsdk.ETPushService"
                    android:enabled="true" />
                <!-- ETPushReceiver and Service -->

                <!-- JB4A SDK Activity required for Cloud Page or Open Direct URLs sent from Marketing Cloud -->
                <activity
                   android:name="com.exacttarget.etpushsdk.ETLandingPagePresenter"
                   android:label="Landing Page" />
                <!-- JB4A SDK Activity required for Cloud Page or Open Direct URLs sent from Marketing Cloud -->
            </application>
        </manifest>
        ~~~