package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.HigherSpeciality;
import com.nacid.data.nomenclatures.regprof.HigherSpecialityRecord;

public class HigherSpecialityImpl extends FlatNomenclatureImpl implements HigherSpeciality {

    public HigherSpecialityImpl(HigherSpecialityRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY;
    }
    
}
