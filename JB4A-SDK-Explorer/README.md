# Journey Builder for Apps SDK Explorer

## About
This is the Journey Builder for Apps SDK Explorer which servers the following purposes:

- Provides a UI for exploring the features of the Journey Builder for Apps SDK.
- Provides an example or template for creating an Android app that uses the Journey Builder for Apps SDK.
- Provides a mechanism to collect and send debugging information to learn about the workings of the SDK as you explore.

The code in this repository includes all of the code used to run the fully functional apk.  However, the API keys have been removed.  If you would like to debug the app or make any adjustments to create your own Practice Field app, you will need to provide several keys within the CONSTS_API class, as follows:

The following keys are keys you normally use to create an app that uses the Android SDK:

1. ET\_APP\_ID - the AppId for your development app as defined in the AppCenter section of the Marketing Cloud
2. ET\_ACCESS\_TOKEN - the Access Token for your development app as defined in the AppCenter section of the Marketing Cloud
3. GCM\_SENDER\_ID - the Google Cloud Messaging ID as defined in the Google Cloud Developers Console for your app

The following keys are keys you need if you want to initiate messages within your customized SDK Explorer:

1. ET\_CLIENT\_ID - the clientId for your API app as defined in the AppCenter section of the Marketing Cloud
2. ET\_CLIENT\_SECRET -  the clientSecret for your API app as defined in the AppCenter section of the Marketing Cloud
3. ET\_STANDARD\_MESSAGE\_ID - the messageId of the template message (API Triggered) set in the Messaging Center of the Marketing Cloud for standard messages.
3. ET\_CLOUDPAGE\_MESSAGE\_ID - the messageId of the template message (API Triggered) set in the Messaging Center of the Marketing Cloud for CloudPage alert messages.

## Marketing Cloud
To create your own version of the SDK Explorer, you will need to have your app defined within the Salesforce Marketing Cloud:

1. Setup a MobilePush and API App in the App Center.
2. Create a template (API Triggered) Message for a standard and CloudPage message in order to send messages from your app.
3. Create messages for your app for each location you'd like to test Geo Fencing.



