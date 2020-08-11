package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.ApplicationDocflowStatusRecord;


/**
 * Created by georgi.georgiev on 14.04.2015.
 */
public class ApplicationDocflowStatusImpl extends FlatNomenclatureImpl implements ApplicationDocflowStatus {


    public ApplicationDocflowStatusImpl(ApplicationDocflowStatusRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());

    }

    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_DOCFLOW_STATUS;
    }


}
