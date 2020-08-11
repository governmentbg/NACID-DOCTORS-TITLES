package com.nacid.bl.applications;

import com.nacid.bl.applications.base.TrainingCourseBase;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.ProfessionGroup;
import com.nacid.bl.nomenclatures.Qualification;
import com.nacid.bl.nomenclatures.Speciality;

import java.util.Collection;
import java.util.List;

public interface TrainingCourse extends TrainingCourseBase {

	public Integer getBaseUniversityId();
	public DiplomaType getDiplomaType();
	
	public List<Integer> getJointUniversityIds();
	public List<Integer> getUniversityIds();
	
    public List<? extends TrainingCourseTrainingLocation> getTrainingCourseTrainingLocations();

    public boolean isRecognized();

    /**
     * @return zapisite ot tablicata training_form
     */
    public TrainingCourseTrainingForm getTrainingCourseTrainingForm();

    /**
     * @return zapisite ot tablicata way_of_graduation
     */
    public List<TrainingCourseGraduationWay> getTrainingCourseGraduationWays();

    public Integer getDiplomaTypeId();

    public UniversityExamination getUniversityExaminationByUniversity(int universityId);
    public Collection<UniversityExamination> getUniversityExaminations();

    /**
     * @return universityValidity za dadeniq
     *         universityExamination(universityId).universityValidityId
     */
    public UniversityValidity getUniversityValidity(int universityId);

    public DiplomaExamination getDiplomaExamination();

    public List<TrainingCourseSpeciality> getSpecialities();

    public List<Integer> getRecognizedSpecialityIds();
    
    public List<Speciality> getRecognizedSpecialities();
    
    public Integer getRecognizedEduLevelId();
    
    public FlatNomenclature getRecognizedEducationLevel();
    
    public Integer getRecognizedQualificationId();
    
    public Qualification getRecognizedQualification();
    
    /**
     * opredelq dali zaqvlenieto e priznato po sedalishte
     * @return
     */
    public boolean isRecognizedByHeadQuarter();

    public Person getOwner();

    public Integer getRecognizedProfGroupId();
    public ProfessionGroup getRecognizedProfGroup();


}
