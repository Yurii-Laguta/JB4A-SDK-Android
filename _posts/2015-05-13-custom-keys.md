---
layout: page
title: "Custom Keys"
subtitle: "Handling Custom Keys Sent with Message Payload"
category: features
date: 2015-05-14 12:00:00
order: 5
---

You can include custom keys in the message payload sent with a Push Message. This key can be used by your app to perform a custom function as dictated by the existance and value of the custom key sent in the payload.

Access the custom key from the message payload in the Activity that opens when a push notification receives a tap:

This example (taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>) uses the SDK_ExplorerDisplayMessageActivity to show the contents of the payload sent with the Push Message.  CONSTS.KEY_PAYLOAD_DISCOUNT is a static string that contains "discount_code".  This is the custom key that has been defined in the Marketing Cloud.  This Activity searches for this key to determine whether it has been sent with the message payload.

Here is the how this custom key is setup in the SDK Explorer Marketing Cloud account for the SDK Explorer App:
<img class="img-responsive" src="{{ site.baseurl }}/assets/custom-key-marketingcloud.png" />

This page is accessible from the Administration section of the MobilePush app in the Marketing Cloud at this link:
<img class="img-responsive" src="{{ site.baseurl }}/assets/administration-link.png" />

The developer and the marketer who sends the message must act in concert for the app to process the custom key as expected.  Here is where the marketer will set the custom key when creating a new outbound push message.
<img class="img-responsive" src="{{ site.baseurl }}/assets/custom-key-outbound-message.png" />

And finally, here is the code that queries the payload sent with the Push Message to determine first if a custom key has been sent (it is optional) and if so, what the value is.  This will then dictate any special processing done within the app as a result of receiving this particular custom key with this particular value.

~~~
        public class SDK_ExplorerDisplayMessageActivity extends BaseActivity {
        …
        …
        
            payloadStr = extras.getString(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD);
            // convert JSON String of saved payload back to bundle to display
            JSONObject jo = null;

            try {
                jo = new JSONObject(payloadStr);
            } catch (Exception e) {
                if (ETPush.getLogLevel() <= Log.ERROR) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            if (jo != null) {
                sb.append("<b>Payload Sent with Message</b>  ");
                sb.append("<br/><br/>");
                sb.append("<i>Key/Value pairs:</i>  ");
                Iterator<String> iterator = jo.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        Object value = jo.get(key);
                        sb.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                        sb.append("<u>");
                        sb.append(key);
                        sb.append("</u>");
                        sb.append(" : ");
                        sb.append(value);
                    } catch (Exception e) {
                        if (ETPush.getLogLevel() <= Log.ERROR) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }

                try {
                    sb.append("<br/><br/>");
                    sb.append("<i>Custom Keys (Discount Code):</i>  ");
                    String payloadDiscountStr = "";
                    if (jo.has(CONSTS.KEY_PAYLOAD_DISCOUNT)) {
                        payloadDiscountStr = jo.getString(CONSTS.KEY_PAYLOAD_DISCOUNT);
                    }

                    if (!payloadDiscountStr.equals("")) {
                        // have an actual discount code
                        // CUSTOM KEYS
                        sb.append(payloadDiscountStr);

                        if (firstOpen) {
                            // if the Activity was refreshed, then don't flow to the discount screen.
                            Intent intent = new Intent(SDK_ExplorerDisplayMessageActivity.this, SDK_ExplorerDiscountActivity.class);
                            intent.putExtra(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, payloadStr);
                            startActivity(intent);
                        }
                    } else {
                        sb.append("n/a");
                        sb.append("<br/>");
                        sb.append("NOTE: No discount_code key was sent with this message.");
                    }
                } catch (Exception e) {
                    sb.append("Problem displaying Custom Keys (Discount Code).  Check logcat.");
                    if (ETPush.getLogLevel() <= Log.ERROR) {
                        Log.e(TAG, e.getMessage(), e);
                    }

                }
            } else {
                // show current push notification received, but payload is null
                sb.append("<b>Problem parsing payload from last push notification. Check logcat.</b>  ");
            }
        …
        …

~~~