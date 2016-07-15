---
layout: page
title: "Analytics"
subtitle: "Adding Analytics"
category: features
date: 2015-05-14 12:00:00
order: 6
---

You can enable analytics by setting the appropriate parameter in the ETPushConfig.Builder(). Before implementing this call, ensure you update your [ETPushConfig.Builder()]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html) as described in step 6. Set the **enableAnalytics** parameter to **true** to implement Analytics in your mobile app. Set the **setWamaEnabled** parameter to **true** to implement [Web and Mobile Analytics](https://help.exacttarget.com/en/documentation/web_and_mobile_analytics/){:target="_blank"} in your mobile app.

<script src="https://gist.github.com/sfmc-mobilepushsdk/a1f32591efa5fcfb6943.js"></script>


## Predictive Intelligence and Collect API Integration

<a name="track_cart"></a>

### Track Cart

Use to track the contents of an in-app shopping cart. For more information about this method's general use with the Predictive Intelligence system, see <a href="http://help.marketingcloud.com/en/documentation/collect_code/install_collect_code/track_cart/" target="_blank">Track Items in Shopping Cart.</a> Sample code for use in your mobile app is below.

<script src="https://gist.github.com/sfmc-mobilepushsdk/2cb3447b64500b02c8ca51dce6efc290.js"></script>

<a name="track_conversion"></a>

### Track Conversion

Use to track a purchase made through your mobile application. For more information about this method's general use with the Predictive Intelligence app, see <a href="http://help.marketingcloud.com/en/documentation/collect_code/install_collect_code/track_conversion/" target="_blank">Track Purchase Details</a>. Sample code for use in your mobile app is below.

<script src="https://gist.github.com/sfmc-mobilepushsdk/1989b6d8e2e7a64e3787fd0e6f11946b.js"></script>

### Track Page View
Call the following method to implement page view analytics in your app. For more information about this method's general use with the Predictive Intelligence app, see <a href="http://help.marketingcloud.com/en/documentation/collect_code/install_collect_code/track_page_view/" target="_blank">Track Items Viewed</a>. Sample code for use in your mobile app is below.

<script src="https://gist.github.com/sfmc-mobilepushsdk/da02ce55ea76aa826a28cb74988b40e4.js"></script>

## Accessing PI/Web & Mobile Analytics
1.  To see your new Web and Mobile Analytics, open the Web and Mobile Analytics app within the Marketing Cloud:
    
    <img class="img-responsive" src="{{ site.baseurl }}/assets/wama_menu.png" />
1.  Then check the checkbox agreeing to the Terms and Conditions to get started:<br/><br/>
    
    <img class="img-responsive" src="{{ site.baseurl }}/assets/wama_t_and_c.png" />