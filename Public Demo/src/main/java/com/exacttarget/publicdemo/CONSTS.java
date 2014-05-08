package com.exacttarget.publicdemo;

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
	public static final int NOTIFICATION_ACTIVITY = 3;
	public static final int OPENDIRECT_ACTIVITY = 4;
	public static final int DISCOUNT_ACTIVITY = 5;
	public static final int ABOUT_ACTIVITY = 6;

	public static final String KEY_ATTRIB_FIRST_NAME = "FirstName";
	public static final String KEY_ATTRIB_LAST_NAME = "LastName";

	public static final String KEY_PREF_FIRST_NAME = "pref_first_name";
	public static final String KEY_PREF_LAST_NAME = "pref_last_name";
	public static final String KEY_PREF_PUSH = "pref_push";
	public static final String KEY_PREF_GEO = "pref_geo";
	public static final String KEY_PREF_USE_CUSTOM_RINGTONE = "pref_use_custom_ringtone";
	public static final String KEY_PREF_CUSTOM_RINGTONE = "pref_custom_ringtone";
	public static final String KEY_PREF_VIBRATE = "pref_vibrate";
	public static final String KEY_PREF_CAT_NFL = "pref_cat_nfl";
	public static final String KEY_PREF_CAT_FC = "pref_cat_fc";

	public static final String KEY_PREF_DISCOUNT_END_DATE = "pref_discount_end_date";
	public static final String KEY_PREF_DISCOUNT_MESSAGE = "pref_discount_message";
	public static final String KEY_PREF_DISCOUNT_IMAGE_FILE = "pref_discount_image_file";

	public static final String KEY_DEBUG_PREF_ENABLE_DEBUG = "debug_pref_enable_debug";
	public static final String KEY_DEBUG_PREF_COLLECT_LOGCAT = "debug_pref_collect_logcat";

	public static final String KEY_PAYLOAD_DISCOUNT = "discount_code";
	public static final String KEY_PAYLOAD_ALERT = "alert";

	public static final String[] KEY_PREF_PUSH_DEPENDENT = {KEY_PREF_GEO, KEY_PREF_USE_CUSTOM_RINGTONE, KEY_PREF_CUSTOM_RINGTONE, KEY_PREF_VIBRATE, KEY_PREF_CAT_NFL,KEY_PREF_CAT_FC };

	public static final long[] NOTIFICATION_CUSTOM_VIBRATE_PATTERN = {0L,250L, 100L,250L, 100L,250L};

	public static final boolean DEFAULT_PREF_PUSH = false;
	public static final boolean DEFAULT_PREF_GEO = false;

}
