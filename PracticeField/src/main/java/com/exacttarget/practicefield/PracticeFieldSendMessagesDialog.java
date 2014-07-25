package com.exacttarget.practicefield;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.Button;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.client.methods.CloseableHttpResponse;
import ch.boye.httpclientandroidlib.entity.StringEntity;
import ch.boye.httpclientandroidlib.util.EntityUtils;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.etpushsdk.util.JSONUtil;
import ch.boye.httpclientandroidlib.impl.client.CloseableHttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.HttpClients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * PracticeFieldSendMessagesDialog is dialog that will send messages and test the receipt of those
 * messages for the device it is running on.
 *
 * This dialog extends Dialog to kick off messages quickly without using the Marketing Cloud.
 *
 * This dialog would not normally be part of your delivered application.  However, it has been
 * created to test the functionality of the app, check the setup of your app within the Marketing
 * Cloud and Google Cloud Messaging, and provide a quick way to determine whether the setup is
 * correct.
 *
 * @author pvandyk
 */

public class PracticeFieldSendMessagesDialog extends Dialog {

	Activity callingActivity;

	private enum MessageType {
		STANDARD,
		CLOUDPAGE
	}

	private static final String TAG = PracticeFieldSendMessagesDialog.class.getName();

	public PracticeFieldSendMessagesDialog(Activity inActivity) {
		super(inActivity);

		// set variables
		callingActivity = inActivity;
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_messages_dialog);
		setTitle("Send Message");

