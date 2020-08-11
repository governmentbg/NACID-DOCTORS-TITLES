package com.nacid.bl.external.applications;

import com.nacid.bl.applications.base.TrainingCourseBase;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.external.applications.ExtTrainingCourseSpecialityRecord;

import java.util.List;

public interface ExtTrainingCourse extends TrainingCourseBase{
    public List<Speciality> getSpecialities();
    public List<Integer> getSpecialityIds();
    //public String getBaseUniversityTxt();
    //public String getSpecialityTxt();
    public String getQualificationTxt();
    public Integer getPrevDiplomaCountry();
    public String getPrevDiplomaCity();
    public String getPrevDiplomaUiniversityTxt();
    public String getPrevDiplomaSpecialityTxt();
    //public String getTrainingInstTxt();
    //public Integer getTrainingInstId();
    //public TrainingInstitution getTrainingInstitution();
    public List<? extends ExtTrainingCourseTrainingLocation> getTrainingCourseTrainingLocations();

    
//    public ExtUniversity getBaseUniversity();
//    public List<? extends ExtUniversity> getJointUniversities();
    
    /**
     * @return getBaseUniversity() + getJointUniversities(); kato pyrviq zapis e baseUniversity
     */
//    public List<? extends ExtUniversity> getUniversities();
    public List<ExtTrainingCourseSpecialityRecord> getTrainingCourseSpecialities();
    public String getSpecialityTxtSeparatedBySemicolon();

    public ExtPerson getOwner();

    @Override
    ExtUniversityWithFaculty getBaseUniversityWithFaculty();

    @Override
    List<ExtUniversityWithFaculty> getJointUniversityWithFaculties();

    @Override
    List<ExtUniversityWithFaculty> getUniversityWithFaculties();
}
