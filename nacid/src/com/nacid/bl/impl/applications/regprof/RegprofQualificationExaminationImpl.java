package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.applications.regprof.RegprofQualificationExamination;
import com.nacid.data.annotations.Table;
@Table(name="regprof.prof_qualification_examination")
public class RegprofQualificationExaminationImpl implements RegprofQualificationExamination, RequestParameterInterface{
    private Integer id;
    private Integer trainingCourseId;
    private Integer recognizedQualificationDegreeId;
    private Integer recognizedQualificationLevelId;
    private Integer userCreated;
    private Integer recognizedProfessionId;
    private Integer articleItemId;
    private int recognizedQualificationTeacher;
    private Integer grade;
    private Integer schoolType;
    private Integer ageRange;

    public RegprofQualificationExaminationImpl() { };
    
    public RegprofQualificationExaminationImpl(Integer id, Integer trainingCourseId, Integer recognizedQualificationDegreeId, Integer recognizedQualificationLevelId, 
            Integer userCreated, Integer recognizedProfessionId, Integer articleItemId, int recognizedQualificationTeacher,
            Integer grade, Integer schoolType, Integer ageRange) {

        this.id = id;
        this.trainingCourseId = trainingCourseId;
        this.recognizedQualificationDegreeId = recognizedQualificationDegreeId;
        this.recognizedQualificationLevelId = recognizedQualificationLevelId;
        this.userCreated = userCreated;
        this.recognizedProfessionId = recognizedProfessionId;
        this.articleItemId = articleItemId;
        this.recognizedQualificationTeacher = recognizedQualificationTeacher;
        this.grade = grade;
        this.schoolType = schoolType;
        this.ageRange = ageRange;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTrainingCourseId() {
        return trainingCourseId;
    }
    public void setTrainingCourseId(Integer trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }
    public Integer getRecognizedQualificationDegreeId() {
        return recognizedQualificationDegreeId;
    }
    public void setRecognizedQualificationDegreeId(
            Integer recognizedQualificationDegreeId) {
        this.recognizedQualificationDegreeId = recognizedQualificationDegreeId;
    }
    public Integer getRecognizedQualificationLevelId() {
        return recognizedQualificationLevelId;
    }
    public void setRecognizedQualificationLevelId(
            Integer recognizedQualificationLevelId) {
        this.recognizedQualificationLevelId = recognizedQualificationLevelId;
    }
    public Integer getUserCreated() {
        return userCreated;
    }
    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }
    public Integer getRecognizedProfessionId() {
        return recognizedProfessionId;
    }
    public void setRecognizedProfessionId(Integer recognizedProfessionId) {
        this.recognizedProfessionId = recognizedProfessionId;
    }
    public Integer getArticleItemId() {
        return articleItemId;
    }
    public void setArticleItemId(Integer articleItemId) {
        this.articleItemId = articleItemId;
    }

    public int getRecognizedQualificationTeacher() {
        return recognizedQualificationTeacher;
    }

    public void setRecognizedQualificationTeacher(int recognizedQualificationTeacher) {
        this.recognizedQualificationTeacher = recognizedQualificationTeacher;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(Integer schoolType) {
        this.schoolType = schoolType;
    }

    public Integer getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(Integer ageRange) {
        this.ageRange = ageRange;
    }
}
