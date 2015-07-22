---
layout: page
title: "Alerts"
subtitle: "Messages with a CloudPage URL"
category: rich-push
date: 2015-05-15 10:24:07
order: 2
---

<h4>Alert+CloudPage Default SDK Processing</h4>
The SDK will automatically process an alert that includes a CloudPage.  This Alert+CloudPage customized push message contains a URL to be opened. By default, the SDK will open the URL in a web view within the ETLandingPageActivity class (which must be included in your AndroidManifest.xml file).  If you would like to customize how the URL is displayed, please refer to the next section.

<h4>Alert+CloudPage Override Default SDK Processing</h4>
If you choose not to have the SDK use the default ETLandingPageActivity class to open the CloudPage URL sent with the message payload, then the following call can be used to specify the Activity to be used to open the CloudPage URL sent in the payload of a push message from the Marketing Cloud.

`ETPush.getInstanceis().setCloudPageDirectRecipient(Class someActivityClass);`

This example (taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>) shows how to create your own Activity to show the CloudPage URL sent with the message payload.

1.  Create the new Activity Class:
    
    ~~~ 
    /**
     * SDK_ExplorerWebContentActivity is an activity that will display the URL sent with the payload
     * of the message sent from the Marketing Cloud.
     *
     */
    
    public class SDK_ExplorerWebContentActivity extends BaseActivity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActionBar().setDisplayHomeAsUpEnabled(true);
    
            // get URL from the payload
            String webPageUrl;
            if (this.getIntent().getExtras().containsKey("_x")) {
                webPageUrl = this.getIntent().getExtras().getString("_x");
            } else {
                webPageUrl = null;
                setTitle("No website URL found in payload.");
            }
    
            // now use this url to display in a webview or other options you want
        }
        …
        …   
    ~~~ 
1.  After readyAimFire() completes, let the SDK know that you would like to use your own activity to show the CloudPage URL sent with the payload. We recommend that you add this code in the ReadyAimFireInitCompletedEvent sent from the [EventBus](eventbus.html) 

    ~~~ 
    public void onEvent(ReadyAimFireInitCompletedEvent event) {
        try {

            if (ETPush.getLogLevel() <= Log.DEBUG) {
                Log.i(TAG, "ReadyAimFireInitCompletedEvent started.");
            }

            if (event.isReadyAimFireReady()) {
    
                // ETPush.getInstance().setCloudPageRecipient
                //
                //		This call is used to specify which activity is used to process an CloudPage URL sent in the payload of a push message from the Marketing Cloud.
                //
                //		If you don't specify this class, but do send a CloudPage Alert when creating the Message in the Marketing
                //      cloud, then the JB4A SDK will use the ETLandingPagePresenter class to display the URL.
                //
                //		Instead, we will use the SDK_ExplorerWebContentActivity which extends ETLandingPagePresenter in order to provide control over the
                //      ActionBar and what happens when the user selects back.
                pushManager.setCloudPageRecipient(SDK_ExplorerWebContentActivity.class);
        …
        …   
    ~~~ 
3.  Modify your AndroidManifest.xml file to include your activity:

    ~~~
    …
    …   
       <activity android:name=".SDK_ExplorerWebContentActivity" />
    …
    …   
    ~~~ 

