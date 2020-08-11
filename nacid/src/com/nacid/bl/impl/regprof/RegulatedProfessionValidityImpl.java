package com.nacid.bl.impl.regprof;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.regprof.RegulatedProfessionValidity;
import com.nacid.data.annotations.Table;

//RayaWritten--------------------------------------
@Table(name="regprof.regulated_validity")
public class RegulatedProfessionValidityImpl implements RegulatedProfessionValidity, RequestParameterInterface{
    private Integer id;
    private Integer countryId;
    //private Integer qualificationHighSdkId;
    //private Integer qualificationSecId;
    //private Integer educationTypeId;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date examinationDate;
    private String notes;
    private String profession;
    private Integer userCreated;
    private Integer isRegulated;
    //private Integer professionExperienceId;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCountryId() {
        return countryId;
    }
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
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
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public Integer getUserCreated() {
        return userCreated;
    }
    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }
    public Integer getIsRegulated() {
        return isRegulated;
    }
    public void setIsRegulated(Integer isRegulated) {
        this.isRegulated = isRegulated;
    }
    
    
}
//-----------------------------------------------------
