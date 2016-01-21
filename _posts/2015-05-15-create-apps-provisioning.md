---
layout: page
title: "Add Provisioning to App Center App"
subtitle: "Add Provisioning Info to your App Center App"
category: create-apps
date: 2015-05-14 12:00:00
order: 4
---
Add the provisioning information created in the [Google]({{ site.baseurl }}/provisioning/google.html) section of this documentation to your MobilePush app in order to allow your Mobile app to connect to the correct Marketing Cloud Account.

After connecting your App Center App to your Marketing Cloud account, you can add the provisioning information using the following form:

* For Google apps, enter the **Key for server applications** **API KEY** provided for your app by the <a href="https://console.developers.google.com/" target="_blank">Google Cloud Console</a>.

<img class="img-responsive" src="{{ site.baseurl }}/assets/CreateNewMobilePushAppsClients.png" />

___
1. Once you populate all the fields required for your application platform, click **Next**.

1. Review the information you provided and check for any potential errors. Click **Finish**.<br/>

The App Center will then display a message indicating the process completed successfully and a screen with the summary of your app information.

<img class="img-responsive" src="{{ site.baseurl }}/assets/Summary.png" /><br/>

You will see a _Success!_ message and an application details screen. Edit any of the areas by clicking the edit icon associated with the **Summary** or **Application Provisioning** sections.<br/>

<img class="img-responsive" src="{{ site.baseurl }}/assets/exampleAppSuccess.png" /><br/>

> Record the **Application ID** and the **Access Token**, as you will use them later in readyAimFire() as shown in [Implement the SDK for Google]({{ site.baseurl }}/sdk-implementation/implement-sdk-google.html).

