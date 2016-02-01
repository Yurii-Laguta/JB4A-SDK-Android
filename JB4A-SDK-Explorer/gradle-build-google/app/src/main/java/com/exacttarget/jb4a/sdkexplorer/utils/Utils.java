/**
 * Copyright (c) 2015 Salesforce Marketing Cloud.
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p/>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * <p/>
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.exacttarget.jb4a.sdkexplorer.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLocationManager;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.data.Attribute;
import com.exacttarget.jb4a.sdkexplorer.CONSTS;
import com.exacttarget.jb4a.sdkexplorer.CONSTS_API;
import com.exacttarget.jb4a.sdkexplorer.R;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerApp;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerCloudPageInboxActivity;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerDebugSettingsActivity;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerDisplayMessageActivity;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerEulaActivity;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerInfoActivity;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerLocationsActivity;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerSendMessageActivity;
import com.exacttarget.jb4a.sdkexplorer.SDK_ExplorerSettingsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * UTILS
 * <p/>
 * A class of utility methods.
 *
 * @author pvandyk
 */
public class Utils {

    private static final String TAG = Utils.formatTag(Utils.class.getSimpleName());

    public static void prepareMenu(int currentPage, Menu menu) {
        menu.findItem(R.id.menu_settings).setVisible(false);
        menu.findItem(R.id.menu_send_message).setVisible(false);
        menu.findItem(R.id.menu_last_message).setVisible(false);
        menu.findItem(R.id.menu_locations).setVisible(false);
        menu.findItem(R.id.menu_cloudpage_inbox).setVisible(false);
        menu.findItem(R.id.menu_debug_settings).setVisible(false);
        menu.findItem(R.id.menu_info).setVisible(false);
        menu.findItem(R.id.menu_eula).setVisible(false);
        switch (currentPage) {
            case CONSTS.HOME_ACTIVITY:
                menu.findItem(R.id.menu_settings).setVisible(true);
                if (!CONSTS_API.getClientId().equals("")) {
                    // can't send messages if Custom keys and no client id provided
                    menu.findItem(R.id.menu_send_message).setVisible(true);
                }
                menu.findItem(R.id.menu_last_message).setVisible(true);

                if (android.os.Build.VERSION.SDK_INT >= 18) {
                    // Bluetooth LE only available on 4.3 and later
                    // https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
                    menu.findItem(R.id.menu_locations).setVisible(true);
                }

                menu.findItem(R.id.menu_cloudpage_inbox).setVisible(true);
                menu.findItem(R.id.menu_debug_settings).setVisible(true);
                menu.findItem(R.id.menu_info).setVisible(true);
                menu.findItem(R.id.menu_eula).setVisible(true);
                break;
            case CONSTS.SEND_MESSAGE_ACTIVITY:
                menu.findItem(R.id.menu_last_message).setVisible(true);
                menu.findItem(R.id.menu_settings).setVisible(true);
                break;
            case CONSTS.LOCATION_ACTIVITY:
                break;
            case CONSTS.DISPLAY_MESSAGE_ACTIVITY:
                break;
            case CONSTS.VIEW_WEB_CONTENT_ACTIVITY:
                break;
            case CONSTS.CLOUDPAGE_ACTIVITY:
                break;
            case CONSTS.CLOUDPAGE_INBOX_ACTIVITY:
                break;
            case CONSTS.DISCOUNT_ACTIVITY:
                break;
            case CONSTS.INFO_ACTIVITY:
                break;
            case CONSTS.SETTINGS_ACTIVITY:
                break;
            case CONSTS.DEBUG_SETTINGS_ACTIVITY:
                break;
        }
    }

