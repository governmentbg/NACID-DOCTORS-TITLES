package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.ProfessionExperienceDocumentType;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleItem;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.bl.regprof.ProfessionalInstitution;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.users.regprof.ResponsibleUser;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.bl.utils.regprof.EducationTypeUtils;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.PersonRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceExaminationRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegprofApplicationDetailsForReportImpl implements RegprofApplicationDetailsForReport {

    private RegprofApplication application;
    private NacidDataProvider nacidDataProvider;

    private String applicationNumber;
    private String applicationDate;

    private String applicantName;
    private String applicantLastName;
    private String applicantEmail;
    private String applicantBirthCountry;

    private String trainingInstitutionName;
    private String sdkTrainingInstitutionName;

    private String profInstitutionOrgName;
    private String sdkProfInstitutionOrgName;

    private String documentType;
    private String documentNumber;
    private String documentDate;
    private String documentSeries;
    private String documentRegNumber;

    private String sdkDocumentType;
    private String sdkDocumentNumber;
    private String sdkDocumentDate;
    private String sdkDocumentSeries;
    private String sdkDocumentRegNumber;

    private List<String> diplomaSpecialityNames;
    private boolean multipleDiplomaSpecialities;

    private List<String> sdkDiplomaSpecialityNames;
    private boolean multipleSdkDiplomaSpecialities;

    //RayaWritten---------------------------------
    private String recognizedProfession;
    private String recognizedQualificationLevel;
    private String responsibleUser;
    private String applicationCountry;
    private String profQualification;
    private String sdkProfQualification;
    private String certificateProfQualification;
    private String secondaryCaliber;
    private String highEduLevel;
    private Boolean higherEducationWithoutLevel;
    private String secQualificationDegree;
    private String articleDirective = "";
    private String articleItem = "";
    private String articleItemQualificationLevelLabel;
    private boolean noQualificationExperienceStatus;
    private String experiencePeriod;
    private Integer articleDirectiveId = 0;
    private boolean multipleExprDocuments;
    private boolean documentsFromExperience;

    private String exprArticleDirective = "";
    private String exprArticleItem = "";
    private Integer exprArticleDirectiveId = 0;
    private String applicantDocumentName = "";
    private Boolean partOfProfession = false;
    //--------------------------------------------

    private String professionExperience;
    private String professionExperienceDocuments;

    private String applicantCivilId;
    private String applicantCivilIdType;
    private String applicantBirthDate;
    private String applicantBirthCity;

    private String certificateNumber;
    private String certificateDate;

    private Integer educationTypeId;
    private Integer highEduLevelId;

    private boolean sdk;
    private int status;
    
    private String qualificationArticleDirective;
    private String qualificationArticleItem;
    private Integer qualificationArticleDirectiveId;

    private boolean recognizedQualificationTeacher;
    private String ageRange;
    private String schoolType;
    private String grade;
    private boolean regulatedEducationTraining;

    public RegprofApplicationDetailsForReportImpl(NacidDataProvider nacidDataProvider, RegprofApplication application) {

        this.nacidDataProvider = nacidDataProvider;
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();

        this.application = application;
        this.applicationNumber = application.getApplicationNumber();
        this.applicationDate = DataConverter.formatDate(application.getApplicationDate());

        PersonRecord applicant = application.getApplicant();
        this.applicantName = applicant.getFullName();
        this.applicantLastName = applicant.getLName();
        this.applicantEmail = application.getEmail();
        this.applicantCivilId = applicant.getCivilId() + "";
        Integer civilIdTypeId = applicant.getCivilIdType();
        FlatNomenclature civilIdType = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, civilIdTypeId);
        this.applicantCivilIdType = civilIdTypeId == CivilIdType.CIVIL_ID_TYPE_PERSONAL_DOCUMENT ? "ЛД" : civilIdType.getName();
        this.applicantBirthDate = DataConverter.formatDate(applicant.getBirthDate());
        this.applicantBirthCity = applicant.getBirthCity() != null ? applicant.getBirthCity() : "";
        this.applicantBirthCountry = applicant.getBirthCountryId() != null ? nDP.getCountry(applicant.getBirthCountryId()).getName() : "";

        this.higherEducationWithoutLevel = null;

        RegprofTrainingCourseDataProvider tcDP = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        ProfessionalInstitutionDataProvider pidp = nacidDataProvider.getProfessionalInstitutionDataProvider();

        /*RegprofCertificateNumberToAttachedDocRecord certNumberRecord = nacidDataProvider.getRegprofApplicationDataProvider().getCertificateNumber(application.getId(), 0);
        if (certNumberRecord != null) {
            this.certificateNumber = certNumberRecord.getCertificateNumber();
            this.certificateDate = certificateNumber.substring(certificateNumber.length() - 10, certificateNumber.length());
        } else {
            this.certificateNumber = "";
            this.certificateDate = "";
        }*/

        RegprofTrainingCourse trainingCourse = tcDP.getRegprofTrainingCourse(application.getId());
        RegprofTrainingCourseDetailsBase details = application.getTrCourseDocumentPersonDetails();
        if(details.getDocumentFname() != null && !details.getDocumentFname().equals("") && details.getDocumentLname() != null && !details.getDocumentLname().equals("")){
            applicantDocumentName = details.getDocumentFname()+" "+ (details.getDocumentSname() != null ? details.getDocumentSname()+" " :"")+
            details.getDocumentLname();
        }
        Integer educationType = details.getEducationTypeId();
        Integer trainingInstitutionId;
        ProfessionalInstitution institution;
        if (educationType != null) {
            this.educationTypeId = details.getEducationTypeId();
            if (educationType == EducationTypeUtils.SDK_EDUCATION_TYPE) {
                trainingInstitutionId = details.getSdkProfInstitutionId();
                institution = pidp.getProfessionalInstitution(trainingInstitutionId);
                this.sdkTrainingInstitutionName = institution.getBgName();
                this.sdkProfInstitutionOrgName = pidp.getProfessionalInstitutionFormerName(details.getSdkProfInstitutionOrgNameId());
                FlatNomenclature eduDocType = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, details.getSdkDocumentType());
                this.sdkDocumentType = eduDocType.getName();
                this.sdkDocumentNumber = (details.getSdkDocumentNumber() == null || details.getSdkDocumentNumber().isEmpty()) ? "" : "№ " + details.getSdkDocumentNumber();
                this.sdkDocumentDate = (details.getSdkDocumentDate() == null || details.getSdkDocumentDate().isEmpty()) ? "" : "" + details.getSdkDocumentDate();
                this.sdkDocumentRegNumber = (details.getSdkDocumentRegNumber() == null || details.getSdkDocumentRegNumber().isEmpty()) ? "" : "Рег. № " + details.getSdkDocumentRegNumber();
                this.sdkDocumentSeries = (details.getSdkDocumentSeries() == null || details.getSdkDocumentSeries().isEmpty()) ? "" : "Серия: " + details.getSdkDocumentSeries();

                FlatNomenclature eduLevel = null;
                if(details.getHighEduLevelId() != null){
                    eduLevel = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, details.getHighEduLevelId());
                }
                this.highEduLevel = eduLevel != null ? eduLevel.getName() : "";
                this.highEduLevelId = details.getHighEduLevelId();

                FlatNomenclature sdkPQ = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getSdkProfQualificationId());
                this.sdkProfQualification = sdkPQ != null ? sdkPQ.getName() : "";

                FlatNomenclature hghPQ = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getHighProfQualificationId());
                this.profQualification = hghPQ != null ? hghPQ.getName() : "";

                this.sdkDiplomaSpecialityNames = new ArrayList<String>();
                this.sdk = true;
                if (details.getHighEduLevelId() != null && details.getHighEduLevelId()== EducationLevel.EDUCATION_LEVEL_VO_NO_LEVEL) {
                    this.higherEducationWithoutLevel = true;
                } else {
                    this.higherEducationWithoutLevel = false;
                }

            } else if (educationType == EducationTypeUtils.SECONDARY_EDUCATION_TYPE || educationType == EducationTypeUtils.SECONDARY_PROFESSIONAL_EDUCATION_TYPE) {
                if (details.getSecCaliberId() != null) {
                    FlatNomenclature secCaliber = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_SECONDARY_CALIBER, details.getSecCaliberId());
                    this.secondaryCaliber = secCaliber != null ? secCaliber.getName() : "";
                } else {
                    this.secondaryCaliber = "";
                }
                SecondaryProfessionalQualification secPQ = nDP.getSecondaryProfessionalQualification(details.getSecProfQualificationId());
                this.profQualification = secPQ != null ? secPQ.getName() : "";
                this.sdk = false;
                if(educationType == EducationTypeUtils.SECONDARY_EDUCATION_TYPE){
                    if(profQualification.contains("част от професията")){
                        this.partOfProfession = true;
                    }
                }

            } else if (educationType == EducationTypeUtils.HIGHER_EDUCATION_TYPE) {
                FlatNomenclature hghPQ = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, details.getHighProfQualificationId());
                this.profQualification = hghPQ != null ? hghPQ.getName() : "";
                FlatNomenclature eduLevel = null;
                if(details.getHighEduLevelId() != null){
                    eduLevel = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, details.getHighEduLevelId());
                }
                this.highEduLevel = eduLevel != null ? eduLevel.getName() : "";
                if (details.getHighEduLevelId() != null && details.getHighEduLevelId() == EducationLevel.EDUCATION_LEVEL_VO_NO_LEVEL) {
                    this.higherEducationWithoutLevel = true;
                } else {
                    this.higherEducationWithoutLevel = false;
                }
                this.sdk = false;
            }
        }

        if (details.getHasExperience() == 1) {
            RegprofProfessionExperienceRecord profExperience = trainingCourse.getExperienceRecord();
            Integer professionExperienceId = profExperience.getNomenclatureProfessionExperienceId();
            FlatNomenclature professionExperience = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, professionExperienceId);
            this.professionExperience = professionExperience.getName();
            this.professionExperienceDocuments = "";
            List<? extends RegprofProfessionExperienceDocumentRecord> experienceDocuments = profExperience.getProfessionExperienceDocuments();
            int iter = 1;
            for (RegprofProfessionExperienceDocumentRecord document : experienceDocuments) {
                int docTypeId = document.getProfExperienceDocTypeId();
                ProfessionExperienceDocumentType docType = nDP.getProfessionExperienceDocumentType(docTypeId);
                this.professionExperienceDocuments += docType.getName() + ", ";
                this.professionExperienceDocuments += (document.getDocumentNumber() == null || document.getDocumentNumber().isEmpty()) ? "" : "№ " + document.getDocumentNumber() + ", ";
                //String date = DataConverter.formatDate(document.getDocumentDate());
                //this.professionExperienceDocuments +=  date.isEmpty() ? "" : date + " г., ";
                this.professionExperienceDocuments += (document.getDocumentDate() == null || document.getDocumentDate().isEmpty()) ? "" : "от дата " + document.getDocumentDate().trim() + ", ";
                this.professionExperienceDocuments += "издадена от " + document.getDocumentIssuer().trim();
                if (iter != experienceDocuments.size()) {
                    this.professionExperienceDocuments += "; ";
                } else {
                    this.professionExperienceDocuments += ". ";
                }
                iter++;
            }
        }

        if (details.getHasEducation() == 1) {
            trainingInstitutionId = details.getProfInstitutionId();
            institution = pidp.getProfessionalInstitution(trainingInstitutionId);
            this.trainingInstitutionName = institution.getBgName();
            FlatNomenclature eduDocType = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, details.getDocumentType());
            this.documentType = eduDocType.getName();
            this.documentNumber = (details.getDocumentNumber() == null || details.getDocumentNumber().isEmpty()) ? "" : "№ " + details.getDocumentNumber();
            this.documentDate = (details.getDocumentDate() == null || details.getDocumentDate().isEmpty()) ? "" : "" + details.getDocumentDate();
            this.documentRegNumber = (details.getDocumentRegNumber() == null || details.getDocumentRegNumber().isEmpty()) ? "" : "Рег. № " + details.getDocumentRegNumber();
            this.documentSeries = (details.getDocumentSeries() == null || details.getDocumentSeries().isEmpty()) ? "" : "серия: " + details.getDocumentSeries();

            this.profInstitutionOrgName = pidp.getProfessionalInstitutionFormerName(details.getProfInstitutionOrgNameId());

            //FlatNomenclature documentTypeNom = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, details.getDocumentType());
            //this.documentType = documentTypeNom != null ? documentTypeNom.getName() : "";

            this.diplomaSpecialityNames = new ArrayList<String>();
            List<RegprofTrainingCourseSpecialityRecord> specialities = tcDP.getTrainingCourseSpecialities(details.getId());
            Integer qualificationDegree = 0;
            String qualificationDegreeName = "";
            for (RegprofTrainingCourseSpecialityRecord speciality : specialities) {
                if (speciality.getSecondarySpecialityId() != null) {
                    SecondarySpeciality secondarySpeciality = nDP.getSecondarySpeciality(speciality.getSecondarySpecialityId());
                    this.diplomaSpecialityNames.add(secondarySpeciality.getName());
                    if (secondarySpeciality.getQualificationDegreeId() != null && qualificationDegree < secondarySpeciality.getQualificationDegreeId()) {
                        qualificationDegree = secondarySpeciality.getQualificationDegreeId();
                        qualificationDegreeName = secondarySpeciality.getQualificationDegreeName();
                    }
                } else if (speciality.getHigherSpecialityId() != null) {
                    FlatNomenclature higherSpeciality = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, speciality.getHigherSpecialityId());
                    this.diplomaSpecialityNames.add(higherSpeciality.getName());
                } else if (speciality.getSdkSpecialityId() != null) {
                    FlatNomenclature sdkSpeciality = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, speciality.getSdkSpecialityId());
                    this.sdkDiplomaSpecialityNames.add(sdkSpeciality.getName());
                }
            }
            this.secQualificationDegree = qualificationDegreeName;
            this.multipleDiplomaSpecialities = this.diplomaSpecialityNames.size() > 1;
            if (educationType == EducationTypeUtils.SDK_EDUCATION_TYPE) {
                this.multipleSdkDiplomaSpecialities = this.sdkDiplomaSpecialityNames.size() > 1;
            }
        }

        Country appCountry = nDP.getCountry(application.getApplicationDetails().getApplicationCountryId());
        this.applicationCountry = appCountry != null ? appCountry.getOfficialName() : "";
        Integer certificateProfQualificationId = trainingCourse.getDetails().getCertificateProfQualificationId();
        FlatNomenclature certificateQualFlat = null;
        if (certificateProfQualificationId != null) {
            certificateQualFlat = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, certificateProfQualificationId);
        }
        this.certificateProfQualification = certificateQualFlat != null ? certificateQualFlat.getName() : "";
        RegprofApplicationDataProvider applicationDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofQualificationExamination qualExamination = applicationDataProvider.getRegprofQualificationExaminationForTrainingCourse(details.getId());
        //nacidDataProvider.getRegprofTrainingCourseDataProvider().getProfessionExperienceExamination(trainingCourse.getExperienceRecord().getId());

        RegprofProfessionExperienceExaminationRecord experienceExamination = null;
        if(trainingCourse.getExperienceRecord() != null){
            experienceExamination = tcDP.getProfessionExperienceExamination(trainingCourse.getExperienceRecord().getId());
        }

        if(experienceExamination != null && experienceExamination.getArticleItemId() != null){
            int professionExperienceId = trainingCourse.getExperienceRecord().getNomenclatureProfessionExperienceId();
            FlatNomenclature profession = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, professionExperienceId);
            RegprofArticleItem item = nDP.getRegprofArticleItem(experienceExamination.getArticleItemId());
            FlatNomenclature article = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, item.getArticleDirectiveId());
            this.recognizedProfession = profession != null ? profession.getName() : "";
            this.exprArticleDirective = article != null ? article.getName() : "";
            this.exprArticleDirectiveId = article != null ? article.getId() : 0;
            this.exprArticleItem = item != null ? item.getName() : "";
        }
        if(qualExamination != null){
            FlatNomenclature profession = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION, qualExamination.getRecognizedProfessionId());
            RegprofArticleItem item = nDP.getRegprofArticleItem(qualExamination.getArticleItemId());
            FlatNomenclature article = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, item.getArticleDirectiveId());
            this.recognizedProfession = profession != null ? profession.getName() : "";
            this.qualificationArticleDirective = this.articleDirective = article != null ? article.getName() : "";
            this.qualificationArticleDirectiveId = this.articleDirectiveId = article != null ? article.getId() : 0;
            this.qualificationArticleItem = this.articleItem = item != null ? item.getName() : "";
            this.articleItemQualificationLevelLabel = item == null ? null : item.getQualificationLevelLabel();
            if (qualExamination.getRecognizedQualificationLevelId() != null) {
                FlatNomenclature qualificationLevel = nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, qualExamination.getRecognizedQualificationLevelId());
                this.recognizedQualificationLevel = qualificationLevel.getName();
            } else {
                this.recognizedQualificationLevel = "";
            }

            recognizedQualificationTeacher = qualExamination.getRecognizedQualificationTeacher() == 1;
            if (recognizedQualificationTeacher) {
                ageRange = qualExamination.getAgeRange() == null ? null : nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_AGE_RANGE, qualExamination.getAgeRange()).getName();
                grade = qualExamination.getGrade() == null ? null : nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADE, qualExamination.getGrade()).getName();
                schoolType = qualExamination.getSchoolType() == null ? null : nDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_SCHOOL_TYPE, qualExamination.getSchoolType()).getName();
            }

        } 

        if (trainingCourse.getExperienceRecord() != null && (
                application.getApplicationStatusId() == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE || trainingCourse.getDetails().getHasEducation() == 0)) {

            RegprofProfessionExperienceRecord exprRecord = trainingCourse.getExperienceRecord();
            List<? extends RegprofProfessionExperienceDocumentRecord> documents = exprRecord.getProfessionExperienceDocuments();
            if(documents.size() > 1){
                multipleExprDocuments = true;
            }
            documentsFromExperience = true;

        } 
        List<ResponsibleUser> respUsers = application.getResponsibleUsers();
        this.responsibleUser = "";
        if (respUsers != null && respUsers.size() > 0) {
            for (ResponsibleUser u : respUsers) {
                responsibleUser += u.getFullName()+", ";
            }
            responsibleUser = responsibleUser.substring(0, responsibleUser.lastIndexOf(','));
        }
        if (details.getHasExperience() == 1) {
            RegprofProfessionExperienceRecord experience = trainingCourse.getExperienceRecord();
            String y = experience.getYears() != 1 ? " години, " : " година, ";
            String m = experience.getMonths() != 1 ? " месеца, " : " месец, ";
            String d = experience.getDays() != 1 ? " дни" : " ден";
            this.experiencePeriod = experience.getYears() + y + experience.getMonths() + m + experience.getDays() + d;
        }
        
        status = application.getApplicationDetails().getStatus();
        if (status == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE ||
                status == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE) {
            noQualificationExperienceStatus = false; //TODO: ako statusa e "izdadeno udostoverenie" ?
        } else {
            noQualificationExperienceStatus = true;
        }

        this.regulatedEducationTraining = trainingCourse.getDetails().getRegulatedEducationTraining() == 1;

    }

    public boolean isDocumentsFromExperience() {
        return documentsFromExperience;
    }

    public String getId() {
        return application.getId() + "";
    }

    public RegprofApplication getApplication() {
        return application;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantLastName() {
        return applicantLastName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getTrainingInstitutionName() {
        return trainingInstitutionName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public String getDiplomaSpecialityNames() {
        if (diplomaSpecialityNames != null && diplomaSpecialityNames.size() > 0) {
            if (diplomaSpecialityNames.size() == 1) {
                return diplomaSpecialityNames.get(0);
            } else {
                UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
                List<String> lst = new ArrayList<String>();
                for (int i = 0; i < diplomaSpecialityNames.size(); i++) {
                    String recognizedSpecialityId = utilsDataProvider.getCommonVariableValue((i + 1) + "diplomaSpeciality");
                    lst.add(recognizedSpecialityId + ": " + diplomaSpecialityNames.get(i));
                }
                return StringUtils.join(lst, ", ");
            }
        }
        return "";
    }

    public boolean isMultipleDiplomaSpecialities() {
        return multipleDiplomaSpecialities;
    }

    public NacidDataProvider getNacidDataProvider() {
        return nacidDataProvider;
    }

    public String getRecognizedProfession() {
        return recognizedProfession;
    }

    public String getResponsibleUser() {
        return responsibleUser;
    }

    public String getApplicationCountry() {
        return applicationCountry;
    }

    public String getProfQualification() {
        return profQualification;
    }

    public String getSecondaryCaliber() {
        return secondaryCaliber;
    }

    public String getDocumentSeries() {
        return documentSeries;
    }

    public String getDocumentRegNumber() {
        return documentRegNumber;
    }

    public String getProfInstitutionOrgName() {
        return profInstitutionOrgName;
    }

    public String getSecQualificationDegree() {
        return secQualificationDegree;
    }

    public String getArticleDirective() {
        return articleDirective;
    }

    public String getArticleItem() {
        return articleItem;
    }

    public boolean isNoQualificationExperienceStatus() {
        return noQualificationExperienceStatus;
    }

    public String getExperiencePeriod() {
        return experiencePeriod;
    }

    public String getSdkTrainingInstitutionName() {
        return sdkTrainingInstitutionName;
    }

    public String getSdkDocumentType() {
        return sdkDocumentType;
    }

    public String getSdkDocumentNumber() {
        return sdkDocumentNumber;
    }

    public String getSdkDocumentDate() {
        return sdkDocumentDate;
    }

    public String getSdkDocumentSeries() {
        return sdkDocumentSeries;
    }

    public String getSdkDocumentRegNumber() {
        return sdkDocumentRegNumber;
    }

    public String getSdkDiplomaSpecialityNames() {
        if (sdkDiplomaSpecialityNames != null && sdkDiplomaSpecialityNames.size() > 0) {
            if (sdkDiplomaSpecialityNames.size() == 1) {
                return sdkDiplomaSpecialityNames.get(0);
            } else {
                UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
                List<String> lst = new ArrayList<String>();
                for (int i = 0; i < sdkDiplomaSpecialityNames.size(); i++) {
                    String recognizedSpecialityId = utilsDataProvider.getCommonVariableValue((i + 1) + "diplomaSpeciality");
                    lst.add(recognizedSpecialityId + ": " + sdkDiplomaSpecialityNames.get(i));
                }
                return StringUtils.join(lst, ", ");
            }
        }
        return "";
    }

    public boolean isMultipleSdkDiplomaSpecialities() {
        return multipleSdkDiplomaSpecialities;
    }

    public String getSdkProfInstitutionOrgName() {
        return sdkProfInstitutionOrgName;
    }

    public String getHighEduLevel() {
        return highEduLevel;
    }

    public Integer getArticleDirectiveId() {
        return articleDirectiveId;
    }

    public String getProfessionExperience() {
        return professionExperience;
    }

    public String getProfessionExperienceDocuments() {
        return professionExperienceDocuments;
    }

    public String getLetterDocflowDate() {
        List<RegprofApplicationAttachment> attachments = this.nacidDataProvider.getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(
                application.getId(), DocumentType.DOC_TYPE_REGPROF_LETTER_TO_APPLICANT_FOR_INFORMATION);
        if (attachments != null && !attachments.isEmpty()) {
            Date docflowDate = null;
            for (RegprofApplicationAttachment attachment : attachments) {
                if (attachment.getDocflowDate() != null) {
                    docflowDate = attachment.getDocflowDate(); // vzema datata ot posledniq dokument
                }
            }
            if (docflowDate != null) {
                return DataConverter.formatDate(docflowDate);
            }
        }
        return "";
    }

    public String getApplicantCivilId() {
        return applicantCivilId;
    }

    public String getApplicantCivilIdType() {
        return applicantCivilIdType;
    }

    public String getApplicantBirthDate() {
        return applicantBirthDate;
    }

    public String getApplicantBirthCity() {
        return applicantBirthCity;
    }

    public String getCertificateProfQualification() {
        return certificateProfQualification;
    }

    public Boolean getHigherEducationWithoutLevel() {
        return higherEducationWithoutLevel;
    }

    public String getRecognizedQualificationLevel() {
        return recognizedQualificationLevel;
    }

    public String getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(String certificateDate) {
        this.certificateDate = certificateDate;
    }

    public Integer getEducationTypeId() {
        return educationTypeId;
    }

    public void setEducationTypeId(Integer educationTypeId) {
        this.educationTypeId = educationTypeId;
    }

    public Integer getHighEduLevelId() {
        return highEduLevelId;
    }

    public void setHighEduLevelId(Integer highEduLevelId) {
        this.highEduLevelId = highEduLevelId;
    }

    public boolean isSdk() {
        return sdk;
    }

    public void setSdk(boolean isSDK) {
        this.sdk = isSDK;
    }

    public String getSdkProfQualification() {
        return sdkProfQualification;
    }

    public boolean isMultipleExprDocuments() {
        return multipleExprDocuments;
    }

    public String getExprArticleDirective() {
        return exprArticleDirective;
    }

    public String getExprArticleItem() {
        return exprArticleItem;
    }

    public Integer getExprArticleDirectiveId() {
        return exprArticleDirectiveId;
    }

   
    public String getQualificationArticleDirective() {
        return qualificationArticleDirective;
    }

    public int getQualificationArticleDirectiveId() {
        return qualificationArticleDirectiveId;
    }

    public String getQualificationArticleItem() {
        return qualificationArticleItem;
    }

    public boolean isQualificationWithExperienceStatus() {
        return status == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE;
    }

    public boolean isRecognizedExperienceStatus() {
        return status == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE;
    }

    public String getApplicantDocumentName() {
        return applicantDocumentName;
    }

    public int getStatus() {
        return status;
    }

    public Boolean getPartOfProfession() {
        return partOfProfession;
    }

    public boolean isRecognizedQualificationTeacher() {
        return recognizedQualificationTeacher;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public String getGrade() {
        return grade;
    }

    public boolean isRegulatedEducationTraining() {
        return regulatedEducationTraining;
    }

    @Override
    public String getArticleItemQualificationLevelLabel() {
        return articleItemQualificationLevelLabel;
    }

    public String getApplicantBirthCountry() {
        return applicantBirthCountry;
    }
}