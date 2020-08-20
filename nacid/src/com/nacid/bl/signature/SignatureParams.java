package com.nacid.bl.signature;


/**
 * Created by denislav.veizov on 22.11.2017 г..
 */

public class SignatureParams {

    private String xml;
    private String type;
    private String successUrl;
    private String cancelUrl;

    public SignatureParams(String xml, String type, String successUrl, String cancelUrl) {
        this.xml = xml;
        this.type = type;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
    }

    public SignatureParams() {
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

}
