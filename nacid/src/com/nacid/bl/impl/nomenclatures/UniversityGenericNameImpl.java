package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.UniversityGenericNameRecord;

public class UniversityGenericNameImpl extends FlatNomenclatureImpl {

    public UniversityGenericNameImpl(UniversityGenericNameRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }

    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME;
    }

}
