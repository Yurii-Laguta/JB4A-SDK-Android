---
layout: page
title: "Overview"
subtitle: "Overview of Rich Push Messages"
category: rich-push
date: 2015-05-14 12:00:00
order: 1
---
This section contains information on implementing rich push functionality in your mobile app. Rich push allows you to send either a page created in the CloudPage app to an Inbox configured in your app or a combination notification/CloudPage as an alert.

Note that the Salesforce Marketing Cloud  must enable the account using this functionality with both MobilePush and CloudPages in order to successfully create and send rich push alerts.

The SDK displays a combination of notification and CloudPage using the ETLandingPage class when the message receives a tap.

For CloudPage-only messages sent to an Inbox configured in your app, the app downloads new messages from the Salesforce Marketing Cloud each time your app comes into the foreground. The app saves these messages in a view, which you must configure in your app to display the CloudPage content.

