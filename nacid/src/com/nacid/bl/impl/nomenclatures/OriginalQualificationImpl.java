package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.Qualification;
import com.nacid.data.nomenclatures.OriginalQualificationRecord;
import com.nacid.data.nomenclatures.QualificationRecord;

public class OriginalQualificationImpl extends FlatNomenclatureImpl implements Qualification {

    public OriginalQualificationImpl(OriginalQualificationRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION;
    }
}

