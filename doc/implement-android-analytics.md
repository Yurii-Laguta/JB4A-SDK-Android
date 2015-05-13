###ET Analytics###
Follow the steps below to implement analytics in your mobile app:
<br />
1. Set the enableAnalytics parameter to true on the call to readyAimFire() in the Application Class.
	```java
      ETPush.readyAimFire(this, 
                          CONSTS_API.getEtAppId(), 
                          CONSTS_API.getAccessToken(), 
                          CONSTS_API.getGcmSenderId(), 
                          true,     // enable ET Analytics 
                          true,     // enable Location Manager, if you purchased this feature
                          true);    // enable Cloud Page, if you purchased this feature
 	```
<br />
1. Your app sends analytics whenever it goes into the background.<br/> 
`Note:` _For apps targetting **earlier than Android API 14**, you need to override onPause() and onResume() in each Activity class to notify the SDK when activities pause and resume so the SDK can determine when your app goes into the background._<br/><br/>
`Note:` _For apps targetting **Android API 14 or later**, the SDK will use registerLifecycleCallbacks() to determine when activities are paused or resumed._

	```java
		    @Override
		    protected void onPause() {
		        super.onPause();
		        
		        try {
		            // Let JB4A SDK know when each activity paused (4.0.0 release or later)
		            ETPush.activityPaused(this);
		            // Let JB4A SDK know when each activity paused (3.5.0 release or earlier)
		            // ETPush.pushManager().activityPaused(this);
		        }
		        catch (Exception e) {
		            if (ETPush.getLogLevel() <= Log.ERROR) {
		                Log.e(TAG, e.getMessage(), e);
		            }
		        }
		    }

		    @Override
		    protected void onResume() {
		        super.onResume();
		        try {
		            // Let JB4A SDK know when each activity resumed( 4.0.0 release or later)
		            ETPush.activityResumed(this);
		            // Let JB4A SDK know when each activity resumed (3.5.0 release or earlier)
		            // ETPush.pushManager().activityResumed(this);
		        }
		        catch (ETException e) {
		            if (ETPush.getLogLevel() <= Log.ERROR) {
		                Log.e(TAG, e.getMessage(), e);
		            }
		        }
		    }
    ```
<!-- END LIST -->

###Mobile Analytics###
Follow the steps below to implement [Web and Mobile Analytics](http://www.exacttarget.com/products/customer-data-platform/web-mobile-analytics) in your mobile app:
<br />
1. Set the enableAnalytics parameter to true on the call to readyAimFire() in the Application Class.
	```java
      ETPush.readyAimFire(this, 
                          CONSTS_API.getEtAppId(), 
                          CONSTS_API.getAccessToken(), 
                          CONSTS_API.getGcmSenderId(), 
                          true,     // enable ET Analytics
                          true,     // enable new Web and Mobile Analytics 
                          true,     // enable Location Manager, if you purchased this feature
                          true);    // enable Cloud Page, if you purchased this feature
 	```
<br />
1. Your app sends analytics whenever it goes into the background.<br/> 
`Note:` _For apps targetting **earlier than Android API 14**, you need to override onPause() and onResume() in each Activity class to notify the SDK when activities pause and resume so the SDK can determine when your app goes into the background._<br/><br/>
`Note:` _For apps targetting **Android API 14 or later**, the SDK will use registerLifecycleCallbacks() to determine when activities are paused or resumed._

	```java
		    @Override
		    protected void onPause() {
		        super.onPause();
		        
		        try {
		            // Let JB4A SDK know when each activity paused (4.0.0 release or later)
		            ETPush.activityPaused(this);
		            // Let JB4A SDK know when each activity paused (3.5.0 release or earlier)
		            // ETPush.pushManager().activityPaused(this);
		        }
		        catch (Exception e) {
		            if (ETPush.getLogLevel() <= Log.ERROR) {
		                Log.e(TAG, e.getMessage(), e);
		            }
		        }
		    }

		    @Override
		    protected void onResume() {
		        super.onResume();
		        try {
		            // Let JB4A SDK know when each activity resumed( 4.0.0 release or later)
		            ETPush.activityResumed(this);
		            // Let JB4A SDK know when each activity resumed (3.5.0 release or earlier)
		            // ETPush.pushManager().activityResumed(this);
		        }
		        catch (ETException e) {
		            if (ETPush.getLogLevel() <= Log.ERROR) {
		                Log.e(TAG, e.getMessage(), e);
		            }
		        }
		    }
    ```
1. To see your new Web and Mobile Analytics, open the Web and Mobile Analytics app within the Marketing Cloud:
<br /> 
<img class="img-responsive" src="/assets/images/mobilepush/wama_menu.png" />
<br /><br />
1. Then check the checkbox agreeing to the Terms and Conditions to get started:<br/><br/>
<img class="img-responsive" src="/assets/images/mobilepush/wama_t_and_c.png" />
