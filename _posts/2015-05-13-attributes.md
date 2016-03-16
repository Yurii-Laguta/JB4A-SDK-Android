---
layout: page
title: "Attributes"
subtitle: "Using Attributes"
category: features
date: 2015-05-14 12:00:00
order: 2
---
To implement segmentation by attributes, include code to reference attributes in the app. Add any attributes you save with the SDK to your Marketing Cloud contact record in advance so that the Marketing Cloud can connect the values sent by the SDK to the correct contact fields.

The ETPush.getInstance().addAttribute() method will create a new registration record and send it to the Marketing Cloud. This value will take up to 5 minutes to appear in the contact record. If the app makes the update without current Internet connectivity, the SDK will save the update and send whenever a network becomes available.

### addAttribute

<script src="https://gist.github.com/sfmc-mobilepushsdk/bcca3dd22e40c43af42d.js"></script>

> NOTE: Neither the key nor the value may be null or an empty string. Also, leading and/or trailing whitespace will be trimmed from the input.<br/>

### getAttributes

<script src="https://gist.github.com/sfmc-mobilepushsdk/449f7dc8f44ea217cb1d.js"></script>

### Reserved Word Restrictions

The JB4A SDK ignores calls to modify values associated with the following attributes, as these attributes represent reserved internal attributes:

<script src="https://gist.github.com/sfmc-mobilepushsdk/34af56f4a7d4a1acd2a3.js"></script>