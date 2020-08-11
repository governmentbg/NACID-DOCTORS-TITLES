package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.ProfessionExperienceDocumentType;
import com.nacid.data.nomenclatures.regprof.ProfessionExperienceDocumentTypeRecord;

public class ProfessionExperienceDocumentTypeImpl extends FlatNomenclatureImpl implements ProfessionExperienceDocumentType {
    public boolean isForExperienceCalculation;
    public ProfessionExperienceDocumentTypeImpl(ProfessionExperienceDocumentTypeRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        isForExperienceCalculation = record.getForExperienceCalculation() == 1;
    }
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE;
    }
    public boolean isForExperienceCalculation() {
        return isForExperienceCalculation;
    }
}
