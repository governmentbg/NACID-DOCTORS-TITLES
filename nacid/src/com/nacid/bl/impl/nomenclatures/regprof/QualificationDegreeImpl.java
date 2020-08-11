package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.QualificationDegree;
import com.nacid.data.nomenclatures.regprof.QualificationDegreeRecord;
//RayaWRitten-----------------------------------------------------------
public class QualificationDegreeImpl extends FlatNomenclatureImpl implements QualificationDegree{

    public QualificationDegreeImpl(QualificationDegreeRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE;
    }
    
}
//--------------------------------------------------------------------------
