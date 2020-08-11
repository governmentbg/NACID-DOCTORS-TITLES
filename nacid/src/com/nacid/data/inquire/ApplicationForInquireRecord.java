package com.nacid.data.inquire;


import com.nacid.data.annotations.Table;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by georgi.georgiev on 23.06.2015.
 */
@Table(name = "vw_inquire_applications")
public class ApplicationForInquireRecord {
    private int id;
    private int tceId;
    private String appNumber;
    private Date appDate;
    private String docflowNumber;
    private String ownerDiplomaNames;
    private String ownerNames;
    private String applicantNames;
    private String ownerCivilId;
    private String ownerCivilIdTypeName;
    private String ownerCitizenshipName;
    private int electronicallyApplied;
    private int esigned;
    private List<String> universityName;
    private List<String> universityNameAndCountry;
    private int isJointDegree;
    private String universityCountry;
    private Integer universityExaminationRecognized;
    private List<String> trainingLocation;
    private String eduLevelName;
    private String qualificationName;
    private List<String> specialities;
    private Date diplomaDate;
    private Date trainingStart;
    private Date trainingEnd;
    private String trainingForm;
    private String schoolName;
    private String schoolCountryName;
    private String schoolCity;
    private Date schoolGraduationDate;
    private String schoolNotes;
    private String previousDiplomaBgName;
    private String previousDiplomaCountry;
    private String previousDiplomaEduLevelName;
    private Date prevDiplomaGraduationDate;
    private String prevDiplomaNotes;
    private String summary;
    private String recognizedEduLevelName;
    private List<String> recognizedSpecialities;
    private String recognizedQualificationName;
    private String applicationStatusName;
    private String finalStatusName;
    private List<String> usersWorked;
    private String responsibleUserName;
    private String docflowStatusName;
    private String recognizedProfGroupName;
    private int applicationType;
    private List<Integer> entryNumSeries;

    public String getFinalStatusName() {
        return finalStatusName;
    }

