/**
 * Copyright (c) 2014 ExactTarget, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
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

package com.exacttarget.jb4a.sdkexplorer;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * MessageContact to provide data to Middle Tier to send a message to a list of devices or subscribers.
 *
 * @author awestberg
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageContact {

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

	@JsonProperty("Sound")
	private String sound;

	@JsonProperty("OpenDirect")
	private String openDirect;

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

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getOpenDirect() {
		return openDirect;
	}

	public void setOpenDirect(String openDirect) {
		this.openDirect = openDirect;
	}

	public HashMap<String, String> getCustomKeys() {
		return customKeys;
	}

	public void setCustomKeys(HashMap<String, String> customKeys) {
		this.customKeys = customKeys;
	}

}
