---
layout: page
title: "Picture Notifications"
subtitle: "Display Pictures in the Message Notification"
category: features
date: 2015-05-14 12:00:00
order: 9
---
The Journey Builder for Apps Android SDK allows you to provide a shortened URL in the payload to display a picture with your message, such as the following sample:

<img class="img-responsive" src="{{ site.baseurl }}/assets/android_sdk_bigpic_picture_message.png" />

The Android SDK will handle the display of the message for you. In order to turn on this functionality in your MobilePush app, you must add a new custom key called et_big_pic.

<img class="img-responsive" src="{{ site.baseurl }}/assets/android_mc_bigpic_admin.png" />

Enter a URL for the image for the et_big_pic value such as http://1.usa.gov/1tlnnnJ, as in this example:

<img class="img-responsive" src="{{ site.baseurl }}/assets/android_mc_bigpic_create_message.png" />