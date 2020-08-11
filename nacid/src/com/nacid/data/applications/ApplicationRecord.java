package com.nacid.data.applications;

import java.sql.Date;
import java.sql.Timestamp;

public class ApplicationRecord {

    private int id;
    private String applicationNumber;
    private Date applicationDate;
    private Integer applicantId;
    private Integer representativeId;
    private int trainingCourseId;
    private String email;
    private int officialEmailCommunication;
    private int homeCountryId;
    private String homeCity;
    private String homePostCode;
    private String homeAddressDetails;
    private String homePhone;
    private int homeIsBg;
    private String bgCity;
    private String bgPostCode;
    private String bgAddressDetails;
    private String bgPhone;
    private int bgAddressOwner;
    private int createdByUserId;
    private Timestamp timeOfCreation;
    private int applicationStatusId;
    private Integer companyId;
    private int differentDiplomaNames;
    private Integer reprCountryId;
    private String reprCity;
    private String reprPcode;
    private String reprAddressDetails;
    private String reprPhone;
    private String summary;
    private String archiveNumber;
    private Integer personalDataUsage;
    private Integer dataAuthentic;
    private Integer representativeAuthorized;
    private String notes;
    private String submittedDocs;
    private String applicantInfo;
    private Integer responsibleUser;
    private int docflowStatusId;
    private Integer applicantCompanyId;
    private Integer finalStatusHistoryId;
    private String representativeType;
    private Short typePayment;
    private Integer deliveryType;
    private Boolean declaration;
    private String courierNameAddress;
    private String outgoingNumber;
    private String internalNumber;
    private Boolean isExpress;
    private int applicantType;
    private int applicationType;
    private Integer documentReceiveMethodId;
    private Integer applicantPersonalIdDocumentType;

    public ApplicationRecord() {
    }

    public ApplicationRecord(int id, String applicationNumber, Date applicationDate, Integer applicantId, Integer applicantCompanyId, Integer representativeId, int trainingCourseId,
            String email, int officialEmailCommunication, int homeCountryId, String homeCity, String homePostCode, String homeAddressDetails,
            int homeIsBg, String bgCity, String bgPostCode, String bgAddressDetails, int bgAddressOwner, int createdByUserId,
            Timestamp timeOfCreation, int applicationStatusId, Integer companyId, int differentDiplomaNames, String homePhone, String bgPhone,
            Integer reprCountryId, String reprCity, String reprPcode, String reprAddressDetail,
            String reprPhone, String summary, String archiveNumber, Integer personalDataUsage, Integer dataAuthentic, Integer representativeAuthorized, String notes,
            String submittedDocs, String applicantInfo, Integer responsibleUser, int docflowStatusId, String representativeType, Short typePayment,
            Integer deliveryType, Boolean declaration, String courierNameAddress, String outgoingNumber, String internalNumber, Boolean isExpress, int applicantType,
            int applicationType, Integer documentReceiveMethodId, Integer applicantPersonalIdDocumentType) {
        this.id = id;
        this.applicationNumber = applicationNumber;
        this.applicationDate = applicationDate;
        this.applicantId = applicantId;
        this.representativeId = representativeId;
        this.trainingCourseId = trainingCourseId;
        this.email = email;
        this.officialEmailCommunication = officialEmailCommunication;
        this.homeCountryId = homeCountryId;
        this.homeCity = homeCity;
        this.homePostCode = homePostCode;
        this.homeAddressDetails = homeAddressDetails;
        this.homeIsBg = homeIsBg;
        this.bgCity = bgCity;
        this.bgPostCode = bgPostCode;
        this.bgAddressDetails = bgAddressDetails;
        this.bgAddressOwner = bgAddressOwner;
        this.createdByUserId = createdByUserId;
        this.timeOfCreation = timeOfCreation;
        this.applicationStatusId = applicationStatusId;
        this.companyId = companyId;
        this.differentDiplomaNames = differentDiplomaNames;
        this.bgPhone = bgPhone;
        this.homePhone = homePhone;
        this.reprCountryId = reprCountryId;
        this.reprCity = reprCity;
        this.reprPcode = reprPcode;
        this.reprAddressDetails = reprAddressDetail;
        this.reprPhone = reprPhone;
        this.summary = summary;
        this.archiveNumber = archiveNumber;
        this.personalDataUsage = personalDataUsage;
        this.dataAuthentic = dataAuthentic;
        this.representativeAuthorized = representativeAuthorized;
        this.notes = notes;
        this.submittedDocs = submittedDocs;
        this.applicantInfo = applicantInfo;
        this.responsibleUser = responsibleUser;
        this.docflowStatusId = docflowStatusId;
        this.applicantCompanyId = applicantCompanyId;
        this.representativeType = representativeType;
        this.typePayment = typePayment;
        this.deliveryType = deliveryType;
        this.declaration = declaration;
        this.courierNameAddress = courierNameAddress;
        this.outgoingNumber = outgoingNumber;
        this.internalNumber = internalNumber;
        this.isExpress = isExpress;
        this.applicantType = applicantType;
        this.applicationType = applicationType;
        this.documentReceiveMethodId = documentReceiveMethodId;
        this.applicantPersonalIdDocumentType = applicantPersonalIdDocumentType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public Integer getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Integer representativeId) {
        this.representativeId = representativeId;
    }

    public int getTrainingCourseId() {
        return trainingCourseId;
    }

