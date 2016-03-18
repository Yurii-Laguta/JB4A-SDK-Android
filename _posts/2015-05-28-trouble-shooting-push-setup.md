---
layout: page
title: "Push Setup"
subtitle: "Push Messages not Working"
category: trouble-shooting
date: 2015-05-14 12:00:00
order: 1
---
If you encounter issues receiving messages in your app, consider these troubleshooting items:

1. Make sure to turn on debug logging using setLogLevel() as shown in step 5.2 of the [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).

1. Check the logcat for errors or warnings and make any necessary corrections.
 > Ensure you attempt this important debugging step. The SDK provides very verbose messages, and you can correct many errors and issues by reviewing the logcat.

1. `getSdkState()` has been added to ETPush which will display the current state of the SDK and the associated configuration values.
<script src="https://gist.github.com/sfmc-mobilepushsdk/968a564ffde3e80ba15f.js"></script>

1.  You may also find your **device_Token** in the logcat by searching for **createHash**.
<script src="https://gist.github.com/sfmc-mobilepushsdk/429809ab848791867379.js"></script>

 > NOTE: Device Token and System Token are the same thing and you may see them used in the documentation or logging interchangably.

1.  Use the Device Token output in the logcat as shown above to send a message directly to your app from Google. (as documented in <a href="https://developer.android.com/google/gcm/http.html" target="_blank">Google Cloud Messaging (GCM) HTTP connection server</a>).

    > You will also need the Server Key found in your GCM Project that you used to provision the App Center app (as documented in [App Center Provisioning]({{ site.baseurl }}/create-apps/create-apps-provisioning.html)).

1.  Review the following screenshots of sending a message directly to your app using PAW. However, any REST client will work.

    >This method of sending the message directly using a REST service helps you receive error messages directly and make any necessary adjustments to your configuration.

1.  For the REST url and headers see this screenshot. Note that the Authorization header should contain the same key you used to provision the App Center app as documented in [App Center Provisioning]({{ site.baseurl }}/create-apps/create-apps-provisioning.html).<br/><br/>

    <img class="img-responsive" src="{{ site.baseurl }}/assets/GCM-rest-url-and-headers.png" />
1.  Then create the body of the REST message using a simple JSON object.  

    > The registration ID should use the Device Token received from Google and sent to the Marketing Cloud in the Registration call, which you put into the logcat as described above.<br/><br/>

    > Keep things simple and only add the alert JSON key pair (which includes the message displayed in your notification).

    <img class="img-responsive" src="{{ site.baseurl }}/assets/GCM-rest-body.png" />
1.  If you successfully receive a message using the GCM REST client but still cannot to receive a message from the Marketing Cloud to your device, check the following:
    
    1.  Wait {{ site.propagationDelay }} after the first registration call for the device you're testing with to ensure your device properly registered in the Marketing Cloud.

    1.  Check the List you created in the Marketing Cloud and ensure the DeviceId you printed in the logcat shows up in the list.

    1.  Check the logcat after sending the message. With `Log.DEBUG` turned on for the Log Level, you should receive a message in the logcat <b>"Hello from ExactTarget! Push Message received"</b>.

        This message represents the first log message written to the logcat by the ETPush Broadcast receiver for `com.google.android.c2dm.intent.RECEIVE`.

        If you do not receive this message in the logcat, then the message did not successfully send to your device. Go back and check the List in the Marketing Cloud and any issues with GCM setup as described above. 