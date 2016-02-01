---
layout: page
title: "Attributes"
subtitle: "Using Attributes"
category: features
date: 2015-05-14 12:00:00
order: 2
---
To implement segmentation by attributes, include code to reference attributes in the app. Add any attributes you save with the SDK to your Marketing Cloud contact record in advance so that the Marketing Cloud can connect the values sent by the SDK to the correct contact fields.

The ETPush.getInstance().addAttribute() method will create a new registration record and send it to the Marketing Cloud. This value will take up to 15 minutes to appear in the contact record. If the app makes the update without current Internet connectivity, the SDK will save the update and send whenever a network becomes available.

###addAttribute

~~~
etPush.addAttribute("someKey", "someValue")
~~~

###getInstance

~~~ 
ETPush etPush = ETPush.getInstance();
for (Attribute attribute : etPush.getAttributes()) {
    // use "attribute" here
}
~~~ 