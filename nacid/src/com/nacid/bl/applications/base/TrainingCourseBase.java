package com.nacid.bl.applications.base;

import com.nacid.bl.applications.University;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.*;

import java.util.Date;
import java.util.List;

public interface TrainingCourseBase {
	public int getId();
	String getDiplomaSeries();
	public String getDiplomaNumber();
	String getDiplomaRegistrationNumber();
	public Date getDiplomaDate();
	public String getFName();
    public String getSName();
    public String getLName();
    public String getFullName();
    public List<? extends TrainingCourseTrainingLocationBase> getTrainingCourseTrainingLocations();
//    public Integer getTrainingLocationCountryId();
//    public Country getTrainingLocationCountry();
//    public String getTrainingLocationCity();
    public Date getTrainingStart();
    public Date getTrainingEnd();
    public Double getTrainingDuration();
    public Integer getDurationUnitId();
    public FlatNomenclature getDurationUnit();
    public Integer getEducationLevelId();
    public FlatNomenclature getEducationLevel();
    public Integer getQualificationId();
    public FlatNomenclature getQualification();
    public Integer getOriginalQualificationId();
    public FlatNomenclature getOriginalQualification();
    public Integer getSchoolCountryId();
    public Country getSchoolCountry();
    public String getSchoolCity();
    public String getSchoolName();
    public Date getSchoolGraduationDate();
    public String getSchoolNotes();
    public Integer getPrevDiplomaUniversityId();
    public University getPrevDiplomaUniversity();
    public Integer getPrevDiplomaEduLevelId();
    public FlatNomenclature getPrevDiplomaEduLevel();
    public Date getPrevDiplomaGraduationDate();
    public String getPrevDiplomaNotes();
    public Integer getPrevDiplomaSpecialityId();
    public Speciality getPrevDiplomaSpeciality();
    public java.math.BigDecimal getCredits();
    public String getThesisTopic();
    public String getThesisTopicEn();
    public Integer getProfGroupId();
    public ProfessionGroup getProfGroup();
    public Date getThesisDefenceDate();
    public Integer getThesisBibliography();
    public Integer getThesisVolume();

    public Integer getThesisLanguageId();
    public Language getThesisLanguage();
    public String getThesisAnnotation();
    public String getThesisAnnotationEn();

    //public Integer getSpecialityId();
    //public Speciality getSpeciality();
    //public List<Integer> getSpecialityIds();

    public String getSpecialityNamesSeparatedBySemiColon();
//    public University getBaseUniversity();
    public UniversityWithFaculty getBaseUniversityWithFaculty();
    
    public boolean isJointDegree();
    
//    public List<? extends University> getJointUniversities();

    public <T extends UniversityWithFaculty> List<T> getJointUniversityWithFaculties();
    
    /**
     * @return getBaseUniversity() + getJointUniversities(); kato pyrviq zapis e baseUniversity
     */
//    public List<? extends University> getUniversities();

    public <T extends UniversityWithFaculty> List<T> getUniversityWithFaculties();

    public Integer getGraduationDocumentTypeId();
    public FlatNomenclature getGraduationDocumentType();
    public Integer getCreditHours();
    public Integer getEctsCredits();

    public int getOwnerId();
    public PersonBase getOwner();

    /**
     * @return dali zaqvlenieto ima informaciq za nachalo/kraj na obuchenieto, prodyljitelnost i forma na obuchenie
     */
    public boolean hasEducationPeriodInformation();
}
