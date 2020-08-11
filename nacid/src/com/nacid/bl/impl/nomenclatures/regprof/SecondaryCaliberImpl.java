package com.nacid.bl.impl.nomenclatures.regprof;

import java.util.Date;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondaryCaliber;
import com.nacid.data.nomenclatures.regprof.SecondaryCaliberRecord;
//RayaWritten-------------------------------------------------------------------------
public class SecondaryCaliberImpl extends FlatNomenclatureImpl implements SecondaryCaliber {

    public SecondaryCaliberImpl(SecondaryCaliberRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }


    public int getNomenclatureType() {
           return NomenclaturesDataProvider.FLAT_NOMENCLATURE_SECONDARY_CALIBER;
    }

}
//---------------------------------------------------------------------------------------
