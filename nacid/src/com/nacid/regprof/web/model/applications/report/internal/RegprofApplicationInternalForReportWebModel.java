package com.nacid.regprof.web.model.applications.report.internal;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Company;
import com.nacid.bl.applications.Person;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.applications.PersonImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.PersonRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceExaminationRecord;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;
import com.nacid.data.regprof.applications.RegprofAppStatusHistoryForListRecord;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
import com.nacid.regprof.web.model.applications.report.base.RegprofApplicationForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofDocumentRecipientBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseSpecialityForReportBaseWebModel;
import com.nacid.web.model.applications.report.internal.PersonInternalForReportWebModel;

import java.util.List;

//RayaWritten-------------------------------------------------------------
public class RegprofApplicationInternalForReportWebModel extends RegprofApplicationForReportBaseWebModel {    
    private String applicationNumber;
    private String applicationDate; 
    private String archiveNumber;
    private boolean showArchiveNumber;
    private String applicationStatus;
    private String applicationStatusId;
    private String applicationStatusForApplicant;
    private String docflowStatusName;

    private PersonInternalForReportWebModel representative;  
    private boolean showRepresentative;    
    private String repPhone;
    private String repAddrDetails;
    private String repEmail;
    private boolean repFromCompany;
    private String representativeCompany;

    private boolean recognized;
    private String certificateNumber;
    private String recognizedProfession;
    private String recognizedExperience;
    
    private boolean dataAuthentic;

    public RegprofApplicationInternalForReportWebModel(RegprofApplication app, NacidDataProvider nacidDataProvider) {

        super(app.getApplicationDetails(), nacidDataProvider);        
        RegprofApplicationDetails appDetails = app.getApplicationDetails();
        PersonRecord applicant = app.getApplicant();       
        Person applicantP = new PersonImpl(applicant, nacidDataProvider);
        super.applicant = applicant == null ? null : new PersonInternalForReportWebModel(applicantP);
        super.applicantEmail = appDetails.getApplicantEmail();
        super.personalEmailInforming = appDetails.getPersonalEmailInforming()==1 ? true : false;
        RegprofTrainingCourse internalTC = nacidDataProvider.getRegprofTrainingCourseDataProvider()
        .getRegprofTrainingCourse(app.getApplicationDetails().getId());
        super.trainingCourseWebModel = new RegprofTrainingCourseInternalForReportWebModel(internalTC.getDetails(), nacidDataProvider);
        super.professionExperience = new RegprofProfExperienceInternalForReport(internalTC.getExperienceRecord(), nacidDataProvider);
        List<RegprofTrainingCourseSpecialityRecord> specialities = nacidDataProvider.getRegprofTrainingCourseDataProvider()
        .getTrainingCourseSpecialities(internalTC.getDetails().getId());
        for(RegprofTrainingCourseSpecialityRecord sp: specialities){
            super.specialities.add(new RegprofTrainingCourseSpecialityForReportBaseWebModel(sp, nacidDataProvider));
        }
        super.applicantDocument = new PersonDocumentInternalForReportWebModel(app.getApplicantDocuments());
        applicationNumber = appDetails.getAppNum();
        applicationDate = DataConverter.formatDate(appDetails.getAppDate());
        archiveNumber = app.getApplicationDetails().getArchiveNum();
        this.showArchiveNumber = app.getApplicationDetails().getDocflowStatusId() == ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE ? true : false;
        ApplicationStatus status = null;
        if(appDetails.getStatus() != null){
            status = nacidDataProvider.getNomenclaturesDataProvider().getApplicationStatus(NumgeneratorDataProvider.REGPROF_SERIES_ID, appDetails.getStatus());
        }
        this.applicationStatus = status == null ? null : status.getName();
        this.applicationStatusId = app.getApplicationDetails().getStatus() + "";
        RegprofAppStatusHistoryForListRecord finalStatus = nacidDataProvider.getRegprofApplicationDataProvider().getFinalStatusHistoryRecord(app.getId());
        this.applicationStatusForApplicant = finalStatus == null ? "В процес на обработка" : finalStatus.getStatusName();

        this.docflowStatusName = nacidDataProvider.getNomenclaturesDataProvider().getApplicationDocflowStatus(NumgeneratorDataProvider.REGPROF_SERIES_ID, app.getApplicationDetails().getDocflowStatusId()).getName();

        showRepresentative = appDetails.getRepresentativeId() != null ? true : false;
        PersonRecord representative = app.getRepresentative();
        Person representativeP = new PersonImpl(representative, nacidDataProvider);
        this.representative = representative == null ? null : new PersonInternalForReportWebModel(representativeP);
        repPhone = appDetails.getRepPhone();
        repEmail = appDetails.getRepEmail();
        repFromCompany = appDetails.getRepFromCompany() != null ? true : false;       
        Company repCompany = null;
        if(appDetails.getRepFromCompany()!= null){
            repCompany = nacidDataProvider.getCompanyDataProvider().getCompany(appDetails.getRepFromCompany());
        }  
        representativeCompany = repCompany != null ? repCompany.getName() : null;        
        applicantDocument = new PersonDocumentInternalForReportWebModel(app.getApplicantDocuments());

        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofCertificateNumberToAttachedDocRecord certificate = appDp.getCertificateNumber(app.getApplicationDetails().getId(), null);
        if(certificate != null){
            certificateNumber = certificate.getCertificateNumber();
        }
        if(ApplicationStatus.REGPROF_POSITIVE_STATUS_CODES.contains(app.getApplicationDetails().getStatus())){
            recognized = true; 
            RegprofQualificationExamination qualExam = appDp.getRegprofQualificationExaminationForTrainingCourse(
                    app.getApplicationDetails().getTrainingCourseId());
            if(qualExam != null){
                FlatNomenclature recProf = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(
                        NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION, qualExam.getRecognizedProfessionId());
                recognizedProfession = recProf != null ? recProf.getName() : null;

            }
            if(recognizedProfession == null){
                recognizedProfession = professionExperience.getNomenclatureProfessionExperience();
            }
            if(internalTC.getExperienceRecord() != null){
                RegprofProfessionExperienceExaminationRecord expr = nacidDataProvider.getRegprofTrainingCourseDataProvider()
                .getProfessionExperienceExamination(internalTC.getExperienceRecord().getId());
                if(expr != null){
                    recognizedExperience = internalTC.getExperienceRecord().getDays() + (internalTC.getExperienceRecord().getDays()  != 1 ? " дни ": " ден ")
                    +internalTC.getExperienceRecord().getMonths()+(internalTC.getExperienceRecord().getMonths() != 1 ? " месеца " : " месец ")+
                    internalTC.getExperienceRecord().getYears() +(internalTC.getExperienceRecord().getYears() != 1 ? " години" : " година");               
                }
            }
        } else {
            recognized = false;
        }
        
        this.dataAuthentic = appDetails.getDataAuthentic() == 1 ? true : false;
        this.documentRecipient = app.getDocumentRecipient() == null ? null : new RegprofDocumentRecipientBaseWebModel(nacidDataProvider.getNomenclaturesDataProvider(), app.getDocumentRecipient());
    }

