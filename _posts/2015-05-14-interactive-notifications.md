---
layout: page
title: "Interactive Notifications"
subtitle: "Display Interactive Notification Messages"
category: features
date: 2015-05-14 12:00:00
order: 8
---
You can add buttons called *interactive notifications* to push notifications in your Mobile app. The Salesforce Marketing Cloud sends the category name for these interactive notifications in the message payload. 

This example (taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>).

You can view a sample of these interactive notifications in the following image, which includes three buttons that can be tapped, each of which will open a custom intent:<br/>

<img class="img-responsive" src="{{ site.baseurl }}/assets/AndroidInteractiveNotification.png" />

The SDK uses a NotificationCompat.Builder to create the notification to be displayed.  A new class called ETNotifications has been created that will create the notification.  This class contains an interface that allows you to override the functionality provided by the SDK.  In order to override funtionality, call ETNotification.setNotificationBuilder as shown in the following code:

~~~
    ETNotifications.setNotificationBuilder(new ETNotificationBuilder() {
         @Override
         public NotificationCompat.Builder setupNotificationBuilder(Context context, Bundle payload) {

             // Allow the SDK to setup the builder
             NotificationCompat.Builder builder = ETNotifications.setupNotificationBuilder(context, payload);

             // Add additional capabilities to the notification to display buttons based on the category sent
             String category = payload.getString("category");
             if (category != null && !category.isEmpty()) {
                 if (ITEM_SPOTLIGHT.equalsIgnoreCase(category)) {
                     //we need to add the 3 item_spotlight buttons to the notification. Android allows
                     //a max of 3 action buttons on the BigStyle notifications.
                     Intent similarIntent = new Intent(context, SDK_ExplorerViewSimilarActivity.class);
                     similarIntent.putExtras(payload);
                     PendingIntent similarPendingIntent = ETNotifications.createPendingIntentWithOpenAnalytics(context, similarIntent, true);
                     builder.addAction(R.drawable.ic_action_labels, "Similar", similarPendingIntent);

                     Intent favoritesIntent = new Intent(context, SDK_ExplorerViewFavoritesActivity.class);
                     favoritesIntent.putExtras(payload);
                     PendingIntent favoritesPendingIntent = ETNotifications.createPendingIntentWithOpenAnalytics(context, favoritesIntent, true);
                     builder.addAction(R.drawable.ic_action_favorite, "Fav", favoritesPendingIntent);

                     Intent reviewsIntent = new Intent(context, SDK_ExplorerViewReviewsActivity.class);
                     reviewsIntent.putExtras(payload);
                     PendingIntent reviewsPendingIntent = ETNotifications.createPendingIntentWithOpenAnalytics(context, reviewsIntent, true);
                     builder.addAction(R.drawable.ic_action_important, "Review", reviewsPendingIntent);
                 } else if (ONE_DAY_SALE.equalsIgnoreCase(category)) {
                     //get custom key for the sale date.
                     String saleDateString = payload.getString("sale_date");
                     if (saleDateString != null && !saleDateString.isEmpty()) {
                         try {
                             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                             df.setTimeZone(TimeZone.getDefault());
                             Date saleDate = df.parse(saleDateString);

                             Intent intent = new Intent(Intent.ACTION_INSERT)
                                     .setData(CalendarContract.Events.CONTENT_URI)
                                     .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, saleDate.getTime())
                                     .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, saleDate.getTime())
                                     .putExtra(CalendarContract.Events.TITLE, payload.getString("event_title"))
                                     .putExtra(CalendarContract.Events.DESCRIPTION, payload.getString("alert"))
                                     .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                                     .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                             PendingIntent reminderPendingIntent = PendingIntent.getActivity(context, 38456, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                             builder.addAction(R.drawable.ic_action_add_alarm, "Add Reminder", reminderPendingIntent);
                         } catch (ParseException e) {
                             Log.e(TAG, e.getMessage(), e);
                         }
                     }
                 }
             }

             return builder;
         }
     });
~~~
