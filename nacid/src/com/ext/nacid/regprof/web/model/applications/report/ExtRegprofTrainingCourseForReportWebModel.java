package com.ext.nacid.regprof.web.model.applications.report;

import com.nacid.bl.NacidDataProvider;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseRecord;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseForReportBaseWebModel;

//RayaWritten-----------------------------------------------------
public class ExtRegprofTrainingCourseForReportWebModel extends RegprofTrainingCourseForReportBaseWebModel{
    private String profInstitutionNameTxt;
    private String profInstitutionOrgNameTxt;
    private String highProfQualificationTxt;
    private String sdkProfInstitutionNameTxt;
    private String sdkProfInstitutionOrgNameTxt;
    private String sdkProfQualificationTxt;
    private String secProfQualificationTxt;
    private String certificateProfQualificationTxt;
    
    public ExtRegprofTrainingCourseForReportWebModel(ExtRegprofTrainingCourseRecord trainingCourseDetails, NacidDataProvider nacidDataProvider){
        super(trainingCourseDetails, nacidDataProvider);
        this.profInstitutionNameTxt = trainingCourseDetails.getProfInstitutionNameTxt();
        this.profInstitutionOrgNameTxt = trainingCourseDetails.getProfInstitutionOrgNameTxt();
        this.highProfQualificationTxt = trainingCourseDetails.getHighProfQualificationTxt();
        this.sdkProfQualificationTxt = trainingCourseDetails.getSdkProfQualificationTxt();
        this.sdkProfInstitutionNameTxt = trainingCourseDetails.getSdkProfInstitutionNameTxt();
        this.sdkProfInstitutionOrgNameTxt = trainingCourseDetails.getSdkProfInstitutionOrgNameTxt();
        this.certificateProfQualificationTxt = trainingCourseDetails.getCertificateProfQualificationTxt();
        this.secProfQualificationTxt = trainingCourseDetails.getSecProfQualificationTxt();
        super.notRestricted = trainingCourseDetails.getNotRestricted() == 1;
    }
    public String getProfInstitutionNameTxt() {
        return profInstitutionNameTxt;
    }
    public String getProfInstitutionOrgNameTxt() {
        return profInstitutionOrgNameTxt;
    }
    public String getHighProfQualificationTxt() {
        return highProfQualificationTxt;
    }
    public String getSdkProfInstitutionNameTxt() {
        return sdkProfInstitutionNameTxt;
    }
    public String getSdkProfInstitutionOrgNameTxt() {
        return sdkProfInstitutionOrgNameTxt;
    }
    public String getSdkProfQualificationTxt() {
        return sdkProfQualificationTxt;
    }
    public String getCertificateProfQualificationTxt() {
        return certificateProfQualificationTxt;
    }
    public String getSecProfQualificationTxt() {
        return secProfQualificationTxt;
    }
    
    
    
}
//-----------------------------------------------------------------
