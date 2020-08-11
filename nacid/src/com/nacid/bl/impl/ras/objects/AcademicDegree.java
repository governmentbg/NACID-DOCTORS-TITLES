package com.nacid.bl.impl.ras.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 22.10.2019 г.
 * Time: 18:52
 */
public class AcademicDegree {
    @JsonProperty("isActive")
    private boolean isActive;
    private Dissertation dissertation;
    private Integer indicatorsSum;
    private Integer totalSumChecked; //????
    private int academicDegreeTypeId;
//    private Institution institution;
    private int institutionId;
    private int researchAreaId;
    private String diplomaNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Sofia")
    private Date diplomaDate;
    private boolean graduatedAbroad;
    private Integer countryId;
    private String foreignTown;
    private String foreignTownAlt;
    private String foreignInstitution;
    private String foreignInstitutionAlt;
    private String certificateNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Sofia")
    private Date certificateDate;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Dissertation getDissertation() {
        return dissertation;
    }

    public void setDissertation(Dissertation dissertation) {
        this.dissertation = dissertation;
    }

    public Integer getIndicatorsSum() {
        return indicatorsSum;
    }

    public void setIndicatorsSum(Integer indicatorsSum) {
        this.indicatorsSum = indicatorsSum;
    }

    public Integer getTotalSumChecked() {
        return totalSumChecked;
    }

    public void setTotalSumChecked(Integer totalSumChecked) {
        this.totalSumChecked = totalSumChecked;
    }

    public int getAcademicDegreeTypeId() {
        return academicDegreeTypeId;
    }

    public void setAcademicDegreeTypeId(int academicDegreeTypeId) {
        this.academicDegreeTypeId = academicDegreeTypeId;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public int getResearchAreaId() {
        return researchAreaId;
    }

    public void setResearchAreaId(int researchAreaId) {
        this.researchAreaId = researchAreaId;
    }

    public String getDiplomaNumber() {
        return diplomaNumber;
    }

    public void setDiplomaNumber(String diplomaNumber) {
        this.diplomaNumber = diplomaNumber;
    }

    public Date getDiplomaDate() {
        return diplomaDate;
    }

    public void setDiplomaDate(Date diplomaDate) {
        this.diplomaDate = diplomaDate;
    }

    public boolean isGraduatedAbroad() {
        return graduatedAbroad;
    }

    public void setGraduatedAbroad(boolean graduatedAbroad) {
        this.graduatedAbroad = graduatedAbroad;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getForeignTown() {
        return foreignTown;
    }

    public void setForeignTown(String foreignTown) {
        this.foreignTown = foreignTown;
    }

    public String getForeignTownAlt() {
        return foreignTownAlt;
    }

    public void setForeignTownAlt(String foreignTownAlt) {
        this.foreignTownAlt = foreignTownAlt;
    }

    public String getForeignInstitution() {
        return foreignInstitution;
    }

    public void setForeignInstitution(String foreignInstitution) {
        this.foreignInstitution = foreignInstitution;
    }

    public String getForeignInstitutionAlt() {
        return foreignInstitutionAlt;
    }

    public void setForeignInstitutionAlt(String foreignInstitutionAlt) {
        this.foreignInstitutionAlt = foreignInstitutionAlt;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Date getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(Date certificateDate) {
        this.certificateDate = certificateDate;
    }
}