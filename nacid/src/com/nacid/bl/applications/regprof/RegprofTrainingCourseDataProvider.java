package com.nacid.bl.applications.regprof;

import java.util.List;

import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceExaminationRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;

public interface RegprofTrainingCourseDataProvider {
    public int saveRegprofProfessionExperienceRecords(RegprofTrainingCourse trainingCourse);
    public RegprofTrainingCourse getRegprofTrainingCourse(Integer applicationId);
    //RayaWritten------------------------------------------------------------
    public RegprofTrainingCourseDetailsImpl getRegprofTrainingCourseDetails(Integer trainingCourseId);
    public void updateRegprofTrainingCourseDetails(RegprofTrainingCourseDetailsBase details, String...columns);
    public Integer insertEmptyTCDetailsRecord();
    public void deleteProfessionExperienceExamination(Integer professionExperienceId);
    //----------------------------------------------------------------------
    public Integer saveProfessionExperienceExamination(RegprofProfessionExperienceExaminationRecord record);
    public RegprofProfessionExperienceExaminationRecord getProfessionExperienceExamination(Integer experienceId);
    public RegprofTrainingCourseDetailsImpl getTrainingCourseDetails(int applicationId);
    public void deleteRegprofProfessionExperienceRecords(int trainingCourseId);
    public void updateTrainingCourseDetails(RegprofTrainingCourseDetailsBase details);
    public List<RegprofTrainingCourseSpecialityRecord> getTrainingCourseSpecialities(int regprofTrainingCourseId);
    public void saveTrainingCourseSpecialities(List<RegprofTrainingCourseSpecialityRecord> records);
    public void deleteTrainingCourseSpecialities(int regprofTrainingCourseId);
    public RegprofProfessionExperienceDocumentRecord getRegprofProfessionExperienceDocument(int professionExperienceDocumentId);
    public int saveTrainingCourseDetails(RegprofTrainingCourseDetailsImpl details);
    public int saveProfessionExperienceRecord(RegprofProfessionExperienceRecord record);
    public int saveProfessionExperienceDocumentRecord(RegprofProfessionExperienceDocumentRecord record);
}
