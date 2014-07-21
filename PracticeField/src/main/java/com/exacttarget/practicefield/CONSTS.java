package com.exacttarget.practicefield;

/**
 * CONSTS
 *
 * Global constants.
 *
 * @author pvandyk
 */
public class CONSTS {

	public static final String KEY_CURRENT_PAGE = "currentPage";

	public static final int HOME_ACTIVITY = 0;
	public static final int SETTINGS_ACTIVITY = 1;
	public static final int DEBUG_SETTINGS_ACTIVITY = 2;
	public static final int SEND_MESSAGE_ACTIVITY = 3;
	public static final int DISPLAY_MESSAGE_ACTIVITY = 4;
	public static final int CLOUDPAGE_ACTIVITY = 5;
	public static final int CLOUDPAGE_INBOX_ACTIVITY = 6;
	public static final int OPENDIRECT_ACTIVITY = 7;
	public static final int DISCOUNT_ACTIVITY = 8;
	public static final int INFO_ACTIVITY = 9;

	public static final String KEY_ATTRIB_FIRST_NAME = "FirstName";
	public static final String KEY_ATTRIB_LAST_NAME = "LastName";

	public static final String KEY_PREF_FIRST_NAME = "pref_first_name";
	public static final String KEY_PREF_LAST_NAME = "pref_last_name";
	public static final String KEY_PREF_PUSH = "pref_push";
	public static final String KEY_PREF_GEO = "pref_geo";
	public static final String KEY_PREF_CAT_NFL = "pref_cat_nfl";
	public static final String KEY_PREF_CAT_FC = "pref_cat_fc";

	public static final String KEY_DEBUG_PREF_ENABLE_DEBUG = "debug_pref_enable_debug";
	public static final String KEY_DEBUG_PREF_COLLECT_LOGCAT = "debug_pref_collect_logcat";

	public static final String KEY_PUSH_RECEIVED_DATE = "push_received_date";
	public static final String KEY_PUSH_RECEIVED_PAYLOAD = "push_received_payload";

	public static final String KEY_PAYLOAD_DISCOUNT = "discount_code";
	public static final String KEY_PAYLOAD_ALERT = "alert";

	public static final String[] KEY_PREF_PUSH_DEPENDENT = {KEY_PREF_GEO, KEY_PREF_CAT_NFL,KEY_PREF_CAT_FC };

	public static final String PAGE_TITLE = "<b>Practice Field for<br/>&nbsp;&nbsp;&nbsp;ET MobilePush SDK</b><br/><br/>";

}
