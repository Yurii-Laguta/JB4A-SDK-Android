# Journey Builder for Apps SDK Explorer

## About
This is the Journey Builder for Apps SDK Explorer which servers the following purposes:

- Provides a UI for exploring the features of the Journey Builder for Apps SDK.
- Provides an example or template for creating an Android app that uses the Journey Builder for Apps SDK.
- Provides a mechanism to collect and send debugging information to learn about the workings of the SDK as you explore.

## Fully Functional APK
The APKFiles folder contains an apk which can be downloaded to experience the Android SDK immediately before writing any code.  Click the following to download this apk to install on your device:<br/>
<a href="https://github.com/ExactTarget/JB4A-SDK-Android/raw/master/JB4A-SDK-Explorer/APKfiles/JB4A-SDK-Explorer-Google-release-2100026.apk" target="_blank">JB4A-SDK-Explorer-Google-release-2100026.apk</a>

If you would rather, you can download the Journey Builder for Apps SDK Explorer app from the Play Store under the name Salesforce Journey Builder for Apps SDK Explorer (coming soon).

This apk file can be installed on your test devices to see the functionality of the Android SDK in action.  Typical features of the SDK are included as well as special features showing how flexible the SDK is.

To test the Location (Geo Fencing) aspects of the SDK, you can use an app on your device that will mock locations (check the Play Store, there are many options).  After installing (it will require you to set your device to allow Mock Locations), you can set a location to several different locations as noted in the SDK Explorer. You should receive a notification welcoming you to that location.

Note that using Mock Locations with a program that fakes GPS locations can interfere with the normal running of how your device sets location and may result in duplicate locations.  It also doesn't work on all devices (we had troubles getting it to properly fake the location on a Samsung 4).  Please keep this in mind when testing and final testing should use real locations to ensure you have your program setup correctly.

Not only can you see how the SDK works, but you can initiate messages from within this app.  Normally, you wouldn't include sending features within your app, however, this app provides a closed loop to allow you to initiate and receive messages within the same app.  This will allow you to put all the pieces together to see how the SDK works.

## Code
The code in this repository includes all of the code used to run the fully functional apk.  However, the API keys have been removed.  If you would like to debug the app or make any adjustments to create your own Practice Field app, you will need to provide several keys within the CONSTS_API class, as follows:

The following keys are keys you normally use to create an app that uses the Android SDK:

1. ET_APP_ID - the AppId for your development app as defined in the AppCenter section of the Marketing Cloud
2. ET_ACCESS_TOKEN - the Access Token for your development app as defined in the AppCenter section of the Marketing Cloud
3. GCM_SENDER_ID - the Google Cloud Messaging ID as defined in the Google Cloud Developers Console for your app

The following keys are keys you need if you want to initiate messages within your customized SDK Explorer:

1. ET_CLIENT_ID - the clientId for your API app as defined in the AppCenter section of the Marketing Cloud
2. ET_CLIENT_SECRET -  the clientSecret for your API app as defined in the AppCenter section of the Marketing Cloud
3. ET_STANDARD_MESSAGE_ID - the messageId of the template message (API Triggered) set in the Messaging Center of the Marketing Cloud for standard messages.
3. ET_CLOUDPAGE_MESSAGE_ID - the messageId of the template message (API Triggered) set in the Messaging Center of the Marketing Cloud for CloudPage alert messages.

## Marketing Cloud
To create your own version of the SDK Explorer, you will need to have your app defined within the Salesforce Marketing Cloud:

1. Setup a MobilePush and API App in the App Center.
2. Create a template (API Triggered) Message for a standard and CloudPage message in order to send messages from your app.
3. Create messages for your app for each location you'd like to test Geo Fencing.

For more information, see the info on [Code@](https://code.exacttarget.com/apis-sdks/journey-builder-for-apps/index.html).



