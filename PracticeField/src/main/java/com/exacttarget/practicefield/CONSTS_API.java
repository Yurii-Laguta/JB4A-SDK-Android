package com.exacttarget.practicefield;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.exacttarget.etpushsdk.ETPush;

/**
 * CONSTS_API
 * <p/>
 * This class holds the constants for the API keys used within the app.
 * <p/>
 * There are two sets:
 * 1) Testing
 * 2) Production
 * <p/>
 * You must fill in the appropriate values for your app.
 *
 * @author pvandyk
 */

public class CONSTS_API {
	public static enum BuildType {
		DEVELOPMENT,
		QA,
		PRODUCTION
	}

	private static BuildType buildType = null;
	private static final String TAG = CONSTS_API.class.getName();

	//
	//	The following values are needed to receive messages in this app.  These are created in the App Center of Code@
	//
	private static final String ET_APP_ID_DEV = "16586d33-807c-4e1a-9a73-feb54a5c4ad1";
	private static final String ET_APP_ID_QA = "9420d0fb-ef76-47dd-bfd9-e2e5dc9a99b4";
	private static final String ET_APP_ID_PROD = "35a5c02d-c9ca-4682-8796-600e1f6d1be1";

	private static final String ET_ACCESS_TOKEN_DEV = "v9q3gd5ysstjrwv2vqcst296";
	private static final String ET_ACCESS_TOKEN_QA = "q46emsp3w6n6t4skkvngmgt2";
	private static final String ET_ACCESS_TOKEN_PROD = "2cfrrqep6r658vfj4g9aqnxc";

	//
	//  The following values are needed to receive messages from Google Cloud Messaging (GCM).  You will find these values in the GCM
	//  developers console. (https://console.developers.google.com)
	//
	private static final String GCM_SENDER_ID_DEV = "348137931902";
	private static final String GCM_SENDER_ID_QA = "348137931902";
	private static final String GCM_SENDER_ID_PROD = "875282375649";

	//
	//	The following values are needed to send messages from this app.  These are not normally found in an app that
	//  catches Push Notifications.  But it provides a way to test this app directly.
	//
	//  The clientId and the clientSecret are the values found in the Marketing Cloud App Center for the Server to Server App.
	private static final String ET_CLIENT_ID_DEV = "ynpwpsn2ffftznsb2zwrt3cj";
	private static final String ET_CLIENT_ID_QA = "rkm928cxj85atfwpq375md32";
	private static final String ET_CLIENT_ID_PROD = "6xr5vfjp4kpwvatn8mtyjjtc";

	private static final String ET_CLIENT_SECRET_DEV = "gU738uJQ8Q5H3s2uVxEacr5k";
	private static final String ET_CLIENT_SECRET_QA = "nBYaAS5kRVgsQfCzmbuhMQmT";
	private static final String ET_CLIENT_SECRET_PROD = "gkYwFtncVrmrWvkEfnH4cTtv";

	//  The standard messageId is found in the Marketing Cloud Message Center for the API Message which is a template for the message to
	//  send.  The PracticeFieldSendMessageDialog overrides the values in the message to customize who receives the message as well
	//  as what is included in the message.
	private static final String ET_STANDARD_MESSAGE_ID_DEV = "MjEzOToxMTQ6MA";
	private static final String ET_STANDARD_MESSAGE_ID_QA = "MToxMTQ6MA";
	private static final String ET_STANDARD_MESSAGE_ID_PROD = "MTA6MTE0OjA";

	//  The CloudPage messageId is found in the Marketing Cloud Message Center for the API Message which is a template for the CloudPage message to
	//  send.  The PracticeFieldSendMessageDialog overrides the values in the message to customize who receives the message as well
	//  as what is included in the message.
	private static final String ET_CLOUDPAGE_MESSAGE_ID_DEV = "tbd";
	private static final String ET_CLOUDPAGE_MESSAGE_ID_QA = "MzA6MTE0OjA";
	private static final String ET_CLOUDPAGE_MESSAGE_ID_PROD = "tbd";

	// for QA testing of this app only
	private static final String QA_URL = "https://auth-qa1s1.exacttargetapis.com/v1/requestToken";
	public static String getQA_url() { return QA_URL;}

	// gettBuildType()
	//
	// Return the build type set in setBuildType() which is determined by the AndroidManifest meta data field.
	//
	public static BuildType getBuildType() {
		if (buildType == null) {
			setBuildType();
		}
		return buildType;
	}

