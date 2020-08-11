package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.HigherProfessionalQualification;
import com.nacid.data.nomenclatures.regprof.HigherProfessionalQualificationRecord;

public class HigherProfessionalQualificationImpl extends FlatNomenclatureImpl implements HigherProfessionalQualification {

    public HigherProfessionalQualificationImpl(HigherProfessionalQualificationRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION;
    }

}
