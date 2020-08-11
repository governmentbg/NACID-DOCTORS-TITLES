package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.PaymentType;
import com.nacid.data.nomenclatures.regprof.PaymentTypeRecord;

public class PaymentTypeImpl extends FlatNomenclatureImpl implements PaymentType {

    public PaymentTypeImpl(PaymentTypeRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_PAYMENT_TYPE;
    }  

}