		prepareDisplay();
	}

	@Override
	public void onBackPressed() {
		dismiss();
		super.onBackPressed();
	}

	private void prepareDisplay() {

		// You can choose to select a Standard message or a CloudPage Message
		// OpenDirect can not be sent with a CloudPage message, so remove from the list of choices
		//
		final RadioGroup messageTypeRG = (RadioGroup) findViewById(R.id.chooseMessageTypeRG);
		final EditText openDirectET = (EditText) findViewById(R.id.openDirectET);
		messageTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				TextView openDirectDesc = (TextView) findViewById(R.id.openDirectDescriptor);
				if (checkedId == R.id.Standard) {
					openDirectDesc.setVisibility(View.VISIBLE);
					openDirectET.setVisibility(View.VISIBLE);
				}
				else {
					openDirectDesc.setVisibility(View.GONE);
					openDirectET.setVisibility(View.GONE);
				}
			}
		});

		TextView demoDisclaimer = (TextView) findViewById(R.id.demoDisclaimerTV);
		demoDisclaimer.setText("Sending messages from an app that catches ET messages is not normally done.  We " +
				"have added this capability in this demo app for demonstration purposes only, allowing you to " +
				"fully test the functionality of ET within this app.\n\nMessages will only be sent to this device.\n\nPlease enter " +
				"the values that would normally be set for a specific message by the marketing team in the ET Marketing Cloud.");

		// get custom key (discount_code)
		List<String> discountCodesDescriptive = new ArrayList<String>();
		final List<String> discountCodeKeys = new ArrayList<String>();
		discountCodesDescriptive.add("No code");
		discountCodeKeys.add("none");
		discountCodesDescriptive.add("10%");
		discountCodeKeys.add("10");
		discountCodesDescriptive.add("15%");
		discountCodeKeys.add("15");
		discountCodesDescriptive.add("20%");
		discountCodeKeys.add("20");

		ArrayAdapter<String> dcAdapter = new ArrayAdapter<String>(callingActivity, android.R.layout.simple_spinner_item, discountCodesDescriptive);
		dcAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final Spinner dcSpinner = (Spinner) findViewById(R.id.discountSpinner);
		dcSpinner.setSelection(0);
		dcSpinner.setAdapter(dcAdapter);

		// prep team spinner with combined list of NFL and FC teams
		String[] nflTeamNames = callingActivity.getResources().getStringArray(R.array.nfl_teamNames);
		String[] nflTeamKeys = callingActivity.getResources().getStringArray(R.array.nfl_teamKeys);
		String[] fcTeamNames = callingActivity.getResources().getStringArray(R.array.fc_teamNames);
		String[] fcTeamKeys = callingActivity.getResources().getStringArray(R.array.fc_teamKeys);

		List<String> allTeamNames = new ArrayList<String>();
		final List<String> allTeamKeys = new ArrayList<String>();
		allTeamNames.add("None");
		allTeamKeys.add("none");

		for (int i = 0; i < nflTeamNames.length; i++) {
			allTeamNames.add(nflTeamNames[i]);
			allTeamKeys.add(nflTeamKeys[i]);
		}

		for (int i = 0; i < fcTeamNames.length; i++) {
			allTeamNames.add(fcTeamNames[i]);
			allTeamKeys.add(fcTeamKeys[i]);
		}

		ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(callingActivity, android.R.layout.simple_spinner_item, allTeamNames);
		teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final Spinner tagSpinner = (Spinner) findViewById(R.id.tagSpinner);
		tagSpinner.setSelection(0);
		tagSpinner.setAdapter(teamAdapter);

		Button btSend = (Button) findViewById(R.id.sendButton);
		btSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				int messageTypeSelected = messageTypeRG.getCheckedRadioButtonId();

				MessageType messageType;
				if (messageTypeSelected == R.id.Standard) {
					messageType = MessageType.STANDARD;
				}
				else {
					messageType = MessageType.CLOUDPAGE;
				}

				EditText messageET = (EditText) findViewById(R.id.messageET);
				String outMsg = messageET.getText().toString();

				RadioGroup soundRG = (RadioGroup) findViewById(R.id.soundRG);
				int selSoundId = soundRG.getCheckedRadioButtonId();
				RadioButton soundRB = (RadioButton) findViewById(selSoundId);
				String outSound = (String) soundRB.getTag();

				String outOD = openDirectET.getText().toString();

				int dcSelectedIndex = dcSpinner.getSelectedItemPosition();
				String outKey = discountCodeKeys.get(dcSelectedIndex);

				int tagSelectedIndex = tagSpinner.getSelectedItemPosition();
				String outTag = allTeamKeys.get(tagSelectedIndex);

				if (ETPush.getLogLevel() <= Log.DEBUG) {
					Log.d(TAG, "Sending message to: " + outKey + " with code : " + outTag + " : " + outMsg);
				}

				sendMessage(outMsg, outSound, outTag, outOD, outKey, messageType);

				dismiss();
			}

		});

		Button btCancel = (Button) findViewById(R.id.cancelButton);
		btCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}

		});
	}

	// sendMessage()
	//
	//		This method is not normally found within a client app.  This code is typically found
	//      within a server app to control sending of messages.
	//
	private void sendMessage(final String outMsg, final String outSound, final String outTag, final String outOD, final String outKey, final MessageType messageType) {

		new Thread(new Runnable() {
			public void run() {
				try {
					//
					//	get an api access token
					//  the clientId and the clientSecret are found in the Marketing Cloud setup for the Server-Server application.
					//
					//  the CONSTS_API will return the appropriate development or production version depending on the setting from
					//  CONSTS_API.setDevelopment() that is called PracticeFieldApp.
					//
					String accessToken = "";
					CloseableHttpClient httpClient = HttpClients.createDefault();

					HttpPost httpRequestToken;
					if (CONSTS_API.getBuildType() == CONSTS_API.BuildType.DEVELOPMENT | CONSTS_API.getBuildType() == CONSTS_API.BuildType.QA) {
						// DEV AND QA go against QA DB
						httpRequestToken = new HttpPost(CONSTS_API.getQA_url());
					}
					else {
						httpRequestToken = new HttpPost("https://auth.exacttargetapis.com/v1/requestToken");
					}
					httpRequestToken.setHeader("Content-type", "application/json");
					httpRequestToken.setEntity(new StringEntity("{\"clientId\":\"" + CONSTS_API.getClientId() + "\",\"clientSecret\":\"" + CONSTS_API.getClientSecret() + "\"}"));
					CloseableHttpResponse httpRequestTokenResponse = httpClient.execute(httpRequestToken);
					try {
						HttpEntity tokenResponseEntity = httpRequestTokenResponse.getEntity();
						String tokenResponseJson = EntityUtils.toString(tokenResponseEntity);

						RequestToken requestToken = JSONUtil.jsonToObject(tokenResponseJson, RequestToken.class);
						accessToken = requestToken.getAccessToken();
						EntityUtils.consume(tokenResponseEntity);
					}
					finally {
						httpRequestTokenResponse.close();
					}

					//
					// once the Access Token is retrieved, it can be used in to send the actual message.
					//
					// The messageId is the Id of the API Message set in the Marketing Cloud.  This API Message creates a template in the
					// Marketing Cloud (including security that connects to this app), and allows you to override the fields required for
					// the message being sent.
					//
					// Two API Messages have been created in the Cloud.  One for standard messages.  One that includes a CloudPage.
					//

					String messageId;
					if (messageType == MessageType.STANDARD) {
						messageId = CONSTS_API.getStandardMessageId();
					}
					else {
						messageId = CONSTS_API.getCloudPageMessageId();
					}

					String postURL = "https://www.exacttargetapis.com/push/v1/messageContact/";
					HttpPost httpPost = new HttpPost(postURL + messageId + "/send?access_token=" + accessToken);
					httpPost.setHeader("Content-type", "application/json");

					//
					// MessageContact is a JSON class that will provide the details to override in the Message.
					//
					// Details can be found here:
					// https://code.exacttarget.com/api/messagecontact-send-0
					//
					MessageContact messageData = new MessageContact();

					// set the text of the message
					messageData.setMessageText(outMsg);

					// set the sound for the message
					if (!outSound.equals("none")) {
						// the sound key is either default to use the default sound that the customer has set for
						// notifications.  Or it will be "custom.caf" which will then sound the sound found in the
						// raw folder of this project called custom.mp3.
						messageData.setSound(outSound);
					}

					// set to current device token
					// you must set a list of device tokens or subscriber keys
					// using this message type will ensure that you don't accidentally send to all your devices!
					ArrayList<String> deviceTokens = new ArrayList<String>(1);
					deviceTokens.add(ETPush.pushManager().getDeviceToken());
					messageData.setDeviceTokens(deviceTokens);

					// set the Open Direct URL.  OpenDirect takes the mobile app user to a specific location within the app after
					// that user interacts with a push message. The marketing user will specify a full URL address with subdomains (if any),
					// including http:// or https:// as applicable in the Create Message page of the ET Marketing Cloud.
					if (!outOD.isEmpty()) {
						messageData.setOpenDirect(outOD);
					}

					// Here a key is set to dictate special processing within the app once the message is received.
					// in this PracticeField App, this key will determine whether to display the 10, 15, or 20% discount QR code
					HashMap<String, String> customKeys = new HashMap<String, String>();
					if (!outKey.equals("none")) {
						customKeys.put(CONSTS.KEY_PAYLOAD_DISCOUNT, outKey);
						messageData.setCustomKeys(customKeys);
					}

					// the following are the tags that will determine whether the user as subscribed to a particular service
					// in this PracticeField App, these are the teams subscribed to in Settings.  Either NFL or FC teams.
					ArrayList<String> inclusionTags = new ArrayList<String>(1);
					if (!outTag.equals("none")) {
						inclusionTags.add(outTag);
						messageData.setInclusionTags(inclusionTags);
					}

					if (ETPush.getLogLevel() <= Log.DEBUG) {
						Log.d(TAG, "Sending..." + JSONUtil.objectToJson(messageData));
					}

					httpPost.setEntity(new StringEntity(JSONUtil.objectToJson(messageData)));

					CloseableHttpResponse messageSendResponse = httpClient.execute(httpPost);
					try {
						HttpEntity messageEntity = messageSendResponse.getEntity();
						String messageJson = EntityUtils.toString(messageEntity);

						if (ETPush.getLogLevel() <= Log.DEBUG) {
							Log.d(TAG, "Received..." + messageJson);
						}

						EntityUtils.consume(messageEntity);
						Handler uiHandler = new Handler(Looper.getMainLooper());
						uiHandler.post(new Runnable() {
							public void run() {
								Toast.makeText(PracticeFieldApp.context(), callingActivity.getString(R.string.alert_send_message1_text), Toast.LENGTH_LONG).show();
							}
						});
					}
					finally {
						messageSendResponse.close();
					}
				}
				catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}).start();

	}
}