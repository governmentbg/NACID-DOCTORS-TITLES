package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.data.nomenclatures.regprof.EducationTypeRecord;

public class EducationTypeImpl extends FlatNomenclatureImpl implements EducationType {

    public EducationTypeImpl(EducationTypeRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_TYPE;
    }

}
