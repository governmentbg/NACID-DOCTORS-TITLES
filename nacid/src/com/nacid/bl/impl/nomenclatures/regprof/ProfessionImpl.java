package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.Profession;
import com.nacid.data.nomenclatures.regprof.ProfessionRecord;
//RayaWritten----------------------------------------------------------------
public class ProfessionImpl extends FlatNomenclatureImpl implements Profession {

    public ProfessionImpl(ProfessionRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION;
    }

}
//------------------------------------------------------------------------