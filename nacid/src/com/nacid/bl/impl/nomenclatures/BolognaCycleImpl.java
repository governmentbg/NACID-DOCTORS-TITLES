package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.BolognaCycle;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.BolognaCycleRecord;

public class BolognaCycleImpl extends FlatNomenclatureImpl implements BolognaCycle {

    public BolognaCycleImpl(BolognaCycleRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE;
    }  

}
