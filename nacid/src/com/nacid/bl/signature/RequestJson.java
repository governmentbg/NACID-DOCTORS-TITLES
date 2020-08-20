package com.nacid.bl.signature;

/**
 * Created by denislav.veizov on 18.12.2017 г..
 */
public class RequestJson {

    private String hmac;

    private String encodedParams;

    public RequestJson(String hmac, String encodedParams) {
        this.hmac = hmac;
        this.encodedParams = encodedParams;
    }

    public RequestJson() {
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getEncodedParams() {
        return encodedParams;
    }

    public void setEncodedParams(String encodedParams) {
        this.encodedParams = encodedParams;
    }
}
