package com.nacid.data.inquire;

import java.util.Date;

public class BGAcademicRecognitionForReportRecord {

    private Integer id;
    private String applicant;

    private String citizenship;
    private String university;
    private String universityCountry;
    private String educationLevel;
    private String diplomaSpeciality;
    private String diplomaNumber;
    private String diplomaDate;
    private String protocolNumber;
    private String denialProtocolNumber;
    private String recognizedSpeciality;
    private String inputNumber;
    private String outputNumber;
    private Date createdDate;
    private String notes;
    private Integer recognitionStatusId;
    private Integer relatedRecognitionId;
    private Integer recognizedUniversityId;

    private String recognizedUniversityName;
    private String recognitionStatusName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getUniversityCountry() {
        return universityCountry;
    }

    public void setUniversityCountry(String universityCountry) {
        this.universityCountry = universityCountry;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getDiplomaSpeciality() {
        return diplomaSpeciality;
    }

    public void setDiplomaSpeciality(String diplomaSpeciality) {
        this.diplomaSpeciality = diplomaSpeciality;
    }

    public String getDiplomaNumber() {
        return diplomaNumber;
    }

    public void setDiplomaNumber(String diplomaNumber) {
        this.diplomaNumber = diplomaNumber;
    }

    public String getDiplomaDate() {
        return diplomaDate;
    }

    public void setDiplomaDate(String diplomaDate) {
        this.diplomaDate = diplomaDate;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public String getDenialProtocolNumber() {
        return denialProtocolNumber;
    }

    public void setDenialProtocolNumber(String denialProtocolNumber) {
        this.denialProtocolNumber = denialProtocolNumber;
    }

    public String getRecognizedSpeciality() {
        return recognizedSpeciality;
    }

    public void setRecognizedSpeciality(String recognizedSpeciality) {
        this.recognizedSpeciality = recognizedSpeciality;
    }

    public String getInputNumber() {
        return inputNumber;
    }

    public void setInputNumber(String inputNumber) {
        this.inputNumber = inputNumber;
    }

    public String getOutputNumber() {
        return outputNumber;
    }

    public void setOutputNumber(String outputNumber) {
        this.outputNumber = outputNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getRecognitionStatusId() {
        return recognitionStatusId;
    }

    public void setRecognitionStatusId(Integer recognitionStatusId) {
        this.recognitionStatusId = recognitionStatusId;
    }

    public Integer getRelatedRecognitionId() {
        return relatedRecognitionId;
    }

    public void setRelatedRecognitionId(Integer relatedRecognitionId) {
        this.relatedRecognitionId = relatedRecognitionId;
    }

    public Integer getRecognizedUniversityId() {
        return recognizedUniversityId;
    }

    public void setRecognizedUniversityId(Integer recognizedUniversityId) {
        this.recognizedUniversityId = recognizedUniversityId;
    }

    public String getRecognizedUniversityName() {
        return recognizedUniversityName;
    }

    public void setRecognizedUniversityName(String recognizedUniversityName) {
        this.recognizedUniversityName = recognizedUniversityName;
    }

    public String getRecognitionStatusName() {
        return recognitionStatusName;
    }

    public void setRecognitionStatusName(String recognitionStatusName) {
        this.recognitionStatusName = recognitionStatusName;
    }

    @Override
    public String toString() {
        return "BGAcademicRecognitionForReportRecord{" +
                "id=" + id +
                ", applicant='" + applicant + '\'' +
                ", citizenship='" + citizenship + '\'' +
                ", university='" + university + '\'' +
                ", universityCountry='" + universityCountry + '\'' +
                ", educationLevel='" + educationLevel + '\'' +
                ", diplomaSpeciality='" + diplomaSpeciality + '\'' +
                ", diplomaNumber='" + diplomaNumber + '\'' +
                ", diplomaDate='" + diplomaDate + '\'' +
                ", protocolNumber='" + protocolNumber + '\'' +
                ", denialProtocolNumber='" + denialProtocolNumber + '\'' +
                ", recognizedSpeciality='" + recognizedSpeciality + '\'' +
                ", inputNumber='" + inputNumber + '\'' +
                ", outputNumber='" + outputNumber + '\'' +
                ", createdDate=" + createdDate +
                ", notes='" + notes + '\'' +
                ", recognitionStatusId=" + recognitionStatusId +
                ", relatedRecognitionId=" + relatedRecognitionId +
                ", recognizedUniversityId=" + recognizedUniversityId +
                ", recognizedUniversityName='" + recognizedUniversityName + '\'' +
                ", recognitionStatusName='" + recognitionStatusName + '\'' +
                '}';
    }
}