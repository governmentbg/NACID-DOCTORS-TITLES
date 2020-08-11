package com.nacid.bl.external.applications;

import com.nacid.bl.impl.external.applications.ExtUniversityIdWithFacultyId;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.external.applications.ExtTrainingCourseSpecialityRecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ExtTrainingCourseDataProvider {

    public ExtTrainingCourse getExtTrainingCourse(int id);
    public int saveExtTrainingCourse(int id, String diplomaSeries, String diplomaNum, String diplomaRegistrationNumber, Date diplomaDate, /*Integer universityId, String universityTxt,*/ String fname, String sname,
            String lname, /*Integer trainingLocationCountryId, String trainingLocationCity,*/ boolean jointDegree, Date trainingStart, Date trainingEnd,
            Double trainingDuration, Integer durationUnitId, BigDecimal credits, /*Integer specialityId, String specialityTxt,*/ Integer eduLevelId,
            Integer qualificationId, String qualificationTxt, Integer schoolCountry, String schoolCity, String schoolName, Date schoolGraduationDate,
            String schoolNotes, Integer prevDiplomaCountry, String prevDiplomaCity, Integer prevDiplomaUniversityId,
            String prevDiplomaUiniversityTxt, Integer prevDiplomaEduLevelId, Date prevDiplomaGraduationDate, String prevDiplomaNotes, Integer prevDiplomaSpecialityId, String prevDiplomaSpecialityTxt, 
            Integer trainingInstId, String trainingInstTxt, Integer graduationDocumentTypeId, Integer creditHours, Integer ectsCredits,
            int ownerId, String thesisTopic, String thesisTopicEn, Integer profGroupId, Date thesisDefenceDate,
                                     Integer thesisBibliography, Integer thesisVolume, Integer thesisLanguage, String thesisAnnotation, String thesisAnnotationEn);
    //public void deleteTrainingCourseUniversities(int trainingCourseId);
    public void updateTrainingCourseUniversities(int trainingCourseId, List<ExtUniversityIdWithFacultyId> universityWithFaculties);
    public int saveTrainingCourse(int courseId, String firstName, String middleName, String lastName, int ownerId);
    public List<ExtPurposeOfRecognition> getExtPurposesOfRecognition(int applicationId);
    public void savePurposesOfrecognition(List<Integer> purposesIds, String note, int applicationId);
    public List<ExtGraduationWay> getExtGraduationWays(int trainingCourseId);
    public void saveGraduationWays(List<Integer> graduationWaysIds, String note, int trainingCourseId);
    public List<ExtTrainingForm> getExtTrainingForms(int trainingCourseId);
    public void saveExtTrainingForms(List<Integer> trainingFormIds, String note, int trainingCourseId);
    
    public void deleteTrainingCourseTrainingLocations(int trainingCourseId);
    public int addTrainingCourseTrainingLocation(int trainingCourseId, int trainigLocationTrainingCountryId, String trainingLocationTrainingCity);
    
    public List<Speciality> loadSpecialities(int trainingCourseId);
    public List<ExtTrainingCourseSpecialityRecord> loadTxtSpecialities(int trainingCourseId);
    public void saveSpeciality(ExtTrainingCourseSpecialityRecord record);
    public void deleteSpecialities(int trainingCourseId);
}
