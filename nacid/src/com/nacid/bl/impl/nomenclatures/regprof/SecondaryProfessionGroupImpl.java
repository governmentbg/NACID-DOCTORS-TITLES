package com.nacid.bl.impl.nomenclatures.regprof;

import java.util.Date;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondaryProfessionGroup;
import com.nacid.data.nomenclatures.regprof.SecondaryProfessionGroupRecord;
//RayaWritten---------------------------------------------------------------
public class SecondaryProfessionGroupImpl extends FlatNomenclatureImpl
        implements SecondaryProfessionGroup {
    private String code;
    public SecondaryProfessionGroupImpl(SecondaryProfessionGroupRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.code = record.getCode();
    }

    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_PROFESSION_GROUP;
    }

    public String getCode() {
        return code;
    }
    

}
//-----------------------------------------------------------------------------------
