package com.nacid.bl.impl.apostille.objectss;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: Georgi
 * Date: 1.4.2020 г.
 * Time: 12:29
 */
public class ApostilleApplication {
    @JsonProperty("external_id")
    private String externalId;
    private ApostilleFile certificate;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public ApostilleFile getCertificate() {
        return certificate;
    }

    public void setCertificate(ApostilleFile certificate) {
        this.certificate = certificate;
    }
}
