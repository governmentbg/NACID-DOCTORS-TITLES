package com.nacid.data.regprof;

import com.nacid.data.annotations.Table;

@Table(name="regprof.profession_experience_examination")
public class RegprofProfessionExperienceExaminationRecord {

    private Integer id;
    private Integer regprofProfessionExperienceId;
    private Integer experienceDocumentRecognized;
    private Integer userId;
    private Integer notRestricted;
    private Integer articleItemId;

    public RegprofProfessionExperienceExaminationRecord() {}
    
    public RegprofProfessionExperienceExaminationRecord(Integer id, Integer regprofProfessionExperienceId, Integer experienceDocumentRecognized, 
            Integer userId, Integer notRestricted, Integer articleItemId) {
        this.id = id;
        this.regprofProfessionExperienceId = regprofProfessionExperienceId;
        this.experienceDocumentRecognized = experienceDocumentRecognized;
        this.userId = userId;
        this.notRestricted = notRestricted;
        this.articleItemId = articleItemId;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getRegprofProfessionExperienceId() {
        return regprofProfessionExperienceId;
    }
    public void setRegprofProfessionExperienceId(Integer regprofProfessionExperienceId) {
        this.regprofProfessionExperienceId = regprofProfessionExperienceId;
    }
   
    public Integer getExperienceDocumentRecognized() {
        return experienceDocumentRecognized;
    }

    public void setExperienceDocumentRecognized(Integer experienceDocumentRecognized) {
        this.experienceDocumentRecognized = experienceDocumentRecognized;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getNotRestricted() {
        return notRestricted;
    }
    public void setNotRestricted(Integer notRestricted) {
        this.notRestricted = notRestricted;
    }

    public Integer getArticleItemId() {
        return articleItemId;
    }

    public void setArticleItemId(Integer articleItemId) {
        this.articleItemId = articleItemId;
    }
    
}
