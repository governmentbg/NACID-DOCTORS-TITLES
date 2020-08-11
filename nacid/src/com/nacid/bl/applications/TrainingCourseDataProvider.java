package com.nacid.bl.applications;

import com.nacid.bl.impl.applications.TrainingCourseSpecialityImpl;
import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.bl.nomenclatures.Speciality;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TrainingCourseDataProvider {
    public TrainingCourse getTrainingCourse(int courseId);
    public int saveTrainingCourse(int id, String diplomaSeries, String diplomaNumber, String diplomaRegistrationNumber, Date diplomaDate, Integer diplomaTypeId, String firstName, String surName, String lastName,
            boolean isJointDegree, /*Integer trainingLocationCountryId, String trainingLocationCity,*/ Date trainingStart, Date trainingEnd, Double trainingDuration,
            Integer durationUnitId, BigDecimal credits, /*List<Integer> specialityIds,*/ Integer educationLevelId, Integer qualificationId, boolean recognized,
            Integer schoolCountryId, String scoolCity,
            String schoolName, Date schoolGraduationDate, String schoolNotes, 
            Integer prevDiplUniversityId, Integer prevDiplEduLevelId,
            Date prevDiplGraduationDate, String prevDiplNotes, Integer prevDiplomaSpecialityId, 
            Integer recognizedEduLevel, 
            /*Integer trainingInstId,*/ Integer recognizedQualification, Integer graduationDocumentTypeId, Integer creditHours, Integer ectsCredits,
            int ownerId, String thesisTopic, String thesisTopicEn, Integer profGroupId, Integer recognizedProfGroupId, Date thesisDefenceDate,
                                  Integer thesisBibliography, Integer thesisVolume, Integer thesisLanguage, String thesisAnnotation, String thesisAnnotationEn, Integer originalQualificationId);
    public void deleteTrainingCourseSpecialities(int trainingCourseId);
    public void saveTrainingCourseSpecialities(int trainingCourseId, List<TrainingCourseSpecialityImpl> specialities);
    
    public void saveRecognizedSpecialities(int trainingCourseId, List<Integer> specialityIds);
    /**
     * tozi method se polzva pri submitvane na pyrvata forma s application data!!
     * @param id
     * @param firstName
     * @param surName
     * @param lastName
     * @return
     */
    public int saveTrainingCourse(int id, String firstName, String surName, String lastName, int ownerId);

    public void deleteTrainingCourseGraduationWays(int trainingCourseId);
    /**
     * predi da po4nat da se vyvejdat novi TrainingCourseGraduationWayRecords, trqbva da se iztriqt starite ot bazata,
     * zashtoto 
     * @param trainingCourseId
     * @param graduationWayId
     * @param notes
     */
    public void addTrainingCourseGraduationWayRecord(int trainingCourseId, Integer graduationWayId, String notes);
    /**
     * V bazata moje da ima samo edin TrainingCourseTrainingFormRecord, zatova pyrvo se zatrivat vsi4ki stari i se dobavq noviq!!!
     * @param trainingCourseId
     * @param trainingFormId
     * @param notes
     */
    public void setTrainingCourseTrainingForm(int trainingCourseId, Integer trainingFormId, String notes);
    /**
     * prezapisva universitieIds, koito se otnasqt za daden trainingCourseId - ako jointUniversities == null
     * slaga samo base university
     * @param trainingCourseId
     * @param baseUniversity
     * @param jointUniversities
     */
    public void updateTrainingCourseUniversities(int trainingCourseId, UniversityIdWithFacultyId baseUniversity, List<UniversityIdWithFacultyId> jointUniversities);

    
    /**
     * zapisva universityExamination - ako za daden trainingCourse + universityId ima ve4e vyveden universityExamination, 
     * to pyrvo stariq zapis se iztriva i se dobavq nov. 
     * universityId-to se opredelq ot universityId-to zaka4en za dadeniq universityValidityId
     */
    public void saveUniversityExamination(int trainingCourseId, int universityValidityId, int userId, String notes);
    
    public int saveDiplomaExamination(int id, int trainingCourseId, int userId, Date examinationDate, String notes, boolean isRecognized, Integer competentInstitutionId, boolean isInstitutionCommunicated, boolean isUniversityCommunicated, boolean foundInRegister);
    public List<Speciality> loadRecognizedSpecialities(int trainingCourseId);
    
    /**
     * predi da se zapisvat novi zapisi v trainingCourseTrainingLocation, trqbva da se iztriqt starite zapisi!!!
     * @param trainingCourseId
     */
    //public void deleteTrainingCourseTrainingLocations(int trainingCourseId);
    /**
     * Ako trainingLocationId == 0, togava se dobavq nov zapis, ako trainingLocationId != 0, togava se update-va star zapis 
     */
    public int saveTrainingCourseTrainingLocation(int trainingLocationId, int trainingCourseId, int trainigLocationTrainingCountryId, String trainingLocationTrainingCity, Integer trainingInstitutionId);

    /**
     * iztriva trainingCourseTrainingLocation po daden training_location_id
     */
    public void deleteTrainingCourseTrainingLocation(int trainingCourseTrainingLocationId);

    public void updateRecognizedDetails(int trainingCourseId, Integer recognizedEduLevelId, Integer recognizedQualificationId, Integer recognizedProfGroupId, List<Integer> recognizedSpecialities);


}
