package com.nacid.regprof.web.model.applications;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;

public class RegprofTrainingCourseSpecialityWebModel {
    
    private Integer id;
    private Integer secondarySpecialityId;
    private String secondarySpecialityName;
    private String secondaryQualificationDegree;
    private Integer higherSpecialityId;
    private String higherSpecialityName;
    private Integer sdkSpecialityId;
    private String sdkSpecialityName;

    public RegprofTrainingCourseSpecialityWebModel(NomenclaturesDataProvider nDP, RegprofTrainingCourseSpecialityRecord record) {
        id = record.getId();
        if (record.getSecondarySpecialityId() != null) {
            secondarySpecialityId = record.getSecondarySpecialityId();
            SecondarySpeciality speciality = nDP.getSecondarySpeciality(secondarySpecialityId);
            secondarySpecialityName = speciality.getName();
            secondaryQualificationDegree = speciality.getQualificationDegreeName().isEmpty() ? "Няма информация за степен" : speciality.getQualificationDegreeName();
        } else if (record.getHigherSpecialityId() != null) {
            higherSpecialityId = record.getHigherSpecialityId();
            higherSpecialityName = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, higherSpecialityId).getName();
        } else if (record.getSdkSpecialityId() != null) {
            sdkSpecialityId = record.getSdkSpecialityId();
            sdkSpecialityName = nDP.getFlatNomenclature(nDP.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, sdkSpecialityId).getName();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSecondarySpecialityId() {
        return secondarySpecialityId;
    }

    public void setSecondarySpecialityId(Integer secondarySpecialityId) {
        this.secondarySpecialityId = secondarySpecialityId;
    }

    public String getSecondarySpecialityName() {
        return secondarySpecialityName;
    }

    public void setSecondarySpecialityName(String secondarySpecialityName) {
        this.secondarySpecialityName = secondarySpecialityName;
    }

    public String getSecondaryQualificationDegree() {
        return secondaryQualificationDegree;
    }

    public void setSecondaryQualificationDegree(String secondaryQualificationDegree) {
        this.secondaryQualificationDegree = secondaryQualificationDegree;
    }

    public Integer getHigherSpecialityId() {
        return higherSpecialityId;
    }

    public void setHigherSpecialityId(Integer higherSpecialityId) {
        this.higherSpecialityId = higherSpecialityId;
    }

    public String getHigherSpecialityName() {
        return higherSpecialityName;
    }

    public void setHigherSpecialityName(String higherSpecialityName) {
        this.higherSpecialityName = higherSpecialityName;
    }

    public Integer getSdkSpecialityId() {
        return sdkSpecialityId;
    }

    public void setSdkSpecialityId(Integer sdkSpecialityId) {
        this.sdkSpecialityId = sdkSpecialityId;
    }

    public String getSdkSpecialityName() {
        return sdkSpecialityName;
    }

    public void setSdkSpecialityName(String sdkSpecialityName) {
        this.sdkSpecialityName = sdkSpecialityName;
    }

}