    public void setTrainingCourseId(int trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOfficialEmailCommunication() {
        return officialEmailCommunication;
    }

    public void setOfficialEmailCommunication(int officialEmailCommunication) {
        this.officialEmailCommunication = officialEmailCommunication;
    }

    public int getHomeCountryId() {
        return homeCountryId;
    }

    public void setHomeCountryId(int homeCountryId) {
        this.homeCountryId = homeCountryId;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getHomePostCode() {
        return homePostCode;
    }

    public void setHomePostCode(String homePostCode) {
        this.homePostCode = homePostCode;
    }

    public String getHomeAddressDetails() {
        return homeAddressDetails;
    }

    public void setHomeAddressDetails(String homeAddressDetails) {
        this.homeAddressDetails = homeAddressDetails;
    }

    public int getHomeIsBg() {
        return homeIsBg;
    }

    public void setHomeIsBg(int homeIsBg) {
        this.homeIsBg = homeIsBg;
    }

    public String getBgCity() {
        return bgCity;
    }

    public void setBgCity(String bgCity) {
        this.bgCity = bgCity;
    }

    public String getBgPostCode() {
        return bgPostCode;
    }

    public void setBgPostCode(String bgPostCode) {
        this.bgPostCode = bgPostCode;
    }

    public String getBgAddressDetails() {
        return bgAddressDetails;
    }

    public void setBgAddressDetails(String bgAddressDetails) {
        this.bgAddressDetails = bgAddressDetails;
    }

    public int getBgAddressOwner() {
        return bgAddressOwner;
    }

    public void setBgAddressOwner(int bgAddressOwner) {
        this.bgAddressOwner = bgAddressOwner;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Timestamp getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(Timestamp timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public int getApplicationStatusId() {
        return applicationStatusId;
    }

    public void setApplicationStatusId(int applicationStatusId) {
        this.applicationStatusId = applicationStatusId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public int getDifferentDiplomaNames() {
        return differentDiplomaNames;
    }

    public void setDifferentDiplomaNames(int differentDiplomaNames) {
        this.differentDiplomaNames = differentDiplomaNames;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getBgPhone() {
        return bgPhone;
    }

    public void setBgPhone(String bgPhone) {
        this.bgPhone = bgPhone;
    }

    public Integer getReprCountryId() {
        return reprCountryId;
    }

    public void setReprCountryId(Integer reprCountryId) {
        this.reprCountryId = reprCountryId;
    }

    public String getReprCity() {
        return reprCity;
    }

    public void setReprCity(String reprCity) {
        this.reprCity = reprCity;
    }

    public String getReprPcode() {
        return reprPcode;
    }

    public void setReprPcode(String reprPcode) {
        this.reprPcode = reprPcode;
    }

    public String getReprAddressDetails() {
        return reprAddressDetails;
    }

    public void setReprAddressDetails(String reprAddressDetails) {
        this.reprAddressDetails = reprAddressDetails;
    }

    public String getReprPhone() {
        return reprPhone;
    }

    public void setReprPhone(String reprPhone) {
        this.reprPhone = reprPhone;
    }
	public void setArchiveNumber(String archiveNumber) {
		this.archiveNumber = archiveNumber;
	}

	public String getArchiveNumber() {
		return archiveNumber;
	}

	public Integer getPersonalDataUsage() {
		return personalDataUsage;
	}

	public void setPersonalDataUsage(Integer personalDataUsage) {
		this.personalDataUsage = personalDataUsage;
	}

	public Integer getDataAuthentic() {
		return dataAuthentic;
	}

	public void setDataAuthentic(Integer dataAuthentic) {
		this.dataAuthentic = dataAuthentic;
	}

	public Integer getRepresentativeAuthorized() {
		return representativeAuthorized;
	}

	public void setRepresentativeAuthorized(Integer representativeAuthorized) {
		this.representativeAuthorized = representativeAuthorized;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSubmittedDocs() {
		return submittedDocs;
	}

	public void setSubmittedDocs(String submittedDocs) {
		this.submittedDocs = submittedDocs;
	}

	public String getApplicantInfo() {
		return applicantInfo;
	}

	public void setApplicantInfo(String applicantInfo) {
		this.applicantInfo = applicantInfo;
	}

    public Integer getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(Integer responsibleUser) {
        this.responsibleUser = responsibleUser;
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

    public Integer getApplicantCompanyId() {
        return applicantCompanyId;
    }

    public void setApplicantCompanyId(Integer applicantCompanyId) {
        this.applicantCompanyId = applicantCompanyId;
    }

    public String getRepresentativeType() {
        return representativeType;
    }

    public void setRepresentativeType(String representativeType) {
        this.representativeType = representativeType;
    }

    public Short getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(Short typePayment) {
        this.typePayment = typePayment;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Boolean getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Boolean declaration) {
        this.declaration = declaration;
    }

    public String getCourierNameAddress() {
        return courierNameAddress;
    }

    public void setCourierNameAddress(String courierNameAddress) {
        this.courierNameAddress = courierNameAddress;
    }

    public String getOutgoingNumber() {
        return outgoingNumber;
    }

    public void setOutgoingNumber(String outgoingNumber) {
        this.outgoingNumber = outgoingNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public Boolean getIsExpress() {
        return isExpress;
    }

    public void setIsExpress(Boolean isExpress) {
        this.isExpress = isExpress;
    }

    public int getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(int applicantType) {
        this.applicantType = applicantType;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(int applicationType) {
        this.applicationType = applicationType;
    }

    public Integer getDocumentReceiveMethodId() {
        return documentReceiveMethodId;
    }

    public void setDocumentReceiveMethodId(Integer documentReceiveMethodId) {
        this.documentReceiveMethodId = documentReceiveMethodId;
    }

    public Integer getApplicantPersonalIdDocumentType() {
        return applicantPersonalIdDocumentType;
    }

    public void setApplicantPersonalIdDocumentType(Integer applicantPersonalIdDocumentType) {
        this.applicantPersonalIdDocumentType = applicantPersonalIdDocumentType;
    }
}

