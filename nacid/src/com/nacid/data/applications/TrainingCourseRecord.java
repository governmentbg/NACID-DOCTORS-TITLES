package com.nacid.data.applications;

import java.math.BigDecimal;
import java.sql.Date;

public class TrainingCourseRecord {
    private int id;
    private String diplomaSeries;
    private String diplomaNumber;
    private String diplomaRegistrationNumber;
    private Date diplomaDate;
    private Integer diplomaTypeId;
    private String firstName;
    private String surName;
    private String lastName;
    private Integer isJointDegree;
    //private Integer trainingLocationCountryId;
    //private String trainingLocationCity;
    private Date trainingStart;
    private Date trainingEnd;
    private Double trainingDuration;
    private Integer durationUnitId;
    private BigDecimal credits;
    private int recognized;
    //private Integer specialityId;
    private Integer educationLevelId;
    private Integer qualificationId;
    
    private Integer schoolCountryId;
    private String schoolCity;
    private String schoolName;
    private Date schoolGraduationDate;
    private String schoolNotes;
    private Integer prevDiplUniversityId;
    private Integer prevDiplEduLevelId;
    private Date prevDiplGraduationDate;
    private String prevDiplNotes;
    private Integer prevDiplSpecialityId;
    private Integer recognizedEduLevel;
    private Integer recognizedQualification;
    private Integer graduationDocumentTypeId;
    private Integer creditHours;
    private Integer ectsCredits;
    private int ownerId;
    private String thesisTopic;
    private String thesisTopicEn;
    private Integer profGroupId;
    private Integer recognizedProfGroupId;

    private Date thesisDefenceDate;
    private Integer thesisBibliography;
    private Integer thesisVolume;
    private Integer thesisLanguageId;
    private String thesisAnnotation;
    private String thesisAnnotationEn;
    private Integer originalQualificationId;

    
    //private Integer trainingInstId;

    public TrainingCourseRecord() {
    }

    public TrainingCourseRecord(int id, String diplomaSeries, String diplomaNumber, String diplomaRegistrationNumber, Date diplomaDate, Integer diplomaTypeId, String firstName, String surName,
                                String lastName, Integer isJointDegree, /*Integer trainingLocationCountryId, String trainingLocationCity,*/ Date trainingStart,
                                Date trainingEnd, Double trainingDuration, Integer durationUnitId, BigDecimal credits/*, Integer specialityId*/, Integer educationLevelId,
                                Integer qualificationId, int recognized, Integer schoolCountryId, String schoolCity,
                                String schoolName, Date schoolGraduationDate, String schoolNotes,
                                Integer prevDiplUniversityId, Integer prevDiplEduLevelId,
                                Date prevDiplGraduationDate, String prevDiplNotes, Integer prevDiplomaSpecialityId,
                                Integer recognizedEduLevel,
            /*Integer trainingInstId,*/ Integer recognizedQualification, Integer graduationDocumentTypeId, Integer creditHours, Integer ectsCredits, int ownerId, String thesisTopic, String thesisTopicEn,
                                Integer profGroupId, Integer recognizedProfGroupId, Date thesisDefenceDate,
                                Integer thesisBibliography, Integer thesisVolume, Integer thesisLanguageId, String thesisAnnotation, String thesisAnnotationEn, Integer originalQualificationId) {
        this.id = id;
        this.diplomaSeries = diplomaSeries;
        this.diplomaNumber = diplomaNumber;
        this.diplomaRegistrationNumber = diplomaRegistrationNumber;
        this.diplomaDate = diplomaDate;
        this.diplomaTypeId = diplomaTypeId;
        this.firstName = firstName;
        this.surName = surName;
        this.lastName = lastName;
        this.isJointDegree = isJointDegree;
        //this.trainingLocationCountryId = trainingLocationCountryId;
        //this.trainingLocationCity = trainingLocationCity;
        this.trainingStart = trainingStart;
        this.trainingEnd = trainingEnd;
        this.trainingDuration = trainingDuration;
        this.durationUnitId = durationUnitId;
        this.credits = credits;
        //this.specialityId = specialityId;
        this.educationLevelId = educationLevelId;
        this.qualificationId = qualificationId;
        this.recognized = recognized;
        
        this.schoolCountryId = schoolCountryId;
        this.schoolCity = schoolCity;
        this.schoolName = schoolName;
        this.schoolGraduationDate = schoolGraduationDate;
        this.schoolNotes = schoolNotes;
        this.prevDiplUniversityId = prevDiplUniversityId;
        this.prevDiplEduLevelId = prevDiplEduLevelId;
        this.prevDiplGraduationDate = prevDiplGraduationDate;
        this.prevDiplNotes = prevDiplNotes;
        this.recognizedEduLevel = recognizedEduLevel;
        
        //this.trainingInstId = trainingInstId;
        
        this.recognizedQualification = recognizedQualification;
        this.prevDiplSpecialityId = prevDiplomaSpecialityId;
        this.graduationDocumentTypeId = graduationDocumentTypeId;
        this.creditHours = creditHours;
        this.ectsCredits = ectsCredits;
        this.ownerId = ownerId;
        this.thesisTopic = thesisTopic;
        this.thesisTopicEn = thesisTopicEn;
        this.profGroupId = profGroupId;
        this.recognizedProfGroupId = recognizedProfGroupId;
        this.thesisDefenceDate = thesisDefenceDate;
        this.thesisBibliography = thesisBibliography;
        this.thesisVolume = thesisVolume;
        this.thesisAnnotation = thesisAnnotation;
        this.thesisAnnotationEn = thesisAnnotationEn;
        this.thesisLanguageId = thesisLanguageId;
        this.originalQualificationId = originalQualificationId;
    }

