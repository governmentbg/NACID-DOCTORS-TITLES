package com.ext.nacid.regprof.web.model.applications;

/**
 * Created by georgi.georgiev on 15.02.2016.
 */
public class EpayRedirectWebModel {
    private String paymentUrl;
    private String urlOk;
    private String urlCancel;
    private String checksum;
    private String encoded;

    public EpayRedirectWebModel(String paymentUrl, String urlOk, String urlCancel, String checksum, String encoded) {
        this.paymentUrl = paymentUrl;
        this.urlOk = urlOk;
        this.urlCancel = urlCancel;
        this.checksum = checksum;
        this.encoded = encoded;
    }

    public String getUrlOk() {
        return urlOk;
    }

    public String getUrlCancel() {
        return urlCancel;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getEncoded() {
        return encoded;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }
}
