package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.applications.regprof.RegprofApplicationDetails;
import com.nacid.data.annotations.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
//RayaWritten----------------------------------------------------------------------
@Table(name="regprof.regprof_application")
public class RegprofApplicationDetailsImpl extends RegprofApplicationDetailsBaseImpl implements RegprofApplicationDetails {
    private String appNum;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date appDate;    
    private Integer repFromCompany;
    private String repPhone;
    private String repAddrDetails;
    private String repEmail;
    private String notes;
    private String applicantEmail;
    private int personalEmailInforming;
    private String archiveNum;
    private int dataAuthentic;
    private String imiCorrespondence;
    private int docflowStatusId;
    private Integer finalStatusHistoryId;

    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date endDate;
    private String externalSystemId;

    public RegprofApplicationDetailsImpl() {
    }
    public RegprofApplicationDetailsImpl(Integer id, String appNum,
            Date appDate, Integer applicantId, Integer representativeId,
            String applicantEmail, String applicantPhone, String applicantCity,
            String applicantAddrDetails, Integer applicantCountryId,
            Integer repFromCompany, String repPhone, String repAddrDetails,
            String repEmail, Integer applicantDocumentsId, String notes,
            Integer status, int docflowStatusId, int personalEmailInforming,
            int personalDataUsage, int dataAuthentic, int namesDontMatch,
            Integer trainingCourseId, Integer applicationCountryId,
            Integer serviceTypeId, Date endDate, String imiCorrepsondence,
            int apostilleApplication, Integer finalStatusHistoryId, Integer documentReceiveMethodId, String externalSystemId,
                                         Integer applicantPersonalIdDocumentType) {
        this.id = id;
        this.appNum = appNum;
        this.appDate = appDate;
        this.applicantId = applicantId;
        this.representativeId = representativeId;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantCity = applicantCity;
        this.applicantAddrDetails = applicantAddrDetails;
        this.applicantCountryId = applicantCountryId;
        this.repFromCompany = repFromCompany;
        this.repPhone = repPhone;
        this.repAddrDetails = repAddrDetails;
        this.repEmail = repEmail;
        this.applicantDocumentsId = applicantDocumentsId;
        this.notes = notes;
        this.status = status;
        this.docflowStatusId = docflowStatusId;
        this.personalEmailInforming = personalEmailInforming;
        this.personalDataUsage = personalDataUsage;
        this.dataAuthentic = dataAuthentic;
        this.namesDontMatch = namesDontMatch;
        this.trainingCourseId = trainingCourseId;
        this.applicationCountryId = applicationCountryId;
        this.serviceTypeId = serviceTypeId;
        this.endDate = endDate;
        this.archiveNum = "";
        this.imiCorrespondence = imiCorrepsondence;
        this.apostilleApplication = apostilleApplication;
        this.finalStatusHistoryId = finalStatusHistoryId;
        this.documentReceiveMethodId = documentReceiveMethodId;
        this.externalSystemId = externalSystemId;
        this.applicantPersonalIdDocumentType = applicantPersonalIdDocumentType;
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
    public Integer getRepFromCompany() {
        return repFromCompany;
    }
    public void setRepFromCompany(Integer repFromCompany) {
        this.repFromCompany = repFromCompany;
    }
    public String getRepPhone() {
        return repPhone;
    }
    public void setRepPhone(String repPhone) {
        this.repPhone = repPhone;
    }
    public String getRepAddrDetails() {
        return repAddrDetails;
    }
    public void setRepAddrDetails(String repAddrDetails) {
        this.repAddrDetails = repAddrDetails;
    }
    public String getRepEmail() {
        return repEmail;
    }
    public void setRepEmail(String repEmail) {
        this.repEmail = repEmail;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public int getPersonalEmailInforming() {
        return personalEmailInforming;
    }
    public void setPersonalEmailInforming(int personalEmailInforming) {
        this.personalEmailInforming = personalEmailInforming;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getApplicantEmail() {
        return applicantEmail;
    }
    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }
    public String getArchiveNum() {
        return archiveNum;
    }
    public void setArchiveNum(String archiveNum) {
        this.archiveNum = archiveNum;
    }
    public int getDataAuthentic() {
        return dataAuthentic;
    }
    public void setDataAuthentic(int dataAuthentic) {
        this.dataAuthentic = dataAuthentic;
    }
    public String getImiCorrespondence() {
        return imiCorrespondence;
    }
    public void setImiCorrespondence(String imiCorrespondence) {
        this.imiCorrespondence = imiCorrespondence;
    }

    public int getDocflowStatusId() {
        return docflowStatusId;
    }

    public void setDocflowStatusId(int docflowStatusId) {
        this.docflowStatusId = docflowStatusId;
    }

    public Integer getFinalStatusHistoryId() {
        return finalStatusHistoryId;
    }

    public void setFinalStatusHistoryId(Integer finalStatusHistoryId) {
        this.finalStatusHistoryId = finalStatusHistoryId;
    }

    public String getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(String externalSystemId) {
        this.externalSystemId = externalSystemId;
    }
}
//----------------------------------------------------------------------------