    public void setFinalStatusName(String finalStatusName) {
        this.finalStatusName = finalStatusName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTceId() {
        return tceId;
    }

    public void setTceId(int tceId) {
        this.tceId = tceId;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public String getDocflowNumber() {
        return docflowNumber;
    }

    public void setDocflowNumber(String docflowNumber) {
        this.docflowNumber = docflowNumber;
    }

    public String getOwnerDiplomaNames() {
        return ownerDiplomaNames;
    }

    public void setOwnerDiplomaNames(String ownerDiplomaNames) {
        this.ownerDiplomaNames = ownerDiplomaNames;
    }

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
    }

    public String getOwnerCivilId() {
        return ownerCivilId;
    }

    public void setOwnerCivilId(String ownerCivilId) {
        this.ownerCivilId = ownerCivilId;
    }

    public String getOwnerCivilIdTypeName() {
        return ownerCivilIdTypeName;
    }

    public void setOwnerCivilIdTypeName(String ownerCivilIdTypeName) {
        this.ownerCivilIdTypeName = ownerCivilIdTypeName;
    }

    public String getOwnerCitizenshipName() {
        return ownerCitizenshipName;
    }

    public void setOwnerCitizenshipName(String ownerCitizenshipName) {
        this.ownerCitizenshipName = ownerCitizenshipName;
    }

    public int getElectronicallyApplied() {
        return electronicallyApplied;
    }

    public void setElectronicallyApplied(int electronicallyApplied) {
        this.electronicallyApplied = electronicallyApplied;
    }

    public int getEsigned() {
        return esigned;
    }

    public void setEsigned(int esigned) {
        this.esigned = esigned;
    }

    public List<String> getUniversityName() {
        return universityName;
    }

    public void setUniversityName(List<String> universityName) {
        this.universityName = universityName;
    }

    public int getIsJointDegree() {
        return isJointDegree;
    }

    public void setIsJointDegree(int isJointDegree) {
        this.isJointDegree = isJointDegree;
    }

    public String getUniversityCountry() {
        return universityCountry;
    }

    public void setUniversityCountry(String universityCountry) {
        this.universityCountry = universityCountry;
    }

    public Integer getUniversityExaminationRecognized() {
        return universityExaminationRecognized;
    }

    public void setUniversityExaminationRecognized(Integer universityExaminationRecognized) {
        this.universityExaminationRecognized = universityExaminationRecognized;
    }

    public List<String> getTrainingLocation() {
        return trainingLocation;
    }

    public void setTrainingLocation(List<String> trainingLocation) {
        this.trainingLocation = trainingLocation;
    }

    public String getEduLevelName() {
        return eduLevelName;
    }

    public void setEduLevelName(String eduLevelName) {
        this.eduLevelName = eduLevelName;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public List<String> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<String> specialities) {
        this.specialities = specialities;
    }

    public Date getDiplomaDate() {
        return diplomaDate;
    }

    public void setDiplomaDate(Date diplomaDate) {
        this.diplomaDate = diplomaDate;
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

    public String getTrainingForm() {
        return trainingForm;
    }

    public void setTrainingForm(String trainingForm) {
        this.trainingForm = trainingForm;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolCountryName() {
        return schoolCountryName;
    }

    public void setSchoolCountryName(String schoolCountryName) {
        this.schoolCountryName = schoolCountryName;
    }

    public String getSchoolCity() {
        return schoolCity;
    }

    public void setSchoolCity(String schoolCity) {
        this.schoolCity = schoolCity;
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

    public String getPreviousDiplomaBgName() {
        return previousDiplomaBgName;
    }

    public void setPreviousDiplomaBgName(String previousDiplomaBgName) {
        this.previousDiplomaBgName = previousDiplomaBgName;
    }

    public String getPreviousDiplomaCountry() {
        return previousDiplomaCountry;
    }

    public void setPreviousDiplomaCountry(String previousDiplomaCountry) {
        this.previousDiplomaCountry = previousDiplomaCountry;
    }

    public String getPreviousDiplomaEduLevelName() {
        return previousDiplomaEduLevelName;
    }

    public void setPreviousDiplomaEduLevelName(String previousDiplomaEduLevelName) {
        this.previousDiplomaEduLevelName = previousDiplomaEduLevelName;
    }

    public Date getPrevDiplomaGraduationDate() {
        return prevDiplomaGraduationDate;
    }

    public void setPrevDiplomaGraduationDate(Date prevDiplomaGraduationDate) {
        this.prevDiplomaGraduationDate = prevDiplomaGraduationDate;
    }

    public String getPrevDiplomaNotes() {
        return prevDiplomaNotes;
    }

    public void setPrevDiplomaNotes(String prevDiplomaNotes) {
        this.prevDiplomaNotes = prevDiplomaNotes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRecognizedEduLevelName() {
        return recognizedEduLevelName;
    }

    public void setRecognizedEduLevelName(String recognizedEduLevelName) {
        this.recognizedEduLevelName = recognizedEduLevelName;
    }

    public List<String> getRecognizedSpecialities() {
        return recognizedSpecialities;
    }

    public void setRecognizedSpecialities(List<String> recognizedSpecialities) {
        this.recognizedSpecialities = recognizedSpecialities;
    }

    public String getRecognizedQualificationName() {
        return recognizedQualificationName;
    }

    public void setRecognizedQualificationName(String recognizedQualificationName) {
        this.recognizedQualificationName = recognizedQualificationName;
    }

    public String getApplicationStatusName() {
        return applicationStatusName;
    }

    public void setApplicationStatusName(String applicationStatusName) {
        this.applicationStatusName = applicationStatusName;
    }
    public boolean isJointDegree() {
        return isJointDegree == 1;
    }
    public String getUniversityNames(boolean addCountryToName) {
        return getUniversityName() == null || getUniversityName().size() == 0 ? "" : StringUtils.join(addCountryToName ? getUniversityNameAndCountry() : getUniversityName(), "; ");
    }
    public String getSpecialityNamesSeparatedBySemiColon() {
        return getSpecialities() == null ? "" : StringUtils.join(getSpecialities(), ";");
    }
    public String getRecognizedSpecialityNamesSeparatedBySemiColon() {
        return getRecognizedSpecialities() == null ? "" : StringUtils.join(getRecognizedSpecialities(), ";");
    }

    public List<String> getUsersWorked() {
        return usersWorked;
    }

    public void setUsersWorked(List<String> usersWorked) {
        this.usersWorked = usersWorked;
    }

    public String getResponsibleUserName() {
        return responsibleUserName;
    }

    public void setResponsibleUserName(String responsibleUserName) {
        this.responsibleUserName = responsibleUserName;
    }

    public String getApplicantNames() {
        return applicantNames;
    }

    public void setApplicantNames(String applicantNames) {
        this.applicantNames = applicantNames;
    }

    public String getDocflowStatusName() {
        return docflowStatusName;
    }

    public void setDocflowStatusName(String docflowStatusName) {
        this.docflowStatusName = docflowStatusName;
    }

    public String getRecognizedProfGroupName() {
        return recognizedProfGroupName;
    }

    public void setRecognizedProfGroupName(String recognizedProfGroupName) {
        this.recognizedProfGroupName = recognizedProfGroupName;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(int applicationType) {
        this.applicationType = applicationType;
    }

    public List<String> getUniversityNameAndCountry() {
        return universityNameAndCountry;
    }

    public void setUniversityNameAndCountry(List<String> universityNameAndCountry) {
        this.universityNameAndCountry = universityNameAndCountry;
    }

    public List<Integer> getEntryNumSeries() {
        return entryNumSeries;
    }

    public void setEntryNumSeries(List<Integer> entryNumSeries) {
        this.entryNumSeries = entryNumSeries;
    }
}
