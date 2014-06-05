package com.exacttarget.publicdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETLocationManager;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.data.Attribute;
import com.exacttarget.etpushsdk.data.DeviceData;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * UTILS
 * <p/>
 * This class of utility methods.
 *
 * @author pvandyk
 */
public class Utils {

	private static final String TAG = Utils.class.getName();

	public static void prepareMenu(int currentPage, Menu menu) {
		switch (currentPage) {
			case CONSTS.HOME_ACTIVITY:
				menu.findItem(R.id.menu_settings).setVisible(true);
				menu.findItem(R.id.menu_debug_settings).setVisible(true);
				menu.findItem(R.id.menu_about).setVisible(true);
				break;
			case CONSTS.NOTIFICATION_ACTIVITY:
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.OPENDIRECT_ACTIVITY:
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.DISCOUNT_ACTIVITY:
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.ABOUT_ACTIVITY:
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.SETTINGS_ACTIVITY:
			case CONSTS.DEBUG_SETTINGS_ACTIVITY:
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
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
					case CONSTS.NOTIFICATION_ACTIVITY:
					case CONSTS.OPENDIRECT_ACTIVITY:
					case CONSTS.DISCOUNT_ACTIVITY:
					case CONSTS.ABOUT_ACTIVITY:
					case CONSTS.SETTINGS_ACTIVITY:
					case CONSTS.DEBUG_SETTINGS_ACTIVITY:
						activity.onBackPressed();
						break;
				}
				return true;

			case R.id.menu_notification:
				// add fields from last push received
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(PublicDemoApp.context());

				long pushReceivedDate = sp.getLong(CONSTS.KEY_PUSH_RECEIVED_DATE, -1);
				String payloadStr = sp.getString(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, "");

				intent = new Intent(activity, PublicDemoNotificationActivity.class);

				intent.putExtra(CONSTS.KEY_PUSH_RECEIVED_DATE, pushReceivedDate);
				intent.putExtra(CONSTS.KEY_PUSH_RECEIVED_PAYLOAD, payloadStr);
				activity.startActivity(intent);
				return true;

			case R.id.menu_settings:
				intent = new Intent(activity, PublicDemoSettingsActivity.class);
				activity.startActivity(intent);
				return true;

			case R.id.menu_debug_settings:
				intent = new Intent(activity, PublicDemoDebugSettingsActivity.class);
				activity.startActivity(intent);
				return true;

			case R.id.menu_about:
				intent = new Intent(activity, PublicDemoInfoActivity.class);
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
		File toFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "ET_PublicDemo_logcat.txt");
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

		}
		catch (IOException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
			Toast.makeText(PublicDemoApp.context(), "Problem creating logcat file: " + e.getMessage(), Toast.LENGTH_LONG).show();
			toFile = null;
		}
		finally {
			if (reader != null)
				try {
					reader.close();
				}
				catch (IOException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, e.getMessage(), e);
					}
					Toast.makeText(PublicDemoApp.context(), "Problem creating logcat file: " + e.getMessage(), Toast.LENGTH_LONG).show();
					toFile = null;
				}
			if (osw != null)
				try {
					osw.close();
				}
				catch (IOException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, e.getMessage(), e);
					}
					Toast.makeText(PublicDemoApp.context(), "Problem creating logcat file: " + e.getMessage(), Toast.LENGTH_LONG).show();
					toFile = null;
				}

		}

		return toFile;
	}

	public static void sendEmail(Activity inActivity, String subject, Spanned body, String[] to, File[] attachments) {
		Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
		try {
			i.setType("plain/text");
			i.putExtra(Intent.EXTRA_EMAIL, to);
			i.putExtra(Intent.EXTRA_SUBJECT, subject);
			i.putExtra(Intent.EXTRA_TEXT, body);

			ArrayList<Uri> attachmentURIs = new ArrayList<Uri>();

			for (File attachment : attachments) {
				attachmentURIs.add(Uri.fromFile(attachment));
			}
			i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentURIs);

			inActivity.startActivity(Intent.createChooser(i, "Send debug info..."));
		}
		catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(PublicDemoApp.context(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

	public static File copyFileToTemp(File src) throws IOException {
		File dst = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/ET_PublicDemo_Temp/" + src.getName());
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

	public static StringBuilder formatInfoPage() {
		StringBuilder sb = new StringBuilder();

		// APP TITLE
		sb.append("<b>");
		sb.append(PublicDemoApp.context().getString(R.string.app_name));
		sb.append("</b>");

		// APP VERSION
		try {
			PackageInfo packageInfo = PublicDemoApp.context().getPackageManager()
					.getPackageInfo(PublicDemoApp.context().getPackageName(), 0);
			sb.append("<br/>");
			sb.append("<i>App Version Name:</i> ");
			sb.append(packageInfo.versionName);
			sb.append("<br/>");
			sb.append("<i>App Version Code:</i> ");
			sb.append(packageInfo.versionCode);
		}
		catch (Exception e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		// SDK VERSION
		sb.append("<br/>");
		sb.append("<i>SDK Version:</i>  ");
		sb.append(ETPush.ETPushSDKVersionString);

		// LOG LEVEL
		sb.append("<br/>");
		sb.append("<i>Log Level:</i>  ");
		sb.append(getLoglevelText(ETPush.getLogLevel()));

		// PRODUCTION OR DEVELOPMENT??
		sb.append("<br/><br/>");
		if (CONSTS_API.isDevelopment()) {
			sb.append("<b>Development App Keys</b>");
		}
		else {
			sb.append("<b>Production App Keys</b>");
		}

		// App ID
		sb.append("<br/>");
		sb.append("<i>App Id:</i> ");
		sb.append(Utils.obfuscateString(CONSTS_API.getEtAppId()));

		// Access Token
		sb.append("<br/>");
		sb.append("<i>Access Token:</i> ");
		sb.append(Utils.obfuscateString(CONSTS_API.getAccessToken()));

		// GCM Id
		sb.append("<br/>");
		sb.append("<i>GCM Id:</i> ");
		sb.append(Utils.obfuscateString(CONSTS_API.getGcmSenderId()));

		// Client Id
		sb.append("<br/>");
		sb.append("<i>Client Id:</i> ");
		sb.append(Utils.obfuscateString(CONSTS_API.getClientId()));

		// Client Secret
		sb.append("<br/>");
		sb.append("<i>Client Secret:</i> ");
		sb.append(Utils.obfuscateString(CONSTS_API.getClientSecret()));

		// Message Id
		sb.append("<br/>");
		sb.append("<i>Message Id:</i> ");
		sb.append(Utils.obfuscateString(CONSTS_API.getMessageId()));

		sb.append("<br/>");
		sb.append("<br/>");

		ETPush pushManager = null;
		boolean pushEnabled = false;

		try {
			pushManager = ETPush.pushManager();
			pushEnabled = pushManager.isPushEnabled();
		}
		catch (Exception e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		// show the settings that have been set
		// get the Attributes saved with ExactTarget registration for this device
		ArrayList<Attribute> attributes;
		try {
			attributes = ETPush.pushManager().getAttributes();
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
			attributes = new ArrayList<Attribute>();
		}

		Attribute firstNameAttrib = Utils.getAttribute(attributes, CONSTS.KEY_ATTRIB_FIRST_NAME);
		Attribute lastNameAttrib = Utils.getAttribute(attributes, CONSTS.KEY_ATTRIB_LAST_NAME);

		// PERSONAL SETTINGS
		sb.append("<b>Attributes</b>");

		// FIRST NAME
		sb.append("<br/>");
		sb.append("<i>First Name:</i>  ");
		sb.append(firstNameAttrib.getValue());

		// LAST NAME
		sb.append("<br/>");
		sb.append("<i>Last Name:</i>  ");
		sb.append(lastNameAttrib.getValue());

		// NOTIFICATION SETTINGS
		sb.append("<br/><br/>");
		sb.append("<b>Notification Settings</b>");

		// PUSH ENABLED
		sb.append("<br/>");
		sb.append("<i>Push Enabled:</i>  ");
		sb.append(pushEnabled);

		// LOCATION ENABLED
		sb.append("<br/>");
		sb.append("<i>Location (Geo Fencing) Enabled:</i>  ");

		boolean locationEnabled = false;
		try {
			locationEnabled = ETLocationManager.locationManager().isWatchingLocation();
			sb.append(locationEnabled);
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
			sb.append("Error determining if Location (Geo Fencing) is enabled.");
		}

		if (pushManager.isPushEnabled()) {

			// DEVICE TOKEN
			sb.append("<br/>");
			sb.append("<i>Device Token:</i> ");

			try {
				sb.append(pushManager.getDeviceToken());
			}
			catch (Exception e) {
				sb.append("None.");
			}

			// DEVICE Id
			sb.append("<br/>");
			sb.append("<i>Device Id:</i> ");
			sb.append(new DeviceData().uniqueDeviceIdentifier(PublicDemoApp.context()));

			// OPEN DEVICE RECIPIENT
			sb.append("<br/>");
			sb.append("<i>Open Direct Recipient:</i> ");

			try {
				sb.append(pushManager.getOpenDirectRecipient().getName());
			}
			catch (Exception e) {
				sb.append("None");
			}

			// NOTIFICATION RECIPIENT
			sb.append("<br/>");
			sb.append("<i>Notification Recipient:</i> ");

			try {
				sb.append(pushManager.getNotificationRecipientClass().getName());
			}
			catch (Exception e) {
				sb.append("None");
			}
		}

		// get the tags that have been saved with ExactTarget registration for this device
		HashSet<String> tags;
		try {
			tags = ETPush.pushManager().getTags();
		}
		catch (ETException e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
			tags = new HashSet<String>();
		}

		if (pushEnabled | locationEnabled) {
			// NFL TEAM TAGS
			sb.append("<br/><br/>");
			sb.append("<b>NFL Team Tags</b>");

			String[] nflTeamNames = PublicDemoApp.context().getResources().getStringArray(R.array.nfl_teamNames);
			String[] nflTeamKeys = PublicDemoApp.context().getResources().getStringArray(R.array.nfl_teamKeys);

			int num_NFL_subs = 0;
			for (int i = 0; i < nflTeamNames.length; i++) {
				if (tags.contains(nflTeamKeys[i])) {
					setSubLine(sb, nflTeamNames[i]);
					num_NFL_subs++;
				}
			}

			if (num_NFL_subs == 0) {
				sb.append("<br/>");
				sb.append("No NFL team tags.");
			}

			// SOCCER TEAM TAGS
			sb.append("<br/><br/>");
			sb.append("<b>FC Team Tags</b>");

			String[] fcTeamNames = PublicDemoApp.context().getResources().getStringArray(R.array.fc_teamNames);
			String[] fcTeamKeys = PublicDemoApp.context().getResources().getStringArray(R.array.fc_teamKeys);

			int numSoccerSubs = 0;
			for (int i = 0; i < fcTeamNames.length; i++) {
				if (tags.contains(fcTeamKeys[i])) {
					setSubLine(sb, fcTeamNames[i]);
					numSoccerSubs++;
				}
			}

			if (numSoccerSubs == 0) {
				sb.append("<br/>");
				sb.append("No FC team tags.");
			}
		}

		return sb;
	}

	private static void setSubLine(StringBuilder sb, String teamName) {
		sb.append("<br/>");
		sb.append(teamName);
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

	public static void sendEmailToEmailAddress(final Activity inActivity, final String subject, final Spanned body, final File[] attachments) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(inActivity);

		dialogBuilder.setTitle("Send Log Data");
		dialogBuilder.setMessage("Please enter an email address to send log data.\n\nPress OK to send the log data to this email address.");

		// Set an EditText view to get user input
		final EditText input = (EditText) LayoutInflater.from(inActivity).inflate(R.layout.public_demo_email_edit_text, null);
		input.setText("MobilePushSupport@exacttarget.com"); // default email address
		dialogBuilder.setView(input);
		dialogBuilder.setPositiveButton("OK", null);
		dialogBuilder.setNegativeButton("Cancel", null);

		final AlertDialog dialog = dialogBuilder.create();
		dialog.show();

		Button okButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(android.view.View v) {
				String emailAddress = input.getText().toString();

				if (!emailAddress.contains("@") | emailAddress.isEmpty()) {
					Utils.flashError(input, "Please enter a valid email address");
					return;
				}

				dialog.dismiss();
				String[] to = new String[] { emailAddress };

				// edits pass, so send email
				sendEmail(inActivity, subject, body, to, attachments);

			}
		});
	}

	public static Attribute getAttribute(ArrayList<Attribute> attributes, String key) {
		for (Attribute attribute : attributes) {
			if (attribute.getKey().equals(key)) {
				return attribute;
			}
		}
		return null;
	}
}
