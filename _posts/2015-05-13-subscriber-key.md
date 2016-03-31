---
layout: page
title: "Subscriber Key"
subtitle: "Updating Subscriber Key"
category: features
date: 2015-05-14 12:00:00
order: 1
feature: "Subscriber Key"
---

Set the Subscriber Key to a specific value provided by your customer or some other unique identifier for the Contact like mobile number, e-mail address, customer number, etc.

> NOTE: The SDK will send changes to {{ page.feature }} to the Marketing Cloud with a REST call one minute after the first change to any Marketing Cloud data. If the REST call fails (no network for example), then it will retry in one minute intervals until the app is suspended. If the send is unsuccessful before the app is suspended, the data will be sent the next time the app is opened. It will take up to {{ site.propagationDelay }} for this value to be recorded in the Contact record once the REST call is made by the SDK."

### setSubscriberKey()
<script src="https://gist.github.com/sfmc-mobilepushsdk/bd21fa6afc45a417618e.js"></script>

> NOTE: Prior to release 4.0.0, the SDK would set a default Subscriber Key to a unique hash called DeviceId.  Because this can cause duplicate records for companies who import contacts, the SDK stopped setting a default Subscriber Key.

> NOTE: Subscriber Key may not be null or an empty string.  Also, leading and/or trailing whitespace will be removed.

> NOTE: By default, if your app does not set the subscriber key using setSubscriberKey(), the Marketing Cloud will match the registration sent with a Contact Record that matches the System Token included in the registration payload. If the Marketing Cloud does not find a match, the Marketing Cloud will set a new subscriber key and will not send the value back to the SDK.

### getSubscriberKey()
<script src="https://gist.github.com/sfmc-mobilepushsdk/0bd43c062bed12184d06.js"></script>
