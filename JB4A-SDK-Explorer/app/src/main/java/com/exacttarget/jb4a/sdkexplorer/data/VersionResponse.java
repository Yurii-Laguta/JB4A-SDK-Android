package com.exacttarget.jb4a.sdkexplorer.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by pvandyk on 1/27/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("version")
    private String version;

    @JsonProperty("update_url")
    private String updateUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

}
