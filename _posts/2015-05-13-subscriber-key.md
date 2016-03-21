---
layout: page
title: "Subscriber Key"
subtitle: "Updating Subscriber Key"
category: features
date: 2015-05-14 12:00:00
order: 1
---

Set the Subscriber Key to a specific value provided by your customer or some other unique identifier for the Contact like mobile number, e-mail address, customer number, etc.

The `ETPush.getInstance().setSubscriberKey()` method will create a new `Registration` record and send it to the Marketing Cloud. The contact record may take up to {{ site.propagationDelay }} to record this value.  If the internet is not available when the update is made, or an error occurs, the SDK will save the update and retry until the update is successful.

### setSubscriberKey()
<script src="https://gist.github.com/sfmc-mobilepushsdk/bd21fa6afc45a417618e.js"></script>

> NOTE: Prior to release 4.0.0, the SDK would set a default Subscriber Key to a unique hash called DeviceId.  Because this can cause duplicate records for companies who import contacts, the SDK stopped setting a default Subscriber Key.

> NOTE: Subscriber Key may not be null or an empty string.  Also, leading and/or trailing whitespace will be removed.

> NOTE: By default, if your app does not set the subscriber key using setSubscriberKey(), the Marketing Cloud will match the registration sent with a Contact Record that matches the System Token included in the registration payload. If the Marketing Cloud does not find a match, the Marketing Cloud will set a new subscriber key and will not send the value back to the SDK.

### getSubscriberKey()
<script src="https://gist.github.com/sfmc-mobilepushsdk/0bd43c062bed12184d06.js"></script>
