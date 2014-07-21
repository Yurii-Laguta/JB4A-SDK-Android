package com.exacttarget.practicefield;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * RequestToken to provide request a token from Middle Tier to send a message POST a request to the Middle Tier.
 *
 * @author awestberg
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("accessToken")
	private String accessToken;

	@JsonProperty("expiresIn")
	private Integer expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

}