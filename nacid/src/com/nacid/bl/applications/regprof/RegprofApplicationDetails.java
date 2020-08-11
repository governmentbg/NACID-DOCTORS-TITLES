package com.nacid.bl.applications.regprof;

import java.util.Date;
//RayaWritten--------------------------------------------------
public interface RegprofApplicationDetails {
    public Integer getId();
    public void setId(Integer id);
    public String getAppNum();
    public void setAppNum(String appNum);
    public Date getAppDate();
    public void setAppDate(Date appDate);
    public Integer getApplicantId();
    public void setApplicantId(Integer applicantId);
    public Integer getRepresentativeId();
    public void setRepresentativeId(Integer representativeId);
    public String getApplicantEmail();
    public void setApplicantEmail(String applicantEmail);
    public String getApplicantPhone();
    public void setApplicantPhone(String applicantPhone);
    public String getApplicantCity();
    public void setApplicantCity(String applicantCity);
    public String getApplicantAddrDetails();
    public void setApplicantAddrDetails(String applicantAddrDetails);
    public Integer getApplicantCountryId();
    public void setApplicantCountryId(Integer applicantCountryId);
    public Integer getRepFromCompany();
    public void setRepFromCompany(Integer repFromCompany);
    public String getRepPhone();
    public void setRepPhone(String repPhone);
    public String getRepAddrDetails();
    public void setRepAddrDetails(String repAddrDetails);
    public String getRepEmail();
    public void setRepEmail(String repEmail);
    public Integer getApplicantDocumentsId();
    public void setApplicantDocumentsId(Integer applicantDocumentsId);
    public String getNotes();
    public void setNotes(String notes);
    public Integer getStatus();
    public void setStatus(Integer status);
    public int getPersonalEmailInforming();
    public void setPersonalEmailInforming(int personalEmailInforming);
    public int getPersonalDataUsage();
    public void setPersonalDataUsage(int personalDataUsage);
    public int getDataAuthentic();
    public void setDataAuthentic(int dataAuthentic);
    //public Integer getResponsibleUser();
    //public void setResponsibleUser(Integer responsibleUser);
    public int getNamesDontMatch();
    public void setNamesDontMatch(int namesDontMatch);
    public Integer getTrainingCourseId();
    public void setTrainingCourseId(Integer trainingCourseId);
    public Integer getApplicationCountryId();
    public void setApplicationCountryId(Integer applicationCountryId);
    public Integer getServiceTypeId();
    public void setServiceTypeId(Integer serviceTypeId);
    public Date getEndDate();
    public void setEndDate(Date endDate);
    public String getArchiveNum();
    public void setArchiveNum(String archiveNum);
    public String getImiCorrespondence();
    public void setImiCorrespondence(String imiCorrespondence);
    public void setApostilleApplication(int apostilleApplication);
    public int getApostilleApplication();

    public int getDocflowStatusId();
    public void setDocflowStatusId(int docflowStatusId);
    public Integer getFinalStatusHistoryId();
    public void setFinalStatusHistoryId(Integer finalStatusHistoryId);

    public Integer getDocumentReceiveMethodId();
    public void setDocumentReceiveMethodId(Integer documentReceiveMethodId);

    public String getExternalSystemId();
    public void setExternalSystemId(String externalSystemId);
}
//--------------------------------------------------------