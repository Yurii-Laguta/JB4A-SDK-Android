package com.exacttarget.publicdemo;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;
import com.exacttarget.etpushsdk.ETPush;

import java.io.*;
import java.util.Calendar;

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
				menu.findItem(R.id.menu_discount).setVisible(true);
				menu.findItem(R.id.menu_settings).setVisible(true);
				menu.findItem(R.id.menu_debug_settings).setVisible(true);
				menu.findItem(R.id.menu_about).setVisible(true);
				break;
			case CONSTS.NOTIFICATION_ACTIVITY:
				menu.findItem(R.id.menu_discount).setVisible(true);
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.OPENDIRECT_ACTIVITY:
				menu.findItem(R.id.menu_discount).setVisible(false);
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.DISCOUNT_ACTIVITY:
				menu.findItem(R.id.menu_discount).setVisible(false);
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.ABOUT_ACTIVITY:
				menu.findItem(R.id.menu_discount).setVisible(false);
				menu.findItem(R.id.menu_settings).setVisible(false);
				menu.findItem(R.id.menu_debug_settings).setVisible(false);
				menu.findItem(R.id.menu_about).setVisible(false);
				break;
			case CONSTS.SETTINGS_ACTIVITY:
			case CONSTS.DEBUG_SETTINGS_ACTIVITY:
				menu.findItem(R.id.menu_discount).setVisible(false);
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

			case R.id.menu_discount:
				intent = new Intent(activity, PublicDemoDiscountActivity.class);
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
				intent = new Intent(activity, PublicDemoAboutActivity.class);
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
		webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

		webView.setScrollbarFadingEnabled(false);
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

	public static String getRingtoneName(String ringtoneStr) {
		String name;
		if (ringtoneStr.isEmpty()) {
			name = PublicDemoApp.context().getString(R.string.silent);
		}
		else {
			Uri ringtoneUri = Uri.parse(ringtoneStr);
			Ringtone ringtone = RingtoneManager.getRingtone(PublicDemoApp.context(), ringtoneUri);
			name = ringtone.getTitle(PublicDemoApp.context());
		}
		return name;
	}

	public static void setToMidnight(Calendar inDate) {
		inDate.set(Calendar.HOUR_OF_DAY, 0);
		inDate.set(Calendar.MINUTE, 0);
		inDate.set(Calendar.SECOND, 0);
		inDate.set(Calendar.MILLISECOND, 0);
	}

	public static void createLogcatFile() {
		Process mLogcatProc;
		BufferedReader reader = null;
		OutputStreamWriter osw = null;
		File toFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "ETPublicDemo_logcat.txt");
		try {
			FileOutputStream fosw = new FileOutputStream(toFile, false);
			osw = new OutputStreamWriter(fosw);
			mLogcatProc = Runtime.getRuntime().exec("logcat -d");

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
			Toast.makeText(PublicDemoApp.context(), "Problem creating logcat file: "+e.getMessage(), Toast.LENGTH_LONG).show();
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
					Toast.makeText(PublicDemoApp.context(), "Problem creating logcat file: "+e.getMessage(), Toast.LENGTH_LONG).show();
				}
			if (osw != null)
				try {
					osw.close();
					Toast.makeText(PublicDemoApp.context(), "Logcat output file created.  See: "+toFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
				}
				catch (IOException e) {
					if (ETPush.getLogLevel() <= Log.ERROR) {
						Log.e(TAG, e.getMessage(), e);
					}
					Toast.makeText(PublicDemoApp.context(), "Problem creating logcat file: "+e.getMessage(), Toast.LENGTH_LONG).show();
				}

		}
	}

}
