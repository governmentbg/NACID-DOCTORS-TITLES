package com.nacid.bl.impl.applications.regprof;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.applications.regprof.RegulatedProfessionExamination;
import com.nacid.data.annotations.Table;

//RayaWritten-----------------------------------------
@Table(name="regprof.regulated_examination")
public class RegulatedProfessionExaminationImpl implements RegulatedProfessionExamination{
    private Integer id;
    private Integer regulatedValidityId;
    private Integer regprofApplicationId;
    private int isRegulated;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date examinationDate;
    private String notes;
    private Integer userCreated;
    
    public RegulatedProfessionExaminationImpl(Integer id,
            Integer regulatedValidityId, Integer regprofApplicationId,
            int isRegulated, Date examinationDate, String notes,
            Integer userCreated) {
        super();
        this.id = id;
        this.regulatedValidityId = regulatedValidityId;
        this.regprofApplicationId = regprofApplicationId;
        this.isRegulated = isRegulated;
        this.examinationDate = examinationDate;
        this.notes = notes;
        this.userCreated = userCreated;
    }
    
    public RegulatedProfessionExaminationImpl() {
        super();
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getRegulatedValidityId() {
        return regulatedValidityId;
    }
    public void setRegulatedValidityId(Integer regulatedValidityId) {
        this.regulatedValidityId = regulatedValidityId;
    }
    public Integer getRegprofApplicationId() {
        return regprofApplicationId;
    }
    public void setRegprofApplicationId(Integer regprofApplicationId) {
        this.regprofApplicationId = regprofApplicationId;
    }
    public int getIsRegulated() {
        return isRegulated;
    }
    public void setIsRegulated(int isRegulated) {
        this.isRegulated = isRegulated;
    }
    public Date getExaminationDate() {
        return examinationDate;
    }
    public void setExaminationDate(Date examinationDate) {
        this.examinationDate = examinationDate;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Integer getUserCreated() {
        return userCreated;
    }
    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }
    
    
}
//--------------------------------------------------------------
