---
layout: page
title: "Encryption"
subtitle: "Encryption Errors"
category: trouble-shooting
date: 2015-05-14 12:00:00
order: 2
---
In the 4.0.3 release of the SDK, we added code to ensure that a device that could not implement encryption would wipe data from the device and inform you that the SDK could not be boot strapped.

As shown in [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html), you should implement the ReadyAimFireInitCompletedEvent from the EventBus in order to determine if the SDK was successfully bootstrapped.

With the 4.0.3 version of the SDK, if encryption fails (usually due to a knockoff device), you will receive a new code from the ReadyAimFireInitCompletedEvent to determine the reason for the failure:

   1.  event.getCode() will return the reason for the failure. 
   
       ~~~
           public void onEvent(ReadyAimFireInitCompletedEvent event) {
              if (ETPush.getLogLevel() <= Log.DEBUG) {
                  Log.i(TAG, "ReadyAimFireInitCompletedEvent started.");
              }
       
              if (event.isReadyAimFireReady()) {
        	      // successful bootstrap with SDK	
       
       
              } else {
        	       // unsuccessful bootstrap with SDK	
                   if (event.getCode() == ETException.RAF_INITIALIZE_ENCRYPTION_FAILURE) {
                        message = "ETPush readyAimFire() did not initialize due to an Encryption failure.";
                    } else if (event.getCode() == ETException.RAF_INITIALIZE_ENCRYPTION_OPTOUT_FAILURE) {
                        message = "ETPush readyAimFire() did not initialize encryption failure and unable to opt-out.";
                    } else if (event.getCode() == ETException.RAF_INITIALIZE_EXCEPTION) {
                        message = "ETPush readyAimFire() did not initialize due to an Exception.";
                    } else {
                        message = "ETPush readyAimFire() did not initialize due to an Exception.";
                    }
                    Log.e(TAG, String.format("ETPush readyAimFire() did not initialize due to an Exception with message: %s and code: %d", event.getMessage(), event.getCode()), event.getException());
                    throw new RuntimeException(message);
              }
           }
       ~~~

> It is also possible the encryption could fail if your app implements the PRNG fix recommended by Google.  You should remove this PRNG fix and count on the SDK implementing that fix for your app.

> Implementing the PRNG fix requires reflection.  Please ensure your Proguard config contains:

~~~
   -keepattributes Extends,Exceptions,InnerClasses

~~~