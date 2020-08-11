package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.nomenclatures.regprof.SPPOO;
import com.nacid.data.annotations.Table;

@Table(name="nomenclatures.sppoo")
public class SPPOOImpl implements SPPOO {
    
    private int id;
    private String qualificationCode;
    private String qualificationName;
    private String professionCode;
    private String professionName;
    private String specialityCode;
    private String specialityName;
    private int degree;
    
    public SPPOOImpl(int id, String qualificationCode, String qualificationName, String professionCode,
            String professionName, String specialityCode, String specialityName, int degree) {
        this.id = id;
        this.qualificationCode = qualificationCode;
        this.qualificationName = qualificationName;
        this.professionCode = professionCode;
        this.professionName = professionName;
        this.specialityCode = specialityCode;
        this.specialityName = specialityName;
        this.degree = degree;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getQualificationCode() {
        return qualificationCode;
    }
    public void setQualificationCode(String qualificationCode) {
        this.qualificationCode = qualificationCode;
    }
    public String getQualificationName() {
        return qualificationName;
    }
    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }
    public String getProfessionCode() {
        return professionCode;
    }
    public void setProfessionCode(String professionCode) {
        this.professionCode = professionCode;
    }
    public String getProfessionName() {
        return professionName;
    }
    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }
    public String getSpecialityCode() {
        return specialityCode;
    }
    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }
    public String getSpecialityName() {
        return specialityName;
    }
    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }
    public int getDegree() {
        return degree;
    }
    public void setDegree(int degree) {
        this.degree = degree;
    }

}