    public PersonInternalForReportWebModel getApplicant() {
        return (PersonInternalForReportWebModel)applicant;
    }

    public RegprofTrainingCourseInternalForReportWebModel getTrainingCourseWebModel() {
        return (RegprofTrainingCourseInternalForReportWebModel) trainingCourseWebModel;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getApplicationStatusId() {
        return applicationStatusId;
    }

    public String getApplicationStatusForApplicant() {
        return applicationStatusForApplicant;
    }

    public PersonInternalForReportWebModel getRepresentative() {
        return representative;
    }

    public boolean isShowRepresentative() {
        return showRepresentative;
    }

    public String getRepPhone() {
        return repPhone;
    }

    public String getRepAddrDetails() {
        return repAddrDetails;
    }

    public String getRepEmail() {
        return repEmail;
    }

    public boolean isRepFromCompany() {
        return repFromCompany;
    }

    public String getRepresentativeCompany() {
        return representativeCompany;
    }

    public boolean isRecognized() {
        return recognized;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public String getRecognizedProfession() {
        return recognizedProfession;
    }

    public String getRecognizedExperience() {
        return recognizedExperience;
    }

    public String getArchiveNumber() {
        return archiveNumber;
    }

    public boolean isShowArchiveNumber() {
        return showArchiveNumber;
    }

    public boolean isDataAuthentic() {
        return dataAuthentic;
    }

    public String getDocflowStatusName() {
        return docflowStatusName;
    }
}
//----------------------------------------------------------------------------