package com.nacid.bl.impl.regprof;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.regprof.ProfessionalInstitutionValidity;

import com.nacid.data.annotations.Table;
@Table(name="regprof.professional_institution_validity")
//RayaWritten--------------------------------------
public class ProfessionalInstitutionValidityImpl implements ProfessionalInstitutionValidity, RequestParameterInterface {
    private Integer id;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date examinationDate;
    private Integer professionalInstitutionId;
    private Integer qualificationBulgariaHighSdkId;
    private Integer qualificationBulgariaSecId;
    private Integer hasRightsEducate;
    private Integer isLegitimate;
    private Integer userCreated;
    private String notes;
    
    public ProfessionalInstitutionValidityImpl(){}
    

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getExaminationDate() {
        return examinationDate;
    }
    public void setExaminationDate(Date examinationDate) {
        this.examinationDate = examinationDate;
    }
    public Integer getProfessionalInstitutionId() {
        return professionalInstitutionId;
    }
    public void setProfessionalInstitutionId(Integer professionalInstitutionId) {
        this.professionalInstitutionId = professionalInstitutionId;
    }
   
    public Integer getHasRightsEducate() {
        return hasRightsEducate;
    }
    public void setHasRightsEducate(Integer hasRightsEducate) {
        this.hasRightsEducate = hasRightsEducate;
    }
    public Integer getIsLegitimate() {
        return isLegitimate;
    }
    public void setIsLegitimate(Integer isLegitimate) {
        this.isLegitimate = isLegitimate;
    }
    public Integer getUserCreated() {
        return userCreated;
    }
    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getQualificationBulgariaHighSdkId() {
        return qualificationBulgariaHighSdkId;
    }


    public void setQualificationBulgariaHighSdkId(
            Integer qualificationBulgariaHighSdkId) {
        this.qualificationBulgariaHighSdkId = qualificationBulgariaHighSdkId;
    }


    public Integer getQualificationBulgariaSecId() {
        return qualificationBulgariaSecId;
    }


    public void setQualificationBulgariaSecId(Integer qualificationBulgariaSecId) {
        this.qualificationBulgariaSecId = qualificationBulgariaSecId;
    }
    
    
}
//----------------------------------------------------