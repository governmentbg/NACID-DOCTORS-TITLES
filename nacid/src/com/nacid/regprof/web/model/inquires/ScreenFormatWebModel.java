package com.nacid.regprof.web.model.inquires;

import com.nacid.bl.impl.applications.regprof.RegProfApplicationForInquireImpl;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenFormatWebModel {






    public static final String DOCFLOW_NUMBER = "docflowNumber";
	public static final String APPLICANT_NAMES = "applicantNames";
	public static final String CIVIL_ID = "civilId";
	public static final String CITIZENSHIP_NAME = "citizenshipName";
	public static final String ELECTRONICALLY_APPLIED = "electronicallyApplied";
	public static final String ESIGNED = "esigned";
	public static final String SERVICE_TYPE = "serviceType";
	public static final String APPLICATION_COUNTRY = "applicationCountry";
	public static final String STATUS = "status";
	public static final String DOCFLOW_STATUS = "docflowStatus";
	public static final String CERTIFICATE_QUALIFICATION = "certificateQualification";
	public static final String PROFESSIONAL_INSTITUTION_NAME = "professionalInstitutionName";
	public static final String PROFESSIONAL_INSTITUTION_FORMER_NAME = "professionalInstitutionFormerName";
	public static final String DOCUMENT_DATE = "documentDate";
	public static final String EDUCATION_TYPE = "educationType";
	public static final String PROFESSIONAL_QUALIFICATION = "professionalQualification";
	public static final String SPECIALITIES = "specialities";

	public static final String PROFESSION_EXPERIENCE_NAME = "professionExperienceName";
	public static final String PROFESSION_EXPERIENCE_DURATION = "professionExperienceDuration";
    public static final String PROFESSION_EXPERIENCE_DOCUMENTS = "professionExperienceDocuments";
    public static final String RECOGNIZED_QUALIFICATION_DEGREE = "recognizedQualificationDegree";
	public static final String RECOGNIZED_EDUCATION_LEVEL = "recognizedEducationLevel";

	public static final String RECOGNIZED_PROFESSION = "recognizedProfession";
	public static final String RECOGNIZED_ARTICLE = "recognizedArticle";
	public static final String IMI_CORRESPONDENCE = "imiCorrespondence";


	public static final Map<String, Boolean> screen1Elements = new HashMap<String, Boolean>();
	static {
        screen1Elements.put(APPLICANT_NAMES, true);
        screen1Elements.put(DOCFLOW_NUMBER, true);
		screen1Elements.put(ELECTRONICALLY_APPLIED, true);
		screen1Elements.put(SERVICE_TYPE, true);
		screen1Elements.put(STATUS, true);
		screen1Elements.put(DOCFLOW_STATUS, true);
		screen1Elements.put(CITIZENSHIP_NAME, true);
		screen1Elements.put(APPLICATION_COUNTRY, true);
	}
	public static final Map<String, Boolean> screen2Elements = new HashMap<String, Boolean>();
	static {
		screen2Elements.putAll(screen1Elements);
		screen2Elements.put(PROFESSIONAL_INSTITUTION_NAME, true);
		screen2Elements.put(PROFESSIONAL_INSTITUTION_FORMER_NAME, true);
        screen2Elements.put(DOCUMENT_DATE, true);
		screen2Elements.put(EDUCATION_TYPE, true);
		screen2Elements.put(PROFESSIONAL_QUALIFICATION, true);
		screen2Elements.put(SPECIALITIES, true);
		screen2Elements.put(CERTIFICATE_QUALIFICATION, true);

	}
	public static final Map<String, Boolean> screen3Elements = new HashMap<String, Boolean>();
	static {
		screen3Elements.putAll(screen2Elements);
		screen3Elements.put(PROFESSION_EXPERIENCE_NAME, true);
		screen3Elements.put(PROFESSION_EXPERIENCE_DURATION, true);
		screen3Elements.put(PROFESSION_EXPERIENCE_DOCUMENTS, true);

		screen3Elements.put(RECOGNIZED_QUALIFICATION_DEGREE, true);
		screen3Elements.put(RECOGNIZED_EDUCATION_LEVEL, true);
		screen3Elements.put(RECOGNIZED_PROFESSION, true);
		screen3Elements.put(RECOGNIZED_ARTICLE, true);

	}
	public static final Map<String, Boolean> screen4Elements = new HashMap<String, Boolean>();
	static {
		screen4Elements.putAll(screen3Elements);
		screen4Elements.put(IMI_CORRESPONDENCE, true);

	}


    private Integer id;
    private String docflowNumber;
    private String applicantDiplomaNames;
    private String applicantNames;
    private String civilId;
    private String citizenshipName;
    private boolean electronicallyApplied;
    private boolean esigned;
    private String serviceType;
    private String applicationCountry;
    private String status;
    private String docflowStatus;
    private String certificateQualification;
    private String professionalInstitutionName;
    private String professionalInstitutionFormerName;
    private String sdkProfessionalInstitutionName;
    private String sdkProfessionalInstitutionFormerName;
    private String documentDate;
    private String educationType;
    private String highProfessionalQualification;
    private String sdkProfessionalQualification;
    private String secondaryProfessionalQualification;
    private String higherSpecialities;
    private String sdkSpecialities;
    private String secondarySpecialities;
    private String professionExperienceName;
    private String professionExperienceDuration;
    private String recognizedQualificationDegree;
    private String recognizedEducationLevel;
    private String recognizedProfession;
    private String recognizedArticle;

    private String imiCorrespondence;

    private boolean hasEducation;
    private boolean hasExperience;
    private Integer educationTypeId;
    protected List<String> professionExperienceDocuments;


	

	/**
	 * @param a
	 * universityName, da se slepq i tazi informaciq kym nego!
	 */
	public ScreenFormatWebModel(RegProfApplicationForInquireImpl a) {

        this.id=a.getId();
        this.docflowNumber=a.getDocflowNumber();
        this.applicantDiplomaNames=a.getApplicantDiplomaNames();
        this.applicantNames=a.getApplicantNames();
        this.civilId=a.getCivilId();
        this.citizenshipName=a.getCitizenshipName();
        this.electronicallyApplied=a.getElectronicallyApplied() == 1;
        this.esigned=a.getEsigned() == 1;
        this.serviceType=a.getServiceType();
        this.applicationCountry=a.getApplicationCountry();
        this.status=a.getStatus();
        this.docflowStatus = a.getDocflowStatus();
        this.certificateQualification=a.getCertificateQualification();
        this.professionalInstitutionName=a.getProfessionalInstitutionName();
        this.professionalInstitutionFormerName=a.getProfessionalInstitutionFormerName();
        this.sdkProfessionalInstitutionName=a.getSdkProfessionalInstitutionName();
        this.sdkProfessionalInstitutionFormerName=a.getSdkProfessionalInstitutionFormerName();
        this.documentDate=a.getDocumentDate();
        this.educationType=a.getEducationType();
        this.highProfessionalQualification=a.getHighProfessionalQualification();
        this.sdkProfessionalQualification=a.getSdkProfessionalQualification();
        this.secondaryProfessionalQualification=a.getSecondaryProfessionalQualification();
        this.higherSpecialities= StringUtils.join(a.getHigherSpecialities(), "; ");
        this.sdkSpecialities=StringUtils.join(a.getSdkSpecialities(), "; ");
        this.secondarySpecialities=StringUtils.join(a.getSecondarySpecialities(), "; ");
        this.professionExperienceName=a.getProfessionExperienceName();
        if (a.getProfessionExperienceYears() != null) {
            this.professionExperienceDuration= a.getProfessionExperienceYears() + " години " + a.getProfessionExperienceMonths() + " месеца " + a.getProfessionExperienceDays() + " дни";
        }
        this.recognizedQualificationDegree=a.getRecognizedQualificationDegree();
        this.recognizedEducationLevel=a.getRecognizedEducationLevel();
        this.recognizedProfession=a.getRecognizedProfession();
        if (a.getRecognizedArticleItem() != null) {
            this.recognizedArticle = a.getRecognizedArticleDirective() + " - " + a.getRecognizedArticleItem();
        }


        this.imiCorrespondence=a.getImiCorrespondence();

        this.educationTypeId = a.getEducationTypeId();
        this.hasEducation = a.getHasEducation() == 1;
        this.hasExperience = a.getHasExperience() == 1;
        this.professionExperienceDocuments = a.getProfessionExperienceDocuments();
		

	}

    public Integer getId() {
        return id;
    }

    public String getDocflowNumber() {
        return docflowNumber;
    }

    public String getApplicantDiplomaNames() {
        return applicantDiplomaNames;
    }

    public String getApplicantNames() {
        return applicantNames;
    }

    public String getCivilId() {
        return civilId;
    }

    public String getCitizenshipName() {
        return citizenshipName;
    }

    public boolean isElectronicallyApplied() {
        return electronicallyApplied;
    }

    public boolean isEsigned() {
        return esigned;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getApplicationCountry() {
        return applicationCountry;
    }

    public String getStatus() {
        return status;
    }

    public String getCertificateQualification() {
        return certificateQualification;
    }

    public String getProfessionalInstitutionName() {
        return professionalInstitutionName;
    }

    public String getProfessionalInstitutionFormerName() {
        return professionalInstitutionFormerName;
    }

    public String getSdkProfessionalInstitutionName() {
        return sdkProfessionalInstitutionName;
    }

    public String getSdkProfessionalInstitutionFormerName() {
        return sdkProfessionalInstitutionFormerName;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public String getEducationType() {
        return educationType;
    }

    public String getHighProfessionalQualification() {
        return highProfessionalQualification;
    }

    public String getSdkProfessionalQualification() {
        return sdkProfessionalQualification;
    }

    public String getSecondaryProfessionalQualification() {
        return secondaryProfessionalQualification;
    }

    public String getHigherSpecialities() {
        return higherSpecialities;
    }

    public String getSdkSpecialities() {
        return sdkSpecialities;
    }

    public String getSecondarySpecialities() {
        return secondarySpecialities;
    }

    public String getProfessionExperienceName() {
        return professionExperienceName;
    }

    public String getProfessionExperienceDuration() {
        return professionExperienceDuration;
    }

    public String getRecognizedQualificationDegree() {
        return recognizedQualificationDegree;
    }

    public String getRecognizedEducationLevel() {
        return recognizedEducationLevel;
    }

    public String getRecognizedProfession() {
        return recognizedProfession;
    }

    public String getRecognizedArticle() {
        return recognizedArticle;
    }

    public String getImiCorrespondence() {
        return imiCorrespondence;
    }

    public boolean isHasEducation() {
        return hasEducation;
    }

    public boolean isHasExperience() {
        return hasExperience;
    }

    public Integer getEducationTypeId() {
        return educationTypeId;
    }
    public boolean isSecondaryEducationType() {
        return educationTypeId != null && EducationType.SECONDARY_EDUCATION_TYPES.contains(educationTypeId);
    }
    public boolean isSdkEducationType() {
        return educationTypeId != null && educationTypeId == EducationType.EDU_TYPE_SDK;
    }
    public boolean isHighEducationType() {
        return educationTypeId != null && educationTypeId == EducationType.EDU_TYPE_HIGH;
    }

    public String getProfessionExperienceDocumentsStr() {
        return StringUtils.join(professionExperienceDocuments, "\n");
    }
    public List<String> getProfessionExperienceDocuments() {
        return professionExperienceDocuments;
    }

    public String getDocflowStatus() {
        return docflowStatus;
    }
}
