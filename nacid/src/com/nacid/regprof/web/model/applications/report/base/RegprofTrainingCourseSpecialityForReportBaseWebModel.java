package com.nacid.regprof.web.model.applications.report.base;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;
import com.nacid.regprof.web.handlers.impl.nomenclatures.SecondarySpecialityHandler;
//RayaWritten------------------------------------------------------------------------
public class RegprofTrainingCourseSpecialityForReportBaseWebModel {
    protected String id;
    protected String secondarySpeciality;
    protected String higherSpeciality;
    protected String sdkSpeciality;

    public RegprofTrainingCourseSpecialityForReportBaseWebModel(RegprofTrainingCourseSpecialityRecord record, NacidDataProvider nacidDataProvider){
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        id = record.getId() + "";
        SecondarySpeciality secSpeciality = null;
        String qualDegree = "";
        if(record.getSecondarySpecialityId()!= null){
            secSpeciality = nomDp.getSecondarySpeciality(record.getSecondarySpecialityId());
            FlatNomenclature secQual = null;       
            if(secSpeciality.getQualificationDegreeId()!= null){
                secQual = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, secSpeciality.getQualificationDegreeId());
            }
            qualDegree = secQual != null ? secQual.getName() : "";
        }
        secondarySpeciality = secSpeciality != null ? secSpeciality.getName()+", " + qualDegree : null;
        FlatNomenclature hSp = null;
        if(record.getHigherSpecialityId()!= null){
            hSp = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, record.getHigherSpecialityId());
        }
        higherSpeciality = hSp != null ? hSp.getName() : null;
        FlatNomenclature sdkSp = null;
        if(record.getSdkSpecialityId() != null){
            sdkSp = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY, record.getSdkSpecialityId());
        }
        sdkSpeciality = sdkSp != null ? sdkSp.getName() : null;
    }

    public String getId() {
        return id;
    }

    public String getSecondarySpeciality() {
        return secondarySpeciality;
    }

    public String getHigherSpeciality() {
        return higherSpeciality;
    }

    public String getSdkSpeciality() {
        return sdkSpeciality;
    }


}
//-------------------------------------------------------------------------------------