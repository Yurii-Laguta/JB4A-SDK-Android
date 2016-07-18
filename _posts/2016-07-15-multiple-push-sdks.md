---
layout: page
title: "Multiple Push SDKs"
subtitle: "Troubleshooting Multiple Push SDKs"
category: trouble-shooting
date: 2016-07-15 12:00:00
order: 4
---
While multiple push SDKs can be integrated into a single app, this may cause issues, and we cannot guarantee results. This section provides some considerations you should keep in mind as you develop your app. Areas of concern can include registration, geolocation and more. Note that this is not an exhaustive list.

For example, registration requires a resilient token. To produce a resilient token, register with every known GCM Sender ID (Project ID) in a comma-delimited string. Here is example code for both single and multiple SDKs:

<script src="https://gist.github.com/sfmc-mobilepushsdk/1d8edec797bfed3aead82956c2dabae4.js"></script>