package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.Qualification;
import com.nacid.data.nomenclatures.QualificationRecord;

public class QualificationImpl extends FlatNomenclatureImpl implements Qualification {

    public QualificationImpl(QualificationRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION;
    }
}

