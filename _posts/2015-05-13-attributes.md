---
layout: page
title: "Attributes"
subtitle: "Using Attributes"
category: features
date: 2015-05-14 12:00:00
order: 2
feature: "Attributes"
---
To implement segmentation by attributes, include code to reference attributes in the app. Add any attributes you save with the SDK to your Marketing Cloud contact record in advance so that the Marketing Cloud can connect the values sent by the SDK to the correct contact fields.

> NOTE: {{ site.propagationDelayText }}

### Add Attribute

<script src="https://gist.github.com/sfmc-mobilepushsdk/bcca3dd22e40c43af42d.js"></script>

> NOTE: Neither the key nor the value may be null or an empty string. Also, leading and/or trailing whitespace will be trimmed from the input.<br/>

### Get Attributes

<script src="https://gist.github.com/sfmc-mobilepushsdk/449f7dc8f44ea217cb1d.js"></script>

### Remove Attribute

<script src="https://gist.github.com/sfmc-mobilepushsdk/10a4fdb234b6de0d0b8e.js"></script>

> NOTE: `removeAttribute()` _ONLY_ removes the attribute from the SDK and does not communicate this change to the Marketing Cloud Servers. If you wish to clear a value stored in the Marketing Cloud please call `addAttribute()` with an empty string as the value.

### <a name="reservedwords"></a>Reserved Word Restrictions

The JB4A SDK ignores calls to modify values associated with the following attributes, as these attributes represent reserved internal attributes:

<script src="https://gist.github.com/sfmc-mobilepushsdk/34af56f4a7d4a1acd2a3.js"></script>