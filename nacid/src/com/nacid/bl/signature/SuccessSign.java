package com.nacid.bl.signature;


/**
 * Created by denislav.veizov on 24.11.2017 г..
 */
public class SuccessSign {

    private String xmlSigned;

    private CertificateInfo certificateInfo;

    public SuccessSign(String xmlSigned, CertificateInfo certificateInfo) {
        this.xmlSigned = xmlSigned;
        this.certificateInfo = certificateInfo;
    }

    public SuccessSign() {
    }

    public String getXmlSigned() {
        return xmlSigned;
    }

    public void setXmlSigned(String xmlSigned) {
        this.xmlSigned = xmlSigned;
    }

    public CertificateInfo getCertificateInfo() {
        return certificateInfo;
    }

        public void setCertificateInfo(CertificateInfo certificateInfo) {
        this.certificateInfo = certificateInfo;
    }
}
