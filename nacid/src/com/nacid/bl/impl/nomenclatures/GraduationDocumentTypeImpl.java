package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.GraduationDocumentType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.GraduationDocumentTypeRecord;

public class GraduationDocumentTypeImpl extends FlatNomenclatureImpl implements GraduationDocumentType {

    public GraduationDocumentTypeImpl(GraduationDocumentTypeRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE;
    }  

}
