package com.exacttarget.publicdemo;

/**
 * CONSTS_API
 *
 * This class holds the constants for the API keys used within the app.
 *
 * There are two sets:
 *    1) Testing
 *    2) Production
 *
 * You must fill in the appropriate values for your app.
 *
 * @author pvandyk
 */

public class CONSTS_API {
	private static enum State {
		DEVELOPMENT,
		PRODUCTION
	}

	private static State state = State.PRODUCTION;

	//
	//	The following values are needed to receive messages in this app.  These are created in the App Center of Code@
	//
	private static final String ET_APP_ID_DEV="[YOUR DEV APP ID FROM APP CENTER IN MARKETING CLOUD]";
	private static final String ET_APP_ID_PROD="[YOUR PROD APP ID FROM APP CENTER IN MARKETING CLOUD]";

	private static final String ET_ACCESS_TOKEN_DEV = "[YOUR DEV ACCESS TOKEN FROM APP CENTER IN MARKETING CLOUD]";
	private static final String ET_ACCESS_TOKEN_PROD = "[YOUR PROD ACCESS TOKEN FROM APP CENTER IN MARKETING CLOUD]";

	//
	//  The following values are needed to receive messages from Google Cloud Messaging (GCM).  You will find these values in the GCM
	//  developers console. (https://console.developers.google.com)
	//
	private static final String GCM_SENDER_ID_DEV = "[YOUR DEV GCM ID GCM DEVELOPER CONSOLE]";
	private static final String GCM_SENDER_ID_PROD = "[YOUR PROD GCM ID GCM DEVELOPER CONSOLE]";

	//
	//	The following values are needed to send messages from this app.  These are not normally found in an app that
	//  catches Push Notifications.  But it provides a way to test this app directly.
	//
	//  The clientId and the clientSecret are the values found in the Marketing Cloud App Center for the Server to Server App.
	private static final String ET_CLIENT_ID_DEV = "[YOUR DEV CLIENT ID FROM APP CENTER IN MARKETING CLOUD]";
	private static final String ET_CLIENT_ID_PROD = "[YOUR PROD CLIENT ID FROM APP CENTER IN MARKETING CLOUD]";

	private static final String ET_CLIENT_SECRET_DEV = "[YOUR DEV CLIENT SECRET FROM APP CENTER IN MARKETING CLOUD]";
	private static final String ET_CLIENT_SECRET_PROD = "[YOUR PROD CLIENT SECRET FROM APP CENTER IN MARKETING CLOUD]";

	//  The messageId is found in the Marketing Cloud Message Center for the API Message which is a template for the message to
	//  send.  The PublicDemoSendMessageDialog overrides the values in the message to customize who receives the message as well
	//  as what is included in the message.
	private static final String ET_MESSAGE_ID_DEV = "[YOUR DEV MESSAGE ID FROM MARKETING CLOUD]";
	private static final String ET_MESSAGE_ID_PROD = "[YOUR PROD MESSAGE ID FROM MARKETING CLOUD]";

	// setDevelopment()
	//
	// call this if you want to use development (Test) values.  Otherwise, Production values will be used by default.
	//
	public static void setDevelopment() {
		state = State.DEVELOPMENT;
	}

	public static boolean isDevelopment() {
		return state == State.DEVELOPMENT;
	}

	// getEtAppId()
	//
	// get the ET App Id from the App Center in the Marketing Cloud for your Mobile Push App
	//
	public static String getEtAppId() {
		if (state == State.PRODUCTION) {
			return ET_APP_ID_PROD;
		}
		else {
			return ET_APP_ID_DEV;
		}
	}

	// getAccessToken()
	//
	// get the Access Token from the App Center in the Marketing Cloud for your Mobile Push App
	//
	public static String getAccessToken() {
		if (state == State.PRODUCTION) {
			return ET_ACCESS_TOKEN_PROD;
		}
		else {
			return ET_ACCESS_TOKEN_DEV;
		}
	}

	// getGcmSenderId()
	//
	// get the GCM Sender Id setup for your Google Cloud Messaging account.
	//
	public static String getGcmSenderId() {
		if (state == State.PRODUCTION) {
			return GCM_SENDER_ID_PROD;
		}
		else {
			return GCM_SENDER_ID_DEV;
		}
	}

	// getClientId()
	//
	// get the Client ID from the App Center in the Marketing Cloud for your Server to Server App in App Center in the Marketing Cloud
	//
	public static String getClientId() {
		if (state == State.PRODUCTION) {
			return ET_CLIENT_ID_PROD;
		}
		else {
			return ET_CLIENT_ID_DEV;
		}
	}

	// getClientSecret()
	//
	// get the Client Secret from the App Center in the Marketing Cloud for your Server to Server App in App Center in the Marketing Cloud
	//
	public static String getClientSecret() {
		if (state == State.PRODUCTION) {
			return ET_CLIENT_SECRET_PROD;
		}
		else {
			return ET_CLIENT_SECRET_DEV;
		}
	}

	// getMessageId()
	//
	// get the Message ID from the API Message you set up in the Messaging Hub
	//
	public static String getMessageId() {
		if (state == State.PRODUCTION) {
			return ET_MESSAGE_ID_PROD;
		}
		else {
			return ET_MESSAGE_ID_DEV;
		}
	}

}
