package com.nacid.data.regprof;

import com.nacid.data.annotations.Table;

@Table(name="regprof.professional_institution_names")
public class ProfessionalInstitutionNamesRecord {
    
    private Integer id;
    private Integer professionalInstitutionId;
    private String formerName;
    private int active;
    
    public ProfessionalInstitutionNamesRecord() { }

    public ProfessionalInstitutionNamesRecord(Integer id, Integer professionalInstitutionId, String formerName, int active) {
        this.id = id;
        this.professionalInstitutionId = professionalInstitutionId;
        this.formerName = formerName;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfessionalInstitutionId() {
        return professionalInstitutionId;
    }

    public void setProfessionalInstitutionId(Integer professionalInstitutionId) {
        this.professionalInstitutionId = professionalInstitutionId;
    }

    public String getFormerName() {
        return formerName;
    }

    public void setFormerName(String formerName) {
        this.formerName = formerName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

}