    public static Boolean selectMenuItem(Activity activity, int currentPage, MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                switch (currentPage) {
                    case CONSTS.HOME_ACTIVITY:
                        break;
                    case CONSTS.SEND_MESSAGE_ACTIVITY:
                    case CONSTS.LOCATION_ACTIVITY:
                    case CONSTS.DISPLAY_MESSAGE_ACTIVITY:
                    case CONSTS.VIEW_WEB_CONTENT_ACTIVITY:
                    case CONSTS.CLOUDPAGE_ACTIVITY:
                    case CONSTS.CLOUDPAGE_INBOX_ACTIVITY:
                    case CONSTS.DISCOUNT_ACTIVITY:
                    case CONSTS.INFO_ACTIVITY:
                    case CONSTS.SETTINGS_ACTIVITY:
                    case CONSTS.DEBUG_SETTINGS_ACTIVITY:
                    case CONSTS.EULA_ACTIVITY:
                        activity.onBackPressed();
                        break;
                }
                return true;

            case R.id.menu_send_message:
                try {
                    if (ETPush.getInstance().isPushEnabled()) {
                        intent = new Intent(activity, SDK_ExplorerSendMessageActivity.class);
                        activity.startActivity(intent);
                    } else {
                        // can't send messages to this device if Push isn't enabled
                        Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.alert_utils5_text), Toast.LENGTH_LONG).show();
                    }
                } catch (ETException e) {
                    if (ETPush.getLogLevel() <= Log.ERROR) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                return true;

            case R.id.menu_last_message:
                intent = new Intent(activity, SDK_ExplorerDisplayMessageActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_cloudpage_inbox:
                intent = new Intent(activity, SDK_ExplorerCloudPageInboxActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_locations:
                intent = new Intent(activity, SDK_ExplorerLocationsActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_settings:
                intent = new Intent(activity, SDK_ExplorerSettingsActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_debug_settings:
                intent = new Intent(activity, SDK_ExplorerDebugSettingsActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_info:
                intent = new Intent(activity, SDK_ExplorerInfoActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_eula:
                intent = new Intent(activity, SDK_ExplorerEulaActivity.class);
                activity.startActivity(intent);
                return true;
        }
        return false;
    }

    public static void setWebView(Activity activity, int res, StringBuilder sb, boolean wideView) {

        sb.insert(0, "<html><body " + (wideView ? "style=\"white-space: nowrap;\")" : "") + "><font size=\"4\">");
        sb.append("</font></body></html>");

        WebView webView = (WebView) activity.findViewById(res);
        webView.loadData(sb.toString(), "text/html", "UTF-8");
        if (wideView) {
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webView.setBackgroundColor(0x00000000);
        webView.setScrollbarFadingEnabled(false);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public static WebView createWebView(Activity activity, StringBuilder sb) {

        WebView webView = new WebView(activity);
        webView.loadData(sb.toString(), "text/html", "UTF-8");
        webView.setBackgroundColor(0x00000000);
        webView.setScrollbarFadingEnabled(false);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        p.leftMargin = 15;
        p.rightMargin = 15;
        p.topMargin = 15;
        p.bottomMargin = 15;
        webView.setLayoutParams(p);

        // for transparency
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }

        return webView;
    }

    public static void flashError(final EditText et, String message) {

        et.setError(message);

        // reset message after 3 second delay
        et.postDelayed(new Runnable() {
            public void run() {
                et.setError(null);
            }
        }, 3000);

    }

    public static File createLogcatFile() {
        Process mLogcatProc;
        BufferedReader reader = null;
        OutputStreamWriter osw = null;
        File toFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "JB4A_SDK_Explorer_logcat.txt");
        try {
            FileOutputStream fosw = new FileOutputStream(toFile, false);
            osw = new OutputStreamWriter(fosw);
            mLogcatProc = Runtime.getRuntime().exec("logcat -d -v threadtime");

            reader = new BufferedReader(new InputStreamReader
                    (mLogcatProc.getInputStream()));

            String line;
            String separator = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                osw.append(line);
                osw.append(separator);
            }

        } catch (IOException e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
            Toast.makeText(SDK_ExplorerApp.context(), SDK_ExplorerApp.context().getString(R.string.alert_utils2_text) + e.getMessage(), Toast.LENGTH_LONG).show();
            toFile = null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    if (ETPush.getLogLevel() <= Log.ERROR) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    Toast.makeText(SDK_ExplorerApp.context(), SDK_ExplorerApp.context().getString(R.string.alert_utils2_text) + e.getMessage(), Toast.LENGTH_LONG).show();
                    toFile = null;
                }
            if (osw != null)
                try {
                    osw.close();
                } catch (IOException e) {
                    if (ETPush.getLogLevel() <= Log.ERROR) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    Toast.makeText(SDK_ExplorerApp.context(), SDK_ExplorerApp.context().getString(R.string.alert_utils2_text) + e.getMessage(), Toast.LENGTH_LONG).show();
                    toFile = null;
                }

        }

        return toFile;
    }

    public static File copyFileToTemp(File src) throws IOException {
        File dst = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/JB4A_SDK_Explorer_Temp/" + src.getName());
        dst.mkdirs();
        if (dst.exists()) {
            dst.delete();
        }

        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        return dst;
    }

    public static String[] formatInfoPages() {
        String[] pages = new String[]{"0", "1", "2", "3", "4", "5", "6"};
        StringBuilder sb = new StringBuilder();

        sb.append(CONSTS.PAGE_TITLE);
        sb.append("<b>App Details</b><br/>");
        sb.append("<hr>");

        // APP VERSION
        try {
            PackageInfo packageInfo = SDK_ExplorerApp.context().getPackageManager()
                    .getPackageInfo(SDK_ExplorerApp.context().getPackageName(), 0);
            sb.append("<i>App Version Name:</i> ");
            sb.append(packageInfo.versionName);
            sb.append("<br/>");
            sb.append("<i>App Version Code:</i> ");
            sb.append(packageInfo.versionCode);
        } catch (Exception e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        //JB4A SDK VERSION
        sb.append("<br/>");
        sb.append("<i>JB4A SDK Version:</i>  ");
        sb.append(ETPush.getSdkVersionName());
        sb.append(" (");
        sb.append(ETPush.getSdkVersionCode());
        sb.append(")");

        // LOG LEVEL
        sb.append("<br/>");
        sb.append("<i>Log Level:</i>  ");
        sb.append(getLoglevelText(ETPush.getLogLevel()));

        pages[0] = sb.toString();
        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);

        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.app_keys_help).replace("\n", "<br/>"));
        sb.append("<br/>");

        // App ID
        sb.append("<br/>");
        sb.append("<b>App Id:</b> ");
        sb.append(Utils.obfuscateString(CONSTS_API.getEtAppId()));

        // Access Token
        sb.append("<br/>");
        sb.append("<b>Access Token:</b> ");
        sb.append(Utils.obfuscateString(CONSTS_API.getAccessToken()));

        // GCM Id
        sb.append("<br/>");
        sb.append("<b>GCM Id:</b> ");
        sb.append(Utils.obfuscateString(CONSTS_API.getGcmSenderId()));

        if (!CONSTS_API.getClientId().equals("")) {
            // no need to display if Custom and no ClientId
            sb.append("<br/><br/>");
            sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.message_keys_help).replace("\n", "<br/>"));
            sb.append("<br/>");

            // Client Id
            sb.append("<br/>");
            sb.append("<b>Client Id:</b> ");
            sb.append(Utils.obfuscateString(CONSTS_API.getClientId()));

            // Client Secret
            sb.append("<br/>");
            sb.append("<b>Client Secret:</b> ");
            sb.append(Utils.obfuscateString(CONSTS_API.getClientSecret()));

            sb.append("<br/><br/>");
            sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.message_api_key_help).replace("\n", "<br/>"));
            sb.append("<br/>");

            // Standard Message Id
            sb.append("<br/>");
            sb.append("<b>Standard Message Id:</b> ");
            sb.append(Utils.obfuscateString(CONSTS_API.getStandardMessageId()));

            // CloudPage Message Id
            sb.append("<br/>");
            sb.append("<b>CloudPage Message Id:</b> ");
            sb.append(Utils.obfuscateString(CONSTS_API.getCloudPageMessageId()));

            // Fuel URL
            sb.append("<br/>");
            sb.append("<b>Fuel URL:</b> ");
            sb.append(CONSTS_API.getFuel_url());

        }

        pages[1] = sb.toString();
        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);

        ETPush pushManager = null;
        boolean pushEnabled = false;

        try {
            pushManager = ETPush.getInstance();
            pushEnabled = pushManager.isPushEnabled();
        } catch (Exception e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        // show the settings that have been set
        // get the Attributes saved with ExactTarget registration for this device
        ArrayList<Attribute> attributes;
        try {
            attributes = ETPush.getInstance().getAttributes();
        } catch (ETException e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
            attributes = new ArrayList<Attribute>();
        }

        Attribute firstNameAttrib = Utils.getAttribute(attributes, CONSTS.KEY_ATTRIB_FIRST_NAME);
        Attribute lastNameAttrib = Utils.getAttribute(attributes, CONSTS.KEY_ATTRIB_LAST_NAME);

        // PERSONAL SETTINGS
        sb.append("<b>Attributes</b><br/>");
        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.attribute_help).replace("\n", "<br/>"));
        sb.append("<br/>");

        // FIRST NAME
        sb.append("<br/>");
        sb.append("<b>First Name:</b>  ");
        sb.append(firstNameAttrib == null ? "" : firstNameAttrib.getValue());

        // LAST NAME
        sb.append("<br/>");
        sb.append("<b>Last Name:</b>  ");
        sb.append(lastNameAttrib == null ? "" : lastNameAttrib.getValue());

        // SUBSCRIBER KEY
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SDK_ExplorerApp.context());
        sb.append("<br/>");
        sb.append("<b>Subscriber Key:</b>  ");
        if (!sp.getString(CONSTS.KEY_PREF_SUBSCRIBER_KEY, "").isEmpty()) {
            sb.append(sp.getString(CONSTS.KEY_PREF_SUBSCRIBER_KEY, ""));
        }

        pages[2] = sb.toString();
        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);

        // NOTIFICATION SETTINGS
        sb.append("<b>Notification Settings</b><br/>");
        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.notification_help).replace("\n", "<br/>"));
        sb.append("<br/>");

        // PUSH ENABLED
        sb.append("<br/>");
        sb.append("<b>Push Enabled:</b>  ");
        sb.append(pushEnabled);

        // LOCATION ENABLED
        sb.append("<br/>");
        sb.append("<b>Location (Geo Fencing) Enabled:</b>  ");

        boolean locationEnabled = false;
        try {
            locationEnabled = ETLocationManager.getInstance().isWatchingLocation();
            sb.append(locationEnabled);
        } catch (ETException e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
            sb.append("Error determining if Location (Geo Fencing) is enabled.");
        }

        pages[3] = sb.toString();
        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);

        // DEVICE TOKEN
        sb.append("<b>Device Token</b><br/>");
        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.system_token_help).replace("\n", "<br/>"));
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<b>System Token: </b>");

        try {
            sb.append(pushManager.getSystemToken());
        } catch (Exception e) {
            sb.append("None.");
        }

        // DEVICE Id
        sb.append("<br/><br/>");
        sb.append("<b>Device Id</b><br/>");
        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.device_id_help).replace("\n", "<br/>"));
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<b>Actual Device Id: </b>");

        if (SDK_ExplorerApp.getDeviceId() == null) {
            sb.append("Device Id not returned yet from registration call");
        } else {
            sb.append(SDK_ExplorerApp.getDeviceId());
        }

        pages[4] = sb.toString();
        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);

        // OPEN DIRECT RECIPIENT
        sb.append("<b>Open Direct Recipient</b><br/>");
        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.open_direct_recipient_help).replace("\n", "<br/>"));
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<b>Actual Open Direct Recipient: </b>");

        try {
            sb.append(pushManager.getOpenDirectRecipient().getName());
        } catch (Exception e) {
            sb.append("None");
        }

        // NOTIFICATION RECIPIENT
        sb.append("<br/><br/>");
        sb.append("<b>Notification Recipient</b><br/>");
        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.notification_recipient_help).replace("\n", "<br/>"));
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("<b>Actual Notification Recipient: </b>");

        try {
            sb.append(pushManager.getNotificationRecipientClass().getName());
        } catch (Exception e) {
            sb.append("None");
        }

        pages[5] = sb.toString();
        sb = new StringBuilder();
        sb.append(CONSTS.PAGE_TITLE);

        // get the tags that have been saved with ExactTarget registration for this device
        TreeSet<String> tags;
        try {
            tags = ETPush.getInstance().getTags();
        } catch (ETException e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
            tags = new TreeSet<>();
        }

        // TAG Help
        sb.append("<b>Tags</b><br/>");
        sb.append("<hr>");
        sb.append(SDK_ExplorerApp.context().getResources().getString(R.string.tag_help).replace("\n", "<br/>"));
        sb.append("<br/>");
        sb.append("<br/>");

        // SPORTS TAGS
        sb.append("<b>Sports Tags</b>");

        String[] activityNames = SDK_ExplorerApp.context().getResources().getStringArray(R.array.activity_names);
        String[] activityKeys = SDK_ExplorerApp.context().getResources().getStringArray(R.array.activity_keys);

        int num_activity_subs = 0;
        for (int i = 0; i < activityNames.length; i++) {
            if (tags.contains(activityKeys[i])) {
                setSubLine(sb, activityNames[i]);
                num_activity_subs++;
            }
        }

        if (num_activity_subs == 0) {
            sb.append("<br/>");
            sb.append("No Activity tags.");
        }

        pages[6] = sb.toString();

        return pages;
    }

    private static void setSubLine(StringBuilder sb, String activityName) {
        sb.append("<br/>");
        sb.append(activityName);
    }

    public static String getLoglevelText(int loglevel) {
        switch (loglevel) {
            case Log.VERBOSE:
                return loglevel + " - Verbose";
            case Log.DEBUG:
                return loglevel + " - Debug";
            case Log.INFO:
                return loglevel + " - Info";
            case Log.WARN:
                return loglevel + " - Warning";
            case Log.ERROR:
                return loglevel + " - Error";
            default:
                return String.valueOf(loglevel);
        }
    }

    public static String obfuscateString(String key) {
        int strLen = key.length();
        long obfuscateLen = Math.round(strLen * .40);
        long remainingLen = Math.round((strLen - obfuscateLen) / 2);
        return key.substring(0, (int) (remainingLen)) + "***************" + key.substring((int) (strLen - remainingLen));
    }

    public static Attribute getAttribute(ArrayList<Attribute> attributes, String key) {
        for (Attribute attribute : attributes) {
            if (attribute.getKey().equals(key)) {
                return attribute;
            }
        }
        return null;
    }

    public static void setActivityTitle(Activity activity, int titleRes) {
        updateAppNameWithVersion(activity, titleRes);
    }

    private static String updateAppNameWithVersion(Activity activity, int titleRes) {
        return updateAppNameWithVersion(activity, activity.getString(titleRes));
    }

    private static String updateAppNameWithVersion(Activity activity, String inputString) {
        return inputString.replace("SDK Explorer", activity.getString(R.string.app_name));
    }

    public static void setActivityTitle(Activity activity, String titleStr) {
        ActionBar ab = activity.getActionBar();
        ab.setTitle(updateAppNameWithVersion(activity, titleStr));
        //		ab.setBackgroundDrawable(new ColorDrawable(R.color.ET_blue));
        ab.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.action_bar_color)));
        ab.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
        ab.setDisplayShowTitleEnabled(true);

        int actionBarTitleId = SDK_ExplorerApp.context().getResources().getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) activity.findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(Color.WHITE);
            }
        }
    }

    public static void setPrefActivityTitle(PreferenceActivity activity, String titleStr) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            android.app.ActionBar ab = activity.getActionBar();
            ab.setTitle(updateAppNameWithVersion(activity, titleStr));
            //		ab.setBackgroundDrawable(new ColorDrawable(R.color.ET_blue));
            ab.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.action_bar_color)));
            ab.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
            ab.setDisplayShowTitleEnabled(true);

            int actionBarTitleId = SDK_ExplorerApp.context().getResources().getSystem().getIdentifier("action_bar_title", "id", "android");
            if (actionBarTitleId > 0) {
                TextView title = (TextView) activity.findViewById(actionBarTitleId);
                if (title != null) {
                    title.setTextColor(Color.WHITE);
                }
            }
        }
    }

    public static String getRawResourceContents(String fileName, boolean isHTML) {
        String fileText = "";
        try {
            Resources res = SDK_ExplorerApp.context().getResources();
            InputStream in_s = res.openRawResource(getResId("R.raw." + fileName));
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(in_s, "UTF-8"));
            String fileLine;
            while ((fileLine = fileReader.readLine()) != null) {
                fileText += fileLine;
                if (!isHTML)
                    fileText += "\n";
            }
        } catch (Exception e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.i(TAG, "Error getting raw resource file: " + e.getLocalizedMessage());
            }
        }

        return fileText;
    }

    public static int getResId(String resName) {
        Context context = SDK_ExplorerApp.context();
        int firstDotPos = resName.indexOf(".");
        int secondDotPos = resName.lastIndexOf(".");

        return context.getResources().getIdentifier(resName.substring(secondDotPos + 1), resName.substring(firstDotPos + 1, secondDotPos), context.getPackageName());
    }

    public static TextView formatPD(ProgressDialog progressDialog, Activity activity, String message) {
        progressDialog.setContentView(R.layout.progress_dialog);
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        progressDialog.getWindow().setLayout((int) (Math.min(displayRectangle.width(), displayRectangle.height()) * 0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView progressBarMsg = (TextView) progressDialog.findViewById(R.id.message);
        progressBarMsg.setText(message);
        return progressBarMsg;
    }

    public static boolean isDebugApp(Context context) {
        //retrieve debug info
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int flags = packageInfo.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            if (ETPush.getLogLevel() <= Log.WARN) {
                Log.w(TAG, e.getMessage());
            }
        }
        return false;
    }

    public static String formatTag(String simpleName) {
        return String.format("%-25s", String.format("~#%1.21s", simpleName.replace("SDK_Explorer", "SDKX")));
    }
}
