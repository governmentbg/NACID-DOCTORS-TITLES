/**
 * 
 */
package com.nacid.report.test.bean;

import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;

import java.util.Collection;

/**
 * @author bily
 *
 */
public class Application implements ApplicationDetailsForReport{
	
	private String date;
	private String applicationNumber;
	private String applicantName;
	private String civilId;
	private String specialityName;
	private String trainingCourseLocation;
	private String commissionSessionDate;
	private String addressDetailsBulgaria;
	private String cityDetailsBulgaria;
	private String applicantLastName;
	private String nacidDirectorName;
	private String diplomaSpecialityName;
	private String diplomaEducationLevel;
	private String acknowledgedEducationLevel;
	private String acknowledgedSpecialityName;
	private String birthPlace;
	private String citizenship;
	private String trainingCourseQualification;
	private String previousEducation;
	private String diplomaDate;
	private String certificateNumber = "123-12/12.12.2009";
	
	
	private Collection collTest; 
	
	
	public String getOwnerBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public String getOwnerCitizenship() {
		return citizenship;
	}
	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}
	public String getTrainingCourseQualification() {
		return trainingCourseQualification;
	}
	public void setTrainingCourseQualification(String trainingCourseQualification) {
		this.trainingCourseQualification = trainingCourseQualification;
	}
	
	
	
	
	public String getNacidDirectorName() {
		return nacidDirectorName;
	}
	public void setNacidDirectorName(String nacidDirectorName) {
		this.nacidDirectorName = nacidDirectorName;
	}
	
	
	public Collection getCollTest() {
		return collTest;
	}
	public void setCollTest(Collection collTest) {
		this.collTest = collTest;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getOwnerName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getOwnerCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	
	public String getCommissionSessionDate() {
		return commissionSessionDate;
	}
	public void setCommissionSessionDate(String commissionSessionDate) {
		this.commissionSessionDate = commissionSessionDate;
	}
	public String getAddressDetailsBulgaria() {
		return addressDetailsBulgaria;
	}
	public void setAddressDetailsBulgaria(String addressDetailsBulgaria) {
		this.addressDetailsBulgaria = addressDetailsBulgaria;
	}
	public String getCityDetailsBulgaria() {
		return cityDetailsBulgaria;
	}
	public void setCityDetailsBulgaria(String cityDetailsBulgaria) {
		this.cityDetailsBulgaria = cityDetailsBulgaria;
	}
	public String getOwnerLastName() {
		return applicantLastName;
	}
	public void setApplicantLastName(String applicantLastName) {
		this.applicantLastName = applicantLastName;
	}
	public String getTrainingCourseLocation() {
		return trainingCourseLocation;
	}
	public void setTrainingCourseLocation(String trainingCourseLocation) {
		this.trainingCourseLocation = trainingCourseLocation;
	}
	public String getDiplomaSpecialityNames() {
		return diplomaSpecialityName;
	}
	public void setDiplomaSpecialityNames(String diplomaSpecialityName) {
		this.diplomaSpecialityName = diplomaSpecialityName;
	}
	public String getDiplomaEducationLevel() {
		return diplomaEducationLevel;
	}

	@Override
	public Integer getDiplomaEducationLevelId() {
		return null;
	}

	public void setDiplomaEducationLevel(String diplomaEducationLevel) {
		this.diplomaEducationLevel = diplomaEducationLevel;
	}
	public String getRecognizedEducationLevel() {
		return acknowledgedEducationLevel;
	}
	public void setAcknowledgedEducationLevel(String acknowledgedEducationLevel) {
		this.acknowledgedEducationLevel = acknowledgedEducationLevel;
	}
	public String getRecognizedSpecialityName() {
		return acknowledgedSpecialityName;
	}
	public void setAcknowledgedSpecialityName(String acknowledgedSpecialityName) {
		this.acknowledgedSpecialityName = acknowledgedSpecialityName;
	}
	public String getPreviousEducation() {
		return previousEducation;
	}
	public void setPreviousEducation(String previousEducation) {
		this.previousEducation = previousEducation;
	}
	@Override
	public String getOwnerAddressDetails() {
		
		return null;
	}
	@Override
	public String getApplicationStatus() {

		return null;
	}

	@Override
	public String getDocflowStatus() {
		return null;
	}

	@Override
	public String getOwnerBirthDate() {

		return null;
	}
	@Override
	public String getDiplomaNumber() {

		return null;
	}
	@Override
	public String getTrainingDuration() {

		return null;
	}
	@Override
	public String getMotives() {

		return null;
	}
	public String getDiplomaDate() {
		return diplomaDate;
	}
	public String getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
    @Override
    public String getRecognizedQualification() {
        return null;
    }
	@Override
	public String getRecognizedSpecialityNameForCertificate() {
		return null;
	}
	@Override
	public String getAllUniversitiesNamesAndLocations() {
		return null;
	}

    @Override
    public String getAllUniversitiesBgNames() {
        return null;
    }

    @Override
    public String getAllUniversitiesFullNames() {
        return null;
    }

    @Override
	public String getAllUniversitiesNamesAndLocationsForCertificate() {
		return null;
	}
	@Override
	public String getTrainingCountries() {
		return null;
	}
	@Override
	public String getTrainingCountriesAndLocations() {
		return null;
	}
	@Override
	public String getAllUniversitiesNames() {
		return null;
	}
	@Override
	public String getCountryPublishedDiploma() {
		return null;
	}
	@Override
	public String getOriginalUniversitiesNameCityCountry() {
		return null;
	}
	@Override
	public boolean isJointDegree() {
		return false;
	}
	@Override
	public String getOwnerBirthCountry() {
		return null;
	}
	public String getDocFlowNumber() {
		return "44-00-1234";
	}
	
	public String getToday() {
		return DataConverter.formatDate(Utils.getToday());
	}
	public String getOwnerDiplomaName() {
		return null;
	}
	public String getCommissionCalendars() {
		return null;
	}
	public String getCommissionStatuses() {
		return null;
	}
	public String getAllUniversitiesCountries() {
		return null;
	}
	@Override
	public String getExpertNames() {
		return null;
	}
	@Override
	public String getLastCommissionCalendarInfo() {
		return null;
	}
	@Override
	public String getDiplomaName() {

		return null;
	}
	@Override
	public String getApplicantInfo() {

		return null;
	}
	@Override
	public String getNotes() {

		return null;
	}
	@Override
	public String getPreviousDiplomaNotes() {

		return null;
	}
	@Override
	public String getFirstExpertName() {

		return null;
	}
	@Override
	public String getId() {

		return null;
	}
	@Override
	public com.nacid.bl.applications.Application getApplication() {

		return null;
	}
	@Override
	public String getOwnerNameForProtocol() {

		return null;
	}
	@Override
	public String getAllNotes() {
		return null;
	}
	@Override
	public String getTrainingYears() {
		return null;
	}
    @Override
    public boolean isMultipleDiplomaSpecialities() {
        return false;
    }

    @Override
    public String getTrainingStart() {
        return null;
    }

    @Override
    public String getTrainingEnd() {
        return null;
    }

    @Override
    public String getTrainingForm() {
        return null;
    }

    @Override
    public String getGraduationWay() {
        return null;
    }

    @Override
    public String getDiplomaOriginalEducationLevel() {
        return null;
    }

    @Override
    public String getDiplomaSpecialitiesForExpertPosition() {
        return null;
    }

    @Override
    public String getBolognaCycle() {
        return null;
    }

    @Override
    public String getEuropeanQualificationsFramework() {
        return null;
    }

    @Override
    public String getNationalQualificationsFramework() {
        return null;
    }

    @Override
    public String getBolognaCycleAccess() {
        return null;
    }

    @Override
    public String getEuropeanQualificationsFrameworkAccess() {
        return null;
    }

    @Override
    public String getNationalQualificationsFrameworkAccess() {
        return null;
    }

    @Override
    public boolean isUniversityAuthorized() {
        return false;
    }

    @Override
    public boolean isLegitimateProgram() {
        return false;
    }

    @Override
    public String getUniversityNamesForExpertPosition() {
        return null;
    }

    @Override
    public String getGraduationDocumentType() {
        return null;
    }

    @Override
    public String getCreditHours() {
        return null;
    }

    @Override
    public String getUniversitiesForExpertPosition() {
        return null;
    }

	@Override
	public String getFacultiesForExpertPosition() {
		return null;
	}

	@Override
    public String getTrainingInstitutionForExpertPosition() {
        return null;
    }

    @Override
    public String getCredits() {
        return null;
    }

    @Override
    public String getAllUniversityCountriesSeparatedByNewLine() {
        return null;
    }

    @Override
    public String getOutgoingNumber() {
        return null;
    }

    @Override
    public String getInternalNumber() {
        return null;
    }

    @Override
    public String getStatuteAuthenticityRecommendationLetterNumber() {
        return null;
    }

    @Override
    public String getTrainingInstitution() {
        return null;
    }

    @Override
    public String getCompetentInstitutionName() {
        return null;
    }

	@Override
	public String getDiplomaProfessionGroup() {
		return null;
	}

	@Override
	public String getRecognizedProfessionGroup() {
		return null;
	}

	@Override
	public String getThesisTopic() {
		return null;
	}

	@Override
	public String getThesisTopicEn() {
		return null;
	}

	@Override
	public String getTrainingInstitutionLocation() {
		return null;
	}

	@Override
	public String getDiplomaOriginalEducationLevelTranslated() {
		return null;
	}
	public String getResponsibleUser() {
		return null;
	}

	@Override
	public String getRecognizedEducationArea() {
		return null;
	}

	@Override
	public String getTrainingInstitutionNameCityCountry() {
		return null;
	}

	@Override
	public String getLegalReasonOrdinanceArticle() {
		return null;
	}

	@Override
	public String getLegalReasonRegulationArticle() {
		return null;
	}

	@Override
	public String getLegalReasonRegulationText() {
		return null;
	}
}
