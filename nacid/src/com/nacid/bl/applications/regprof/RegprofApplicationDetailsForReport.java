package com.nacid.bl.applications.regprof;

import com.nacid.bl.NacidDataProvider;

public interface RegprofApplicationDetailsForReport {
    
    public String getId();
    public RegprofApplication getApplication();
    public String getApplicationNumber();
    public String getApplicationDate();
    public String getApplicantName();
    public String getApplicantLastName();
    public String getApplicantEmail();
    public String getCertificateNumber();
    public String getTrainingInstitutionName();
    public String getDocumentType();
    public String getDocumentNumber();
    public String getDocumentDate();
    public String getDiplomaSpecialityNames();
    public boolean isMultipleDiplomaSpecialities();
    public NacidDataProvider getNacidDataProvider();
    public String getRecognizedProfession();
    public String getResponsibleUser();
    public String getApplicationCountry();
    public String getProfQualification();
    public String getSecondaryCaliber();
    public String getDocumentSeries();
    public String getDocumentRegNumber();
    public String getProfInstitutionOrgName();
    public String getSecQualificationDegree();
    public String getArticleDirective();
    public String getArticleItem();
    public String getArticleItemQualificationLevelLabel();
    public boolean isNoQualificationExperienceStatus();
    public String getExperiencePeriod();
    public String getSdkTrainingInstitutionName();
    public String getSdkDocumentType();
    public String getSdkDocumentNumber();
    public String getSdkDocumentDate();
    public String getSdkDocumentSeries();
    public String getSdkDocumentRegNumber();
    public String getSdkDiplomaSpecialityNames();
    public boolean isMultipleSdkDiplomaSpecialities();
    public String getSdkProfInstitutionOrgName();
    public String getHighEduLevel();
    public Integer getArticleDirectiveId();
    public String getProfessionExperience();
    public String getProfessionExperienceDocuments();
    public String getLetterDocflowDate();
    public String getApplicantCivilId();
    public String getApplicantCivilIdType();
    public String getApplicantBirthDate();
    public String getApplicantBirthCity();
    public String getCertificateProfQualification();
    public Boolean getHigherEducationWithoutLevel();
    public String getRecognizedQualificationLevel();
    public String getCertificateDate();
    public Integer getEducationTypeId();
    public Integer getHighEduLevelId();
    public boolean isSdk();
    public boolean isQualificationWithExperienceStatus();
    public boolean isRecognizedExperienceStatus();
    
    public int getQualificationArticleDirectiveId();
    public String getQualificationArticleDirective();
    public String getQualificationArticleItem();
    
    /*public int getExperienceArticleDirectiveId();
    public String getExperienceArticleDirective();
    public String getExperienceArticleItem();*/

    public boolean isRecognizedQualificationTeacher();
    public String getAgeRange();
    public String getSchoolType();
    public String getGrade();

    public boolean isRegulatedEducationTraining();

    public String getApplicantBirthCountry();
}
