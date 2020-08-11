package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.EducationDocumentType;
import com.nacid.data.nomenclatures.regprof.EducationDocumentTypeRecord;

public class EducationDocumentTypeImpl extends FlatNomenclatureImpl implements EducationDocumentType {

    public EducationDocumentTypeImpl(EducationDocumentTypeRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE; //rostislav: predi beshe FLAT_NOMENCLATURE_HIGHER_SPECIALITY
    }
    
}
