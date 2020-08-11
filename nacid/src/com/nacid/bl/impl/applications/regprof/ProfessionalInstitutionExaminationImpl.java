package com.nacid.bl.impl.applications.regprof;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.applications.regprof.ProfessionalInstitutionExamination;
import com.nacid.data.annotations.Table;
//RayaWritten------------------------------------------------------------------------------------
@Table(name="regprof.professional_institution_examination")
public class ProfessionalInstitutionExaminationImpl implements ProfessionalInstitutionExamination{
    private Integer id;
    private Integer professionalInstitutionValidityId;
    private Integer regprofApplicationId;
    private int isLegitimate;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date examinationDate;
    private String notes;
    private Integer userCreated;
    
    public ProfessionalInstitutionExaminationImpl(){}
    
    public ProfessionalInstitutionExaminationImpl(Integer id,
            Integer professionalInstitutionValidityId,
            Integer regprofApplicationId, Integer isLegitimate,
            Date examinationDate, String notes, Integer userCreated) {
        super();
        this.id = id;
        this.professionalInstitutionValidityId = professionalInstitutionValidityId;
        this.regprofApplicationId = regprofApplicationId;
        this.isLegitimate = isLegitimate;
        this.examinationDate = examinationDate;
        this.notes = notes;
        this.userCreated = userCreated;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getProfessionalInstitutionValidityId() {
        return professionalInstitutionValidityId;
    }
    public void setProfessionalInstitutionValidityId(
            Integer professionalInstitutionValidityId) {
        this.professionalInstitutionValidityId = professionalInstitutionValidityId;
    }
    public Integer getRegprofApplicationId() {
        return regprofApplicationId;
    }
    public void setRegprofApplicationId(Integer regprofApplicationId) {
        this.regprofApplicationId = regprofApplicationId;
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

    public int getIsLegitimate() {
        return isLegitimate;
    }

    public void setIsLegitimate(int isLegitimate) {
        this.isLegitimate = isLegitimate;
    }
    
    
    
}
//---------------------------------------------------------------------------------------------------------------