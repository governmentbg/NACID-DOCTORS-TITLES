package com.ext.nacid.regprof.web.model.applications;

import org.apache.commons.lang.StringUtils;

public class ExtRegprofApplicationWebModel {
    private String statusName;
    private String docflowNumber;
    private String certificateNumber;
    public ExtRegprofApplicationWebModel(String statusName,
            String docflowNumber, String certificateNumber) {
        this.statusName = statusName;
        this.docflowNumber = StringUtils.isEmpty(docflowNumber) ? "" : "Деловоден № " + docflowNumber;
        this.certificateNumber = StringUtils.isEmpty(certificateNumber) ? "" : ". Сертификат №" + certificateNumber;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getDocflowNumber() {
        return docflowNumber;
    }
    public void setDocflowNumber(String docflowNumber) {
        this.docflowNumber = docflowNumber;
    }
    public String getCertificateNumber() {
        return certificateNumber;
    }
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
    
    
}
