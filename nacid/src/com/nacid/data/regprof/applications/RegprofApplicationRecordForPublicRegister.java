package com.nacid.data.regprof.applications;

import java.util.Date;
import java.util.List;

import com.nacid.bl.applications.regprof.RegprofApplicationForPublicRegister;

public class RegprofApplicationRecordForPublicRegister implements RegprofApplicationForPublicRegister {
    private int id;
    private String appNum;
    private Date appDate;
    private String applicantName;
    private String applicationCountryName;
    private String validatedCertNumber;
    private List<String> invalidatedCertNumbers;
    private int finalStatusId;
    
    public String getApplicationCountryName() {
        return applicationCountryName;
    }
    public void setApplicationCountryName(String applicationCountryName) {
        this.applicationCountryName = applicationCountryName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAppNum() {
        return appNum;
    }
    public void setAppNum(String appNum) {
        this.appNum = appNum;
    }
    public Date getAppDate() {
        return appDate;
    }
    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }
    public String getApplicantName() {
        return applicantName;
    }
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
    public String getValidatedCertNumber() {
        return validatedCertNumber;
    }
    public void setValidatedCertNumber(String validatedCertNumber) {
        this.validatedCertNumber = validatedCertNumber;
    }
    public List<String> getInvalidatedCertNumbers() {
        return invalidatedCertNumbers;
    }
    public void setInvalidatedCertNumbers(List<String> invalidatedCertNumbers) {
        this.invalidatedCertNumbers = invalidatedCertNumbers;
    }

    public int getFinalStatusId() {
        return finalStatusId;
    }

    public void setFinalStatusId(int finalStatusId) {
        this.finalStatusId = finalStatusId;
    }
}
