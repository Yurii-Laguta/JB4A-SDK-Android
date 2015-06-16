---
layout: page
title: "OpenDirect"
subtitle: "Messages with an OpenDirect URL"
category: features
date: 2015-05-14 12:00:00
order: 4
---

The OpenDirect customized push message contains a URL to be opened in a web view. By default, the SDK will open the URL in a web view within the ETLandingPage class.  If you would like to customize how the URL is displayed, you can craft your application to react appropriately when the mobile device receives that type of push message.

If you choose not to have the SDK use the default ETLandingPage activity to open the OpenDirect URL sent with the message payload, then the following call can be used to specify the Activity to be used to open an Open Direct URL sent in the payload of a push message from the Marketing Cloud.
`ETPush.getInstanceis().setOpenDirectRecipient(Class someActivityClass);`

This example (taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android-Beta/tree/beta/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer (Beta) for Android</a>) shows how to create your own Activity to show the OpenDirect URL sent with the message payload.

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
            if (this.getIntent().getExtras().containsKey("_od")) {
                webPageUrl = this.getIntent().getExtras().getString("_od");
            } else {
                webPageUrl = null;
                setTitle("No website URL found in payload.");
            }
    
            // now use this url to display in a webview or other options you want
        }
        …
        …   
    ~~~ 
1.  After readyAimFire() completes, let the SDK know that you would like to use your own activity to show the OpenDirect URL sent with the payload. We recommend that you add this code in the ReadyAimFireInitCompletedEvent sent from the [EventBus](eventbus.html) 

    ~~~ 
    public void onEvent(ReadyAimFireInitCompletedEvent event) {
        try {

            if (ETPush.getLogLevel() <= Log.DEBUG) {
                Log.i(TAG, "ReadyAimFireInitCompletedEvent started.");
            }

            if (event.isReadyAimFireReady()) {
    
                // ETPush.getInstance().setOpenDirectRecipient
                //
                //      This call is used to specify which activity is used to process an Open Direct URL sent in the payload of a push message from the Marketing Cloud.
                //
                //      If you don't specify this class, but do specify a URL in the OpenDirect field when creating the Message in the Marketing
                //      cloud, then the JB4A SDK will use the ETLandingPagePresenter class to display the URL.
                //
                //      Instead, we will use the SDK_ExplorerWebContentActivity which extends ETLandingPagePresenter in order to provide control over the
                //      ActionBar and what happens when the user selects back.
                ETPush.getInstance().setOpenDirectRecipient(SDK_ExplorerWebContentActivity.class);
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