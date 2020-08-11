package com.nacid.bl.applications.regprof;
//RayaWritten------------------------------------------
public interface RegprofQualificationExamination {
    public Integer getId();
    public void setId(Integer id);
    public Integer getTrainingCourseId();
    public void setTrainingCourseId(Integer trainingCourseId);
    public Integer getRecognizedQualificationDegreeId();
    public void setRecognizedQualificationDegreeId(
            Integer recognizedQualificationDegreeId);
    public Integer getRecognizedQualificationLevelId();
    public void setRecognizedQualificationLevelId(
            Integer recognizedQualificationLevelId);
    public Integer getUserCreated();
    public void setUserCreated(Integer userCreated);
    public Integer getRecognizedProfessionId();
    public void setRecognizedProfessionId(Integer recognizedProfessionId);
    public Integer getArticleItemId();
    public void setArticleItemId(Integer articleItemId);
    public int getRecognizedQualificationTeacher();
    public Integer getGrade();
    public Integer getSchoolType();
    public Integer getAgeRange();
}
//-------------------------------------------------------
