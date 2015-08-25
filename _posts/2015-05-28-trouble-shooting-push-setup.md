---
layout: page
title: "Push Setup"
subtitle: "Push Messages not Working"
category: trouble-shooting
date: 2015-05-14 12:00:00
order: 1
---
If you have trouble receiving messages in your app, there are several trouble shooting items to consider:

1.  If you compiled using debug settings, make sure your machine's SHA key has been added to your Google Cloud Messaging project as described in step 7 of [Google Provisioning]({{ site.baseurl }}/provisioning/google.html).  However, if you compiled with release settings, make sure the production keystore SHA key has also been added.

1.  Make sure to turn on debug logging using setLogLevel() as shown in step 5.2 of the [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).

1.  Check the logcat for errors or warnings and make any necessary corrections.

> This is an important debugging step.  The SDK is very verbose and many errors and issues can be corrected by reviewing the logcat.

1.  Find your `device_Token` in the logcat by searching for `createHash`.  The output will be similar to this: 

    ~~~ 
D/~!ETPushService﹕ com.example.helloworld.HelloWorldApplication : createHash(final String string) // string = '{"deviceID":"49ce1d9e493af5bf1873628ddcfa3317","device_Token":"APA91bHQoBkqr76Ci72MRLFHNzTjpAHXF8IaehiM2KizEKhty3-8RyNx6wo1W9NO8JqlL-ILy98JZqO6jFtoyKZ6gaY1nb5S8roKGCqz8TLZvsMG8-eQX5ieDuTfAyoto9rXcmBUFX9W","sdk_version":"4.0.5","app_version":"1.4 : 23","gcmSenderId":"{gcm_sender_id}","dST":true,"location_enabled":true,"platform_Version":"5.1.1","push_enabled":true,"timeZone":-18000,"subscriberKey":"email@isp.com","platform":"Android","hwid":"LGE Nexus 5","etAppId":"{et_app_id}","badge":0,"locale":"en_US","tags":["1.0d","ALL","Android","Debug"],"attributes":[{"key":"FirstName","value":"EtPushHelloWorld"},{"key":"LastName","value":"{gcm_sender_id}"}]}'

    ~~~ 
1.  Use the Device Token output in the logcat as shown above to send a message directly to your app from Google.  This is documented in <a href="https://developer.android.com/google/gcm/http.html" target="_blank">Google Cloud Messaging (GCM) HTTP connection server</a>

    > You will also need the Server Key found in your GCM Project that you used to provision the App Center app as documented in [App Center Provisioning]({{ site.baseurl }}/create-apps/create-apps-provisioning.html).

1.  As a sample, we have provided the following screenshots of sending a message directly to your app using PAW.  However, any REST client will work.

    > The advantage of sending the message directly using this REST service is to get the error messages directly and make any necessary adjustments to your configuration.

1.  For the REST url and headers see this screenshot.  Note that the Authorization header should contain the same key you used to provision the App Center app as documented in [App Center Provisioning]({{ site.baseurl }}/create-apps/create-apps-provisioning.html).<br/><br/>

    <img class="img-responsive" src="{{ site.baseurl }}/assets/GCM-rest-url-and-headers.png" />
1.  Then create the body of the REST message using a simple JSON object.  

    > The registration ID should be the Device Token received from Google and sent to the Marketing Cloud in the Registration call which you put into the logcat as described above.<br/><br/>

    > Keep things simple and only add the alert JSON key pair which will be the message displayed in your notification.

    <img class="img-responsive" src="{{ site.baseurl }}/assets/GCM-rest-body.png" />
1.  If you successfully receive a message using the GCM REST client, but are still unable to receive a message from the Marketing Cloud to your device, check the following:
    
    1.  Wait 15 minutes after the first registration call for the device you are testing with to ensure your device is properly registered in the Marketing Cloud.

    1.  Check the List you have created in the Marketing Cloud and ensure that the DeviceId you printed in the logcat is shown in list.

    1.  Check the logcat after sending the message.  With debug turned on for the LogLevel, you should receive a message in the logcat <b>"Hello from ExactTarget! Push Message received"</b>.

        This is the first log message written to the logcat by the ETPush Broadcast receiver for both com.google.android.c2dm.intent.RECEIVE (Google) and com.amazon.device.messaging.intent.RECEIVE (Amazon).

        If you are not receiving this message in the logcat, then the message has not been successfully sent to your device.  So, go back and check the List in the Marketing Cloud and any issues with GCM setup as described above. 
