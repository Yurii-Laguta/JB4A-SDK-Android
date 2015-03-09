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

package com.exacttarget.jb4a.sdkexplorer.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PushConfigInternal {

	@JsonProperty(value="configurationName")
	private String configName;

	@JsonProperty(value="appID")
	private String etAppId;

	@JsonProperty(value="accessToken")
	private String etAccessToken;

	@JsonProperty(value="gcm_sender_id")
	private String etGCMSenderId;

	@JsonProperty(value="client_id")
	private String etClientId;

	@JsonProperty(value="client_secret")
	private String etClientSecret;

	@JsonProperty(value="standard_message_id")
	private String etStandardMessageId;

	@JsonProperty(value="cloudpage_message_id")
	private String etCloudPageMessageId;

	@JsonProperty(value="rest_url")
	private String etRestUrl;

	public PushConfigInternal() {
		super();
	}


	public String getConfigName() {
		return configName;
	}


	public void setConfigName(String configName) {
		this.configName = configName;
	}


	public String getEtAppId() {
		return etAppId;
	}


	public void setEtAppId(String etAppId) {
		this.etAppId = etAppId;
	}


	public String getEtAccessToken() {
		return etAccessToken;
	}


	public void setEtAccessToken(String etAccessToken) {
		this.etAccessToken = etAccessToken;
	}

	public String getEtGCMSenderId() {
		return etGCMSenderId;
	}

	public void setEtGCMSenderId(String etGCMSenderId) {
		this.etGCMSenderId = etGCMSenderId;
	}

	public String getEtClientId() {
		return etClientId;
	}

	public void setEtClientId(String etClientId) {
		this.etClientId = etClientId;
	}

	public String getEtClientSecret() {
		return etClientSecret;
	}

	public void setEtClientSecret(String etClientSecret) {
		this.etClientSecret = etClientSecret;
	}

	public String getEtStandardMessageId() {
		return etStandardMessageId;
	}

	public void setEtStandardMessageId(String etStandardMessageId) {
		this.etStandardMessageId = etStandardMessageId;
	}

	public String getEtCloudPageMessageId() {
		return etCloudPageMessageId;
	}

	public void setEtCloudPageMessageId(String etCloudPageMessageId) {
		this.etCloudPageMessageId = etCloudPageMessageId;
	}

	public String getEtRestUrl() {
		return etRestUrl;
	}

	public void setEtRestUrl(String etRestUrl) {
		this.etRestUrl = etRestUrl;
	}
}
