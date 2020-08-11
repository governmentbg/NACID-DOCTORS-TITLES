package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.OriginalSpeciality;
import com.nacid.data.nomenclatures.OriginalSpecialityRecord;

public class OriginalSpecialityImpl extends FlatNomenclatureImpl implements OriginalSpeciality {

    public OriginalSpecialityImpl(OriginalSpecialityRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY;
    }  

}
