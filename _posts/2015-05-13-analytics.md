---
layout: page
title: "Analytics"
subtitle: "Adding Analytics"
category: features
date: 2015-05-14 12:00:00
order: 6
---

You can enable analytics by setting the appropriate parameter in the ETPushConfig.Builder(). Before implementing this call, ensure you update your [ETPushConfig.Builder()]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html) as described in step 6. Set the **enableAnalytics** parameter to **true** to implement ETAnalytics in your mobile app. Set the **enableAnalytics** parameter to **true** to implement [Web and Mobile Analytics](https://help.exacttarget.com/en/documentation/web_and_mobile_analytics/){:target="_blank"} in your mobile app.

<script src="https://gist.github.com/sfmc-mobilepushsdk/a1f32591efa5fcfb6943.js"></script>

###Track Page View

1.  To implement page view analytics for your app, call the following method:

    ~~~
    ETAnalytics.trackPageView(url)
    ETAnalytics.trackPageView(url, title)
    ETAnalytics.trackPageView(url, title, item)
    ETAnalytics.trackPageView(url, title, item, searchTerms)  
    ~~~
    You must provide a URL value for the page in this method. You can provide null values for the other optional string values:

    * page title - title of the page
    * item - item referred to on the page
    * searchTerm - search terms used to find page
    <br/><br/>
1.  To see your new Web and Mobile Analytics, open the Web and Mobile Analytics app within the Marketing Cloud:
    
    <img class="img-responsive" src="{{ site.baseurl }}/assets/wama_menu.png" />
1.  Then check the checkbox agreeing to the Terms and Conditions to get started:<br/><br/>
    
    <img class="img-responsive" src="{{ site.baseurl }}/assets/wama_t_and_c.png" />