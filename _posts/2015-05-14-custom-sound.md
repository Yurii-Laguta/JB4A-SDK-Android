---
layout: page
title: "Custom Sound"
subtitle: "Specify Custom Notification Sound"
category: features
date: 2015-05-14 12:00:00
order: 7
---
When a Push Message is received in your app, the SDK will create a notification and will use the default notification sound.  You can override this notification sound if the payload of the push message indicates that the custom sound should be used.

1.  Place a file named custom.mp3 in the raw folder of the res directory using Gradle:
	
	<img class="img-responsive" src="{{ site.baseurl }}/assets/custom-notification-sound-gradle.png" />

	or Eclipse:

    <img class="img-responsive" src="{{ site.baseurl }}/assets/custom-notification-sound-eclipse.jpg" />
2.  When creating a push message, the marketer can request that that particular message plays this custom sound file.  If no custom sound is found in the payload, then the default notification sound will be used when the message is displayed.
    
    <img class="img-responsive" src="{{ site.baseurl }}/assets/custom-notification-sound.png" />
