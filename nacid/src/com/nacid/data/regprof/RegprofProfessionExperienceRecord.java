package com.nacid.data.regprof;

import java.util.Date;
import java.util.List;

import org.springframework.util.AutoPopulatingList;

import com.nacid.data.annotations.SuppressColumn;

public class RegprofProfessionExperienceRecord {
    
    private Integer id;
    private Integer nomenclatureProfessionExperienceId;
    private Integer userId;
    private Date dateCreated;
    private Integer years;
    private Integer months;
    private Integer days;
    @SuppressColumn
    protected List<? extends RegprofProfessionExperienceDocumentRecord> professionExperienceDocuments;// = new AutoPopulatingList<RegprofProfessionExperienceDocumentRecord>(RegprofProfessionExperienceDocumentRecord.class);
    private Integer trainingCourseId;
    
    public RegprofProfessionExperienceRecord() {
        professionExperienceDocuments = new AutoPopulatingList<RegprofProfessionExperienceDocumentRecord>(RegprofProfessionExperienceDocumentRecord.class);
    }
        
    public RegprofProfessionExperienceRecord(
            Integer id,
            Integer nomenclatureProfessionExperienceId,
            Integer userId,
            Date dateCreated,
            Integer years,
            Integer months,
            Integer days,
            List<? extends RegprofProfessionExperienceDocumentRecord> professionExperienceDocuments,
            Integer trainingCourseId) {
        this.id = id;
        this.nomenclatureProfessionExperienceId = nomenclatureProfessionExperienceId;
        this.userId = userId;
        this.dateCreated = dateCreated;
        this.years = years;
        this.months = months;
        this.days = days;
        this.professionExperienceDocuments = professionExperienceDocuments;
        this.trainingCourseId = trainingCourseId;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getNomenclatureProfessionExperienceId() {
        return nomenclatureProfessionExperienceId;
    }
    public void setNomenclatureProfessionExperienceId(Integer nomenclatureProfessionExperienceId) {
        this.nomenclatureProfessionExperienceId = nomenclatureProfessionExperienceId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Integer getYears() {
        return years;
    }
    public void setYears(Integer years) {
        this.years = years;
    }
    public Integer getMonths() {
        return months;
    }
    public void setMonths(Integer months) {
        this.months = months;
    }
    public Integer getDays() {
        return days;
    }
    public void setDays(Integer days) {
        this.days = days;
    }
   
    public List<? extends RegprofProfessionExperienceDocumentRecord> getProfessionExperienceDocuments() {
        return professionExperienceDocuments;
    }

    public void setProfessionExperienceDocuments(
            List<? extends RegprofProfessionExperienceDocumentRecord> professionExperienceDocuments) {
        this.professionExperienceDocuments = professionExperienceDocuments;
    }

    public Integer getTrainingCourseId() {
        return trainingCourseId;
    }

    public void setTrainingCourseId(Integer trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }
    
}