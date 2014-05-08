package com.exacttarget.publicdemo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * MessageContact to provide data to Middle Tier to send a message to a list of devices or subscribers.
 *
 * @author awestberg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("InclusionTags")
	private ArrayList<String> inclusionTags;

	@JsonProperty("ExclusionTags")
	private ArrayList<String> exclusionTags;

	@JsonProperty("DeviceTokens")
	private ArrayList<String> deviceTokens;

	@JsonProperty("Override")
	private Boolean override = Boolean.TRUE;

	@JsonProperty("MessageText")
	private String messageText;

	@JsonProperty("SendTime")
	private String sendTime = "2012-10-31 09:00";

	@JsonProperty("Sound")
	private String sound = "custom.caf";

	@JsonProperty("CustomKeys")
	private HashMap<String,String> customKeys;

	public ArrayList<String> getInclusionTags() {
		return inclusionTags;
	}

	public void setInclusionTags(ArrayList<String> inclusionTags) {
		this.inclusionTags = inclusionTags;
	}

	public ArrayList<String> getExclusionTags() {
		return exclusionTags;
	}

	public void setExclusionTags(ArrayList<String> exclusionTags) {
		this.exclusionTags = exclusionTags;
	}

	public ArrayList<String> getDeviceTokens() {
		return deviceTokens;
	}

	public void setDeviceTokens(ArrayList<String> deviceTokens) {
		this.deviceTokens = deviceTokens;
	}

	public Boolean getOverride() {
		return override;
	}

	public void setOverride(Boolean override) {
		this.override = override;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public HashMap<String, String> getCustomKeys() {
		return customKeys;
	}

	public void setCustomKeys(HashMap<String, String> customKeys) {
		this.customKeys = customKeys;
	}

}
