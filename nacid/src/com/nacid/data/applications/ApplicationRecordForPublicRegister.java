package com.nacid.data.applications;

import java.util.Date;
import java.util.List;

import com.nacid.bl.applications.ApplicationForPublicRegister;

public class ApplicationRecordForPublicRegister implements ApplicationForPublicRegister {
    private int id;
    private String appNum;
    private Date appDate;
    private String applicantName;
    private String universityName;
    private String universityCountry;
    private String recognizedSpecialityName;
    private String validatedCertNumber;
    private List<String> invalidatedCertNumbers;
    private int finalStatusId;
    //private String certNumber;
    //private Integer invalidated;
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
    public String getUniversityName() {
        return universityName;
    }
    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
    public String getRecognizedSpecialityName() {
        return recognizedSpecialityName;
    }
    public void setRecognizedSpecialityName(String recognizedSpecialityName) {
        this.recognizedSpecialityName = recognizedSpecialityName;
    }
    
    public String getUniversityCountry() {
        return universityCountry;
    }
    public void setUniversityCountry(String universityCountry) {
        this.universityCountry = universityCountry;
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

    @Override
    public int getFinalStatusId() {
        return finalStatusId;
    }

    public void setFinalStatusId(int finalStatusId) {
        this.finalStatusId = finalStatusId;
    }

    @Override
    public String toString() {
        return "ApplicationRecordForPublicRegister{" +
                "id=" + id +
                ", appNum='" + appNum + '\'' +
                ", appDate=" + appDate +
                ", applicantName='" + applicantName + '\'' +
                ", universityName='" + universityName + '\'' +
                ", universityCountry='" + universityCountry + '\'' +
                ", recognizedSpecialityName='" + recognizedSpecialityName + '\'' +
                ", validatedCertNumber='" + validatedCertNumber + '\'' +
                ", invalidatedCertNumbers=" + invalidatedCertNumbers +
                ", finalStatusId=" + finalStatusId +
                '}';
    }
}
