package com.ext.nacid.regprof.web.model.applications;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseSpecialitiesRecord;

public class ExtRegprofTrainingCourseSpecialitiesWebModel {
    private String id;
    private String secondarySpecialityId;
    private String secondarySpecialityTxt;
    private String secondarySpecialityTxtToDisplay;
    private String higherSpecialityId;
    private String higherSpecialityTxtToDisplay;
    private String higherSpecialityTxt;
    private String sdkSpecialityId;
    private String sdkSpecialityTxt;
    private String sdkSpecialityTxtToDisplay;
    public ExtRegprofTrainingCourseSpecialitiesWebModel(NomenclaturesDataProvider nomenclaturesDataProvider, ExtRegprofTrainingCourseSpecialitiesRecord record) {
        this.id = record.getId() + "";
        if (record.getSecondarySpecialityId() != null) {
            this.secondarySpecialityId = record.getSecondarySpecialityId().toString();
            SecondarySpeciality spec = nomenclaturesDataProvider.getSecondarySpeciality(record.getSecondarySpecialityId());
            secondarySpecialityTxtToDisplay = spec.getName() + ", " + spec.getQualificationDegreeName();
        } else {
            this.secondarySpecialityTxt = record.getSecondarySpecialityTxt();
            secondarySpecialityTxtToDisplay = secondarySpecialityTxt;
        }
        
        this.higherSpecialityId = record.getHigherSpecialityId() == null ? "" : record.getHigherSpecialityId().toString();
        this.higherSpecialityTxt = record.getHigherSpecialityTxt();
        this.higherSpecialityTxtToDisplay = ExtRegprofTrainingCourseWebModel.getFlatNomenclatureValue(nomenclaturesDataProvider, NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, record.getHigherSpecialityId(), record.getHigherSpecialityTxt());
        
        this.sdkSpecialityId = record.getSdkSpecialityId() == null ? "" : record.getSdkSpecialityId().toString();
        this.sdkSpecialityTxt = record.getSdkSpecialityTxt();
        this.sdkSpecialityTxtToDisplay = ExtRegprofTrainingCourseWebModel.getFlatNomenclatureValue(nomenclaturesDataProvider, NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, record.getSdkSpecialityId(), record.getSdkSpecialityTxt());
        
        
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSecondarySpecialityId() {
        return secondarySpecialityId;
    }
    public void setSecondarySpecialityId(String secondarySpecialityId) {
        this.secondarySpecialityId = secondarySpecialityId;
    }
    public String getHigherSpecialityId() {
        return higherSpecialityId;
    }
    public void setHigherSpecialityId(String higherSpecialityId) {
        this.higherSpecialityId = higherSpecialityId;
    }
    public String getHigherSpecialityTxt() {
        return higherSpecialityTxt;
    }
    public void setHigherSpecialityTxt(String higherSpecialityTxt) {
        this.higherSpecialityTxt = higherSpecialityTxt;
    }
    public String getSdkSpecialityId() {
        return sdkSpecialityId;
    }
    public void setSdkSpecialityId(String sdkSpecialityId) {
        this.sdkSpecialityId = sdkSpecialityId;
    }
    public String getSdkSpecialityTxt() {
        return sdkSpecialityTxt;
    }
    public void setSdkSpecialityTxt(String sdkSpecialityTxt) {
        this.sdkSpecialityTxt = sdkSpecialityTxt;
    }

    public String getHigherSpecialityTxtToDisplay() {
        return higherSpecialityTxtToDisplay;
    }

    public void setHigherSpecialityTxtToDisplay(String higherSpecialityTxtToDisplay) {
        this.higherSpecialityTxtToDisplay = higherSpecialityTxtToDisplay;
    }

    public String getSdkSpecialityTxtToDisplay() {
        return sdkSpecialityTxtToDisplay;
    }

    public void setSdkSpecialityTxtToDisplay(String sdkSpecialityTxtToDisplay) {
        this.sdkSpecialityTxtToDisplay = sdkSpecialityTxtToDisplay;
    }

    public String getSecondarySpecialityTxtToDisplay() {
        return secondarySpecialityTxtToDisplay;
    }

    public void setSecondarySpecialityTxtToDisplay(
            String secondarySpecialityTxtToDisplay) {
        this.secondarySpecialityTxtToDisplay = secondarySpecialityTxtToDisplay;
    }

    public String getSecondarySpecialityTxt() {
        return secondarySpecialityTxt;
    }

    public void setSecondarySpecialityTxt(String secondarySpecialityTxt) {
        this.secondarySpecialityTxt = secondarySpecialityTxt;
    }
    
}
