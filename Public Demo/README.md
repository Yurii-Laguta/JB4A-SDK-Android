# PublicDemo App

## About
This is the PublicDemo App for Android. It serves two purposes:

- Provide an example or template for creating Android apps that use the Exact Target Android SDK.
- Provide a UI for testing various features of the SDK, as well as feedback as to what it's doing. 

## Fully Functional APK
The APKFiles folder contains et-publicdemo-x.x.x-RELEASE.apk (where x.x.x is the current version of the PublicDemo App) which can be downloaded to experience the Android SDK immediately before writing any code.

This apk file can be installed on your test devices to see the functionality of the Android SDK in action.  Typical features of the SDK are included as well as special features showing how flexible the SDK is.

To test the Location (Geo Fencing) aspects of the SDK, you can use an app on your device that will mock locations (for example Fake GPS, found here: https://play.google.com/store/apps/details?id=com.lexa.fakegps.  After installing (it will require you to set your device to allow Mock Locations), once you set a location to any of the stadiums of the teams found in the Settings Page of the app, you should receive a notification welcoming you to that stadium.  The Notification, when clicked, will open to the webpage for that team.

Note that using Mock Locations with a program like FakeGPS can interfere with the normal running of how your device sets location and may result in duplicate locations.  Keep that in mind when testing and final testing should use real locations to ensure you have your program setup correctly.

Not only can you see how the SDK works, but you can initiate messages from within this app.  Normally, you wouldn't include sending features within your app, however, this app provides a closed loop to allow you to initiate and receive messages within the same app.  This will allow you to put all the pieces together to see how the SDK works.

## Code
The code in this repository includes all of the code used to run the fully functional apk.  However, the API keys have been removed.  If you would like to debug the app or make any adjustments to create your own demo, you will need to provide several keys within the CONSTS_API class, as follows:

You can save both Development and Production keys in the CONSTS_API class, but for a debugging/test app, you only need to define the development keys.

The following keys are keys you normally use to create an app that uses the Android SDK:

1. ET_APP_ID_DEV - the AppId for your development app as defined in the AppCenter section of the Marketing Cloud
2. ET_ACCESS_TOKEN_DEV - the Access Token for your development app as defined in the AppCenter section of the Marketing Cloud
3. GCM_SENDER_ID_DEV - the Google Cloud Messaging ID as defined in the Google Cloud Developers Console for your app

The following keys are keys you need if you want to initiate messages within your customized demo:

1. ET_CLIENT_ID_DEV - the clientId for your development server to server app as defined in the AppCenter section of the Marketing Cloud
2. ET_CLIENT_SECRET_DEV -  the clientSecret for your development server to server app as defined in the AppCenter section of the Marketing Cloud
3. ET_MESSAGE_ID_DEV - the messageId of the template message (API Triggered) set in the Messaging Center of the Marketing Cloud

## Libraries
The libs folder contains additional libraries required for building your own development version.  You will need to add the ET SDK found in the libs folder of this repository.

As well, we have included the most recent version of Google Play Services, which is required in order to use Google Cloud Messaging.  You should check the documentation for using Google Play Services in your project and make the necessary adjustments needed based on the development environment you use and the latest Google Play Services that is available.

## Marketing Cloud
To create a development version of the PublicDemo App, you will need to have your app defined within the Exact Target Marketing Cloud

1. Setup your App in the App Center.
2. Send a request to connect the Google Cloud Messaging project to this app.
3. Create a Server to Server App to process messages within the demo
4. Create a template (API Triggered) Message for the app in order to send messages from the demo.
5. Create messages for your app for each location you'd like to test Geo Fencing.



