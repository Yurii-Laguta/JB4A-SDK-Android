---
layout: page
title: "Background Pushes"
subtitle: "Using Background Pushes"
category: features
date: 2015-07-02 12:00:00
order: 13
---
####Background Push Messages for MobilePush
This document contains conceptual and procedural information about sending a background push message to a mobile app using the MobilePush app and the REST API.

####What are Background Push Messages
A background push message appears on a mobile app without triggering a visual or audible alert on the mobile device. Examples include subscriptions read inside the Google Newsstand app or updates to messages within an app that do not require notifications.

####How to Send Background Push Messages
Follow the steps below to create and send background push messages:

Create an API-triggered MobilePush message.
Ensure you set the `content-available` property to `1`.
Set the override property to true.
Use the sample payload below as a model for your own message:

~~~

Content-Type: application/json
{
    "Override": true,
    "SendTime": "2012-10-31 09:00",
     "content-available":1
}
~~~

Once you create the original message, you can pass text to the message using subsequent messages as part of the Override value.

####Receiving Background Push Messages
Background Push Messages are posted to the EventBus as a 
[SilentPushReceivedEvent]({{ site.baseurl }}/features/eventbus.html). The payload can be extracted from this event and acted upon.