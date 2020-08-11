package com.nacid.data.regprof.external;

import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsBaseImpl;
import com.nacid.data.annotations.Table;


@Table(name="eservices.regprof_training_course")
public class ExtRegprofTrainingCourseRecord extends RegprofTrainingCourseDetailsBaseImpl {
    private String profInstitutionNameTxt;
    private String profInstitutionOrgNameTxt;
    private String highProfQualificationTxt;
    private String sdkProfInstitutionNameTxt;
    private String sdkProfInstitutionOrgNameTxt;
    private String sdkProfQualificationTxt;
    private String secProfQualificationTxt;
    private String certificateProfQualificationTxt;
    
    public String getProfInstitutionNameTxt() {
        return profInstitutionNameTxt;
    }
    public void setProfInstitutionNameTxt(String profInstitutionNameTxt) {
        this.profInstitutionNameTxt = profInstitutionNameTxt;
    }
    public String getProfInstitutionOrgNameTxt() {
        return profInstitutionOrgNameTxt;
    }
    public void setProfInstitutionOrgNameTxt(String profInstitutionOrgNameTxt) {
        this.profInstitutionOrgNameTxt = profInstitutionOrgNameTxt;
    }
    public String getHighProfQualificationTxt() {
        return highProfQualificationTxt;
    }
    public void setHighProfQualificationTxt(String highProfQualificationTxt) {
        this.highProfQualificationTxt = highProfQualificationTxt;
    }
    public String getSdkProfInstitutionNameTxt() {
        return sdkProfInstitutionNameTxt;
    }
    public void setSdkProfInstitutionNameTxt(String sdkProfInstitutionTxt) {
        this.sdkProfInstitutionNameTxt = sdkProfInstitutionTxt;
    }
    public String getSdkProfInstitutionOrgNameTxt() {
        return sdkProfInstitutionOrgNameTxt;
    }
    public void setSdkProfInstitutionOrgNameTxt(String sdkProfInstitutionOrgNameTxt) {
        this.sdkProfInstitutionOrgNameTxt = sdkProfInstitutionOrgNameTxt;
    }
    public String getSdkProfQualificationTxt() {
        return sdkProfQualificationTxt;
    }
    public void setSdkProfQualificationTxt(String sdkProfQualificationTxt) {
        this.sdkProfQualificationTxt = sdkProfQualificationTxt;
    }
    public String getCertificateProfQualificationTxt() {
        return certificateProfQualificationTxt;
    }
    public void setCertificateProfQualificationTxt(String certificateProfQualificationTxt) {
        this.certificateProfQualificationTxt = certificateProfQualificationTxt;
    }
    public String getSecProfQualificationTxt() {
        return secProfQualificationTxt;
    }
    public void setSecProfQualificationTxt(String secProfQualificationTxt) {
        this.secProfQualificationTxt = secProfQualificationTxt;
    }
    

}