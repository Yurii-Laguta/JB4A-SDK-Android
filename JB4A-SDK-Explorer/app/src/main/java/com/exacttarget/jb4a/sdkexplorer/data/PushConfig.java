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

/**
 * @author eroger
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PushConfig {
	
	@JsonProperty(value="PushConfig")
	private PushConfigInternal pci = new PushConfigInternal();
	
	public PushConfig()
	{
		super();
	}
	
	public PushConfigInternal getPci() {
		return pci;
	}

	public void setPci(PushConfigInternal pci) {
		this.pci = pci;
	}

	public String toString()
	{
		return this.pci.getConfigName();
	}
	
	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return this.pci.getConfigName();
	}

	/**
	 * @return the etAppId
	 */
	public String getEtAppId() {
		return this.pci.getEtAppId();
	}

	/**
	 * @return the etAccessToken
	 */
	public String getEtAccessToken() {
		return this.pci.getEtAccessToken();
	}

	/**
	 * @return the etGCMSenderId
	 */
	public String getEtGCMSenderId() {
		return this.pci.getEtGCMSenderId();
	}

	/**
	 * @return the etClientId
	 */
	public String getEtClientId() {
		return this.pci.getEtClientId();
	}

	/**
	 * @return the etClientSecret
	 */
	public String getEtClientSecret() {
		return this.pci.getEtClientSecret();
	}

	/**
	 * @return the etStandardMessageId
	 */
	public String getEtStandardMessageId() {
		return this.pci.getEtStandardMessageId();
	}

	/**
	 * @return the etCloudPageMessageId
	 */
	public String getEtCloudPageMessageId() {
		return this.pci.getEtCloudPageMessageId();
	}

	/**
	 * @return the etRestUrl
	 */
	public String getEtRestUrl() {
		return this.pci.getEtRestUrl();
	}

}
