package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.comision.CommissionCalendarProtocol;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.external.applications.ExtApplicationImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.*;
import com.nacid.data.external.applications.ExtApplicationRecord;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class ApplicationImpl implements Application {
    private ApplicationRecord record;
    private NacidDataProviderImpl nacidDataProvider;
    //null - not assigned; 1 - finished; 0 - not finished; -1 - no experts assigned
    private Integer isFinishedByexperts;
    private TrainingCourse trainingCourse;
    private Person applicant;
    private ExtApplication extApplication;
    private boolean isExtApplicationGenerated;
    private List<String> invalidatedCertificates;
    private String certificate;
    private boolean isCertificatesFilled;
    private List<ApplicationKind> applicationKinds;
    private boolean documentRecipientRead;
    private DocumentRecipient documentRecipient;
    public ApplicationImpl(NacidDataProviderImpl nacidDataProvider, ApplicationRecord record) {
        this.record = record;
        this.nacidDataProvider = nacidDataProvider;
    }

    public int getId() {
        return record.getId();
    }
    public String getApplicationNumber() {
        return record.getApplicationNumber();
    }

    public Date getApplicationDate() {
        return record.getApplicationDate();
    }
    public String getDocFlowNumber() {
        return getApplicationNumber() + "/" + DataConverter.formatDate(getApplicationDate());
    }
    public Integer getApplicantId() {
        return record.getApplicantId();
    }
    public Person getApplicant() {
        if (applicant == null && record.getApplicantId() != null) {
            synchronized (this) {
                if (applicant == null) {
                    applicant = nacidDataProvider.getApplicationsDataProvider().getPerson(record.getApplicantId());
                }
            }
        }
        return applicant;
    }
    public void setApplicant(PersonRecord personRecord) {
        if (applicant == null) {
            synchronized (this) {
                if (applicant == null) {
                    applicant = new PersonImpl(personRecord, nacidDataProvider);			
                }
            }
        }

    }
    public Person getRepresentative() {
        if (record.getRepresentativeId() == null) {
            return null;
        }
        return nacidDataProvider.getApplicationsDataProvider().getPerson(record.getRepresentativeId());
    }
    public Integer getRepresentativeId() {
        return record.getRepresentativeId();
    }

    public TrainingCourse getTrainingCourse() {
        if (trainingCourse == null) {
            synchronized (this) {
                if (trainingCourse == null) {
                    TrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getTrainingCourseDataProvider();
                    trainingCourse = trainingCourseDataProvider.getTrainingCourse(record.getTrainingCourseId());			
                }
            }
        }
        return trainingCourse;

    }
    public void setTrainingCourse(TrainingCourseRecord trainingCourseRecord) {
        if (trainingCourse == null) {
            synchronized (this) {
                if (trainingCourse == null) {
                    trainingCourse = new TrainingCourseImpl(nacidDataProvider, nacidDataProvider.getTrainingCourseDataProvider(), trainingCourseRecord);
                }
            }

        }

    }

    public int getTrainingCourseId() {
        return record.getTrainingCourseId();
    }

    public String getEmail() {
        return record.getEmail();
    }

    public int getOfficialEmailCommunication() {
        return record.getOfficialEmailCommunication();
    }

    public int getHomeCountryId() {
        return record.getHomeCountryId();
    }
    public Country getHomeCountry() {
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getCountry(record.getHomeCountryId());
    }

    public String getHomeCity() {
        return record.getHomeCity();
    }

    public String getHomePostCode() {
        return record.getHomePostCode();
    }

    public String getHomeAddressDetails() {
        return record.getHomeAddressDetails();
    }

    public boolean homeIsBg() {
        return record.getHomeIsBg() == 1 ? true : false;
    }

    public String getBgCity() {
        return record.getBgCity();
    }

    public String getBgPostCode() {
        return record.getBgPostCode();
    }

    public String getBgAddressDetails() {
        return record.getBgAddressDetails();
    }

    public int getBgAddressOwner() {
        return record.getBgAddressOwner();
    }

    public int getCreatedByUserId() {
        return record.getCreatedByUserId();
    }

    public Date getTimeOfCreation() {
        return record.getTimeOfCreation();
    }

    public int getApplicationStatusId() {
        return record.getApplicationStatusId();
    }
    public FlatNomenclature getApplicationStatus() {
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        return nomenclaturesDataProvider.getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, getApplicationStatusId());
    }

    public Integer getCompanyId() {
        return record.getCompanyId();
    }
    public Company getCompany() {
        if (record.getCompanyId() == null) {
            return null;
        }
        return nacidDataProvider.getCompanyDataProvider().getCompany(record.getCompanyId());
    }

    @Override
    public Integer getApplicantCompanyId() {
        return record.getApplicantCompanyId();
    }

    @Override
    public Company getApplicantCompany() {
        return record.getApplicantCompanyId() == null ? null : nacidDataProvider.getCompanyDataProvider().getCompany(record.getApplicantCompanyId());
    }

    public boolean differentApplicantAndDiplomaNames() {
        return record.getDifferentDiplomaNames() == 1 ? true : false;
    }

    public List<ApplicationRecognitionPurpose> getApplicationRecoginitionPurposes() {
        return nacidDataProvider.getApplicationsDataProvider().getApplicationRecognitionPurposes(getId());
    }
    public boolean allowExpertAssignment() {
        return true;
        /*if (getApplicationType() == ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE) {
            return true;
        }
        if (getApplicationStatusId() >= ApplicationStatus.APPLICATION_AUTHENTIC_STATUS_CODE) {
            return true;
        }
        return false;*/
        /*TrainingCourse trainingCourse = getTrainingCourse();
        DiplomaExamination diplomaExamination = trainingCourse.getDiplomaExamination();
        UniversityValidity universityValidity = trainingCourse.getUniversityValidity();

        if (diplomaExamination == null || universityValidity == null) {
            return false;
        }
        if (diplomaExamination.isRecognized() && universityValidity.isRecognized()) {
            return true;
        }
        return false;*/
    }
    public List<ApplicationExpert> getApplicationExperts() {
        if (allowExpertAssignment()) {
            return nacidDataProvider.getApplicationsDataProvider().getApplicationExperts(getId());
        }
        return null;
    }

    @Override
    public String getBgPhone() {
        return record.getBgPhone();
    }

    @Override
    public String getHomePhone() {
        return record.getHomePhone();
    }

    @Override
    public String getReprAddressDetails() {
        return record.getReprAddressDetails();
    }

    @Override
    public String getReprCity() {
        return record.getReprCity();
    }

    @Override
    public Integer getReprCountryId() {
        return record.getReprCountryId();
    }
    public Country getReprCountry() {
        return record.getReprCountryId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(record.getReprCountryId());
    }

    @Override
    public String getReprPcode() {
        return record.getReprPcode();
    }

    @Override
    public String getReprPhone() {
        return record.getReprPhone();
    }

    @Override
    public String getSummary() {
        return record.getSummary();
    }

    public boolean hasMoreApplicationsWithSameApplicant() {
        return getApplicantId() == null ? false : nacidDataProvider.getApplicationsDataProvider().hasMoreApplicationsByApplicant(getApplicantId(), record.getId());
    }

    @Override
    public String getArchiveNumber() {
        return record.getArchiveNumber();
    }

    @Override
    public Boolean isFinishedByexperts() {
        if(isFinishedByexperts == null) {
            List<ApplicationExpert> exps = getApplicationExperts();
            isFinishedByexperts = -1;
            if(exps != null && !exps.isEmpty()) {
                isFinishedByexperts = 1;
                for(ApplicationExpert ae : exps) {
                    if(ae.getProcessStat() == 0) {
                        isFinishedByexperts = 0;
                        break;
                    }
                }
            }
        }
        return isFinishedByexperts == -1 ? null : (isFinishedByexperts == 1 ? true : false);
    }


    public Boolean isPersonalDataUsage() {
        return record.getPersonalDataUsage() == null ? null : DataConverter.parseBoolean(record.getPersonalDataUsage() + "");
    }

    @Override


    public String getCertificateNumber() {
        if (!isCertificatesFilled) {
            setCertificateNumbers(nacidDataProvider.getApplicationsDataProvider().getAllCertificateNumbers(getId()));
        } 
        return certificate;
    }
    public List<String> getInvalidatedCertificateNumbers() {
        if (!isCertificatesFilled) {
            setCertificateNumbers(nacidDataProvider.getApplicationsDataProvider().getAllCertificateNumbers(getId()));
        } 
        return invalidatedCertificates;
    }
    public Boolean getDataAuthentic() {
        return DataConverter.parseIntegerToBoolean(record.getDataAuthentic());
    }
    public Boolean getRepresentativeAuthorized() {
        return DataConverter.parseIntegerToBoolean(record.getRepresentativeAuthorized());
    }
    public String getNotes() {
        return record.getNotes();
    }

    public boolean isMigrated() {
        if (getApplicationStatusId() == ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE) {
            return false;
        }
        List<AppStatusHistory> appStatusHistory = nacidDataProvider.getApplicationsDataProvider().getAppStatusHistory(getId());
        return appStatusHistory != null && appStatusHistory.size() > 0 && appStatusHistory.get(appStatusHistory.size() - 1).getApplicationStatusId() == ApplicationStatus.APPLICATION_STATUS_MIGRATED;
    }

    public List<AppStatusHistory> getAppStatusHistory() {
        return nacidDataProvider.getApplicationsDataProvider().getAppStatusHistory(getId());
    }
    public List<AppStatusHistory> getCommissionAppStatusHistory() {
        List<AppStatusHistory> statusHistory = getAppStatusHistory();
        List<AppStatusHistory> result = new ArrayList<AppStatusHistory>();
        if (statusHistory != null) {
            Set<Integer> availableStatuses = new HashSet<Integer>(ApplicationStatus.RUDI_APPLICATION_STATUSES_FROM_COMMISSION);
            for (AppStatusHistory s:statusHistory) {
                if (availableStatuses.contains(s.getApplicationStatusId())) {
                    result.add(s);
                }

            }
        }
        return result.size() == 0 ? null : result;
    }

    public List<CommissionCalendar> getCommissionCalendars() {
        return nacidDataProvider.getCommissionCalendarDataProvider().getCommissionCalendarsByApplication(getId());
    }

    public ExtApplication getExtApplication() {
        if (!isExtApplicationGenerated) {
            synchronized (this) {
                if (!isExtApplicationGenerated) {
                    ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
                    extApplication = extApplicationsDataProvider.getApplicationByInternalApplicationId(getId());
                    isExtApplicationGenerated = true;
                }
            }
        }
        return extApplication;

    }
    public synchronized void setExtApplication(ExtApplicationRecord record) {
        isExtApplicationGenerated = true;
        extApplication = record == null ? null : new ExtApplicationImpl(nacidDataProvider, record);
    }
    public String getApplicantInfo() {
        return record.getApplicantInfo();
    }
    public String getSubmittedDocs() {
        return record.getSubmittedDocs();
    }
    public boolean isRecognizedAndContainsCommissionProtocol() {
        List<AppStatusHistory> historyRecords = getAppStatusHistory();
        boolean result = false;
        for (AppStatusHistory r:historyRecords) {
            if (r.getApplicationStatusId() == ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE) {
                result = true;
                break;
            }
            if (r.getApplicationStatusId() == ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE) {
                break;
            }
        }
        if (!result) {
            return result;
        }

        CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
        CommissionCalendar cal = commissionCalendarDataProvider.getLastCommissionExaminedApplication(getId());
        if (cal == null) {
            return false;
        }
        CommissionCalendarProtocol protocol = commissionCalendarDataProvider.loadCalendarProtocol(cal.getId(), false);
        return protocol != null && !StringUtils.isEmpty(protocol.getFileName());


    }
    public FlatNomenclature getFinalAppStatus() {
        return record.getFinalStatusHistoryId() == null ? null : nacidDataProvider.getApplicationsDataProvider().getApplicationStatusByAppStatusHistoryId(record.getFinalStatusHistoryId());
    }

    public List<String> getEmployeesWorkedOnThisApplication() {
        return nacidDataProvider.getApplicationsDataProvider().getEmployeesWorkedOnApplication(getId());
    }


    public Integer getResponsibleUserId() {
        return record.getResponsibleUser();
    }

    public void setCertificateNumbers(List<CertificateNumberToAttachedDocRecord> certs) {
        if (certs != null) {
            for (CertificateNumberToAttachedDocRecord cn:certs) {
                if (cn.getInvalidated() == 1) {
                    if (invalidatedCertificates == null) {
                        invalidatedCertificates = new ArrayList<String>();
                    }
                    invalidatedCertificates.add(cn.getCertificateNumber());
                } else if (cn.getInvalidated() == 0) {
                    certificate = cn.getCertificateNumber();
                }
            }    
        }
        
        isCertificatesFilled = true;    
    }

    @Override
    public int getApplicationDocflowStatusId() {
        return record.getDocflowStatusId();
    }

    @Override
    public FlatNomenclature getApplicationDocflowStatus() {
        return nacidDataProvider.getNomenclaturesDataProvider().getApplicationDocflowStatus(NumgeneratorDataProvider.NACID_SERIES_ID, getApplicationDocflowStatusId());
    }


    @Override
    public String getRepresentativeType() {
        return record.getRepresentativeType();
    }

    @Override
    public Short getTypePayment() {
        return record.getTypePayment();
    }

    @Override
    public Integer getDeliveryType() {
        return record.getDeliveryType();
    }

    @Override
    public Boolean getDeclaration() {
        return record.getDeclaration();
    }

    @Override
    public String getCourierNameAddress() {
        return record.getCourierNameAddress();
    }

    @Override
    public String getOutgoingNumber() {
        return record.getOutgoingNumber();
    }

    @Override
    public String getInternalNumber() {
        return record.getInternalNumber();
    }

    @Override
    public Boolean getIsExpress() {
        return record.getIsExpress();
    }

    @Override
    public int getApplicantType() {
        return record.getApplicantType();
    }

    public com.nacid.bl.nomenclatures.FlatNomenclature getPaymentType() {
        return getTypePayment() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PAYMENT_TYPE, getTypePayment().intValue());
    }

    @Override
    public List<ApplicationKind> getApplicationKinds() {
        if (applicationKinds == null) {
            applicationKinds = nacidDataProvider.getApplicationsDataProvider().getApplicationKindsPerApplication(getId());
        }
        return applicationKinds;
    }

    @Override
    public int getApplicationType() {
        return record.getApplicationType();
    }

    @Override
    public String getApplicantNames() {
        if (getApplicant() != null) {
            return getApplicant().getFullName();
        } else {
            return getApplicantCompany().getName();
        }
    }

    @Override
    public Integer getDocumentReceiveMethodId() {
        return record.getDocumentReceiveMethodId();
    }

    @Override
    public DocumentReceiveMethod getDocumentReceiveMethod() {
        return record.getDocumentReceiveMethodId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod( record.getDocumentReceiveMethodId());
    }


    @Override
    public DocumentRecipient getDocumentRecipient() {
        if (!documentRecipientRead) {
            documentRecipient = nacidDataProvider.getApplicationsDataProvider().getDocumentRecipient(record.getId());
            documentRecipientRead = true;
        }
        return documentRecipient;
    }

    @Override
    public Integer getApplicantPersonalIdDocumentTypeId() {
        return record.getApplicantPersonalIdDocumentType();
    }
    public FlatNomenclature getApplicantPersonalIdDocumentType() {
        return record.getApplicantPersonalIdDocumentType() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, record.getApplicantPersonalIdDocumentType());
    }
}
