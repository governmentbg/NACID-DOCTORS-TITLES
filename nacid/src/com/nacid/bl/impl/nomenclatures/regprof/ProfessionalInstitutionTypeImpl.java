package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.ProfessionalInstitutionType;
import com.nacid.data.nomenclatures.regprof.ProfessionalInstitutionTypeRecord;
//RayaWritten----------------------------------------------------------------------------------------------------
public class ProfessionalInstitutionTypeImpl extends FlatNomenclatureImpl implements ProfessionalInstitutionType {
    public ProfessionalInstitutionTypeImpl(ProfessionalInstitutionTypeRecord record){
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE;
    }    
}
//-----------------------------------------------------------------------------------------------------------
