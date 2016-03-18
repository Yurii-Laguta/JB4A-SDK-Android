---
layout: page
title: "Custom Keys"
subtitle: "Handling Custom Keys Sent with Message Payload"
category: features
date: 2015-05-14 12:00:00
order: 5
---

Include custom keys in the message payload sent with a push message. Your app can use this key to perform a custom function as dictated by the existence and value of the custom key sent in the payload.

Access the custom key from the message payload in the Activity that opens when a push notification receives a tap:

This example (taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer for Android</a>) uses the SDK_ExplorerDisplayMessageActivity to show the contents of the payload sent with the push message. CONSTS.KEY_PAYLOAD_DISCOUNT includes a static string that contains **discount_code**. This value represents the custom key defined in the Marketing Cloud. The activity searches for this key to determine if the message payload contained the value.

The following image illustrates the [key creation process](http://help.exacttarget.com/en/documentation/mobilepush/administering_your_mobilepush_account/apps_and_optional_settings_in_your_mobilepush_account/#customkeys):
<img class="img-responsive" src="{{ site.baseurl }}/assets/custom-key-marketingcloud.png" />

Access this page from the Administration section of the MobilePush app in the Marketing Cloud (shown below):
<img class="img-responsive" src="{{ site.baseurl }}/assets/administration-link.png" />

The developer and the marketer who sends the message must act together for the app to process the custom key as expected. This images shows the location where the marketer will set the custom key when creating a new outbound push message.
<img class="img-responsive" src="{{ site.baseurl }}/assets/custom-key-outbound-message.png" />

The code shown below queries the payload sent with the push message to determine first if the message sent an optional custom key and if so, what value the key contained. This value will dictate any special processing done within the app as a result of receiving the specified custom key value.

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