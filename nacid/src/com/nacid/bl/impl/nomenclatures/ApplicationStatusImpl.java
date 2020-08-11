package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.ApplicationStatusRecordExtended;

public class ApplicationStatusImpl extends FlatNomenclatureImpl implements ApplicationStatus {

    private boolean isLegal;
    public ApplicationStatusImpl(ApplicationStatusRecordExtended record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.isLegal = record.getIsLegal() != 0;
    }

    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_STATUS;
    }

    public boolean isLegal() {
        return isLegal;
    }
}
