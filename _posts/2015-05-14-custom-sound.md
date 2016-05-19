---
layout: page
title: "Custom Sound"
subtitle: "Specify Custom Notification Sound"
category: features
date: 2015-05-14 12:00:00
order: 7
---
When a contact receives a push message on the mobile app, the SDK will create a notification and will use the default notification sound. You can override this notification sound if the payload of the push message indicates the use of a custom sound.

1. Place a file named **custom.mp3** in the raw folder of the res directory using Gradle:
	
	<img class="img-responsive" src="{{ site.baseurl }}/assets/custom-notification-sound-gradle.png" />

2.  When creating a push message, the marketer can request the particular message plays this custom sound file. If the app finds no custom sound in the payload, the app will use the default notification sound when displaying the message.
    
    <img class="img-responsive" src="{{ site.baseurl }}/assets/custom-notification-sound.png" />