    /*public Integer getTrainingInstId() {
        return trainingInstId;
    }

    public void setTrainingInstId(Integer trainingInstId) {
        this.trainingInstId = trainingInstId;
    }*/

    public Integer getRecognizedEduLevel() {
        return recognizedEduLevel;
    }

    public void setRecognizedEduLevel(Integer recognizedEduLevel) {
        this.recognizedEduLevel = recognizedEduLevel;
    }

    public int getRecognized() {
        return recognized;
    }

    public void setRecognized(int recognized) {
        this.recognized = recognized;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiplomaNumber() {
        return diplomaNumber;
    }

    public void setDiplomaNumber(String diplomaNumber) {
        this.diplomaNumber = diplomaNumber;
    }

    public Date getDiplomaDate() {
        return diplomaDate;
    }

    public void setDiplomaDate(Date diplomaDate) {
        this.diplomaDate = diplomaDate;
    }

    public Integer getDiplomaTypeId() {
        return diplomaTypeId;
    }

    public void setDiplomaTypeId(Integer diplomaTypeId) {
        this.diplomaTypeId = diplomaTypeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getIsJointDegree() {
        return isJointDegree;
    }

    public void setIsJointDegree(Integer isJointDegree) {
        this.isJointDegree = isJointDegree;
    }

    public Date getTrainingStart() {
        return trainingStart;
    }

    public void setTrainingStart(Date trainingStart) {
        this.trainingStart = trainingStart;
    }

    public Date getTrainingEnd() {
        return trainingEnd;
    }

    public void setTrainingEnd(Date trainingEnd) {
        this.trainingEnd = trainingEnd;
    }

    public Double getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Double trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public Integer getDurationUnitId() {
        return durationUnitId;
    }

    public void setDurationUnitId(Integer durationUnitId) {
        this.durationUnitId = durationUnitId;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    /*public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }*/

    public Integer getEducationLevelId() {
        return educationLevelId;
    }

    public void setEducationLevelId(Integer educationLevelId) {
        this.educationLevelId = educationLevelId;
    }

    public Integer getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }

    public Integer getSchoolCountryId() {
        return schoolCountryId;
    }

    public void setSchoolCountryId(Integer schoolCountryId) {
        this.schoolCountryId = schoolCountryId;
    }

    public String getSchoolCity() {
        return schoolCity;
    }

    public void setSchoolCity(String schoolCity) {
        this.schoolCity = schoolCity;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Date getSchoolGraduationDate() {
        return schoolGraduationDate;
    }

    public void setSchoolGraduationDate(Date schoolGraduationDate) {
        this.schoolGraduationDate = schoolGraduationDate;
    }

    public String getSchoolNotes() {
        return schoolNotes;
    }

    public void setSchoolNotes(String schoolNotes) {
        this.schoolNotes = schoolNotes;
    }

    public Integer getPrevDiplUniversityId() {
        return prevDiplUniversityId;
    }

    public void setPrevDiplUniversityId(Integer prevDiplUniversityId) {
        this.prevDiplUniversityId = prevDiplUniversityId;
    }

    public Integer getPrevDiplEduLevelId() {
        return prevDiplEduLevelId;
    }

    public void setPrevDiplEduLevelId(Integer prevDiplEduLevelId) {
        this.prevDiplEduLevelId = prevDiplEduLevelId;
    }

    public Date getPrevDiplGraduationDate() {
        return prevDiplGraduationDate;
    }

    public void setPrevDiplGraduationDate(Date prevDiplGraduationDate) {
        this.prevDiplGraduationDate = prevDiplGraduationDate;
    }

    public String getPrevDiplNotes() {
        return prevDiplNotes;
    }

    public void setPrevDiplNotes(String prevDiplNotes) {
        this.prevDiplNotes = prevDiplNotes;
    }

    public Integer getRecognizedQualification() {
        return recognizedQualification;
    }

    public void setRecognizedQualification(Integer recognizedQualification) {
        this.recognizedQualification = recognizedQualification;
    }

	public Integer getPrevDiplSpecialityId() {
		return prevDiplSpecialityId;
	}

	public void setPrevDiplSpecialityId(Integer prevDiplSpecialityId) {
		this.prevDiplSpecialityId = prevDiplSpecialityId;
	}

    /*
    public Integer getTrainingLocationCountryId() {
        return trainingLocationCountryId;
    }

    public void setTrainingLocationCountryId(Integer trainingLocationCountryId) {
        this.trainingLocationCountryId = trainingLocationCountryId;
    }

    public String getTrainingLocationCity() {
        return trainingLocationCity;
    }

    public void setTrainingLocationCity(String trainingLocationCity) {
        this.trainingLocationCity = trainingLocationCity;
    }
     */


    public Integer getGraduationDocumentTypeId() {
        return graduationDocumentTypeId;
    }

    public void setGraduationDocumentTypeId(Integer graduationDocumentTypeId) {
        this.graduationDocumentTypeId = graduationDocumentTypeId;
    }

    public Integer getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(Integer creditHours) {
        this.creditHours = creditHours;
    }

    public Integer getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(Integer ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getThesisTopic() {
        return thesisTopic;
    }

    public void setThesisTopic(String thesisTopic) {
        this.thesisTopic = thesisTopic;
    }

    public String getThesisTopicEn() {
        return thesisTopicEn;
    }

    public void setThesisTopicEn(String thesisTopicEn) {
        this.thesisTopicEn = thesisTopicEn;
    }

    public Integer getProfGroupId() {
        return profGroupId;
    }

    public void setProfGroupId(Integer profGroupId) {
        this.profGroupId = profGroupId;
    }

    public Integer getRecognizedProfGroupId() {
        return recognizedProfGroupId;
    }

    public void setRecognizedProfGroupId(Integer recognizedProfGroupId) {
        this.recognizedProfGroupId = recognizedProfGroupId;
    }

    public String getDiplomaSeries() {
        return diplomaSeries;
    }

    public void setDiplomaSeries(String diplomaSeries) {
        this.diplomaSeries = diplomaSeries;
    }

    public String getDiplomaRegistrationNumber() {
        return diplomaRegistrationNumber;
    }

    public void setDiplomaRegistrationNumber(String diplomaRegistrationNumber) {
        this.diplomaRegistrationNumber = diplomaRegistrationNumber;
    }

    public Date getThesisDefenceDate() {
        return thesisDefenceDate;
    }

    public Integer getThesisBibliography() {
        return thesisBibliography;
    }

    public Integer getThesisVolume() {
        return thesisVolume;
    }

    public Integer getThesisLanguageId() {
        return thesisLanguageId;
    }

    public String getThesisAnnotation() {
        return thesisAnnotation;
    }

    public String getThesisAnnotationEn() {
        return thesisAnnotationEn;
    }

    public void setThesisDefenceDate(Date thesisDefenceDate) {
        this.thesisDefenceDate = thesisDefenceDate;
    }

    public void setThesisBibliography(Integer thesisBibliography) {
        this.thesisBibliography = thesisBibliography;
    }

    public void setThesisVolume(Integer thesisVolume) {
        this.thesisVolume = thesisVolume;
    }

    public void setThesisLanguageId(Integer thesisLanguageId) {
        this.thesisLanguageId = thesisLanguageId;
    }

    public void setThesisAnnotation(String thesisAnnotation) {
        this.thesisAnnotation = thesisAnnotation;
    }

    public void setThesisAnnotationEn(String thesisAnnotationEn) {
        this.thesisAnnotationEn = thesisAnnotationEn;
    }

    public Integer getOriginalQualificationId() {
        return originalQualificationId;
    }

    public void setOriginalQualificationId(Integer originalQualificationId) {
        this.originalQualificationId = originalQualificationId;
    }
}
