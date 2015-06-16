---
layout: page
title: "Override Notifications"
subtitle: "How to Override Notification Intents"
category: features
date: 2015-05-14 12:00:00
order: 11
---
There are three ways to override the default intent that is opened when someone taps on a Push Message received by your Android app:

1.  Use the ETNotifications class to override the launch intent.
1.  Launch your own activity to handle the notifcation tap
1.  Specify an action and URI to launch your own activity when the notification receives a tap

___

1.  Use the ETNotifications class to override the launch intent.
    
    This example (taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>)

    After readyAimFire() completes, let the SDK the code you would like to execute when the pending intent is setup.

    ~~~
    ETNotifications.setNotificationLaunchIntent(new ETNotificationLaunchIntent() {
        @Override
        public Intent setupLaunchIntent(Context context, Bundle payload) {
            //
            // save the push notification payload received info for later display
            //
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SDK_ExplorerApp.context());
            SharedPreferences.Editor spEditor = sp.edit();
            long currTime = Calendar.getInstance().getTimeInMillis();
            spEditor.putLong(CONSTS.KEY_PUSH_RECEIVED_DATE, currTime);

            JSONObject jo = new JSONObject();
            try {
                for (String key : payload.keySet()) {
                    jo.put(key, payload.get(key));
                }
            } catch (Exception e) {
                if (ETPush.getLogLevel() <= Log.ERROR) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            spEditor.putString(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, jo.toString());

            spEditor.commit();

            // Since the SDK_ExplorerDisplayMessageActivity will show either the last or current message
            // pass a similar bundle to what is saved in Shared Preferences
            payload.putLong(CONSTS.KEY_PUSH_RECEIVED_DATE, currTime);
            payload.putString(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, jo.toString());

            //
            // This override will make sure that the launch intent is the only intent that is launched so only 1 is viewable at a time.
            // FLAG_ACTIVITY_CLEAR_TOP will close the current Intent (as well as any Activities stacked on top), and then show this new one
            // (if it is currently open).
            //
            // Remove .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) if you want to have a separate Intent be displayed for your notifications
            // if the app user receives a second notification message while viewing a previous message.
            //
            return ETNotifications.setupLaunchIntent(context, payload).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
    });
    ~~~

    > Make sure to include the payload in the launch intent if you don't return ETNotifications.setupLaunchIntent so that the SDK can handle OpenDirect and CloudPage messages.

1.  Launch your own activity to handle the notifcation tap

    1.  Set up a notification recipient activity class that launches when the notification receives a tap. This activity launches when your user taps your notification and the intent contains the message payload sent from the Marketing Cloud.

    	~~~
        // YourNotificationActivity
        …
        …  
        @Override
        protected void onResume() {
            super.onResume();
            Intent intent = getIntent();
            Bundle payload = intent.getExtras();

            // handle the payload in order to process Custom Keys, or to display the OpenDirect or CloudPage URL
        …
        …        
    	}
    	~~~  
    1.  Modify your AndroidManifest.xml file to include your activity:

        ~~~
        …
        …   
           <activity android:name=".YourNotificationActivity" />
        …
        …   
        ~~~ 
    1.  After readyAimFire() completes, let the SDK know that this activity is to be used to receive the tap from the notification. We recommend that you add this code in the ReadyAimFireInitCompletedEvent sent from the [EventBus](eventbus.html) 

        ~~~
        // After readyAimFire() completes.
        ETPush.getInstance().setNotificationRecipientClass(YourNotificationActivity.class);
        ~~~~
1.  Specify an action and URI to launch your own activity when the notification receives a tap

    1.  Set up a notification recipient activity class that launches when the notification receives a tap. This activity launches when your user taps your notification and the intent contains the message payload sent from the Marketing Cloud.

    	~~~
        // YourNotificationActivity
        …
        …  
        @Override
        protected void onResume() {
            super.onResume();
            Intent intent = getIntent();
            Bundle payload = intent.getExtras();

            // handle the payload in order to process Custom Keys, or to display the OpenDirect or CloudPage URL
        …
        …        
    	}
    	~~~  
    1.  Modify your ApplicationManifest.xml to contain an intent-filter configured to receive the intent:

        ~~~
        <!--ApplicationManifest.xml -->
            <activity android:name=".YourNotificationActivity" />
                <intent-filter>
                    <action android:name="android.intent.action.VIEW" />
                    <data android:mimeType="application/myapp" />
                </intent-filter>
            </activity>
        ~~~
    1.  After readyAimFire() completes, let the SDK know that this activity is to be used to receive the tap from the notification. We recommend that you add this code in the ReadyAimFireInitCompletedEvent sent from the [EventBus](eventbus.html) 
        
        ~~~
        // After readyAimFire() completes.
        ETPush.getInstance().setNotificationAction("android.intent.action.VIEW");
        ETPush.getInstance().setNotificationActionUri(Uri.parse("application/myapp"));
        ~~~