	// setBuildType()
	//
	// This class will set the build type.
	//
	// Production values are used by default.  So, the meta data build type in the manifest is only needed for Dev and QA.
	// To set to Dev or Prod, the following is needed in the AndroidManifest.xml file under the application section:
	//         <meta-data android:name="buildType" android:value="dev"/>
	//
	private static void setBuildType() {
		try {
			ApplicationInfo ai = PracticeFieldApp.context().getPackageManager().getApplicationInfo(PracticeFieldApp.context().getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			String manifestBuildType = bundle.getString("buildType");

			if (manifestBuildType.equalsIgnoreCase("prod")) {
				buildType = BuildType.PRODUCTION;
			}
			else if (manifestBuildType.equalsIgnoreCase("QA")) {
				buildType = BuildType.QA;
			}
			else if (manifestBuildType.equalsIgnoreCase("dev")) {
				buildType = BuildType.DEVELOPMENT;
			}
			else {
				if (ETPush.getLogLevel() <= Log.DEBUG) {
					Log.d(TAG, "No build type set in meta data.  Setting to production.");
					buildType = BuildType.PRODUCTION;
				}
			}
		}
		catch (Exception e) {
			if (ETPush.getLogLevel() <= Log.ERROR) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	// getEtAppId()
	//
	// get the ET App Id from the App Center in the Marketing Cloud for your Mobile Push App
	//
	public static String getEtAppId() {
		if (buildType == null) {
			setBuildType();
		}
		switch (buildType) {
			case DEVELOPMENT:
				return ET_APP_ID_DEV;
			case QA:
				return ET_APP_ID_QA;
			default:
				return ET_APP_ID_PROD;
		}
	}

	// getAccessToken()
	//
	// get the Access Token from the App Center in the Marketing Cloud for your Mobile Push App
	//
	public static String getAccessToken() {
		if (buildType == null) {
			setBuildType();
		}
		switch (buildType) {
			case DEVELOPMENT:
				return ET_ACCESS_TOKEN_DEV;
			case QA:
				return ET_ACCESS_TOKEN_QA;
			default:
				return ET_ACCESS_TOKEN_PROD;
		}
	}

	// getGcmSenderId()
	//
	// get the GCM Sender Id setup for your Google Cloud Messaging account.
	//
	public static String getGcmSenderId() {
		if (buildType == null) {
			setBuildType();
		}
		switch (buildType) {
			case DEVELOPMENT:
				return GCM_SENDER_ID_DEV;
			case QA:
				return GCM_SENDER_ID_QA;
			default:
				return GCM_SENDER_ID_PROD;
		}
	}

	// getClientId()
	//
	// get the Client ID from the App Center in the Marketing Cloud for your Server to Server App in App Center in the Marketing Cloud
	//
	public static String getClientId() {
		if (buildType == null) {
			setBuildType();
		}
		switch (buildType) {
			case DEVELOPMENT:
				return ET_CLIENT_ID_DEV;
			case QA:
				return ET_CLIENT_ID_QA;
			default:
				return ET_CLIENT_ID_PROD;
		}
	}

	// getClientSecret()
	//
	// get the Client Secret from the App Center in the Marketing Cloud for your Server to Server App in App Center in the Marketing Cloud
	//
	public static String getClientSecret() {
		if (buildType == null) {
			setBuildType();
		}
		switch (buildType) {
			case DEVELOPMENT:
				return ET_CLIENT_SECRET_DEV;
			case QA:
				return ET_CLIENT_SECRET_QA;
			default:
				return ET_CLIENT_SECRET_PROD;
		}
	}

	// getStandardMessageId()
	//
	// get the Message ID from the API Message you set up in the Marketing Cloud for standard alerts
	//
	public static String getStandardMessageId() {
		if (buildType == null) {
			setBuildType();
		}
		switch (buildType) {
			case DEVELOPMENT:
				return ET_STANDARD_MESSAGE_ID_DEV;
			case QA:
				return ET_STANDARD_MESSAGE_ID_QA;
			default:
				return ET_STANDARD_MESSAGE_ID_PROD;
		}
	}

	// getMessageId()
	//
	// get the Message ID from the API Message you set up in the Marketing Cloud for a CloudPage alert
	//
	public static String getCloudPageMessageId() {
		if (buildType == null) {
			setBuildType();
		}
		switch (buildType) {
			case DEVELOPMENT:
				return ET_CLOUDPAGE_MESSAGE_ID_DEV;
			case QA:
				return ET_CLOUDPAGE_MESSAGE_ID_QA;
			default:
				return ET_CLOUDPAGE_MESSAGE_ID_PROD;
		}
	}

}
