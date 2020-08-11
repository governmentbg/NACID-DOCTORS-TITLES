package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionalQualification;
import com.nacid.data.nomenclatures.regprof.SecondaryProfessionalQualificationRecord;
//RayaWritten--------------------------------------------------
public class SecondaryProfessionalQualificationImpl extends
        FlatNomenclatureImpl implements SecondaryProfessionalQualification {
    private Integer professionGroupId;
    private String professionGroupName;
    private String code;
    
    
    public SecondaryProfessionalQualificationImpl(NomenclaturesDataProvider nomenclaturesDataProvider, SecondaryProfessionalQualificationRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.professionGroupId = record.getProfessionGroupId();
        this.professionGroupName = record.getProfessionGroupId() == null ? "" : nomenclaturesDataProvider.getSecondaryProfessionGroup(professionGroupId).getName();
        this.code = record.getCode();
    }

    @Override
    public Integer getProfessionGroupId() {
        return professionGroupId;
    }

    @Override
    public String getProfessionGroupName() {
        return professionGroupName;
    }

    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_PROFESSIONAL_QUALIFICATION;
    }

    public String getCode() {
        return code;
    }
    
    
}
//---------------------------------------------------------------------
